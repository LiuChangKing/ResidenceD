package com.bekvon.bukkit.residence.landcore;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.CuboidArea;
import com.liuchangking.dreamengine.shop.hook.VaultHook;
import com.liuchangking.dreamengine.utils.MessageUtil;
import com.liuchangking.dreamengine.utils.HeadUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.entity.Player;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;

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
    private final NamespacedKey holoKey;
    private static final String CORE_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzk1ZDM3OTkzZTU5NDA4MjY3ODQ3MmJmOWQ4NjgyMzQxM2MyNTBkNDMzMmEyYzdkOGM1MmRlNDk3NmIzNjIifX19";

    public LandCoreManager(Residence plugin) {
        this.plugin = plugin;
        this.config = new LandCoreConfig(plugin);
        this.coreKey = new NamespacedKey(plugin, "landcore");
        this.ownerKey = new NamespacedKey(plugin, "owner");
        this.holoKey = new NamespacedKey(plugin, "landcore_holo");
    }

    public void load() {
        config.load();
        if (config.getConfig().isConfigurationSection("cores")) {
            for (String key : config.getConfig().getConfigurationSection("cores").getKeys(false)) {
                int lvl = config.getConfig().getInt("cores."+key+".level",1);
                String res = config.getConfig().getString("cores."+key+".res");
                cores.put(key, new LandCoreData(lvl, res));
                Location loc = parseKey(key);
                String owner = null;
                if (loc != null && loc.getBlock().getState() instanceof Skull skull) {
                    owner = skull.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
                }
                if (loc != null) {
                    spawnOrUpdateHologram(loc, lvl, owner);
                }
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

    private Location parseKey(String key) {
        String[] arr = key.split(",");
        if (arr.length != 4) return null;
        World w = plugin.getServer().getWorld(arr[0]);
        if (w == null) return null;
        try {
            int x = Integer.parseInt(arr[1]);
            int y = Integer.parseInt(arr[2]);
            int z = Integer.parseInt(arr[3]);
            return new Location(w, x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean isCore(Block block) {
        return cores.containsKey(key(block.getLocation()));
    }

    /** Check if the given block or any adjacent block is a land core. */
    public boolean isAdjacentToCore(Block block) {
        if (isCore(block)) return true;
        for (BlockFace face : new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH,
                BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST}) {
            if (isCore(block.getRelative(face))) {
                return true;
            }
        }
        return false;
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
            HeadUtil.applyTexture(item, CORE_TEXTURE);
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
            String finalOwner = owner;
            meta.setLore(lore.stream().map(l -> apply(l, lvl, finalOwner)).toList());
        } else {
            meta.setLore(null);
        }
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        HeadUtil.applyTexture(item, CORE_TEXTURE);
    }

    /**
     * Update all core items in a player's inventory.
     *
     * <p>When using {@link PlayerInventory#getContents()} Bukkit returns a
     * copy of the item array, meaning modifications to the returned items will
     * not automatically be applied to the inventory. To ensure that updated
     * meta is sent to the client we must set the items back into their slots
     * after updating.</p>
     */
    public void updatePlayerInventory(Player player) {
        PlayerInventory inv = player.getInventory();
        for (int slot = 0; slot < inv.getSize(); slot++) {
            ItemStack item = inv.getItem(slot);
            updateCoreItem(item);
            inv.setItem(slot, item);
        }
    }

    private void spawnOrUpdateHologram(Location loc, int level, String ownerName) {
        if (loc == null || loc.getWorld() == null) return;
        Location holoLoc = loc.clone().add(0.5, 0.5, 0.5);
        String name = apply(config.getItem(level).getName(), level, ownerName == null ? "" : ownerName);
        for (Entity ent : holoLoc.getWorld().getNearbyEntities(holoLoc, 0.5, 0.5, 0.5)) {
            if (ent instanceof ArmorStand stand &&
                    stand.getPersistentDataContainer().has(holoKey, PersistentDataType.INTEGER)) {
                stand.setCustomName(name);
                stand.setCustomNameVisible(true);
                return;
            }
        }
        ArmorStand stand = (ArmorStand) holoLoc.getWorld().spawn(holoLoc, ArmorStand.class);
        stand.setInvisible(true);
        stand.setMarker(true);
        stand.setGravity(false);
        stand.setCustomNameVisible(true);
        stand.setCustomName(name);
        stand.getPersistentDataContainer().set(holoKey, PersistentDataType.INTEGER, 1);
    }

    public void removeAllHolograms() {
        for (String k : cores.keySet()) {
            Location loc = parseKey(k);
            if (loc == null || loc.getWorld() == null) continue;
            Location holoLoc = loc.clone().add(0.5, 1.2, 0.5);
            for (Entity ent : holoLoc.getWorld().getNearbyEntities(holoLoc, 0.5, 0.5, 0.5)) {
                if (ent instanceof ArmorStand stand &&
                        stand.getPersistentDataContainer().has(holoKey, PersistentDataType.INTEGER)) {
                    stand.remove();
                }
            }
        }
    }


    /** Generate a random alphanumeric string of given length. */
    private String randomString(int len) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, len);
    }

    /**
     * Copy skull-related metadata from an item directly onto a block state.
     *
     * <p>This preserves the custom texture set by {@link HeadUtil#applyTexture}
     * so the placed head looks identical to the item.</p>
     */
    private void applyItemToSkull(Skull skull, ItemStack item, int level) {
        if (skull == null || item == null) return;
        skull.getPersistentDataContainer().set(coreKey, PersistentDataType.INTEGER, level);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        String owner = meta.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
        if (owner != null) {
            skull.getPersistentDataContainer().set(ownerKey, PersistentDataType.STRING, owner);
        }
        if (meta instanceof SkullMeta sm) {
            PlayerProfile ownerProfile = sm.getOwnerProfile();
            if (ownerProfile != null) {
                skull.setOwnerProfile(ownerProfile);
                return;
            }
        }
        // Fallback to default core texture if item lacks a readable profile
        ItemStack temp = new ItemStack(Material.PLAYER_HEAD);
        ItemStack tmp = HeadUtil.applyTexture(temp, CORE_TEXTURE);
        ItemMeta tm = tmp.getItemMeta();
        if (tm instanceof SkullMeta sm2 && sm2.getOwnerProfile() != null) {
            skull.setOwnerProfile(sm2.getOwnerProfile());
        }
    }

    /**
     * Place a land core if the location is within allowed height.
     * @return true if placement succeeded
     */
    public boolean placeCore(Player player, Location loc, ItemStack item, int level) {
        if (loc.getBlockY() < config.getMinPlaceY() || loc.getBlockY() > config.getMaxPlaceY()) {
            MessageUtil.notifyError(player, "放置失败", "领地核心只能在Y" + config.getMinPlaceY() + "-" + config.getMaxPlaceY() + "之间使用");
            return false;
        }
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
        // use resadmin=true to avoid Residence plugin charging player again
        if (!plugin.getResidenceManager().addResidence(player, resName, loc1, loc2, true)) {
            return  false; // creation failed
        }
        // floor and border
        int floorY = loc.getBlockY() - 1;
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                world.getBlockAt(x, floorY, z).setType(Material.OAK_PLANKS);
                if (x == minX || x == maxX || z == minZ || z == maxZ) {
                    world.getBlockAt(x, loc.getBlockY(), z).setType(Material.STONE_SLAB);
                }
            }
        }
        int px = loc.getBlockX();
        int pz = loc.getBlockZ();
        if (px == minX || px == maxX || pz == minZ || pz == maxZ) {
            // this block is reset by BlockPlaceEvent when cancelled,
            // so delay the slab placement by one tick
            Bukkit.getScheduler().runTask(plugin, () ->
                    world.getBlockAt(px, loc.getBlockY(), pz).setType(Material.STONE_SLAB)
            );
        }
        int centerX = (minX + maxX) / 2;
        int centerZ = (minZ + maxZ) / 2;
        Location coreLoc = new Location(world, centerX, loc.getBlockY(), centerZ);
        Block core = coreLoc.getBlock();
        core.setType(Material.PLAYER_HEAD);
        String ownerName = null;
        if (core.getState() instanceof Skull skull) {
            applyItemToSkull(skull, item, level);
            ItemMeta im = item.getItemMeta();
            if (im != null) {
                ownerName = im.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
            }
            skull.update(true);
        }
        spawnOrUpdateHologram(coreLoc, level, ownerName);
        LandCoreData data = new LandCoreData(level,resName);
        cores.put(key(coreLoc), data);
        save();
        return true;
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
            MessageUtil.notifyError(player, "升级失败", "已经到达最大等级");
            return;
        }
        ClaimedResidence res = plugin.getResidenceManager().getByName(data.getResidenceName());
        if (res == null) {
            MessageUtil.notifyError(player, "升级失败", "领地不存在");
            return;
        }

        int newLevel = level + 1;
        World world = block.getWorld();
        int chunkX = block.getChunk().getX();
        int chunkZ = block.getChunk().getZ();
        int radius = newLevel - 1;
        int minX = (chunkX - radius) * 16;
        int maxX = (chunkX + radius) * 16 + 15;
        int minZ = (chunkZ - radius) * 16;
        int maxZ = (chunkZ + radius) * 16 + 15;
        int minY = world.getMinHeight();
        int maxY = world.getMaxHeight() - 1;
        CuboidArea newArea = new CuboidArea(new Location(world, minX, minY, minZ),
                new Location(world, maxX, maxY, maxZ));
        // check collision before charging player
        String collisionRes = plugin.getResidenceManager().checkAreaCollision(newArea, res);
        if (collisionRes != null) {
            MessageUtil.notifyError(player, "升级失败", "与其他领地冲突, 领地名:"+collisionRes);
            return;
        }

        LandCoreConfig.UpgradeCost cost = config.getUpgradeCost(level + 1);
        int money = cost.getMoney();
        // check item availability first
        for (LandCoreConfig.UpgradeItem it : cost.getItems()) {
            if (countMatchingItems(player, it) < it.getAmount()) {
                String name = it.getLore() == null ? "物品" : it.getLore();
                MessageUtil.notifyError(player, "升级失败", "缺少物品: " + name + " x" + it.getAmount());
                return;
            }
        }
        if (money > 0 && !VaultHook.takeMoney(player, money)) {
            MessageUtil.notifyError(player, "升级失败", "金币不足," + money);
            return;
        }

        // use resadmin=true to avoid Residence plugin charging player again
        boolean replaced = res.replaceArea(player, newArea, "main", true);
        if (!replaced) {
            if (money > 0) {
                VaultHook.giveMoney(player, money);
            }
            MessageUtil.notifyError(player, "升级失败");
            return;
        }

        for (LandCoreConfig.UpgradeItem it : cost.getItems()) {
            removeMatchingItems(player, it, it.getAmount());
        }

        String ownerName = null;
        if (block.getState() instanceof Skull skull) {
            skull.getPersistentDataContainer().set(coreKey, PersistentDataType.INTEGER, newLevel);
            ownerName = skull.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
            skull.update(true);
        }
        spawnOrUpdateHologram(block.getLocation(), newLevel, ownerName);
        data.setLevel(newLevel);
        save();
        MessageUtil.notifySuccess(player, "升级成功", "已提升至等级" + newLevel);
    }
}