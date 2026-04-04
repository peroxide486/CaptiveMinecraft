package com.github.peroxide486.captiveminecraft.utils;

import java.util.UUID;

public class Regions {
    private UUID uuid;
    private int regionX;
    private int regionZ;
    
    public Regions() {

    }
    
    public Regions(UUID uuid, int regionX, int regionZ) {
        this.uuid = uuid;
        this.regionX = regionX;
        this.regionZ = regionZ;
    }
    
    public Regions(int regionX, int regionZ) {
        this.regionX = regionX;
        this.regionZ = regionZ;
    }
    
    public UUID getUUID() {
        return uuid;
    }
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public int getRegionX() {
        return regionX;
    }
    public void setRegionX(int regionX) {
        this.regionX = regionX;
    }

    public int getRegionZ() {
        return regionZ;
    }
    public void setRegionZ(int regionZ) {
        this.regionZ = regionZ;
    }
}
