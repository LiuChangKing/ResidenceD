package com.bekvon.bukkit.residence.landcore.tasks;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Gradually fill an area with a floor of planks and a border of slabs
 * to avoid lagging the main thread.
 */
public class AreaFillTask extends BukkitRunnable {
    private final World world;
    private final int minX, maxX, minZ, maxZ;
    private final int floorY, slabY;
    private int x, z;
    private static final int BATCH_SIZE = 512;

    public AreaFillTask(World world, int minX, int maxX, int minZ, int maxZ, int floorY, int slabY) {
        this.world = world;
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
        this.floorY = floorY;
        this.slabY = slabY;
        this.x = minX;
        this.z = minZ;
    }

    @Override
    public void run() {
        int processed = 0;
        while (processed < BATCH_SIZE && x <= maxX) {
            world.getBlockAt(x, floorY, z).setType(Material.OAK_PLANKS, false);
            if (x == minX || x == maxX || z == minZ || z == maxZ) {
                world.getBlockAt(x, slabY, z).setType(Material.STONE_SLAB, false);
            }
            if (++z > maxZ) {
                z = minZ;
                x++;
            }
            processed++;
        }
        if (x > maxX) {
            cancel();
        }
    }
}
