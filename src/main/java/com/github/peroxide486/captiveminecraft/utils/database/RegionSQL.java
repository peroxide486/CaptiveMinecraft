package com.github.peroxide486.captiveminecraft.utils.database;

import com.github.peroxide486.captiveminecraft.utils.Regions;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.UUID;

@RegisterConstructorMapper(Regions.class)
public interface RegionSQL {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS player_region (" +
            "player_uuid VARCHAR(36) PRIMARY KEY, " +
            "region_x INT NOT NULL, " +
            "region_z INT NOT NULL, " +
            "region_size DOUBLE NOT NULL)")
    void createTable();

    @SqlUpdate("REPLACE INTO player_region(player_uuid, region_x, region_z, region_size) VALUES(:uuid, :regionX, :regionZ, :regionSize)")
    void upsertRegion(@Bind("uuid") UUID uuid, @Bind("regionX") int regionX, @Bind("regionZ") int regionZ, @Bind("regionSize") double regionSize);

    @SqlQuery("SELECT player_uuid AS uuid, region_x AS regionX, region_z AS regionZ, region_size AS regionSize FROM player_region WHERE player_uuid = :uuid")
    Regions getRegionByUUID(@Bind("uuid") UUID uuid);

    @SqlQuery("SELECT player_uuid AS uuid, region_x, region_z, region_size FROM player_region")
    List<Regions> getAllRegions();
}
