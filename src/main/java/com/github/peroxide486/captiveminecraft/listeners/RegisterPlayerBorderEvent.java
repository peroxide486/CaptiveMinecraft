package com.github.peroxide486.captiveminecraft.listeners;

import com.github.peroxide486.captiveminecraft.CaptiveMinecraft;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class RegisterPlayerBorderEvent implements Listener {
    private CaptiveMinecraft plugin;

    @EventHandler
    public void onPlayerFirstJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!player.hasPlayedBefore()) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            });
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            });
        }
    }
}
