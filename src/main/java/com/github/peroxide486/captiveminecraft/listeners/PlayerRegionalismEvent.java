package com.github.peroxide486.captiveminecraft.listeners;

import com.github.peroxide486.captiveminecraft.CaptiveMinecraft;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerRegionalismEvent implements Listener {
    private CaptiveMinecraft plugin;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRegionalism(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("captiveminecraft.regionalism")) {
            if (!player.hasPlayedBefore()) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

                });
            }
        } else {
            player.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
        }
    }
}
