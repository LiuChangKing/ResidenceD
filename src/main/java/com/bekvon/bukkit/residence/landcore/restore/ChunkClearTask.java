package com.bekvon.bukkit.residence.landcore.restore;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * Remove blocks from the provided locations in small batches on the main thread.
 */
public class ChunkClearTask extends BukkitRunnable {
    private final List<Location> toClear;
    private final Player player;
    private int index = 0;
    private static final int BATCH_SIZE = 512;

    public ChunkClearTask(List<Location> toClear, Player player) {
        this.toClear = toClear;
        this.player = player;
    }

    @Override
    public void run() {
        int processed = 0;
        while (processed < BATCH_SIZE && index < toClear.size()) {
            Location loc = toClear.get(index++);
            loc.getBlock().setType(Material.AIR, false);
            processed++;
        }
        if (index >= toClear.size()) {
            cancel();
        }
    }
}
