package com.github.peroxide486.captiveminecraft.utils.database;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.github.peroxide486.captiveminecraft.CaptiveMinecraft;
import com.github.peroxide486.captiveminecraft.config.DatabaseConfig;

import java.io.File;

public class DatabaseManagerFactory {
    public static DatabaseManager createDatabaseManager(CaptiveMinecraft plugin) {
        SettingsManager settingsManager = SettingsManagerBuilder
                .withYamlFile(new File("database-config.yml"))
                .configurationData(DatabaseConfig.class)
                .useDefaultMigrationService()
                .create();

        String databaseType = settingsManager.getProperty(DatabaseConfig.DATABASE_TYPE);
        switch (databaseType) {
            case "MySQL":
                String host = settingsManager.getProperty(DatabaseConfig.MYSQL_HOST);
                int port = settingsManager.getProperty(DatabaseConfig.MYSQL_PORT);
                String database = settingsManager.getProperty(DatabaseConfig.MYSQL_DATABASE);
                String queryString = settingsManager.getProperty(DatabaseConfig.MYSQL_QUERY_STRING);
                String username = settingsManager.getProperty(DatabaseConfig.MYSQL_USER);
                String password = settingsManager.getProperty(DatabaseConfig.MYSQL_PASSWORD);
                int maxPoolSize = settingsManager.getProperty(DatabaseConfig.MYSQL_MAX_POOL_SIZE);
                return new MySQLDatabaseManager(host, port, database, queryString, username, password, maxPoolSize);
            case "SQLite":
            default:
                String fileName = settingsManager.getProperty(DatabaseConfig.SQLITE_FILE_NAME);
                return new SQLiteDatabaseManager(plugin.getDataFolder(), fileName);
        }
    }
}
