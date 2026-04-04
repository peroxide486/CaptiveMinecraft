package com.github.peroxide486.captiveminecraft.listeners;

import com.github.peroxide486.captiveminecraft.CaptiveMinecraft;
import com.github.peroxide486.captiveminecraft.utils.RegionManager;
import com.github.peroxide486.captiveminecraft.utils.Regions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerRegionalismEvent implements Listener {
    private final CaptiveMinecraft plugin;
    private final RegionManager regionManager;

    public PlayerRegionalismEvent(CaptiveMinecraft plugin, RegionManager regionManager) {
        this.plugin = plugin;
        this.regionManager = regionManager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRegionalism(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Regions regions = regionManager.getPlayerRegion(player);

        if (regions == null) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    Regions newRegion = regionManager.assignNewRegion(player);
                    Location center = regionManager.getRegionCenter(newRegion, player.getWorld());
                    player.teleport(center);
                    player.sendMessage(Component.text("A private area has been successfully created for you!", NamedTextColor.GREEN));
                });
            });
        } else {
            Location center = regionManager.getRegionCenter(regions, player.getWorld());
            if (!regionManager.isInPlayerRegion(player, player.getLocation())) {
                player.teleport(center);
                player.sendMessage(Component.text("You have been teleported back to your private area!", NamedTextColor.GREEN));
            }
        }
    }
}
