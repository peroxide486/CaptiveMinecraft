package com.github.peroxide486.captiveminecraft.utils;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.github.peroxide486.captiveminecraft.CaptiveMinecraft;
import com.github.peroxide486.captiveminecraft.config.MainConfig;
import com.github.peroxide486.captiveminecraft.utils.database.RegionSQL;
import com.github.peroxide486.captiveminecraft.utils.database.StorageService;
import com.github.yannicklamprecht.worldborder.api.WorldBorderApi;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class RegionManager {
    private final CaptiveMinecraft plugin;
    private final StorageService storageService;
    private final WorldBorderApi worldBorderAPI;
    private final RegionSQL regionSQL;

    private final Set<String> allocatedRegions = new HashSet<>();
    private final Map<UUID, Regions> playerRegions = new HashMap<>();

    private final SettingsManager settingsManager = SettingsManagerBuilder
            .withYamlFile(new File("config.yml"))
            .configurationData(MainConfig.class)
            .useDefaultMigrationService()
            .create();
    public final int REGION_SIZE = settingsManager.getProperty(MainConfig.MAX_REGION_SIZE);
    public final int BORDER_SIZE = settingsManager.getProperty(MainConfig.MIN_REGION_SIZE);

    private int currentLayer = 0;
    private int currentIndex = 0;

    public RegionManager(CaptiveMinecraft plugin, StorageService storageService, WorldBorderApi worldBorderAPI, RegionSQL regionSQL) {
        this.plugin = plugin;
        this.storageService = storageService;
        this.worldBorderAPI = worldBorderAPI;
        this.regionSQL = regionSQL;
        loadData();
    }

    private void loadData() {
        if (REGION_SIZE <= 0 || BORDER_SIZE <= 0) {
            plugin.getLogger().warning("Region Size must be greater than 0!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        } else if (REGION_SIZE < BORDER_SIZE) {
            plugin.getLogger().warning("Max region Size must be greater than minimum region size!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        playerRegions.clear();
        allocatedRegions.clear();

        Map<UUID, Regions> allPlots = storageService.loadAllRegions();
        int maxLayer = -1;
        for (Map.Entry<UUID, Regions> entry : allPlots.entrySet()) {
            UUID uuid = entry.getKey();
            Regions region = entry.getValue();
            playerRegions.put(uuid, region);
            String coordKey = region.getRegionX() + "," + region.getRegionZ();
            allocatedRegions.add(coordKey);

            int layer = Math.max(Math.abs(region.getRegionX()), Math.abs(region.getRegionZ()));
            if (layer > maxLayer) maxLayer = layer;
        }

        if (maxLayer >= 0) {
            currentLayer = maxLayer;
            currentIndex = 0;
            int[] firstCoord = getSpiralCoordinate(currentLayer, 0);
            while (allocatedRegions.contains(firstCoord[0] + "," + firstCoord[1])) {
                currentIndex++;
                if (currentIndex >= 8 * currentLayer) {
                    currentLayer++;
                    currentIndex = 0;
                }
                firstCoord = getSpiralCoordinate(currentLayer, currentIndex);
            }
        } else {
            currentLayer = 0;
            currentIndex = 0;
        }
    }

    public synchronized Regions assignNewRegion(Player player) {
        UUID uuid = player.getUniqueId();
        if (playerRegions.containsKey(uuid)) {
            return playerRegions.get(uuid);
        }

        int[] nextCoord = getNextFreeRegion();
        int regionX = nextCoord[0];
        int regionZ = nextCoord[1];
        Regions newRegion = new Regions(regionX, regionZ);

        applyBorder(player, newRegion, BORDER_SIZE);
        double borderSize = worldBorderAPI.getWorldBorder(player).getSize();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            storageService.saveRegion(uuid, newRegion, borderSize);
            plugin.getLogger().info("Saved " + player.getName() + " region " + newRegion);
        });

        playerRegions.put(uuid, newRegion);
        allocatedRegions.add(regionX + "," + regionZ);

        return newRegion;
    }

    public synchronized Regions getPlayerRegion(Player player) {
        return playerRegions.get(player.getUniqueId());
    }

    public RegionBorder getRegionBorder(Regions region, World world) {
        int minX = region.getRegionX() * REGION_SIZE;
        int maxX = (region.getRegionX() + 1) * REGION_SIZE - 1;
        int minZ = region.getRegionZ() * REGION_SIZE;
        int maxZ = (region.getRegionZ() + 1) * REGION_SIZE - 1;
        return new RegionBorder(world, minX, maxX, minZ, maxZ);
    }

//    public boolean isInPlayerRegion(Player player, Location location) {
//        Regions region = getPlayerRegion(player);
//        if (region == null) {
//            return false;
//        }
//        double borderSize = region.getRegionSize();
//        RegionBorder border = getRegionBorder(region, location.getWorld(), borderSize);
//        return border.contains(location);
//    }

    public Location getRegionCenter(Regions region, World world) {
        int centerX = region.getRegionX() * REGION_SIZE + REGION_SIZE / 2;
        int centerZ = region.getRegionZ() * REGION_SIZE + REGION_SIZE / 2;
        int y = world.getHighestBlockYAt(centerX, centerZ);
        return new Location(world, centerX + 0.5, y + 1, centerZ + 0.5);
    }

    public void applyBorder(Player player, Regions region, double borderSize) {
        World world = player.getWorld();
        worldBorderAPI.setBorder(player, borderSize, getRegionCenter(region, world));
    }

    public void resizeRegion(Player player, double newSize) {
        Regions region = playerRegions.get(player.getUniqueId());
        if (region == null || newSize < 1) {
            player.sendMessage(Component.text("You need to specify a region or a region size!", NamedTextColor.RED));
            return;
        }

        int half = (int) Math.ceil(newSize / 2);
        int newMinX = region.getRegionX() - half;
        int newMaxX = region.getRegionX() + half;
        int newMinZ = region.getRegionZ() - half;
        int newMaxZ = region.getRegionZ() + half;
        if (isOverlapping(newMinX, newMaxX, newMinZ, newMaxZ, player.getUniqueId())) {
            player.sendMessage(Component.text("The new size will result in an overlap with other players' areas!",  NamedTextColor.RED));
            return;
        }

        region.setRegionSize(newSize);
        regionSQL.upsertRegion(region.getUUID(), region.getRegionX(), region.getRegionZ(), newSize);
        applyBorder(player, region, newSize);
        player.sendMessage(Component.text("The changed area size is " + newSize + " !", NamedTextColor.GREEN));
    }

    private boolean isOverlapping(int minX, int maxX, int minZ, int maxZ, UUID excludeUUID) {
        for (Map.Entry<UUID, Regions> entry : playerRegions.entrySet()) {
            if (entry.getKey().equals(excludeUUID)) continue;
            Regions other = entry.getValue();
            int otherHalf = (int) Math.ceil(other.getRegionSize() / 2);
            int otherMinX = other.getRegionX() - otherHalf;
            int otherMaxX = other.getRegionX() + otherHalf;
            int otherMinZ = other.getRegionZ() - otherHalf;
            int otherMaxZ = other.getRegionZ() + otherHalf;
            if (minX <= otherMaxX && maxX >= otherMinX && minZ <= otherMaxZ && maxZ >= otherMinZ) {
                return true;
            }
        }
        return false;
    }

    private int[] getSpiralCoordinate(int layer, int index) {
        if (layer == 0) return new int[]{0, 0};
        int sideLen = layer * 2;
        int maxIndex = 8 * layer - 1;
        if (index < 0) index = 0;
        if (index > maxIndex) index = maxIndex;

        int edge = index / sideLen;
        int offset = index % sideLen;

        int x = 0, z = 0;
        switch (edge) {
            case 0:
                x = -layer + offset;
                z = layer;
                break;
            case 1:
                x = layer;
                z = layer - 1 - offset;
                break;
            case 2:
                x = layer - 1 - offset;
                z = -layer;
                break;
            case 3:
                x = -layer;
                z = -layer + 1 + offset;
                break;
        }
        return new int[]{x, z};
    }

    private int[] getNextFreeRegion() {
        while (true) {
            int[] coord = getSpiralCoordinate(currentLayer, currentIndex);
            String key = coord[0] + "," + coord[1];
            if (!allocatedRegions.contains(key)) {
                return coord;
            }
            currentIndex++;
            if (currentLayer > 0 && currentIndex >= 8 * currentLayer) {
                currentLayer++;
                currentIndex = 0;
            } else if (currentLayer == 0 && currentIndex >= 1) {
                currentLayer = 1;
                currentIndex = 0;
            }
        }
    }

    public int getMaxRegionSize() {
        return REGION_SIZE;
    }
}
