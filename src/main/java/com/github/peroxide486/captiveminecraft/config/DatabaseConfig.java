package com.github.peroxide486.captiveminecraft.config;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class DatabaseConfig implements SettingsHolder {

    @Comment("Database type, supports MySQL and SQLite.")
    public static final Property<String> DATABASE_TYPE =
            newProperty("database-type", "SQLite");

    public static final Property<String> SQLITE_FILE_NAME =
            newProperty("sqlite.file-name", "player_region.db");

    public static final Property<String> MYSQL_HOST =
            newProperty("mysql.host", "localhost");

    public static final Property<Integer> MYSQL_PORT =
            newProperty("mysql.port", 3306);

    public static final Property<String> MYSQL_DATABASE =
            newProperty("mysql.database", "captiveminecraft");

    public static final Property<String> MYSQL_USER =
            newProperty("mysql.user", "root");

    public static final Property<String> MYSQL_PASSWORD =
            newProperty("mysql.password", "547193");

    public static final Property<Integer> MYSQL_MAX_POOL_SIZE =
            newProperty("mysql.max-pool-size", 10);

    public static final Property<String> MYSQL_QUERY_STRING =
            newProperty("mysql.query-string", "?useSSL=false&serverTimezone=UTC&characterEncoding=utf8");
}
