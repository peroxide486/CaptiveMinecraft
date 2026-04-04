package com.github.peroxide486.captiveminecraft.utils.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.File;

public class SQLiteDatabaseManager implements DatabaseManager {
    private final Jdbi jdbi;
    private final HikariDataSource dataSource;

    public SQLiteDatabaseManager(File dataFolder, String fileName) {
        File dbFile = new File(dataFolder, fileName);
        String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setDriverClassName("org.sqlite.JDBC");
        config.setMaximumPoolSize(1);
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("foreign_keys", "off");

        this.dataSource = new HikariDataSource(config);
        this.jdbi = Jdbi.create(dataSource);
        this.jdbi.installPlugin(new SqlObjectPlugin());
    }

    @Override
    public Jdbi getJdbi() {
        return jdbi;
    }

    @Override
    public void initializeTables() {
        String sql = "CREATE TABLE IF NOT EXISTS " + DatabaseConstants.TABLE_NAME + " (" +
                DatabaseConstants.COL_UUID + " VARCHAR(36) PRIMARY KEY, " +
                DatabaseConstants.COL_REGION_X + " INT NOT NULL, " +
                DatabaseConstants.COL_REGION_Z + " INT NOT NULL, " +
                DatabaseConstants.COL_REGION_SIZE + " DOUBLE NOT NULL)";
        jdbi.useHandle(handle -> handle.execute(sql));
    }

    @Override
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
