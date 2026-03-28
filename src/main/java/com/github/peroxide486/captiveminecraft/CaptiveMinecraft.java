package com.github.peroxide486.captiveminecraft;

import com.github.peroxide486.captiveminecraft.database.DatabaseManager;
import com.github.peroxide486.captiveminecraft.listeners.RegisterPlayerBorderEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class CaptiveMinecraft extends JavaPlugin {
    private CaptiveMinecraft plugin;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        try {
            DatabaseManager.init();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        getServer().getPluginManager().registerEvents(new RegisterPlayerBorderEvent(), this);
        getLogger().info("CaptiveMinecraft Enabled!");
    }

    @Override
    public void onDisable() {
        DatabaseManager.close();
        getLogger().info("CaptiveMinecraft Disabled!");
    }
}
