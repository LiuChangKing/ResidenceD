package com.bekvon.bukkit.residence.landcore;

import net.Zrips.CMILib.Locale.YmlMaker;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/** Configuration for LandCore module. */
public class LandCoreConfig {
    private final JavaPlugin plugin;
    private final File file;
    private FileConfiguration cfg;
    public static class UpgradeItem {
        private final int amount;
        private final String lore;

        public UpgradeItem(int amount, String lore) {
            this.amount = amount;
            this.lore = lore;
        }

        public int getAmount() {
            return amount;
        }

        public String getLore() {
            return lore;
        }
    }

    public static class UpgradeCost {
        private final int money;
        private final java.util.List<UpgradeItem> items;

        public UpgradeCost(int money, java.util.List<UpgradeItem> items) {
            this.money = money;
            this.items = items;
        }

        public int getMoney() {
            return money;
        }

        public java.util.List<UpgradeItem> getItems() {
            return items;
        }
    }

    private final Map<Integer, UpgradeCost> upgradeCosts = new HashMap<>();
    public static class CoreItem {
        private final String name;
        private final List<String> lore;

        public CoreItem(String name, List<String> lore) {
            this.name = name;
            this.lore = lore;
        }

        public String getName() { return name; }
        public List<String> getLore() { return lore; }
    }

    private String defaultItemName;
    private List<String> defaultItemLore = new ArrayList<>();
    private final Map<Integer, CoreItem> itemMap = new HashMap<>();
    private int minPlaceY;
    private int maxPlaceY;

    public LandCoreConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "landcore.yml");
    }

    public void load() {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                YmlMaker yml = new YmlMaker(plugin, "landcore.yml");
                yml.saveDefaultConfig();
                yml.ConfigFile.renameTo(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cfg = YamlConfiguration.loadConfiguration(file);
        upgradeCosts.clear();
        if (cfg.isConfigurationSection("upgrade-costs")) {
            for (String k : cfg.getConfigurationSection("upgrade-costs").getKeys(false)) {
                int level = Integer.parseInt(k);
                int money = 0;
                List<UpgradeItem> items = new ArrayList<>();
                if (cfg.isConfigurationSection("upgrade-costs." + k)) {
                    var sec = cfg.getConfigurationSection("upgrade-costs." + k);
                    money = sec.getInt("money", 0);
                    if (sec.isList("items")) {
                        for (Object obj : sec.getList("items")) {
                            if (!(obj instanceof Map<?,?> map)) continue;
                            Object amtObj = map.get("amount");
                            Object loreObj = map.get("lore");
                            int amt = amtObj instanceof Number ? ((Number) amtObj).intValue() : 1;
                            String lore = loreObj == null ? null : String.valueOf(loreObj);
                            items.add(new UpgradeItem(amt, lore));
                        }
                    }
                } else {
                    money = cfg.getInt("upgrade-costs." + k, 0);
                }
                upgradeCosts.put(level, new UpgradeCost(money, items));
            }
        }
        defaultItemName = cfg.getString("item.default.name", "§a%level_cn%领地核心");
        defaultItemLore = cfg.getStringList("item.default.lore");
        itemMap.clear();
        if (cfg.isConfigurationSection("item")) {
            var sec = cfg.getConfigurationSection("item");
            for (String k : sec.getKeys(false)) {
                if ("default".equalsIgnoreCase(k)) continue;
                int lvl;
                try { lvl = Integer.parseInt(k); } catch (NumberFormatException e) { continue; }
                String name = sec.getString(k+".name", defaultItemName);
                List<String> lore = sec.getStringList(k+".lore");
                if (lore.isEmpty()) lore = new ArrayList<>(defaultItemLore);
                itemMap.put(lvl, new CoreItem(name, lore));
            }
        }
        minPlaceY = cfg.getInt("place-height.min", 0);
        maxPlaceY = cfg.getInt("place-height.max", 320);
    }

    public UpgradeCost getUpgradeCost(int level) {
        return upgradeCosts.getOrDefault(level, new UpgradeCost(0, new ArrayList<>()));
    }

    public FileConfiguration getConfig() {
        return cfg;
    }

    public CoreItem getItem(int level) {
        CoreItem it = itemMap.get(level);
        if (it != null) return it;
        return new CoreItem(defaultItemName,
                new ArrayList<>(defaultItemLore));
    }

    public void setItem(int level, String name, List<String> lore) {
        CoreItem ci = new CoreItem(name, lore);
        itemMap.put(level, ci);
        cfg.set("item."+level+".name", name);
        cfg.set("item."+level+".lore", lore);
    }

    public int getMinPlaceY() {
        return minPlaceY;
    }

    public int getMaxPlaceY() {
        return maxPlaceY;
    }

    public void save() {
        try { cfg.save(file); } catch (Exception ignored) {}
    }
}
