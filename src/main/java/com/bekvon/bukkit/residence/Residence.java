package com.bekvon.bukkit.residence;

import com.bekvon.bukkit.residence.Placeholders.Placeholder;
import com.bekvon.bukkit.residence.Placeholders.PlaceholderAPIHook;
import com.bekvon.bukkit.residence.api.*;
import com.bekvon.bukkit.residence.bigDoors.BigDoorsManager;
import com.bekvon.bukkit.residence.commands.padd;
import com.bekvon.bukkit.residence.containers.*;
import com.bekvon.bukkit.residence.dynmap.DynMapListeners;
import com.bekvon.bukkit.residence.dynmap.DynMapManager;
import com.bekvon.bukkit.residence.economy.*;
import com.bekvon.bukkit.residence.gui.FlagUtil;
import com.bekvon.bukkit.residence.itemlist.WorldItemManager;
import com.bekvon.bukkit.residence.listeners.*;
import com.bekvon.bukkit.residence.permissions.PermissionManager;
import com.bekvon.bukkit.residence.persistance.YMLSaveHelper;
import com.bekvon.bukkit.residence.protection.*;
import com.bekvon.bukkit.residence.protection.FlagPermissions.FlagCombo;
import com.bekvon.bukkit.residence.selection.*;
import com.bekvon.bukkit.residence.signsStuff.SignUtil;
import com.bekvon.bukkit.residence.slimeFun.SlimefunManager;
import com.bekvon.bukkit.residence.text.Language;
import com.bekvon.bukkit.residence.text.help.HelpEntry;
import com.bekvon.bukkit.residence.text.help.InformationPager;
import com.bekvon.bukkit.residence.utils.*;
import com.bekvon.bukkit.residence.vaultinterface.ResidenceVaultAdapter;
import com.liuchangking.dreamengine.api.DreamServerAPI;
import com.liuchangking.dreamengine.utils.MessageUtil;
import com.residence.mcstats.Metrics;
import com.residence.zip.ZipLibrary;
import com.bekvon.bukkit.residence.landcore.LandCoreManager;
import com.bekvon.bukkit.residence.landcore.LandCoreListener;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;
import net.Zrips.CMILib.Version.Schedulers.CMITask;
import net.Zrips.CMILib.Version.Version;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Gary Smoak - bekvon
 * 
 */

public class Residence extends JavaPlugin {

    private static Residence instance;

    private boolean fullyLoaded = false;

    protected String ResidenceVersion;
    protected List<String> authlist;
    protected ResidenceManager rmanager;
    protected SelectionManager smanager;
    public PermissionManager gmanager;
    protected ConfigManager configManager;

    protected boolean spigotPlatform = false;

    protected SignUtil signmanager;

    protected ResidenceBlockListener blistener;
    protected ResidencePlayerListener plistener;
    protected ResidenceEntityListener elistener;

    protected ResidenceCommandListener commandManager;

    protected PermissionListManager pmanager;
    public WorldItemManager imanager;
    public WorldFlagManager wmanager;
    protected Server server;
    public HelpEntry helppages;
    protected LocaleManager LocaleManager;
    protected Language newLanguageManager;
    protected PlayerManager PlayerManager;
    protected FlagUtil FlagUtilManager;
//    private TownManager townManager;
    protected RandomTp RandomTpManager;
    protected DynMapManager DynManager;
    protected Sorting SortingManager;
    protected AutoSelection AutoSelectionManager;
    private InformationPager InformationPagerManager;

    // Land core module
    protected LandCoreManager landCoreManager;

    protected CommandFiller cmdFiller;

    protected ZipLibrary zip;

    protected boolean firstenable = true;
    protected EconomyInterface economy;
    public File dataFolder;
    private boolean useMysql = true;
    private String serverId = "default";
    /**
     * Look up the server id that owns the specified residence.
     *
     * <p>If MySQL is disabled or no record is found, the current
     * server id is returned.</p>
     */
    public String getResidenceServerId(String resName) {
        if (!useMysql) {
            return serverId;
        }
        try (java.sql.Connection conn = com.liuchangking.dreamengine.service.MysqlManager.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement("SELECT server_id FROM residences WHERE res_name=? LIMIT 1")) {
            ps.setString(1, resName);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverId;
    }

    /**
     * Transfer a player to another server for a cross-server residence teleport.
     */
    public void sendPlayerToResidenceServer(Player player, String resName, String serverId) {
        if (com.liuchangking.dreamengine.config.Config.redisEnabled) {
            try (redis.clients.jedis.Jedis j = com.liuchangking.dreamengine.service.RedisManager.getPool().getResource()) {
                j.setex("res_tp:" + player.getUniqueId(), 30, resName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String targetServer = DreamServerAPI.getServerName(serverId);
        DreamServerAPI.sendPlayerToServer(player, targetServer);
        MessageUtil.notifySuccess(player, "正在传送...");
    }
    protected CMITask healBukkitId = null;
    protected CMITask feedBukkitId = null;
    protected CMITask effectRemoveBukkitId = null;
    protected CMITask despawnMobsBukkitId = null;
    protected CMITask autosaveBukkitId = null;

    private boolean SlimeFun = false;
    private boolean BigDoors = false;
    Metrics metrics = null;

    protected boolean initsuccess = false;
    public Map<String, String> deleteConfirm;    private ConcurrentHashMap<String, OfflinePlayer> OfflinePlayerList = new ConcurrentHashMap<String, OfflinePlayer>();
    private Map<UUID, OfflinePlayer> cachedPlayerNameUUIDs = new HashMap<UUID, OfflinePlayer>();
    private Map<UUID, String> cachedPlayerNames = new HashMap<UUID, String>();

//    private String ServerLandname = "Server_Land";
    private UUID ServerLandUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private UUID TempUserUUID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    public HashMap<String, Long> rtMap = new HashMap<String, Long>();
    public HashMap<UUID, SafeLocationCache> teleportMap = new HashMap<UUID, SafeLocationCache>();

    private Placeholder Placeholder;
    private boolean PlaceholderAPIEnabled = false;

    private String prefix = ChatColor.GREEN + "[" + ChatColor.GOLD + "Residence" + ChatColor.GREEN + "]" + ChatColor.GRAY;

    public boolean isSpigot() {
        return spigotPlatform;
    }

    public HashMap<UUID, SafeLocationCache> getTeleportMap() {
        return teleportMap;
    }

    public HashMap<String, Long> getRandomTeleportMap() {
        return rtMap;
    }

    // API
    private ResidenceApi API = new ResidenceApi();    private ResidencePlayerInterface PlayerAPI = null;
    private ResidenceInterface ResidenceAPI = null;

    public ResidencePlayerInterface getPlayerManagerAPI() {
        if (PlayerAPI == null)
            PlayerAPI = PlayerManager;
        return PlayerAPI;
    }

    public ResidenceInterface getResidenceManagerAPI() {
        if (ResidenceAPI == null)
            ResidenceAPI = rmanager;
        return ResidenceAPI;
    }

    public Placeholder getPlaceholderAPIManager() {
        if (Placeholder == null)
            Placeholder = new Placeholder(this);
        return Placeholder;
    }

    public boolean isPlaceholderAPIEnabled() {
        return PlaceholderAPIEnabled;
    }
    public ResidenceCommandListener getCommandManager() {
        if (commandManager == null)
            commandManager = new ResidenceCommandListener(this);
        return commandManager;
    }

    public ResidenceApi getAPI() {
        return API;
    }
    // API end

    private Runnable doHeals = () -> plistener.doHeals();

    private Runnable doFeed = () -> plistener.feed();

    private Runnable removeBadEffects = () -> plistener.badEffects();

    private Runnable DespawnMobs = () -> plistener.DespawnMobs();


    private Runnable autoSave = () -> {
        if (!initsuccess)
            return;

        CMIScheduler.runTaskAsynchronously(this, () -> {
            try {
                saveYml();
            } catch (Throwable e) {
                Logger.getLogger("Minecraft").log(Level.SEVERE, getPrefix() + " SEVERE SAVE ERROR", e);
                e.printStackTrace();
            }
        });
    };

    public void reloadPlugin() {
        this.onDisable();
        this.reloadConfig();
        this.onEnable();
    }

    @Override
    public void onDisable() {
        if (autosaveBukkitId != null)
            autosaveBukkitId.cancel();
        if (healBukkitId != null)
            healBukkitId.cancel();
        if (feedBukkitId != null)
            feedBukkitId.cancel();
        if (effectRemoveBukkitId != null)
            effectRemoveBukkitId.cancel();
        if (despawnMobsBukkitId != null)
            despawnMobsBukkitId.cancel();

        if (landCoreManager != null) {
            landCoreManager.removeAllHolograms();
            landCoreManager.save();
        }

        this.getPermissionManager().stopCacheClearScheduler();

        this.getSelectionManager().onDisable();

        if (this.metrics != null)
            try {
                metrics.disable();
            } catch (IOException e) {
                e.printStackTrace();
            }

        if (getDynManager() != null && getDynManager().getMarkerSet() != null)
            getDynManager().getMarkerSet().deleteMarkerSet();

        if (initsuccess) {
            try {
                saveYml();
                if (zip != null)
                    zip.backup();
            } catch (Exception ex) {
                Logger.getLogger("Minecraft").log(Level.SEVERE, "[Residence] SEVERE SAVE ERROR", ex);
            }
            Bukkit.getConsoleSender().sendMessage(getPrefix() + " Disabled!");
        }
    }

    @Override
    public void onEnable() {
        try {
            instance = this;

            initsuccess = false;
            deleteConfirm = new HashMap<String, String>();
            server = this.getServer();
            dataFolder = this.getDataFolder();

            // Load mysql.yml to determine if MySQL should be used
            File mysqlFile = new File(dataFolder, "mysql.yml");
            if (!mysqlFile.isFile()) {
                writeDefaultFileFromJar(mysqlFile, "mysql.yml", true);
            }
            YamlConfiguration mysqlCfg = YamlConfiguration.loadConfiguration(mysqlFile);
            useMysql = mysqlCfg.getBoolean("enabled", false);

            serverId = com.liuchangking.dreamengine.api.DreamServerAPI.getServerId();

            // When MySQL is enabled, verify DreamEngine configuration and connection
            if (useMysql) {
                if (!com.liuchangking.dreamengine.config.Config.mysqlEnabled) {
                    consoleMessage("MySQL is enabled in mysql.yml but disabled in DreamEngine config!");
                    Bukkit.shutdown();
                    return;
                }
                try (java.sql.Connection c = com.liuchangking.dreamengine.service.MysqlManager.getConnection()) {
                    // test connection
                } catch (Exception e) {
                    consoleMessage("Failed to connect to MySQL: " + e.getMessage());
                    Bukkit.shutdown();
                    return;
                }
                createTables();
            }

            ResidenceVersion = this.getDescription().getVersion();
            authlist = this.getDescription().getAuthors();

            cmdFiller = new CommandFiller();
            cmdFiller.fillCommands();

            SortingManager = new Sorting();

            if (!dataFolder.isDirectory()) {
                dataFolder.mkdirs();
            }

            if (!new File(dataFolder, "groups.yml").isFile() && !new File(dataFolder, "flags.yml").isFile() && new File(dataFolder, "config.yml").isFile()) {
                this.convertFile();
            }

            if (!new File(dataFolder, "uuids.yml").isFile()) {
                File file = new File(this.getDataFolder(), "uuids.yml");
                file.createNewFile();
            }

            if (!new File(dataFolder, "flags.yml").isFile()) {
                this.writeDefaultFlagsFromJar();
            }
            if (!new File(dataFolder, "groups.yml").isFile()) {
                this.writeDefaultGroupsFromJar();
            }

            this.getCommand("res").setExecutor(getCommandManager());
            this.getCommand("resadmin").setExecutor(getCommandManager());
            this.getCommand("residence").setExecutor(getCommandManager());

            this.getCommand("resreload").setExecutor(getCommandManager());
            this.getCommand("resload").setExecutor(getCommandManager());

            TabComplete tab = new TabComplete();
            this.getCommand("res").setTabCompleter(tab);
            this.getCommand("resadmin").setTabCompleter(tab);
            this.getCommand("residence").setTabCompleter(tab);

//	    Residence.getConfigManager().UpdateConfigFile();

//	    if (this.getConfig().getInt("ResidenceVersion", 0) == 0) {
//		this.writeDefaultConfigFromJar();
//		this.getConfig().load("config.yml");
//		System.out.println("[Residence] Config Invalid, wrote default...");
//	    }
            String multiworld = getConfigManager().getMultiworldPlugin();
            if (multiworld != null) {
                Plugin plugin = server.getPluginManager().getPlugin(multiworld);
                if (plugin != null && !plugin.isEnabled()) {
                    Bukkit.getConsoleSender().sendMessage(getPrefix() + " - Enabling multiworld plugin: " + multiworld);
                    server.getPluginManager().enablePlugin(plugin);
                }
            }

            getConfigManager().UpdateFlagFile();

            getFlagUtilManager().load();

            try {
                Class<?> c = Class.forName("org.bukkit.entity.Player");
                for (Method one : c.getDeclaredMethods()) {
                    if (one.getName().equalsIgnoreCase("Spigot"))
                        spigotPlatform = true;
                }
            } catch (Exception e) {
            }

            this.getPermissionManager().startCacheClearScheduler();

            imanager = new WorldItemManager(this);
            wmanager = new WorldFlagManager(this);
            LocaleManager = new LocaleManager(this);

            PlayerManager = new PlayerManager(this);
            RandomTpManager = new RandomTp(this);
//	    townManager = new TownManager(this);

            InformationPagerManager = new InformationPager(this);

            zip = new ZipLibrary(this);


            SlimeFun = Bukkit.getPluginManager().getPlugin("Slimefun") != null;

            if (SlimeFun) {
                try {
                    SlimefunManager.register(this);
                } catch (Throwable e) {
                    SlimeFun = false;
                    e.printStackTrace();
                }
            }

            BigDoors = Bukkit.getPluginManager().getPlugin("BigDoors") != null;

            if (BigDoors) {
                try {
                    BigDoorsManager.register(this);
                } catch (Throwable e) {
                    BigDoors = false;
                    e.printStackTrace();
                }
            }

            this.getConfigManager().copyOverTranslations();

            parseHelpEntries();

            economy = null;
            if (this.getConfig().getBoolean("Global.EnableEconomy", false)) {
                Bukkit.getConsoleSender().sendMessage(getPrefix() + " Scanning for economy systems...");
                switch (this.getConfigManager().getEconomyType()) {
                case None:
                    if (this.getPermissionManager().getPermissionsPlugin() instanceof ResidenceVaultAdapter) {
                        ResidenceVaultAdapter vault = (ResidenceVaultAdapter) this.getPermissionManager().getPermissionsPlugin();
                        if (vault.economyOK()) {
                            economy = vault;
                            consoleMessage("Found Vault using economy system: &5" + vault.getEconomyName());
                        }
                    }
                    if (economy == null) {
                        this.loadVaultEconomy();
                    }
                    break;
                case Vault:
                    if (this.getPermissionManager().getPermissionsPlugin() instanceof ResidenceVaultAdapter) {
                        ResidenceVaultAdapter vault = (ResidenceVaultAdapter) this.getPermissionManager().getPermissionsPlugin();
                        if (vault.economyOK()) {
                            economy = vault;
                            consoleMessage("Found Vault using economy system: &5" + vault.getEconomyName());
                        }
                    }
                    if (economy == null) {
                        this.loadVaultEconomy();
                    }
                    break;
                default:
                    break;
                }

                if (economy == null) {
                    Bukkit.getConsoleSender().sendMessage(getPrefix() + " Unable to find an economy system...");
                    economy = new BlackHoleEconomy();
                }
            }

            // Only fill if we need to convert player data
            if (getConfigManager().isUUIDConvertion()) {
                Bukkit.getConsoleSender().sendMessage(getPrefix() + " Loading (" + Bukkit.getOfflinePlayers().length + ") player data");
                for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                    if (player == null)
                        continue;
                    String name = player.getName();
                    if (name == null)
                        continue;
                    this.addOfflinePlayerToChache(player);
                }
                Bukkit.getConsoleSender().sendMessage(getPrefix() + " Player data loaded: " + OfflinePlayerList.size());
            } else {
                CMIScheduler.runTaskAsynchronously(this, () -> {
                    for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                        if (player == null)
                            continue;
                        String name = player.getName();
                        if (name == null)
                            continue;
                        addOfflinePlayerToChache(player);
                    }
                });
            }

            rmanager = new ResidenceManager(this);

            pmanager = new PermissionListManager(this);

            getLocaleManager().LoadLang(getConfigManager().getLanguage());
            getLM().LanguageReload();

            if (firstenable) {
                if (!this.isEnabled()) {
                    return;
                }

                File f = new File(getDataFolder(), "flags.yml");
                YamlConfiguration conf = YamlConfiguration.loadConfiguration(f);
                for (String oneFlag : conf.getStringList("Global.GroupedFlags." + padd.groupedFlag)) {
                    Flags flag = Flags.getFlag(oneFlag);
                    if (flag != null) {
                        flag.addGroup(padd.groupedFlag);
                    }
                    FlagPermissions.addFlagToFlagGroup(padd.groupedFlag, oneFlag);
                }

            }

            try {
                this.loadYml();
            } catch (Exception e) {
                this.getLogger().log(Level.SEVERE, "Unable to load save file", e);
                throw e;
            }

            signmanager = new SignUtil(this);
            getSignUtil().LoadSigns();

            if (getConfigManager().isUseResidenceFileClean())
                (new FileCleanUp(this)).cleanOldResidence();

            if (firstenable) {
                if (!this.isEnabled()) {
                    return;
                }
                FlagPermissions.initValidFlags();

                if (smanager == null)
                    smanager = new SelectionManager(server, this);

                PluginManager pm = getServer().getPluginManager();

                // Minimum supported version is 1.18.2, register only listeners for newer versions
                if (Version.isCurrentEqualOrHigher(Version.v1_19_R1))
                    pm.registerEvents(new ResidencePlayerListener1_19(this), this);
                if (Version.isCurrentEqualOrHigher(Version.v1_20_R1))
                    pm.registerEvents(new ResidencePlayerListener1_20(this), this);
                if (Version.isCurrentEqualOrHigher(Version.v1_21_R1))
                    pm.registerEvents(new ResidencePlayerListener1_21(this), this);

                blistener = new ResidenceBlockListener(this);
                plistener = new ResidencePlayerListener(this);
                elistener = new ResidenceEntityListener(this);

                pm.registerEvents(blistener, this);
                pm.registerEvents(plistener, this);
                pm.registerEvents(elistener, this);
                pm.registerEvents(new com.bekvon.bukkit.residence.listeners.CrossServerTeleportListener(), this);
                pm.registerEvents(new ResidenceFixesListener(), this);
                // Events for old versions removed

                firstenable = false;
            } else {
                plistener.reload();
            }

            AutoSelectionManager = new AutoSelection(this);

            landCoreManager = new LandCoreManager(this);
            landCoreManager.load();
            getServer().getPluginManager().registerEvents(new LandCoreListener(this, landCoreManager), this);

            try {
                Class.forName("org.bukkit.event.player.PlayerItemDamageEvent");
                getServer().getPluginManager().registerEvents(new SpigotListener(), this);
            } catch (Exception e) {
            }

            if (setupPlaceHolderAPI()) {
                Bukkit.getConsoleSender().sendMessage(getPrefix() + " PlaceholderAPI was found - Enabling capabilities.");
                PlaceholderAPIEnabled = true;
            }

            if (getServer().getPluginManager().getPlugin("CrackShot") != null)
                getServer().getPluginManager().registerEvents(new CrackShot(this), this);

            try {
                // DynMap
                Plugin dynmap = Bukkit.getPluginManager().getPlugin("dynmap");
                if (dynmap != null && getConfigManager().DynMapUse) {
                    DynManager = new DynMapManager(this);
                    getServer().getPluginManager().registerEvents(new DynMapListeners(this), this);
                    getDynManager().api = (DynmapAPI) dynmap;
                    getDynManager().activate();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }


            int autosaveInt = getConfigManager().getAutoSaveInterval();
            if (!useMysql) {
                if (autosaveInt < 1) {
                    autosaveInt = 1;
                }
                autosaveInt = autosaveInt * 60 * 20;
                autosaveBukkitId = CMIScheduler.scheduleSyncRepeatingTask(this, autoSave, autosaveInt, autosaveInt);
            }

            if (getConfigManager().getHealInterval() > 0)
                healBukkitId = CMIScheduler.scheduleSyncRepeatingTask(this, doHeals, 20, getConfigManager().getHealInterval() * 20);
            if (getConfigManager().getFeedInterval() > 0)
                feedBukkitId = CMIScheduler.scheduleSyncRepeatingTask(this, doFeed, 20, getConfigManager().getFeedInterval() * 20);
            if (getConfigManager().getSafeZoneInterval() > 0)
                effectRemoveBukkitId = CMIScheduler.scheduleSyncRepeatingTask(this, removeBadEffects, 20, getConfigManager().getSafeZoneInterval() * 20);

            if (getConfigManager().AutoMobRemoval())
                despawnMobsBukkitId = CMIScheduler.scheduleSyncRepeatingTask(this, DespawnMobs, 20 * getConfigManager().AutoMobRemovalInterval(), 20
                    * getConfigManager().AutoMobRemovalInterval());

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (getPermissionManager().isResidenceAdmin(player)) {
                    ResAdmin.turnResAdminOn(player);
                }
            }
            try {
                metrics = new Metrics(this);
                metrics.start();
            } catch (IOException e) {
                // Failed to submit the stats :-(
            }
            Bukkit.getConsoleSender().sendMessage(getPrefix() + " Enabled! Version " + this.getDescription().getVersion() + " by Zrips");
            initsuccess = true;

        } catch (Exception ex) {
            initsuccess = false;
            getServer().getPluginManager().disablePlugin(this);
            Bukkit.getConsoleSender().sendMessage(getPrefix() + " - FAILED INITIALIZATION! DISABLED! ERROR:");
            Logger.getLogger(Residence.class.getName()).log(Level.SEVERE, null, ex);
            Bukkit.getServer().shutdown();
        }


        fullyLoaded = true;
    }

    public void parseHelpEntries() {

        try {
            File langFile = new File(new File(dataFolder, "Language"), getConfigManager().getLanguage() + ".yml");

            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(new FileInputStream(langFile), StandardCharsets.UTF_8));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }

            if (langFile.isFile()) {
                FileConfiguration langconfig = new YamlConfiguration();
                langconfig.load(in);
                helppages = HelpEntry.parseHelp(langconfig, "CommandHelp");
            } else {
                Bukkit.getConsoleSender().sendMessage(getPrefix() + " Language file does not exist...");
            }
            if (in != null)
                in.close();
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage(getPrefix() + " Failed to load language file: " + getConfigManager().getLanguage()
                + ".yml setting to default - English");

            File langFile = new File(new File(dataFolder, "Language"), "English.yml");

            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(new FileInputStream(langFile), StandardCharsets.UTF_8));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }

            try {
                if (langFile.isFile()) {
                    FileConfiguration langconfig = new YamlConfiguration();
                    langconfig.load(in);
                    helppages = HelpEntry.parseHelp(langconfig, "CommandHelp");
                } else {
                    Bukkit.getConsoleSender().sendMessage(getPrefix() + " Language file does not exist...");
                }
            } catch (Throwable e) {

            } finally {
                if (in != null)
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    private boolean setupPlaceHolderAPI() {
        if (!getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
            return false;
        return new PlaceholderAPIHook(this).register();
    }

    public SignUtil getSignUtil() {
        return signmanager;
    }

    public void consoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(CMIChatColor.translate(getPrefix() + " " + message));
    }

    public boolean validName(String name) {
        if (name.contains(":") || name.contains(".") || name.contains("|")) {
            return false;
        }
        if (getConfigManager().getResidenceNameRegex() == null) {
            return true;
        }
        String namecheck = name.replaceAll(getConfigManager().getResidenceNameRegex(), "");
        return name.equals(namecheck);
    }




    public Residence getPlugin() {
        return this;
    }

//    public LWC getLwc() {
//	return lwc;
//    }

    public File getDataLocation() {
        return dataFolder;
    }


    public CommandFiller getCommandFiller() {
        if (cmdFiller == null) {
            cmdFiller = new CommandFiller();
            cmdFiller.fillCommands();
        }
        return cmdFiller;
    }

    public ResidenceManager getResidenceManager() {
        return rmanager;
    }

    public SelectionManager getSelectionManager() {
        if (smanager == null)
            smanager = new SelectionManager(server, this);
        return smanager;
    }

    public FlagUtil getFlagUtilManager() {
        if (FlagUtilManager == null)
            FlagUtilManager = new FlagUtil(this);
        return FlagUtilManager;
    }

    public PermissionManager getPermissionManager() {
        if (gmanager == null)
            gmanager = new PermissionManager(this);
        return gmanager;
    }

    public PermissionListManager getPermissionListManager() {
        return pmanager;
    }

    public DynMapManager getDynManager() {
        return DynManager;
    }


    public AutoSelection getAutoSelectionManager() {
        return AutoSelectionManager;
    }

    public Sorting getSortingManager() {
        return SortingManager;
    }

    public RandomTp getRandomTpManager() {
        return RandomTpManager;
    }

    public LandCoreManager getLandCoreManager() {
        return landCoreManager;
    }

    public EconomyInterface getEconomyManager() {
        return economy;
    }

    public Server getServ() {
        return server;
    }

    public boolean isUsingMysql() {
        return useMysql;
    }

    public String getServerId() {
        return serverId;
    }

    /**
     * Persist a single residence immediately when MySQL mode is enabled.
     */
    public void saveResidenceMysql(com.bekvon.bukkit.residence.protection.ClaimedResidence res) {
        if (!useMysql || res == null)
            return;
        com.bekvon.bukkit.residence.persistance.MysqlSaveHelper helper =
                new com.bekvon.bukkit.residence.persistance.MysqlSaveHelper(serverId);
        try {
            helper.saveResidence(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a residence record from MySQL immediately.
     */
    public void deleteResidenceMysql(String name) {
        if (!useMysql)
            return;
        com.bekvon.bukkit.residence.persistance.MysqlSaveHelper helper =
                new com.bekvon.bukkit.residence.persistance.MysqlSaveHelper(serverId);
        try {
            helper.deleteResidence(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the name of a residence in MySQL immediately.
     */
    public void renameResidenceMysql(String oldName, com.bekvon.bukkit.residence.protection.ClaimedResidence res) {
        if (!useMysql || res == null)
            return;
        com.bekvon.bukkit.residence.persistance.MysqlSaveHelper helper =
                new com.bekvon.bukkit.residence.persistance.MysqlSaveHelper(serverId);
        try {
            helper.renameResidence(oldName, res.getResidenceName());
            helper.saveResidence(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PlayerManager getPlayerManager() {
        return PlayerManager;
    }

    public HelpEntry getHelpPages() {
        return helppages;
    }

    @Deprecated
    public void setConfigManager(ConfigManager cm) {
        configManager = cm;
    }

    public ConfigManager getConfigManager() {
        if (configManager == null)
            configManager = new ConfigManager(this);
        return configManager;
    }

    public WorldItemManager getItemManager() {
        return imanager;
    }

    public WorldFlagManager getWorldFlags() {
        return wmanager;
    }


    public LocaleManager getLocaleManager() {
        return LocaleManager;
    }

    public Language getLM() {
        if (newLanguageManager == null) {
            newLanguageManager = new Language(this);
            newLanguageManager.LanguageReload();
        }
        return newLanguageManager;
    }

    public ResidencePlayerListener getPlayerListener() {
        return plistener;
    }

    public ResidenceBlockListener getBlockListener() {
        return blistener;
    }

    public ResidenceEntityListener getEntityListener() {
        return elistener;
    }

    public String getResidenceVersion() {
        return ResidenceVersion;
    }

    public List<String> getAuthors() {
        return authlist;
    }

    public FlagPermissions getPermsByLoc(Location loc) {
        ClaimedResidence res = rmanager.getByLoc(loc);
        if (res != null) {
            return res.getPermissions();
        }
        return wmanager.getPerms(loc.getWorld().getName());

    }

    public FlagPermissions getPermsByLocForPlayer(Location loc, Player player) {
        ClaimedResidence res = rmanager.getByLoc(loc);
        if (res != null) {
            return res.getPermissions();
        }
        if (player != null)
            return wmanager.getPerms(player);

        return wmanager.getPerms(loc.getWorld().getName());
    }


    private void loadVaultEconomy() {
        Plugin p = getServer().getPluginManager().getPlugin("Vault");
        if (p != null) {
            ResidenceVaultAdapter vault = new ResidenceVaultAdapter(getServer());
            if (vault.economyOK()) {
                consoleMessage("Found Vault using economy: &5" + vault.getEconomyName());
                economy = vault;
            } else {
                consoleMessage("Found Vault, but Vault reported no usable economy system...");
            }
        } else {
            consoleMessage("Vault NOT found!");
        }
    }

    public boolean isResAdminOn(CommandSender sender) {
        return ResAdmin.isResAdmin(sender);
    }

    @Deprecated
    /**
    * @deprecated Use {@link ResAdmin#isResAdmin(Player)} instead.
    */
    public boolean isResAdminOn(Player player) {
        return ResAdmin.isResAdmin(player);
    }

    @Deprecated
    /**
    * @deprecated Use {@link ResAdmin#turnResAdmin(Player, Boolean)} instead.
    */
    public void turnResAdminOn2(Player player) {
        ResAdmin.turnResAdmin(player, true);
    }

    @Deprecated
    /**
    * @deprecated Use {@link ResAdmin#turnResAdmin(Player, Boolean)} instead.
    */
    public void turnResAdminOff2(Player player) {
        ResAdmin.turnResAdmin(player, false);
    }

    @Deprecated
    /**
    * @deprecated Use {@link ResAdmin#isResAdmin(Player)} instead.
    */
    public boolean isResAdminOn(String player) {
        ResidencePlayer rPlayer = this.getPlayerManager().getResidencePlayer(player);
        if (rPlayer == null)
            return false;
        return ResAdmin.isResAdmin(rPlayer.getUniqueId());
    }

    private static void saveBackup(File ymlSaveLoc, String worldName, File worldFolder) {
        if (ymlSaveLoc.isFile()) {
            File backupFolder = new File(worldFolder, "Backup");
            backupFolder.mkdirs();
            File backupFile = new File(backupFolder, "res_" + worldName + ".yml");
            if (backupFile.isFile()) {
                backupFile.delete();
            }
            ymlSaveLoc.renameTo(backupFile);
        }
    }

    private void saveYml() throws IOException {
        if (useMysql) {
            saveMysql();
            return;
        }
        File saveFolder = new File(dataFolder, "Save");
        File worldFolder = new File(saveFolder, "Worlds");
        if (!worldFolder.isDirectory())
            worldFolder.mkdirs();
        YMLSaveHelper syml;
        File tmpFile = null;
        File ymlSaveLoc = null;
        Map<String, Object> save = rmanager.save();
        for (Entry<String, Object> entry : save.entrySet()) {

            boolean emptyRecord = false;
            // Not saving files without any records in them. Mainly for servers with many small temporary worlds
            try {
                emptyRecord = ((LinkedHashMap) entry.getValue()).isEmpty();
            } catch (Throwable e) {
            }

            ymlSaveLoc = new File(worldFolder, "res_" + entry.getKey() + ".yml");

            if (emptyRecord) {
                saveBackup(ymlSaveLoc, entry.getKey(), worldFolder);
                continue;
            }

            tmpFile = new File(worldFolder, "tmp_res_" + entry.getKey() + ".yml");

            syml = new YMLSaveHelper(tmpFile);
            if (this.getResidenceManager().getMessageCatch(entry.getKey()) != null)
                syml.getRoot().put("Messages", this.getResidenceManager().getMessageCatch(entry.getKey()));
            if (this.getResidenceManager().getFlagsCatch(entry.getKey()) != null)
                syml.getRoot().put("Flags", this.getResidenceManager().getFlagsCatch(entry.getKey()));

            syml.getRoot().put("Residences", entry.getValue());
            syml.save();

            saveBackup(ymlSaveLoc, entry.getKey(), worldFolder);

            tmpFile.renameTo(ymlSaveLoc);
        }

        YMLSaveHelper yml;


        // permlist save
        ymlSaveLoc = new File(saveFolder, "permlists.yml");
        tmpFile = new File(saveFolder, "tmp_permlists.yml");
        yml = new YMLSaveHelper(tmpFile);
        yml.getRoot().put("PermissionLists", pmanager.save());
        yml.save();
        if (ymlSaveLoc.isFile()) {
            File backupFolder = new File(saveFolder, "Backup");
            backupFolder.mkdirs();
            File backupFile = new File(backupFolder, "permlists.yml");
            if (backupFile.isFile()) {
                backupFile.delete();
            }
            ymlSaveLoc.renameTo(backupFile);
        }
        tmpFile.renameTo(ymlSaveLoc);



        if (getConfigManager().showIntervalMessages()) {
            System.out.println("[Residence] - Saved Residences...");
        }
    }

    private void saveMysql() {
        com.bekvon.bukkit.residence.persistance.MysqlSaveHelper helper = new com.bekvon.bukkit.residence.persistance.MysqlSaveHelper(serverId);
        for (com.bekvon.bukkit.residence.protection.ClaimedResidence res : rmanager.getResidences().values()) {
            try {
                helper.saveResidence(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Save shared permission lists and caches using legacy structure
        //
        // MySQL already stores each residence individually via saveResidence.
        // Calling saveWorld here would overwrite the extended fields
        // (owner_uuid and coordinates) with partial data. The legacy call is
        // therefore skipped to preserve complete records.

        Map<String, Object> pr = new java.util.LinkedHashMap<>();
        pr.put("PermissionLists", pmanager.save());
        try {
            helper.savePermLists(pr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (getConfigManager().showIntervalMessages()) {
            System.out.println("[Residence] - Saved Residences...");
        }
    }

    private void createTables() {
        try (java.sql.Connection conn = com.liuchangking.dreamengine.service.MysqlManager.getConnection();
             java.sql.Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS residences (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "server_id VARCHAR(32)," +
                    "world_name VARCHAR(64)," +
                    "res_name VARCHAR(64)," +
                    "owner_uuid CHAR(36)," +
                    "owner_name VARCHAR(64)," +
                    "leave_message TEXT," +
                    "tp_loc MEDIUMTEXT," +
                    "enter_message TEXT," +
                    "player_flags MEDIUMTEXT," +
                    "area_flags MEDIUMTEXT," +
                    "created_on BIGINT," +
                    "areas MEDIUMTEXT," +
                    "updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                    "UNIQUE KEY uniq_res_name (res_name)," +
                    "KEY world_idx (server_id, world_name))");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS residence_permlists (id INT PRIMARY KEY, data MEDIUMTEXT)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Import existing YAML data into MySQL tables.
     */
    public void migrateToMysql() throws Exception {
        if (!useMysql) {
            return;
        }
        com.bekvon.bukkit.residence.persistance.MysqlSaveHelper helper = new com.bekvon.bukkit.residence.persistance.MysqlSaveHelper(serverId);
        File saveFolder = new File(dataFolder, "Save");
        File worldFolder = new File(saveFolder, "Worlds");
        if (worldFolder.isDirectory()) {
            for (File f : worldFolder.listFiles()) {
                if (!f.isFile())
                    continue;
                String name = f.getName();
                if (!name.startsWith(saveFilePrefix) || !name.endsWith(".yml"))
                    continue;
                String world = name.substring(saveFilePrefix.length(), name.length() - 4);
                YMLSaveHelper yml = new YMLSaveHelper(f);
                yml.load();
                Map<String, Object> root = yml.getRoot();
                if (root != null && root.containsKey("Residences")) {
                    Map<String, Object> resMap = (Map<String, Object>) root.get("Residences");
                    for (Object obj : resMap.values()) {
                        try {
                            com.bekvon.bukkit.residence.protection.ClaimedResidence res = com.bekvon.bukkit.residence.protection.ClaimedResidence.load(world, (Map<String, Object>) obj, null, this);
                            helper.saveResidence(res);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        File permFile = new File(saveFolder, "permlists.yml");
        if (permFile.isFile()) {
            YMLSaveHelper yml = new YMLSaveHelper(permFile);
            yml.load();
            helper.savePermLists(yml.getRoot());
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean loadMysql() throws Exception {
        com.bekvon.bukkit.residence.persistance.MysqlSaveHelper helper = new com.bekvon.bukkit.residence.persistance.MysqlSaveHelper(serverId);
        HashMap<String, Object> worlds = new HashMap<>();
        for (String worldName : this.getResidenceManager().getWorldNames()) {
            Map<String, Object> root = null;
            try {
                root = helper.loadWorld(worldName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (root == null)
                continue;
            loadMessagesAndFlags(worldName, root);
            worlds.put(worldName, root.get("Residences"));
        }

        getResidenceManager().load(worlds);

        try {
            Map<String, Object> pr = helper.loadPermLists();
            if (pr != null)
                pmanager = getPermissionListManager().load((Map) pr.get("PermissionLists"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public final static String saveFilePrefix = "res_";

    private void loadFlags(String worldName, YMLSaveHelper yml) {
        if (!yml.getRoot().containsKey("Flags"))
            return;

        HashMap<Integer, MinimizeFlags> c = getResidenceManager().getCacheFlags().get(worldName);
        if (c == null)
            c = new HashMap<Integer, MinimizeFlags>();
        Map<Integer, Object> ms = (Map<Integer, Object>) yml.getRoot().get("Flags");
        if (ms == null)
            return;

        for (Entry<Integer, Object> one : ms.entrySet()) {
            try {
                HashMap<String, Boolean> msgs = (HashMap<String, Boolean>) one.getValue();
                c.put(one.getKey(), new MinimizeFlags(one.getKey(), msgs));
            } catch (Exception e) {

            }
        }
        getResidenceManager().getCacheFlags().put(worldName, c);
    }

    private void loadMessages(String worldName, YMLSaveHelper yml) {
        if (!yml.getRoot().containsKey("Messages"))
            return;

        HashMap<Integer, MinimizeMessages> c = getResidenceManager().getCacheMessages().get(worldName);
        if (c == null)
            c = new HashMap<Integer, MinimizeMessages>();
        Map<Integer, Object> ms = (Map<Integer, Object>) yml.getRoot().get("Messages");
        if (ms == null)
            return;

        for (Entry<Integer, Object> one : ms.entrySet()) {
            try {
                Map<String, String> msgs = (Map<String, String>) one.getValue();
                c.put(one.getKey(), new MinimizeMessages(one.getKey(), msgs.get("EnterMessage"), msgs.get("LeaveMessage")));
            } catch (Exception e) {

            }
        }
        getResidenceManager().getCacheMessages().put(worldName, c);
    }

    private void loadMessagesAndFlags(String worldName, YMLSaveHelper yml, File worldFolder) {
        loadMessages(worldName, yml);
        loadFlags(worldName, yml);
    }

    private void loadFlags(String worldName, Map<String, Object> root) {
        if (root == null || !root.containsKey("Flags"))
            return;
        HashMap<Integer, MinimizeFlags> c = getResidenceManager().getCacheFlags().get(worldName);
        if (c == null)
            c = new HashMap<Integer, MinimizeFlags>();
        Map<Integer, Object> ms = (Map<Integer, Object>) root.get("Flags");
        if (ms == null)
            return;
        for (Entry<Integer, Object> one : ms.entrySet()) {
            try {
                HashMap<String, Boolean> msgs = (HashMap<String, Boolean>) one.getValue();
                c.put(one.getKey(), new MinimizeFlags(one.getKey(), msgs));
            } catch (Exception e) {

            }
        }
        getResidenceManager().getCacheFlags().put(worldName, c);
    }

    private void loadMessages(String worldName, Map<String, Object> root) {
        if (root == null || !root.containsKey("Messages"))
            return;
        HashMap<Integer, MinimizeMessages> c = getResidenceManager().getCacheMessages().get(worldName);
        if (c == null)
            c = new HashMap<Integer, MinimizeMessages>();
        Map<Integer, Object> ms = (Map<Integer, Object>) root.get("Messages");
        if (ms == null)
            return;
        for (Entry<Integer, Object> one : ms.entrySet()) {
            try {
                Map<String, String> msgs = (Map<String, String>) one.getValue();
                c.put(one.getKey(), new MinimizeMessages(one.getKey(), msgs.get("EnterMessage"), msgs.get("LeaveMessage")));
            } catch (Exception e) {

            }
        }
        getResidenceManager().getCacheMessages().put(worldName, c);
    }

    private void loadMessagesAndFlags(String worldName, Map<String, Object> root) {
        loadMessages(worldName, root);
        loadFlags(worldName, root);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected boolean loadYml() throws Exception {
        if (useMysql) {
            return loadMysql();
        }
        File saveFolder = new File(dataFolder, "Save");
        try {
            File worldFolder = new File(saveFolder, "Worlds");
            if (!saveFolder.isDirectory()) {
                saveFolder.mkdir();
                if (!saveFolder.isDirectory()) {
                    this.getLogger().warning("Save directory does not exist...");
                    this.getLogger().warning("Please restart server");
                    return true;
                }
            }
            long time;
            YMLSaveHelper yml;
            File loadFile;
            HashMap<String, Object> worlds = new HashMap<>();

            for (String worldName : this.getResidenceManager().getWorldNames()) {
                loadFile = new File(worldFolder, saveFilePrefix + worldName + ".yml");
                if (!loadFile.isFile())
                    continue;

                time = System.currentTimeMillis();

                if (!isDisabledWorld(worldName) && !this.getConfigManager().CleanerStartupLog)
                    Bukkit.getConsoleSender().sendMessage(getPrefix() + " Loading save data for world " + worldName + "...");

                yml = new YMLSaveHelper(loadFile);
                yml.load();
                if (yml.getRoot() == null)
                    continue;

                loadMessagesAndFlags(worldName, yml, worldFolder);

                worlds.put(worldName, yml.getRoot().get("Residences"));

                int pass = (int) (System.currentTimeMillis() - time);
                String pastTime = pass > 1000 ? String.format("%.2f", (pass / 1000F)) + " sec" : pass + " ms";

                if (!isDisabledWorld(worldName) && !this.getConfigManager().CleanerStartupLog)
                    Bukkit.getConsoleSender().sendMessage(getPrefix() + " Loaded " + worldName + " data. (" + pastTime + ")");
            }

            getResidenceManager().load(worlds);


            if (getConfigManager().isUUIDConvertion()) {
                getConfigManager().ChangeConfig("Global.UUIDConvertion", false);
            }

            loadFile = new File(saveFolder, "permlists.yml");
            if (loadFile.isFile()) {
                yml = new YMLSaveHelper(loadFile);
                yml.load();
                Map<String, Object> root = yml.getRoot();
                if (root != null)
                    pmanager = getPermissionListManager().load((Map) root.get("PermissionLists"));
            }

//	    for (Player one : Bukkit.getOnlinePlayers()) {
//		ResidencePlayer rplayer = getPlayerManager().getResidencePlayer(one);
//		if (rplayer != null)
//		    rplayer.recountRes();
//	    }

            // System.out.print("[Residence] Loaded...");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(Residence.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }


    private void writeDefaultGroupsFromJar() {
        if (this.writeDefaultFileFromJar(new File(this.getDataFolder(), "groups.yml"), "groups.yml", true)) {
            System.out.println("[Residence] Wrote default groups...");
        }
    }

    private void writeDefaultFlagsFromJar() {
        if (this.writeDefaultFileFromJar(new File(this.getDataFolder(), "flags.yml"), "flags.yml", true)) {
            System.out.println("[Residence] Wrote default flags...");
        }
    }

    private void convertFile() {
        File file = new File(this.getDataFolder(), "config.yml");

        File file_old = new File(this.getDataFolder(), "config_old.yml");

        File newfile = new File(this.getDataFolder(), "groups.yml");

        File newTempFlags = new File(this.getDataFolder(), "flags.yml");

        try {
            copy(file, file_old);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            copy(file, newfile);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            copy(file, newTempFlags);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        File newGroups = new File(this.getDataFolder(), "config.yml");

        List<String> list = new ArrayList<String>();
        list.add("ResidenceVersion");
        list.add("Global.Flags");
        list.add("Global.FlagPermission");
        list.add("Global.ResidenceDefault");
        list.add("Global.CreatorDefault");
        list.add("Global.GroupDefault");
        list.add("Groups");
        list.add("GroupAssignments");
        list.add("ItemList");

        try {
            remove(newGroups, list);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File newConfig = new File(this.getDataFolder(), "groups.yml");
        list.clear();
        list = new ArrayList<String>();
        list.add("ResidenceVersion");
        list.add("Global");
        list.add("ItemList");

        try {
            remove(newConfig, list);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File newFlags = new File(this.getDataFolder(), "flags.yml");
        list.clear();
        list = new ArrayList<String>();
        list.add("ResidenceVersion");
        list.add("GroupAssignments");
        list.add("Groups");
        list.add("Global.Language");
        list.add("Global.SelectionToolId");
        list.add("Global.InfoToolId");
        list.add("Global.MoveCheckInterval");
        list.add("Global.SaveInterval");
        list.add("Global.DefaultGroup");
        list.add("Global.EnablePermissions");
        list.add("Global.LegacyPermissions");
        list.add("Global.EnableEconomy");
        list.add("Global.ResidenceChatEnable");
        list.add("Global.UseActionBar");
        list.add("Global.ResidenceChatColor");
        list.add("Global.AdminOnlyCommands");
        list.add("Global.AdminOPs");
        list.add("Global.MultiWorldPlugin");
        list.add("Global.ResidenceFlagsInherit");
        list.add("Global.StopOnSaveFault");
        list.add("Global.ResidenceNameRegex");
        list.add("Global.ShowIntervalMessages");
        list.add("Global.CustomContainers");
        list.add("Global.CustomBothClick");
        list.add("Global.CustomRightClick");

        try {
            remove(newFlags, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void remove(File newGroups, List<String> list) throws IOException {

        YamlConfiguration conf = YamlConfiguration.loadConfiguration(newGroups);
        conf.options().copyDefaults(true);

        for (String one : list) {
            conf.set(one, null);
        }
        try {
            conf.save(newGroups);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copy(File source, File target) throws IOException {
        InputStream in = new FileInputStream(source);
        OutputStream out = new FileOutputStream(target);
        try {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            in.close();
            out.close();
        }
    }

    private boolean writeDefaultFileFromJar(File writeName, String jarPath, boolean backupOld) {
        try {
            File fileBackup = new File(this.getDataFolder(), "backup-" + writeName);
            File jarloc = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getCanonicalFile();
            if (jarloc.isFile()) {
                JarFile jar = new JarFile(jarloc);
                try {
                    JarEntry entry = jar.getJarEntry(jarPath);
                    if (entry != null && !entry.isDirectory()) {
                        InputStream in = jar.getInputStream(entry);
                        InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
                        if (writeName.isFile()) {
                            if (backupOld) {
                                if (fileBackup.isFile()) {
                                    fileBackup.delete();
                                }
                                writeName.renameTo(fileBackup);
                            } else {
                                writeName.delete();
                            }
                        }
                        FileOutputStream out = new FileOutputStream(writeName);
                        OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
                        try {
                            char[] tempbytes = new char[512];
                            int readbytes = isr.read(tempbytes, 0, 512);
                            while (readbytes > -1) {
                                osw.write(tempbytes, 0, readbytes);
                                readbytes = isr.read(tempbytes, 0, 512);
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        } finally {
                            osw.close();
                            isr.close();
                            out.close();
                        }
                        return true;
                    }
                } catch (Throwable ex) {
                    ex.printStackTrace();
                } finally {
                    jar.close();
                }
            }
            return false;
        } catch (Exception ex) {
            System.out.println("[Residence] Failed to write file: " + writeName);
            return false;
        }
    }

    public boolean isPlayerExist(CommandSender sender, String name, boolean inform) {
        if (getPlayerUUID(name) != null)
            return true;
        if (inform)
            sender.sendMessage(msg(lm.Invalid_Player));
        @SuppressWarnings("unused")
        String a = "%%__USER__%%";
        @SuppressWarnings("unused")
        String b = "%%__RESOURCE__%%";
        @SuppressWarnings("unused")
        String c = "%%__NONCE__%%";
        return false;

    }

    public UUID getPlayerUUID(String playername) {
//	if (Residence.getConfigManager().isOfflineMode())
//	    return null;
        Player p = getServ().getPlayer(playername);
        if (p == null) {
            OfflinePlayer po = OfflinePlayerList.get(playername.toLowerCase());
            if (po != null)
                return po.getUniqueId();
        } else
            return p.getUniqueId();
        return null;
    }

    public OfflinePlayer getOfflinePlayer(String Name) {
        if (Name == null)
            return null;
        OfflinePlayer offPlayer = OfflinePlayerList.get(Name.toLowerCase());
        if (offPlayer != null)
            return offPlayer;

        Player player = Bukkit.getPlayerExact(Name);
        if (player != null)
            return player;

//	offPlayer = Bukkit.getOfflinePlayer(Name);
//	if (offPlayer != null)
//	    addOfflinePlayerToChache(offPlayer);
        return offPlayer;
    }

    public String getPlayerUUIDString(String playername) {
        UUID playerUUID = getPlayerUUID(playername);
        if (playerUUID != null)
            return playerUUID.toString();
        return null;
    }

    public OfflinePlayer getOfflinePlayer(UUID uuid) {
        OfflinePlayer offPlayer = cachedPlayerNameUUIDs.get(uuid);
        if (offPlayer != null)
            return offPlayer;

        Player player = Bukkit.getPlayer(uuid);
        if (player != null)
            return player;

//	offPlayer = Bukkit.getOfflinePlayer(uuid);
//	if (offPlayer != null)
//	    addOfflinePlayerToChache(offPlayer);
        return offPlayer;
    }

    public void addOfflinePlayerToChache(OfflinePlayer player) {
        if (player == null)
            return;
        if (player.getName() != null) {
            OfflinePlayerList.put(player.getName().toLowerCase(), player);
            cachedPlayerNames.put(player.getUniqueId(), player.getName());
        }
        cachedPlayerNameUUIDs.put(player.getUniqueId(), player);
    }

    public String getPlayerName(String uuid) {
        try {
            return getPlayerName(UUID.fromString(uuid));
        } catch (IllegalArgumentException ex) {
        }
        return null;
    }

    @Deprecated
    public String getServerLandname() {
        return getServerLandName();
    }

    public String getServerLandName() {
        return this.getLM().getMessage(lm.server_land);
    }

    @Deprecated
    public String getServerLandUUID() {
        return ServerLandUUID.toString();
    }

    @Deprecated
    public String getTempUserUUID() {
        return TempUserUUID.toString();
    }

    public UUID getServerUUID() {
        return ServerLandUUID;
    }

    public UUID getEmptyUserUUID() {
        return TempUserUUID;
    }

    public String getPlayerName(UUID uuid) {
        if (uuid == null)
            return null;

        String cache = cachedPlayerNames.get(uuid);
        if (cache != null) {
            return cache.equalsIgnoreCase("_UNKNOWN_") ? null : cache;
        }

        OfflinePlayer p = getServ().getPlayer(uuid);
        if (p == null)
            p = getOfflinePlayer(uuid);
        if (p != null) {
            cachedPlayerNames.put(uuid, p.getName());
            return p.getName();
        }

        // Last attempt, slowest one
        p = getServ().getOfflinePlayer(uuid);

        if (p != null) {
            String name = p.getName() == null ? "_UNKNOWN_" : p.getName();
            cachedPlayerNames.put(uuid, name);
            return p.getName();
        }

        return null;
    }

    public boolean isDisabledWorld(World world) {
        return isDisabledWorld(world.getName());
    }

    public boolean isDisabledWorld(String worldname) {
        if (!getConfigManager().EnabledWorldsList.isEmpty()) {
            return !getConfigManager().EnabledWorldsList.contains(worldname);
        }
        return getConfigManager().DisabledWorldsList.contains(worldname);
    }

    public boolean isDisabledWorldListener(World world) {
        return isDisabledWorldListener(world.getName());
    }

    public boolean isDisabledWorldListener(String worldname) {

        if (!getConfigManager().EnabledWorldsList.isEmpty()) {
            return !getConfigManager().EnabledWorldsList.contains(worldname) && getConfigManager().DisableListeners;
        }

        return getConfigManager().DisabledWorldsList.contains(worldname) && getConfigManager().DisableListeners;
    }

    public boolean isDisabledWorldCommand(World world) {
        return isDisabledWorldCommand(world.getName());
    }

    public boolean isDisabledWorldCommand(String worldname) {

        if (!getConfigManager().EnabledWorldsList.isEmpty()) {
            return !getConfigManager().EnabledWorldsList.contains(worldname) && getConfigManager().DisableCommands;
        }

        return getConfigManager().DisabledWorldsList.contains(worldname) && getConfigManager().DisableCommands;
    }

    public String msg(String path) {
        return getLM().getMessage(path);
    }

    public void msg(CommandSender sender, String text) {
        if (sender != null && text.length() > 0)
            sender.sendMessage(CMIChatColor.translate(text));
    }

    public void msg(Player player, String text) {
        if (player != null && !text.isEmpty())
            player.sendMessage(CMIChatColor.translate(text));
    }

    public void msg(CommandSender sender, lm lm, Object... variables) {

        if (sender == null)
            return;

        if (getLM().containsKey(lm.getPath())) {
            String msg = getLM().getMessage(lm, variables);
            if (msg.length() > 0)
                sender.sendMessage(msg);
        } else {
            String msg = lm.getPath();
            if (msg.length() > 0)
                sender.sendMessage(lm.getPath());
        }
    }

    public List<String> msgL(lm lm) {
        return getLM().getMessageList(lm);
    }

    public String msg(lm lm, Object... variables) {
        return getLM().getMessage(lm, variables);
    }

    public InformationPager getInfoPageManager() {
        return InformationPagerManager;
    }


    public static Residence getInstance() {
        return instance;
    }

    public String getPrefix() {
        return prefix;
    }

    public String[] reduceArgs(String[] args) {
        if (args.length <= 1)
            return new String[0];
        return Arrays.copyOfRange(args, 1, args.length);
    }

    public boolean isSlimefunPresent() {
        return SlimeFun;
    }

    public boolean isFullyLoaded() {
        return fullyLoaded;
    }
}
