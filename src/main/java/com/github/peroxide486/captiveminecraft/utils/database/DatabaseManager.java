package com.github.peroxide486.captiveminecraft.utils.database;

import org.jdbi.v3.core.Jdbi;

public interface DatabaseManager {
    Jdbi getJdbi();

    void initializeTables();

    void close();
}
