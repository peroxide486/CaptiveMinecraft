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
            "region_z INT NOT NULL)")
    void createTable();

    @SqlUpdate("REPLACE INTO player_region(player_uuid, region_x, region_z) VALUES(:uuid, :regionX, :regionZ)")
    void upsertRegion(@Bind("uuid") UUID uuid, @Bind("regionX") int regionX, @Bind("regionZ") int regionZ);

    @SqlQuery("SELECT player_uuid AS uuid, region_x AS regionX, region_z AS regionZ FROM player_region WHERE player_uuid = :uuid")
    Regions getRegionByUUID(@Bind("uuid") UUID uuid);

    @SqlQuery("SELECT player_uuid AS uuid, plot_x, plot_z FROM player_plots")
    List<Regions> getAllPlots();
}
