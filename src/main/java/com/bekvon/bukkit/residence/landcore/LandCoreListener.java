package com.bekvon.bukkit.residence.landcore;

import com.bekvon.bukkit.residence.Residence;
import com.liuchangking.dreamengine.api.CrossPlatformMenu;
import com.liuchangking.dreamengine.api.CrossUI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class LandCoreListener implements Listener {
    private final Residence plugin;
    private final LandCoreManager manager;
    private final NamespacedKey key;

    public LandCoreListener(Residence plugin, LandCoreManager manager) {
        this.plugin = plugin;
        this.manager = manager;
        this.key = new NamespacedKey(plugin, "landcore");
    }

    private boolean isCoreItem(ItemStack item) {
        if (item == null || item.getType() != Material.PLAYER_HEAD) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        boolean result = pdc.has(key, PersistentDataType.INTEGER);
        if (result) {
            manager.updateCoreItem(item);
        }
        return result;
    }

    private int parseLevel(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            Integer lvl = pdc.get(key, PersistentDataType.INTEGER);
            if (lvl != null) return lvl;
        }
        return 1;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (!isCoreItem(item)) return;
        event.setCancelled(true);
        int lvl = parseLevel(item);
        manager.placeCore(event.getPlayer(), event.getBlockPlaced().getLocation(), item, lvl);
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount()-1);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.PLAYER_HEAD) return;
        if (!manager.isCore(block)) return;
        event.setCancelled(true);
        CrossPlatformMenu<String> menu = CrossUI.stringMenu(event.getPlayer());
        menu.title("领地核心");
        menu.button("领地升级", "upgrade");
        menu.button("领地设置", "set");
        menu.onClick(ev -> {
            if ("upgrade".equals(ev.getPayload())) {
                manager.upgrade(ev.getPlayer(), block);
            } else if ("set".equals(ev.getPayload())) {
                ev.getPlayer().performCommand("res set " + manager.get(block).getResidenceName());
            }
        });
        menu.open(event.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        manager.updatePlayerInventory(event.getPlayer());
    }
}
