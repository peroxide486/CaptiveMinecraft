package com.github.peroxide486.captiveminecraft.utils;

import org.bukkit.Location;
import org.bukkit.World;

public class RegionBorder {
    private final World world;
    private final int minX;
    private final int minZ;
    private final int maxX;
    private final int maxZ;

    public RegionBorder(World world, int minX, int maxX, int minZ, int maxZ) {
        this.world = world;
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    public boolean contains(Location location) {
        if (!location.getWorld().equals(world)) return false;
        int x = location.getBlockX();
        int z = location.getBlockZ();
        return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
    }
}
