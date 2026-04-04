package com.github.peroxide486.captiveminecraft.utils.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class MySQLDatabaseManager implements DatabaseManager {
    private final Jdbi jdbi;
    private final HikariDataSource dataSource;

    public MySQLDatabaseManager(String host, int port, String database, String queryString, String username, String password, int maxPoolSize) {
        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + queryString;

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(maxPoolSize);
        config.setConnectionTestQuery("SELECT 1");

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
                DatabaseConstants.COL_REGION_SIZE + " DOUBLE NOT NULL) " +
                "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
        jdbi.useHandle(handle -> handle.execute(sql));
    }

    @Override
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
