package com.github.peroxide486.captiveminecraft.utils;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.github.peroxide486.captiveminecraft.CaptiveMinecraft;
import com.github.peroxide486.captiveminecraft.config.MainConfig;
import com.github.peroxide486.captiveminecraft.utils.database.StorageService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class RegionManager {
    private final CaptiveMinecraft plugin;
    private final StorageService storageService;

    private final Set<String> allocatedRegions = new HashSet<>();
    private final Map<UUID, Regions> playerRegions = new HashMap<>();

    private final SettingsManager settingsManager = SettingsManagerBuilder
            .withYamlFile(new File("config.yml"))
            .configurationData(MainConfig.class)
            .useDefaultMigrationService()
            .create();
    private final int REGION_SIZE = settingsManager.getProperty(MainConfig.MAX_REGION_SIZE);

    private int currentLayer = 0;
    private int currentIndex = 0;

    public RegionManager(CaptiveMinecraft plugin, StorageService storageService) {
        this.plugin = plugin;
        this.storageService = storageService;
        loadData();
    }

    private void loadData() {

    }

    public Regions assignNewRegion(Player player) {
        int[] nextCoord = getNextFreeRegion();
        int regionX = nextCoord[0];
        int regionZ = nextCoord[1];
        Regions newRegion = new Regions(regionX, regionZ);

        UUID uuid = player.getUniqueId();
        playerRegions.put(uuid, newRegion);
        allocatedRegions.add(regionX + "," + regionZ);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            storageService.saveRegion(uuid, newRegion);
            plugin.getLogger().info("Saved " + player.getName() + " region " + newRegion);
        });

        return newRegion;
    }

    public Regions getPlayerRegion(Player player) {
        return playerRegions.get(player.getUniqueId());
    }

    public RegionBorder getRegionBorder(Regions region, World world) {
        int minX = region.getRegionX() * REGION_SIZE;
        int maxX = (region.getRegionX() + 1) * REGION_SIZE - 1;
        int minZ = region.getRegionZ() * REGION_SIZE;
        int maxZ = (region.getRegionZ() + 1) * REGION_SIZE - 1;
        return new RegionBorder(world, minX, maxX, minZ, maxZ);
    }

    public boolean isInPlayerRegion(Player player, Location location) {
        Regions regions = getPlayerRegion(player);
        if (regions == null) {
            return false;
        }
        RegionBorder border = getRegionBorder(regions, location.getWorld());
        return border.contains(location);
    }

    public Location getRegionCenter(Regions region, World world) {
        int centerX = region.getRegionX() * REGION_SIZE + REGION_SIZE / 2;
        int centerZ = region.getRegionZ() * REGION_SIZE + REGION_SIZE / 2;
        int y = world.getHighestBlockYAt(centerX, centerZ);
        return new Location(world, centerX + 0.5, y + 1, centerZ + 0.5);
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
