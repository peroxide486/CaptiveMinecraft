package com.github.peroxide486.captiveminecraft.database;

import com.github.peroxide486.captiveminecraft.CaptiveMinecraft;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class DatabaseManager {
    private static CaptiveMinecraft plugin;
    private static SQLiteDataSource sqLiteDataSource;
    private static HikariDataSource hikariDataSource;

    public static void init() throws SQLException {
        if (Objects.equals(plugin.getConfig().getString("database.type"), "sqlite")) {
            sqLiteDataSource = new SQLiteDataSource();
            sqLiteDataSource.setUrl("jdbc:sqlite:" + plugin.getConfig().getString("database.mysql.username"));
            try (Connection connection = sqLiteDataSource.getConnection()) {
                String sql = getCreateTableSql();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();

            }
        } else if (Objects.equals(plugin.getConfig().getString("database.type"), "mysql")) {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl("jdbc:mysql://" + plugin.getConfig().getString("database.mysql.host") + ":" + plugin.getConfig().getString("database.mysql.port") + "/" + plugin.getConfig().getString("database.mysql.database") + plugin.getConfig().getString("database.mysql.connectionOptions"));
            hikariConfig.setUsername(plugin.getConfig().getString("database.mysql.username"));
            hikariConfig.setPassword(plugin.getConfig().getString("database.mysql.password"));
            hikariConfig.setMaximumPoolSize(plugin.getConfig().getInt("database.mysql.maximumPoolSize"));
            hikariDataSource = new HikariDataSource(hikariConfig);
            hikariDataSource.getConnection();
            String sql = getCreateTableSql();
            try (PreparedStatement preparedStatement = hikariDataSource.getConnection().prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            }
        } else {
            plugin.getLogger().warning("Database type not supported! Disable plugin!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public static void close() {
        if (Objects.equals(plugin.getConfig().getString("database.type"), "sqlite")) {
            plugin.getLogger().info("Closing SQLite Database!");
        } else if (Objects.equals(plugin.getConfig().getString("database.type"), "mysql")) {
            hikariDataSource.close();
            plugin.getLogger().info("Closing MySQL Database!");
        } else {
            plugin.getLogger().warning("Database type not supported! Disable plugin!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    private static String getCreateTableSql() {
        if (Objects.equals(plugin.getConfig().getString("database.type"), "mysql")) {
            return "CREATE TABLE IF NOT EXISTS players (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "player_name VARCHAR(50) NOT NULL," +
                    "player_uuid CHAR(36) NOT NULL UNIQUE," +
                    "x DOUBLE NOT NULL," +
                    "z DOUBLE NOT NULL," +
                    "total_size BIGINT NOT NULL," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
        } else if (Objects.equals(plugin.getConfig().getString("database.type"), "sqlite")) {
            return "CREATE TABLE IF NOT EXISTS players (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "player_name TEXT NOT NULL," +
                    "player_uuid TEXT NOT NULL UNIQUE," +
                    "x REAL NOT NULL," +
                    "z REAL NOT NULL," +
                    "total_size INTEGER NOT NULL," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
        } else {
            plugin.getLogger().warning("Database type not supported! Disable plugin!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return null;
        }
    }
}
