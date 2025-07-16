package com.bekvon.bukkit.residence.listeners;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.liuchangking.dreamengine.service.RedisManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import redis.clients.jedis.Jedis;

public class CrossServerTeleportListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!com.liuchangking.dreamengine.config.Config.redisEnabled) {
            return;
        }
        Player player = event.getPlayer();
        try (Jedis j = RedisManager.getPool().getResource()) {
            String key = "res_tp:" + player.getUniqueId();
            String resName = j.get(key);
            if (resName != null) {
                j.del(key);
                Bukkit.getScheduler().runTaskLater(Residence.getInstance(), () -> {
                    ClaimedResidence res = Residence.getInstance().getResidenceManager().getByName(resName);
                    if (res != null) {
                        res.tpToResidence(player, player, false);
                    }
                }, 20L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
