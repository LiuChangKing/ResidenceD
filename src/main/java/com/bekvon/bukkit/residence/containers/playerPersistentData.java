package com.bekvon.bukkit.residence.containers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class playerPersistentData {

    protected static ConcurrentHashMap<UUID, playerPersistentData> playersData = new ConcurrentHashMap<UUID, playerPersistentData>();

    private Location lastOutsideLoc = null;

    public Location getLastOutsideLoc() {
        return lastOutsideLoc;
    }

    public void setLastOutsideLoc(Location lastOutsideLoc) {
        this.lastOutsideLoc = lastOutsideLoc;
    }


    public static playerPersistentData get(Player player) {
        return get(player.getUniqueId());
    }

    public static playerPersistentData get(UUID uuid) {
        return playersData.computeIfAbsent(uuid, k -> new playerPersistentData());
    }

    public static void remove(UUID uuid) {
        playersData.remove(uuid);
    }
}
