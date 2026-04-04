package com.github.peroxide486.captiveminecraft.utils.database;

import com.github.peroxide486.captiveminecraft.utils.Regions;
import org.jdbi.v3.core.Jdbi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StorageService {
    private final RegionSQL regionSQL;

    public StorageService(Jdbi jdbi) {
        this.regionSQL = jdbi.onDemand(RegionSQL.class);
        regionSQL.createTable();
    }

    public void saveRegion(UUID uuid, Regions regions) {
        regionSQL.upsertRegion(uuid, regions.getRegionX(), regions.getRegionZ());
    }

    public Regions loadRegion(UUID uuid) {
        return regionSQL.getRegionByUUID(uuid);
    }

    public Map<UUID, Regions> loadAllRegions() {
        List<Regions> regions = regionSQL.getAllPlots();
        Map<UUID, Regions> map = new HashMap<>();
        for (Regions region : regions) {
            map.put(region.getUUID(), new Regions(region.getRegionX(), region.getRegionZ()));
        }
        return map;
    }
}
