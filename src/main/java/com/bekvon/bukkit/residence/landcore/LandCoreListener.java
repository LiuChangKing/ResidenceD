package com.bekvon.bukkit.residence.landcore;

import com.bekvon.bukkit.residence.Residence;
import com.liuchangking.dreamengine.api.CrossPlatformMenu;
import com.liuchangking.dreamengine.api.CrossUI;
import com.liuchangking.dreamengine.api.PlatformAPI;
import com.liuchangking.dreamengine.utils.MessageUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.util.Collections;
import java.util.List;

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
        if (manager.placeCore(event.getPlayer(), event.getBlockPlaced().getLocation(), item, lvl)) {
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                item.setAmount(item.getAmount()-1);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.PLAYER_HEAD) return;
        if (!manager.isCore(block)) return;
        if (manager.isWithdrawing(block)) {
            event.setCancelled(true);
            MessageUtil.notifyError(event.getPlayer(), "正在收回", "领地正在回收中");
            return;
        }
        event.setCancelled(true);
        Player player = event.getPlayer();
        int level = manager.get(block).getLevel();
        List<String> lore = manager.getUpgradeLore(level);

        CrossPlatformMenu<String> menu = CrossUI.stringMenu(player);
        menu.title("领地核心");
        menu.buttonAt(0, Material.NETHER_STAR,"领地升级", lore, "upgrade");
        menu.buttonAt(1, Material.BOOK,"公共权限", "set");
        menu.buttonAt(8, Material.BARRIER, "收回领地",
                Collections.singletonList("§7这会删除并清空你的领地"),
                "withdraw");
        final Block coreBlock = block;
        final List<String> upgradeLore = lore;
        menu.onClick(ev -> {
            if ("upgrade".equals(ev.getPayload())) {
                if (PlatformAPI.isBedrockPlayer(ev.getPlayer())) {
                    CrossUI.menu(ev.getPlayer(), String.class)
                            .title("确认升级?")
                            .content(String.join("\n", upgradeLore))
                            .buttonAt(3, Material.LIME_WOOL, "升级", "yes")
                            .buttonAt(5, Material.RED_WOOL, "取消", "no")
                            .onClick(resp -> {
                                resp.getPlayer().closeInventory();
                                if ("yes".equals(resp.getPayload())) {
                                    manager.upgrade(resp.getPlayer(), coreBlock);
                                }
                            })
                            .open(ev.getPlayer());
                } else {
                    ev.getPlayer().closeInventory();
                    manager.upgrade(ev.getPlayer(), coreBlock);
                }
            } else if ("set".equals(ev.getPayload())) {
                ev.getPlayer().closeInventory();
                ev.getPlayer().performCommand("res set " + manager.get(coreBlock).getResidenceName());
            } else if ("withdraw".equals(ev.getPayload())) {
                CrossUI.menu(ev.getPlayer(), String.class)
                        .title("确认收回领地?")
                        .buttonAt(3, Material.LIME_WOOL, "确定", "yes")
                        .buttonAt(5, Material.RED_WOOL, "取消", "no")
                        .onClick(resp -> {
                            resp.getPlayer().closeInventory();
                            if ("yes".equals(resp.getPayload())) {
                                manager.withdraw(resp.getPlayer(), coreBlock);
                                MessageUtil.notifySuccess(player, "正在收回", "领地正在收回中...");
                            }
                        })
                        .open(ev.getPlayer());
            }
        });
        menu.open(player);
    }


    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        if (manager.isCore(event.getBlock())) {
            MessageUtil.notifyError(event.getPlayer(), "无法破坏", "你可以右键领地核心,点击收回领地即可删除当前的领地");
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucket(PlayerBucketEmptyEvent event) {
        Block target = event.getBlockClicked().getRelative(event.getBlockFace());
        if (manager.isAdjacentToCore(target)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFlow(BlockFromToEvent event) {
        if (manager.isCore(event.getToBlock())) {
            event.setCancelled(true);
        }
    }
}
