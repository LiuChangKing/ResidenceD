package com.bekvon.bukkit.residence.landcore;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.liuchangking.dreamengine.shop.hook.VaultHook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.UUID;

/** Manager for land cores. */
public class LandCoreManager {
    private final Residence plugin;
    private final LandCoreConfig config;
    private final Map<String, LandCoreData> cores = new HashMap<>();
    private final NamespacedKey coreKey;
    private final NamespacedKey ownerKey;

    public LandCoreManager(Residence plugin) {
        this.plugin = plugin;
        this.config = new LandCoreConfig(plugin);
        this.coreKey = new NamespacedKey(plugin, "landcore");
        this.ownerKey = new NamespacedKey(plugin, "owner");
    }

    public void load() {
        config.load();
        if (config.getConfig().isConfigurationSection("cores")) {
            for (String key : config.getConfig().getConfigurationSection("cores").getKeys(false)) {
                int lvl = config.getConfig().getInt("cores."+key+".level",1);
                String res = config.getConfig().getString("cores."+key+".res");
                cores.put(key, new LandCoreData(lvl, res));
            }
        }
    }

    public void save() {
        for (Map.Entry<String, LandCoreData> e : cores.entrySet()) {
            config.getConfig().set("cores."+e.getKey()+".level", e.getValue().getLevel());
            config.getConfig().set("cores."+e.getKey()+".res", e.getValue().getResidenceName());
        }
        config.save();
    }

    private String key(Location loc) {
        return loc.getWorld().getName()+","+loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ();
    }

    public boolean isCore(Block block) {
        return cores.containsKey(key(block.getLocation()));
    }

    public LandCoreData get(Block block) {
        return cores.get(key(block.getLocation()));
    }

    /**
     * Create a land core item with the specified level.
     * If an owner is provided, the item lore will display their name
     * and the owner name will be stored in the item's data.
     */
    public ItemStack createCoreItem(int level, Player owner) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            LandCoreConfig.CoreItem def = config.getItem(level);
            String ownerName = owner != null ? owner.getName() : "";
            String name = apply(def.getName(), level, ownerName);
            meta.setDisplayName(name);
            meta.getPersistentDataContainer().set(coreKey, PersistentDataType.INTEGER, level);
            if (owner != null) {
                meta.getPersistentDataContainer().set(ownerKey, PersistentDataType.STRING, ownerName);
            }
            List<String> lore = def.getLore();
            if (lore != null && !lore.isEmpty()) {
                meta.setLore(lore.stream().map(l -> apply(l, level, ownerName)).toList());
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack createCoreItem(int level) {
        return createCoreItem(level, null);
    }

    private String apply(String str, int level, String playerName) {
        return str
                .replace("%level%", String.valueOf(level))
                .replace("%level_cn%", chineseNumber(level))
                .replace("%player%", playerName);
    }

    private String chineseNumber(int level) {
        String[] arr = {"零","一","二","三","四","五","六","七","八","九","十"};
        if (level >= 0 && level < arr.length) {
            return arr[level];
        }
        return String.valueOf(level);
    }

    /** Update an existing core item to use latest name and lore from config. */
    public void updateCoreItem(ItemStack item) {
        if (item == null || item.getType() != Material.PLAYER_HEAD) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        Integer lvl = meta.getPersistentDataContainer().get(coreKey, PersistentDataType.INTEGER);
        if (lvl == null) return;
        LandCoreConfig.CoreItem def = config.getItem(lvl);
        String owner = meta.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
        if (owner == null) owner = "";
        String name = apply(def.getName(), lvl, owner);
        List<String> lore = def.getLore();
        if (lore != null && !lore.isEmpty()) {
            meta.setLore(lore.stream().map(l -> apply(l, lvl, owner)).toList());
        } else {
            meta.setLore(null);
        }
        meta.setDisplayName(name);
        item.setItemMeta(meta);
    }

    /** Update all core items in a player's inventory. */
    public void updatePlayerInventory(Player player) {
        PlayerInventory inv = player.getInventory();
        for (ItemStack it : inv.getContents()) {
            updateCoreItem(it);
        }
    }


    /** Generate a random alphanumeric string of given length. */
    private String randomString(int len) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, len);
    }

    public void placeCore(Player player, Location loc, ItemStack item, int level) {
        World world = loc.getWorld();
        int chunkX = loc.getChunk().getX();
        int chunkZ = loc.getChunk().getZ();
        int radius = level - 1;
        int minX = (chunkX - radius) * 16;
        int maxX = (chunkX + radius) * 16 + 15;
        int minZ = (chunkZ - radius) * 16;
        int maxZ = (chunkZ + radius) * 16 + 15;
        int minY = world.getMinHeight();
        int maxY = world.getMaxHeight() - 1;
        Location loc1 = new Location(world, minX, minY, minZ);
        Location loc2 = new Location(world, maxX, maxY, maxZ);
        String resName = player.getName() + "的领地" + randomString(4);
        if (!plugin.getResidenceManager().addResidence(player, resName, loc1, loc2, false)) {
            return; // creation failed
        }
        // floor and border
        int floorY = loc.getBlockY() - 1;
        for (int x=minX; x<=maxX; x++) {
            for (int z=minZ; z<=maxZ; z++) {
                world.getBlockAt(x,floorY,z).setType(Material.OAK_PLANKS);
                if (x==minX || x==maxX || z==minZ || z==maxZ) {
                    world.getBlockAt(x, loc.getBlockY(), z).setType(Material.STONE_SLAB);
                }
            }
        }
        Block core = loc.getBlock();
        core.setType(Material.PLAYER_HEAD);
        if (core.getState() instanceof Skull skull) {
            skull.getPersistentDataContainer().set(coreKey, PersistentDataType.INTEGER, level);
            ItemMeta im = item.getItemMeta();
            if (im != null) {
                String ownerName = im.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
                if (ownerName != null) {
                    skull.getPersistentDataContainer().set(ownerKey, PersistentDataType.STRING, ownerName);
                }
            }
            skull.update(true);
        }
        LandCoreData data = new LandCoreData(level,resName);
        cores.put(key(loc), data);
        save();
    }

    private int countMatchingItems(Player player, LandCoreConfig.UpgradeItem req) {
        int count = 0;
        for (ItemStack is : player.getInventory().getContents()) {
            if (is == null) continue;
            ItemMeta meta = is.getItemMeta();
            if (meta == null || !meta.hasLore()) continue;
            boolean match = false;
            if (req.getLore() == null) {
                match = true;
            } else {
                for (String l : meta.getLore()) {
                    if (l != null && l.contains(req.getLore())) {
                        match = true;
                        break;
                    }
                }
            }
            if (match) count += is.getAmount();
        }
        return count;
    }

    private void removeMatchingItems(Player player, LandCoreConfig.UpgradeItem req, int amount) {
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length && amount > 0; i++) {
            ItemStack is = contents[i];
            if (is == null) continue;
            ItemMeta meta = is.getItemMeta();
            if (meta == null || !meta.hasLore()) continue;
            boolean match = false;
            if (req.getLore() == null) {
                match = true;
            } else {
                for (String l : meta.getLore()) {
                    if (l != null && l.contains(req.getLore())) {
                        match = true;
                        break;
                    }
                }
            }
            if (!match) continue;
            int take = Math.min(is.getAmount(), amount);
            is.setAmount(is.getAmount() - take);
            if (is.getAmount() == 0) contents[i] = null;
            amount -= take;
        }
        player.getInventory().setContents(contents);
    }

    public void upgrade(Player player, Block block) {
        LandCoreData data = get(block);
        if (data == null) return;
        int level = data.getLevel();
        if (level >= 5) {
            player.sendMessage("已经到达最大等级");
            return;
        }
        LandCoreConfig.UpgradeCost cost = config.getUpgradeCost(level + 1);
        int money = cost.getMoney();
        // check item availability first
        for (LandCoreConfig.UpgradeItem it : cost.getItems()) {
            if (countMatchingItems(player, it) < it.getAmount()) {
                String name = it.getLore() == null ? "物品" : it.getLore();
                player.sendMessage("缺少物品: " + name + " x" + it.getAmount());
                return;
            }
        }
        if (money > 0 && !VaultHook.takeMoney(player, money)) {
            player.sendMessage("金币不足," + money);
            return;
        }
        for (LandCoreConfig.UpgradeItem it : cost.getItems()) {
            removeMatchingItems(player, it, it.getAmount());
        }
        // remove old residence
        ClaimedResidence res = plugin.getResidenceManager().getByName(data.getResidenceName());
        if (res != null) {
            plugin.getResidenceManager().removeResidence(res);
        }
        data.setLevel(level+1);
        placeCore(player, block.getLocation(), createCoreItem(data.getLevel(), player), data.getLevel());
    }
}
