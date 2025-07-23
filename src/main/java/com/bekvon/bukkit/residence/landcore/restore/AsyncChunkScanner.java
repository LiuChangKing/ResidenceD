package com.bekvon.bukkit.residence.landcore.restore;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Scan a chunk asynchronously and schedule clearing of ores and containers.
 */
public class AsyncChunkScanner extends BukkitRunnable {
    private final JavaPlugin plugin;
    private final Chunk chunk;
    private final Player player;

    public AsyncChunkScanner(JavaPlugin plugin, Chunk chunk, Player player) {
        this.plugin = plugin;
        this.chunk = chunk;
        this.player = player;
    }

    @Override
    public void run() {
        World world = chunk.getWorld();
        int minY = world.getMinHeight();
        int maxY = world.getMaxHeight();

        List<Location> toClear = new ArrayList<>();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = minY; y < maxY; y++) {
                    Block b = chunk.getBlock(x, y, z);
                    Material m = b.getType();
                    if (isOre(m) || m == Material.CHEST || m == Material.TRAPPED_CHEST || m == Material.SPAWNER) {
                        toClear.add(b.getLocation());
                    }
                }
            }
        }

        new ChunkClearTask(toClear, player).runTaskTimer(plugin, 0L, 1L);
    }

    private boolean isOre(Material m) {
        return switch (m) {
            case COAL_ORE, IRON_ORE, GOLD_ORE, REDSTONE_ORE,
                 DIAMOND_ORE, EMERALD_ORE, COPPER_ORE, LAPIS_ORE,
                 NETHER_QUARTZ_ORE, NETHER_GOLD_ORE -> true;
            default -> false;
        };
    }
}
