package com.github.peroxide486.captiveminecraft;

import com.github.peroxide486.captiveminecraft.commands.BorderChangeCommand;
import com.github.peroxide486.captiveminecraft.listeners.PlayerRegionalismEvent;
import com.github.peroxide486.captiveminecraft.utils.RegionManager;
import com.github.peroxide486.captiveminecraft.utils.database.DatabaseManager;
import com.github.peroxide486.captiveminecraft.utils.database.DatabaseManagerFactory;
import com.github.peroxide486.captiveminecraft.utils.database.RegionSQL;
import com.github.peroxide486.captiveminecraft.utils.database.StorageService;
import com.github.yannicklamprecht.worldborder.api.WorldBorderApi;
import com.sk89q.worldguard.WorldGuard;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class CaptiveMinecraft extends JavaPlugin {
    private WorldBorderApi worldBorderAPI;
    private WorldGuard worldGuardAPI;
    private DatabaseManager databaseManager;
    private StorageService storageService;
    private RegionManager regionManager;
    private RegionSQL regionSQL;
    private BorderChangeCommand borderChangeCommand;

    @Override
    public void onEnable() {
        RegisteredServiceProvider<WorldBorderApi> worldBorderAPIRegisteredServiceProvider = getServer().getServicesManager().getRegistration(WorldBorderApi.class);

        if (worldBorderAPIRegisteredServiceProvider == null) {
            getLogger().info("WorldBorderApi not found");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        worldGuardAPI = WorldGuard.getInstance();
        worldBorderAPI = worldBorderAPIRegisteredServiceProvider.getProvider();
        databaseManager = DatabaseManagerFactory.createDatabaseManager(this);
        databaseManager.initializeTables();

        storageService = new StorageService(databaseManager.getJdbi());
        regionManager = new RegionManager(this, storageService, worldBorderAPI, regionSQL);

        getServer().getPluginManager().registerEvents(new PlayerRegionalismEvent(this, regionManager), this);
        try {
            getCommand("").setExecutor(borderChangeCommand);
        } catch (NullPointerException e) {
            getLogger().warning("There is a problem with command registration" + e.getMessage());
        }

        getLogger().info("CaptiveMinecraft Enabled!");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("CaptiveMinecraft Disabled!");
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
}
