package com.github.peroxide486.captiveminecraft;

import com.github.peroxide486.captiveminecraft.utils.RegionManager;
import com.github.peroxide486.captiveminecraft.utils.database.DatabaseManager;
import com.github.peroxide486.captiveminecraft.utils.database.DatabaseManagerFactory;
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
        regionManager = new RegionManager(this, storageService);

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
