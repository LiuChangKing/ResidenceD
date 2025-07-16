package com.bekvon.bukkit.residence;

import com.bekvon.bukkit.residence.containers.ELMessageType;
import com.bekvon.bukkit.residence.containers.EconomyType;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.containers.RandomTeleport;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import com.bekvon.bukkit.residence.protection.FlagPermissions.FlagState;
import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMIList;
import net.Zrips.CMILib.Container.CMINumber;
import net.Zrips.CMILib.Effects.CMIEffectManager.CMIParticle;
import net.Zrips.CMILib.FileHandler.ConfigReader;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Locale.YmlMaker;
import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.Version.Version;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ConfigManager {
    protected String defaultGroup;
    protected boolean useLeases;
    protected boolean ResMoneyBack;
    protected boolean enableEconomy;
    protected boolean chargeOnCreation;
    protected boolean chargeOnExpansion;
    protected boolean chargeOnAreaAdd;
    private EconomyType VaultEconomy;
    protected boolean adminsOnly;
    protected boolean allowEmptyResidences;
    protected boolean NoLava;
    protected boolean NoWater;
    protected boolean NoLavaPlace;
    protected boolean useBlockFall;
    protected boolean NoWaterPlace;
    protected boolean AutoCleanUp;
    protected boolean UseClean = false;
    protected boolean PvPFlagPrevent;
    protected boolean OverridePvp;
    protected boolean BlockAnyTeleportation;
    protected CMIMaterial infoTool;
    protected int AutoCleanUpDays;
    protected boolean AutoCleanDetailsOnUnknown;
    protected boolean AutoCleanUpRegenerate;
    protected boolean CanTeleportIncludeOwner;
    private boolean LoadEveryWorld;
    public boolean CleanerStartupLog;
    protected CMIMaterial selectionTool;
    protected boolean adminOps;
    protected boolean AdminFullAccess;
    protected String multiworldPlugin;
    protected boolean enableRentSystem;
    // Rent system options removed
    protected boolean leaseAutoRenew;
    protected boolean ShortInfoUse;
    private boolean InfoExcludeDFlags;
    protected int rentCheckInterval;
    protected int leaseCheckInterval;
    protected int autoSaveInt;
    protected boolean NewSaveMechanic;

    private int ItemPickUpDelay;

    private boolean ARCRatioInform;
    private boolean ARCRatioConfirmation;
    private int ARCRatioValue;
    private boolean ARCCheckCollision;
    private boolean ARCOldMethod;
    private String ARCIncrementFormat;
    private int ARCSizePercentage;
    private boolean ARCSizeEnabled;
    private int ARCSizeMin;
    private int ARCSizeMax;

    private boolean ConsoleLogsShowFlagChanges = true;

    // Backup stuff
    protected boolean BackupAutoCleanUpUse;
    protected int BackupAutoCleanUpDays;
    protected boolean UseZipBackup;
    protected boolean BackupWorldFiles;
    protected boolean BackupleasesFile;
    protected boolean BackuppermlistsFile;
    protected boolean BackupflagsFile;
    protected boolean BackupgroupsFile;
    protected boolean BackupconfigFile;

    protected int FlowLevel;
    protected int PlaceLevel;
    protected int BlockFallLevel;
    protected int CleanLevel = 63;
    protected int NewPlayerRangeX;
    protected int NewPlayerRangeY;
    protected int NewPlayerRangeZ;
    protected int VisualizerRange;
    protected int VisualizerShowFor;
    protected int VisualizerUpdateInterval;
    protected int TeleportDelay;
    protected boolean TeleportTitleMessage;
    private List<String> TeleportBlockedWorlds;
    protected int VisualizerRowSpacing;
    protected int VisualizerCollumnSpacing;
    protected int VisualizerSkipBy;
    private int VisualizerFrameCap;
    private int VisualizerSidesCap;
    protected boolean flagsInherit;
    protected boolean ignoreGroupedFlagAcess;
    protected CMIChatColor chatColor;
    private ELMessageType EnterLeaveMessageType;
//    protected boolean actionBar;
//    protected boolean titleMessage;
    protected boolean ActionBarOnSelection;
    protected boolean visualizer;
    protected int minMoveUpdate;

    protected int HealInterval;
    protected int FeedInterval;
    protected int SafeZoneInterval;
    protected FlagPermissions globalCreatorDefaults;
    protected FlagPermissions globalResidenceDefaults;
    protected Map<String, FlagPermissions> globalGroupDefaults;
    protected String language;
    protected String DefaultWorld;
    protected String DateFormat;
    protected String DateFormatShort;
    protected String TimeZone;
    protected boolean preventBuildInRent;
    protected boolean PreventSubZoneRemoval;
    protected boolean stopOnSaveError;

    protected String namefix;
    protected boolean showIntervalMessages;
    protected boolean ShowNoobMessage;
    protected boolean NewPlayerUse;
    protected boolean NewPlayerFree;
    protected boolean spoutEnable;
    protected boolean AutoMobRemoval;
    protected boolean BounceAnimation;
    private boolean EnterAnimation;
    protected boolean useFlagGUI;
    protected int AutoMobRemovalInterval;
    protected boolean enableLeaseMoneyAccount;
    protected boolean Couldroncompatibility;
    protected boolean enableDebug = false;
    protected boolean versionCheck = true;
    protected boolean UUIDConvertion = true;
    protected boolean OfflineMode = false;
    protected boolean SelectionIgnoreY = false;
    protected boolean SelectionIgnoreYInSubzone = false;
    private int SelectionNetherHeight = 128;
    protected boolean NoCostForYBlocks = false;
    protected boolean useVisualizer;
    protected boolean DisableListeners;
    protected boolean DisableCommands;
    private boolean DisableResidenceCreation;

    //Town
//    private boolean TownEnabled = false;
//    private int TownMinRange = 0;

//    protected boolean DisableNoFlagMessageUse;
//    protected List<String> DisableNoFlagMessageWorlds = new ArrayList<String>();

    private HashMap<String, Integer> AntiGreefRangeGaps = new HashMap<String, Integer>();

    protected boolean TNTExplodeBelow;
    protected int TNTExplodeBelowLevel;
    protected boolean CreeperExplodeBelow;
    protected int CreeperExplodeBelowLevel;

    protected List<Material> customContainers = new ArrayList<Material>();
    protected List<Material> customBothClick = new ArrayList<Material>();
    protected List<Material> customRightClick = new ArrayList<Material>();
    protected List<Material> CleanBlocks = new ArrayList<Material>();

    protected List<String> NoFlowWorlds;
    protected List<String> AutoCleanUpWorlds;
    private boolean AutoCleanTrasnferToUser;
    private String AutoCleanUserName;
    protected List<String> NoPlaceWorlds;
    protected List<String> BlockFallWorlds;
    protected List<String> CleanWorlds;
    protected List<String> FlagsList;
    protected List<String> NegativePotionEffects;
    protected List<String> NegativeLingeringPotionEffects;
    private double WalkSpeed1;
    private double WalkSpeed2;
    private int SignsMaxPerResidence;

    protected Location KickLocation;
    protected Location FlyLandLocation;

    protected List<RandomTeleport> RTeleport = new ArrayList<RandomTeleport>();

    protected List<String> DisabledWorldsList = new ArrayList<String>();
    protected List<String> EnabledWorldsList = new ArrayList<String>();

    protected int rtCooldown;
    protected int rtMaxTries;

    private HashMap<FlagState, ItemStack> guiBottonStates = new HashMap<FlagState, ItemStack>();

    private boolean enforceAreaInsideArea;

    protected CMIParticle SelectedFrame;
    protected CMIParticle SelectedSides;

    protected CMIParticle OverlapFrame;
    protected CMIParticle OverlapSides;

//    protected CMIParticle SelectedSpigotFrame;
//    protected CMIParticle SelectedSpigotSides;
//
//    protected CMIParticle OverlapSpigotFrame;
//    protected CMIParticle OverlapSpigotSides;

    // DynMap
    public boolean DynMapHideByDefault;
    public boolean DynMapUse;

    public boolean DynMapShowFlags;
    public boolean DynMapExcludeDefaultFlags;
    public boolean DynMapHideHidden;
    public boolean DynMapLayer3dRegions;
    public int DynMapLayerSubZoneDepth;
    public String DynMapBorderColor;
    public double DynMapBorderOpacity;
    public int DynMapBorderWeight;
    public String DynMapFillColor;
    public double DynMapFillOpacity;
    public List<String> DynMapVisibleRegions;
    public List<String> DynMapHiddenRegions;
    // DynMap

    // Schematics
    public boolean SchematicsSaveOnFlagChange;
    // Schematics

    // Global chat
    public boolean GlobalChatEnabled;
    public boolean GlobalChatSelfModify;
    public String GlobalChatFormat;
    // Global chat

    private Residence plugin;

    public ConfigManager(Residence plugin) {
//	FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Residence.dataFolder, "config.yml"));
        this.plugin = plugin;
        globalCreatorDefaults = new FlagPermissions();
        globalResidenceDefaults = new FlagPermissions();
        globalGroupDefaults = new HashMap<String, FlagPermissions>();
        UpdateConfigFile();
        this.loadFlags();
    }

    public static String Colors(String text) {
        return CMIChatColor.translate(text);
    }

    public static List<String> getClassesFromPackage(String pckgname, String cleaner) throws ClassNotFoundException {
        List<String> result = new ArrayList<String>();
        try {
            for (URL jarURL : ((URLClassLoader) Residence.class.getClassLoader()).getURLs()) {
                try {
                    result.addAll(getClassesInSamePackageFromJar(pckgname, jarURL.toURI().getPath(), cleaner));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException x) {
            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Null pointer exception)");
        }
        return result;
    }

    private static List<String> getClassesInSamePackageFromJar(String packageName, String jarPath, String cleaner) {
        JarFile jarFile = null;
        List<String> listOfCommands = new ArrayList<String>();
        try {
            jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> en = jarFile.entries();
            while (en.hasMoreElements()) {
                JarEntry entry = en.nextElement();
                String entryName = entry.getName();
                packageName = packageName.replace(".", "/");
                if (entryName != null && entryName.endsWith(".yml") && entryName.startsWith(packageName)) {
                    String name = entryName.replace(packageName, "").replace(".yml", "").replace("/", "");
                    if (name.contains("$"))
                        name = name.split("\\$")[0];
                    if (cleaner != null)
                        name = name.replace(cleaner, "");
                    listOfCommands.add(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jarFile != null)
                try {
                    jarFile.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return listOfCommands;
    }

    public void copyOverTranslations() {

        ArrayList<String> languages = new ArrayList<String>();
        try {
            languages.addAll(getClassesFromPackage("Language", null));
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        for (String one : languages) {
            File file = new File(plugin.getDataFolder(), "Language" + File.separator + one + ".yml");
            if (!file.exists()) {
                YmlMaker f = new YmlMaker(plugin, "Language" + File.separator + one + ".yml");
                f.saveDefaultConfig();
                f.ConfigFile.renameTo(file);
            }
        }
    }

    public void ChangeConfig(String path, Boolean stage) {
        File f = new File(plugin.getDataFolder(), "config.yml");

        YamlConfiguration conf = YamlConfiguration.loadConfiguration(f);

        if (!conf.isBoolean(path))
            return;

        conf.set(path, stage);

        try {
            conf.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        plugin.getConfigManager().UpdateConfigFile();
    }

    public static List<String> ColorsArray(List<String> text, Boolean colorize) {
        List<String> temp = new ArrayList<String>();
        for (String part : text) {
            if (colorize)
                part = Colors(part);
            temp.add(Colors(part));
        }
        return temp;
    }

    private static int argb(int alpha, Color color) {
        return alpha << 24 | color.getRed() << 16 | color.getGreen() << 8 | color.getBlue();
    }

    void UpdateFlagFile() {

        File f = new File(plugin.getDataFolder(), "flags.yml");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(f);

//	if (!conf.isConfigurationSection("Global.CompleteDisable"))
//	    conf.crea.createSection("Global.CompleteDisable");

        if (!conf.isList("Global.TotalFlagDisabling"))
            conf.set("Global.TotalFlagDisabling", Arrays.asList("Completely", "Disable", "Particular", "Flags"));

        if (!conf.isBoolean("Global.CommandLimits.Global.Inherit"))
            conf.set("Global.CommandLimits.Global.Inherit", false);

        if (!conf.isList("Global.CommandLimits.Global.WhiteList"))
            conf.set("Global.CommandLimits.Global.WhiteList", Arrays.asList("some allowed command"));

        if (!conf.isList("Global.CommandLimits.Global.BlackList"))
            conf.set("Global.CommandLimits.Global.BlackList", Arrays.asList("some blocked command"));

        TreeMap<String, Flags> sorted = new TreeMap<>();
        for (Flags fl : Flags.values()) {
            sorted.put(fl.getName(), fl);
        }

        for (Flags fl : sorted.values()) {
            if (conf.isBoolean("Global.FlagPermission." + fl))
                continue;
            conf.createSection("Global.FlagPermission." + fl);
            conf.set("Global.FlagPermission." + fl, fl.isEnabled());
        }

        if (!conf.isConfigurationSection("Global.FlagGui")) {
            conf.createSection("Global.FlagGui");
        }



        ConfigurationSection guiSection = conf.getConfigurationSection("Global.FlagGui");

        for (Flags fl : sorted.values()) {
            guiSection.set(fl.toString(), guiSection.get(fl.toString(), fl.getIcon().toString()));
        }

        try {
            conf.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ConfigReader cfg = null;
        try {
            cfg = new ConfigReader(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cfg == null)
            return;
        cfg.load();
        cfg.addComment("Global", "这是 Residence 插件的全局设置。");
        cfg.addComment("Global.Flags", "当玩家不在领地内时生效的世界旗帜设置。");
        cfg.addComment("Global.Flags.Global", "所有世界通用的默认值，可在下方按组覆盖",
            "使用 command: false 旗帜可禁用或允许预设指令，具体列表在 CommandLimits 部分设置");

        cfg.addComment("Global.CommandLimits", "在此列出要允许或阻止的指令", "在全局或世界旗帜中使用 'command: false' 时生效",
            "例如在允许列表写入 'res create'，而在禁止列表使用 '*'，则除了 'res create' 外其余指令均被禁止", "可像世界旗帜一样按世界分别设定",
            "此设定不影响领地内部，领地内的指令限制取决于其 command 旗帜及相应列表",
            "若希望某个领地拥有独立指令限制，需要为其设置 'command: false' 以覆盖全局限制");

        cfg.addComment("Global.CommandLimits.Global.Inherit", "启用后，领地内允许或禁止的指令将继承全局列表，并与领地自身的指令限制合并");

        cfg.addComment("Global.FlagPermission", "除非某权限组被明确禁止，否则所有组都可修改这些旗帜");
        cfg.addComment("Global.FlagGui", "为每个旗帜在 GUI 中设置展示物品，未设置时默认使用灰色羊毛");
        cfg.addComment("Global.ResidenceDefault", "所有用户组创建的领地默认启用的旗帜");
        cfg.addComment("Global.CreatorDefault", "所有用户组的领地创建者默认拥有的旗帜");
        cfg.addComment("Global.GroupedFlags", "组合旗帜设置，执行 /res pset 玩家 redstone true 时会同时赋予列表中所有旗帜，设为 false 或移除时亦如此");
        cfg.addComment("Global.GroupedFlags.trusted", "此组旗帜用于 padd 子指令");
        cfg.addComment("Global.TotalFlagDisabling", "完全禁用指定旗帜，即便 resadmin 命令也无法使用",
            "若不需要某些检测，可通过此项减少服务器开销");
        cfg.addComment("Global.GroupDefault", "任何用户组创建的领地都将默认应用的组旗帜");
        cfg.addComment("ItemList", "在此可以创建物品黑/白名单");
        cfg.addComment("ItemList.DefaultList", "列表名称并不重要，只需保证唯一，最好取个有意义的名称");
        cfg.addComment("ItemList.DefaultList.Type", "列表的类型，可选择 blacklist、whitelist 或 ignorelist");
        cfg.addComment("ItemList.DefaultList.Items", "若需要，可让此列表仅在指定世界生效，否则适用于所有世界",
            "World: world",
            "也可让此列表仅作用于某个权限组，否则对所有组有效",
            "Group: default",
            "以下为此列表允许或禁止的物品名称",
            "可在游戏内使用 /res material <id> 查询物品名称",
            "当然也可以直接填入物品 ID，但这样不够直观，难以快速判断列表内容");

        for (Flags fl : Flags.values()) {
            cfg.addComment("Global.FlagPermission." + fl, "适用于： " + fl.getFlagMode());
        }

        cfg.save();
    }

    public void UpdateGroupedFlagsFile() {

        File f = new File(plugin.getDataFolder(), "flags.yml");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(f);

        if (!conf.isConfigurationSection("Global.GroupedFlags")) {
            conf.createSection("Global.GroupedFlags");
            conf.set("Global.GroupedFlags.redstone", Arrays.asList(
                Flags.note.toString(),
                Flags.pressure.toString(),
                Flags.lever.toString(),
                Flags.button.toString(),
                Flags.diode.toString()));
            conf.set("Global.GroupedFlags.crafting", Arrays.asList(
                Flags.brew.toString(),
                Flags.table.toString(),
                Flags.enchant.toString()));
            conf.set("Global.GroupedFlags.trusted", Arrays.asList(
                Flags.use.toString(),
                Flags.tp.toString(),
                Flags.build.toString(),
                Flags.container.toString(),
                Flags.move.toString(),
                Flags.leash.toString(),
                Flags.animalkilling.toString(),
                Flags.mobkilling.toString(),
                Flags.shear.toString(),
                Flags.chat.toString()));
            conf.set("Global.GroupedFlags.fire", Arrays.asList(
                Flags.ignite.toString(),
                Flags.firespread.toString()));

            try {
                conf.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (Flags one : Flags.values()) {
            one.resetGroups();
        }

        for (String oneGroup : conf.getConfigurationSection("Global.GroupedFlags").getKeys(false)) {
            for (String oneFlag : conf.getStringList("Global.GroupedFlags." + oneGroup)) {

                Flags flag = Flags.getFlag(oneFlag);
                if (flag != null) {
                    flag.addGroup(oneGroup);
                }

                FlagPermissions.addFlagToFlagGroup(oneGroup, oneFlag);
            }
        }
    }

    public void UpdateConfigFile() {

        String defaultWorldName = Bukkit.getServer().getWorlds().size() > 0 ? Bukkit.getServer().getWorlds().get(0).getName() : "World";

        ConfigReader c = null;
        try {
            c = new ConfigReader(Residence.getInstance(), "config.yml");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (c == null)
            return;

        c.copyDefaults(true);

        c.addComment("Global", "Residence 的全局设定");

        c.addComment("Global.UUIDConvertion", "插件启动时进行 UUID 转换", "如不确定请勿更改");
        UUIDConvertion = c.get("Global.UUIDConvertion", true);

        c.addComment("Global.OfflineMode",
            "极不推荐启用，仅在确实需要时再开启",
            "若服务器为离线模式，建议保持为 false 并使用 UUID 管理归属");
        OfflineMode = c.get("Global.OfflineMode", false);

        c.addComment("Global.versionCheck", "拥有 residence.versioncheck 权限的玩家登录时会收到新版本提示");
        versionCheck = c.get("Global.versionCheck", true);

        c.addComment("Global.Language", "从 Language 文件夹加载指定语言文件");
        language = c.get("Global.Language", "English");

        c.addComment("Global.SelectionToolId", "默认的区域选择工具，默认为木锄");
        selectionTool = CMIMaterial.get(c.get("Global.SelectionToolId", CMIMaterial.WOODEN_HOE.name()));

        c.addComment("Global.Selection.IgnoreY", "若启用，选择区域时将忽略 Y 坐标，从基岩到天空");
        SelectionIgnoreY = c.get("Global.Selection.IgnoreY", false);

        c.addComment("Global.Selection.IgnoreYInSubzone",
            "启用后，在已有领地内选择子区块时会自动使用该领地的上下高度",
            "关闭则按玩家实际选择的高度");
        SelectionIgnoreYInSubzone = c.get("Global.Selection.IgnoreYInSubzone", false);

        c.addComment("Global.Selection.netherHeight",
            "在创建住宅时定义了裸体的高度。当执行 /res选择Vert或 /Res Auto之类的命令时，这主要适用，该命令将扩展到定义的高度",
            "这不能高于319或低于1");
        SelectionNetherHeight = c.get("Global.Selection.netherHeight", 128);
        SelectionNetherHeight = SelectionNetherHeight > 319 ? 319 : SelectionNetherHeight < 1 ? 1 : SelectionNetherHeight;

        c.addComment("Global.Selection.NoCostForYBlocks", "通过将其设置为True，玩家只会为X*Z块支付忽略高度",
            "这将降低居住价格高达319次，因此在启用此事之前调整块价格");
        NoCostForYBlocks = c.get("Global.Selection.NoCostForYBlocks", false);


        c.addComment("Global.InfoToolId", "该确定您可以使用哪种工具来查看有关住宅的信息，默认为字符串。",
            "只需装备此工具并击中住宅内部的位置，它将显示其信息。");
        infoTool = CMIMaterial.get(c.get("Global.InfoToolId", Material.STRING.toString()));

        c.addComment("Global.Optimizations.LoadEveryWorld", "启用后，即使世界不存在，我们也会从每个世界文件中加载数据，但以后可能会加载",
            "通常只有在您有多元插件的多元插件时才有用");
        LoadEveryWorld = c.get("Global.Optimizations.LoadEveryWorld", true);

        c.addComment("Global.Optimizations.CleanerStartupLog", "启用后减少插件启动时的额外日志输出");
        CleanerStartupLog = c.get("Global.Optimizations.CleanerStartupLog", true);

        c.addComment("Global.Optimizations.CanTeleportIncludeOwner", "这将稍微更改组的行为文件canteleport部分，其中包括服务器所有者",
            "当将其设置为false和Canteleport设置为False时，玩家将无法选择传送到其他玩家住所，仅为自己",
            "当将其设置为True且Canteleport设置为False时，玩家将无法选择传送到住所",
            "请记住，这仅适用于 /res tp之类的命令");
        CanTeleportIncludeOwner = c.get("Global.Optimizations.CanTeleportIncludeOwner", false);

        c.addComment("Global.Optimizations.DefaultWorld", "主世界名称，通常为默认的 'world'，请务必注意大小写");
        DefaultWorld = c.get("Global.Optimizations.DefaultWorld", defaultWorldName);

        c.addComment("Global.Optimizations.DisabledWorlds.BlackList", "该插件被禁用的世界列表", "请确保世界名称大小写正确",
            "如果白名单包含任何条目，则完全忽略了本节");
        DisabledWorldsList = c.get("Global.Optimizations.DisabledWorlds.BlackList", (List<String>) c.getC().getList("Global.Optimizations.DisabledWorlds.List", new ArrayList<String>()));

        c.addComment("Global.Optimizations.DisabledWorlds.WhiteList", "启用此插件的世界列表", "请确保世界名称大小写正确",
            "如果白名单包含任何条目，则完全忽略了黑名单部分");
        EnabledWorldsList = c.get("Global.Optimizations.DisabledWorlds.WhiteList", new ArrayList<String>());

        c.addComment("Global.Optimizations.DisabledWorlds.DisableListeners", "禁用包含世界的所有听众");
        DisableListeners = c.get("Global.Optimizations.DisabledWorlds.DisableListeners", true);
        c.addComment("Global.Optimizations.DisabledWorlds.DisableCommands", "禁用包含世界中的任何命令用法");
        DisableCommands = c.get("Global.Optimizations.DisabledWorlds.DisableCommands", true);
        c.addComment("Global.Optimizations.DisabledWorlds.DisableResidenceCreation", "禁用包含世界的居住地创建");
        DisableResidenceCreation = c.get("Global.Optimizations.DisabledWorlds.DisableResidenceCreation", true);

        c.addComment("Global.Optimizations.ItemPickUpDelay", "在居住标志之后，延迟在项目拾取之间的几秒钟阻止它", "将其保持在10秒钟以降低不确定性检查");
        ItemPickUpDelay = c.get("Global.Optimizations.ItemPickUpDelay", 10);

        c.addComment("Global.Optimizations.AutomaticResidenceCreation.CheckCollision",
            "设置为true /res auto命令时，将检查与其他住宅的新区域碰撞，以避免重叠。",
            "将其设置为假以获得一些表演，但是新的住所通常可以与旧住所重叠");
        ARCCheckCollision = c.get("Global.Optimizations.AutomaticResidenceCreation.CheckCollision", true);

        c.addComment("Global.Optimizations.AutomaticResidenceCreation.Ratio",
            "自动创建领地时检测形状是否过于奇怪",
            "在创建前会提醒玩家该领地并非标准长方体");
        ARCRatioInform = c.get("Global.Optimizations.AutomaticResidenceCreation.Ratio.Inform", true);

        c.addComment("Global.Optimizations.AutomaticResidenceCreation.Confirmation",
            "虽然启用播放器将被要求单击聊天消息或执行 /res create [resname]以最终确定居住的创建");
        ARCRatioConfirmation = c.get("Global.Optimizations.AutomaticResidenceCreation.Ratio.Confirmation", true);

        c.addComment("Global.Optimizations.AutomaticResidenceCreation.Ratio.Value",
            "定义定量价值时，当居住时间算作怪异形状时",
            "值为3将意味着Cuboid的一侧之一至少比其余的侧面大3倍");
        ARCRatioValue = c.get("Global.Optimizations.AutomaticResidenceCreation.Ratio.Value", 3);

        c.addComment("Global.Optimizations.AutomaticResidenceCreation.OldMethod",
            "启用了这将切换到用于计算新居住区域的旧方法", "旧方法的分配效率较低，因此当您拥有100多个居住区时，它不会推荐使用");
        ARCOldMethod = c.get("Global.Optimizations.AutomaticResidenceCreation.OldMethod", false);

        c.addComment("Global.Optimizations.AutomaticResidenceCreation.IncrementFormat",
            "使用自动住宅创建命令时定义新的住宅名称增量如果已经存在该名称的住宅");
        ARCIncrementFormat = c.get("Global.Optimizations.AutomaticResidenceCreation.IncrementFormat", "_[number]");

        c.addComment("Global.Optimizations.AutomaticResidenceCreation.Size.Enabled",
            "启用时，我们将尝试通过定义的界限创建区域");
        ARCSizeEnabled = c.get("Global.Optimizations.AutomaticResidenceCreation.Size.Enabled", false);

        c.addComment("Global.Optimizations.AutomaticResidenceCreation.Size.Percentage",
            "价值在1到100之间，这将定义我们将根据玩家许可组创建的居住尺寸");
        ARCSizePercentage = c.get("Global.Optimizations.AutomaticResidenceCreation.Size.Percentage", 50);
        ARCSizePercentage = CMINumber.clamp(ARCSizePercentage, 1, 100);

        c.addComment("Global.Optimizations.AutomaticResidenceCreation.Size.Min",
            "块中的值。虽然以前的百分比将决定一般规模，但可以用来避免拥有较小的住宅",
            "例如，如果玩家可以访问30x30居住地，并且百分比设置为50％，那么而不是使用15个块大小，我们将使用20（默认值）",
            "请记住，这不会覆盖实际的最大/分钟居住地大小的玩家可以拥有");
        ARCSizeMin = c.get("Global.Optimizations.AutomaticResidenceCreation.Size.Min", 5);
        ARCSizeMin = ARCSizeMin < 1 ? 1 : ARCSizeMin;

        c.addComment("Global.Optimizations.AutomaticResidenceCreation.Size.Max",
            "块中的值。虽然以前的百分比将决定一般规模，但可以用来避免拥有庞大的住宅",
            "例如，如果玩家可以访问500x500居住地，并且百分比设置为50％，那么而不是使用250个块大小，我们将使用100（默认）",
            "请记住，这不会覆盖实际的最大/分钟居住地大小的玩家可以拥有");
        ARCSizeMax = c.get("Global.Optimizations.AutomaticResidenceCreation.Size.Max", 100);
        ARCSizeMax = ARCSizeMax < ARCSizeMin ? ARCSizeMin : ARCSizeMax;

//	c.addComment("Global.Optimizations.DisabledNoFlagMessage.Use", "启用如果您想在特定世界中隐藏不隐藏标志错误消息",
//	    "您可以使用Residence绕过它。CHECKBADFLAGS权限节点");
//	DisableNoFlagMessageUse = c.get("Global.Optimizations.DisabledNoFlagMessage.Use", false);
//	c.addComment("Global.Optimizations.DisabledNoFlagMessage.Worlds", "玩家无法获得错误消息的世界列表");
//	DisableNoFlagMessageWorlds = c.get("Global.Optimizations.DisabledNoFlagMessage.Worlds", Arrays.asList(Bukkit.getWorlds().get(0).getName()));

        c.addComment("Global.Optimizations.GlobalChat.Enabled",
            "启用或禁用聊天修改，包括播放器主要居住名称");
        GlobalChatEnabled = c.get("Global.Optimizations.GlobalChat.Enabled", false);
        c.addComment("Global.Optimizations.GlobalChat.SelfModify",
            "修改聊天内容以加入聊天前缀；若使用其他聊天管理插件，可在格式中加入 {residence} 标签并关闭此项");
        GlobalChatSelfModify = c.get("Global.Optimizations.GlobalChat.SelfModify", true);
        GlobalChatFormat = c.get("Global.Optimizations.GlobalChat.Format", "&c[&e%1&c]");

        c.addComment("Global.Optimizations.BlockAnyTeleportation",
            "当此项为 true 时，若玩家没有 tp 旗帜，任何传送到该领地的行为都会被拒绝",
            "可避免他人通过第三方插件（如 Essentials 的 /tpa）传送进来");
        BlockAnyTeleportation = c.get("Global.Optimizations.BlockAnyTeleportation", true);

        c.addComment("Global.Optimizations.OverridePvp", "通过将其设置为True，常规PVP标志将充当OverridePVP标志",
            "OverridePVP标志试图忽略任何其他插件在该住宅中的任何PVP保护");
        OverridePvp = c.get("Global.Optimizations.OverridePvp", false);

        // residence kick location
        c.addComment("Global.Optimizations.KickLocation.Use",
            "通过将其设置为True，当玩家从Residence踢另一个玩家时，他将被传送到这个位置，而不是到户外居住");
        Boolean UseKick = c.get("Global.Optimizations.KickLocation.Use", false);
        String KickLocationWorld = c.get("Global.Optimizations.KickLocation.World", defaultWorldName);
        Double KickLocationX = c.get("Global.Optimizations.KickLocation.X", 0.5);
        Double KickLocationY = c.get("Global.Optimizations.KickLocation.Y", 63.0);
        Double KickLocationZ = c.get("Global.Optimizations.KickLocation.Z", 0.5);
        c.addComment("Global.Optimizations.KickLocation.Pitch", "小于0-向上，超过0-朝下。从-90到90不等");
        Double KickPitch = c.get("Global.Optimizations.KickLocation.Pitch", 0.0);
        c.addComment("Global.Optimizations.KickLocation.Yaw", "头部位于左右。从-180到180不等");
        Double KickYaw = c.get("Global.Optimizations.KickLocation.Yaw", 0.0);
        if (UseKick) {
            World world = Bukkit.getWorld(KickLocationWorld);
            if (world != null) {
                KickLocation = new Location(world, KickLocationX, KickLocationY, KickLocationZ);
                KickLocation.setPitch(KickPitch.floatValue());
                KickLocation.setYaw(KickYaw.floatValue());
            }
        } else {
            KickLocation = null;
        }

        c.addComment("Global.Optimizations.FlyLandLocation.World", "当玩家飞行状态时使用时使用，因为飞旗，没有稳固的土地来供球员降落");
        String FlyLocationWorld = c.get("Global.Optimizations.FlyLandLocation.World", defaultWorldName);
        Double FlyLocationX = c.get("Global.Optimizations.FlyLandLocation.X", 0.5);
        Double FlyLocationY = c.get("Global.Optimizations.FlyLandLocation.Y", 63.0);
        Double FlyLocationZ = c.get("Global.Optimizations.FlyLandLocation.Z", 0.5);
        c.addComment("Global.Optimizations.FlyLandLocation.Pitch", "小于0-向上，超过0-朝下。从-90到90不等");
        Double FlyPitch = c.get("Global.Optimizations.FlyLandLocation.Pitch", 0.0);
        c.addComment("Global.Optimizations.FlyLandLocation.Yaw", "头部位于左右。从-180到180不等");
        Double FlyYaw = c.get("Global.Optimizations.FlyLandLocation.Yaw", 0.0);
        World world = Bukkit.getWorld(FlyLocationWorld);
        if (world != null) {
            FlyLandLocation = new Location(world, FlyLocationX, FlyLocationY, FlyLocationZ);
            FlyLandLocation.setPitch(FlyPitch.floatValue());
            FlyLandLocation.setYaw(FlyYaw.floatValue());
        }

        c.addComment("Global.Optimizations.InfoCommand.ShortInformation",
            "通过将其设置为true，在使用 /res信息检查居住信息时，您将仅在列表中获得名称，通过徘徊在它们上，您将获得标志列表");
        ShortInfoUse = c.get("Global.Optimizations.InfoCommand.ShortInformation", c.getC().getBoolean("Global.Optimizations.ShortInfo.Use", true));

        c.addComment("Global.Optimizations.InfoCommand.ExcludeDefaultFlags",
            "将设置为flags.yl文件中设置的true默认居住标志时，将从info命令输出中排除，不会显示", "如果标志获得不同的状态，则将显示");
        InfoExcludeDFlags = c.get("Global.Optimizations.InfoCommand.ExcludeDefaultFlags", false);



        c.addComment("Global.Optimizations.ConsoleLogs.ShowFlagChanges", "如果此真实，将在控制台中记录Flag更改GUI的GUI");
        ConsoleLogsShowFlagChanges = c.get("Global.Optimizations.ConsoleLogs.ShowFlagChanges", true);

        // Healing/Feed interval
        c.addComment("Global.Optimizations.Intervals.Heal", "用适当的标志在几秒钟内治愈/喂养参与者的频率",
            "更大的数字可以节省一些服务器资源", "如果要完全禁用特定的检查，请设置为0。建议如果您不使用特定标志");
        HealInterval = c.get("Global.Optimizations.Intervals.Heal", 1);
        FeedInterval = c.get("Global.Optimizations.Intervals.Feed", 5);
        SafeZoneInterval = c.get("Global.Optimizations.Intervals.SafeZone", 3);

        // negative potion effect list
        c.addComment("Global.Optimizations.NegativePotionEffects",
            "如果居住没有PvP True Flag集，则包含一种含义的药水将被忽略");
        NegativePotionEffects = c.get("Global.Optimizations.NegativePotionEffects", Arrays.asList("blindness", "confusion", "harm", "hunger", "poison", "slow",
            "slow_digging", "weakness", "wither"));

        NegativeLingeringPotionEffects = c.get("Global.Optimizations.NegativeLingeringPotions", Arrays.asList("slowness", "instant_damage", "poison",
            "slowness"));

        c.addComment("Global.Optimizations.WalkSpeed",
            "定义特定WSPEED1和WSPEED2标志的速度。可以从0到5");
        WalkSpeed1 = c.get("Global.Optimizations.WalkSpeed.1", 0.5D);
        WalkSpeed1 = WalkSpeed1 < 0 ? 0 : WalkSpeed1;
        WalkSpeed1 = WalkSpeed1 > 5 ? 5 : WalkSpeed1;
        WalkSpeed1 = WalkSpeed1 / 5.0;
        WalkSpeed2 = c.get("Global.Optimizations.WalkSpeed.2", 2D);
        WalkSpeed2 = WalkSpeed2 < 0 ? 0 : WalkSpeed2;
        WalkSpeed2 = WalkSpeed2 > 5 ? 5 : WalkSpeed2;
        WalkSpeed2 = WalkSpeed2 / 5.0;

        SignsMaxPerResidence = c.get("Global.Signs.MaxPerResidence", 5);
        SignsMaxPerResidence = SignsMaxPerResidence < 0 ? 0 : SignsMaxPerResidence;

        c.addComment("Global.MoveCheckInterval", "运动检查之间的间隔为毫秒。", "减少这将增加服务器上的负载。",
            "增加这将使玩家在被限制的运动区域被传送出去之前进一步移动。");
        minMoveUpdate = c.get("Global.MoveCheckInterval", 500);

        c.addComment("Global.Tp.TeleportDelay", "该间隔在几秒钟内用于传送。", "使用0禁用");
        TeleportDelay = c.get("Global.Tp.TeleportDelay", 3);
        c.addComment("Global.Tp.TeleportTitleMessage", "玩家传送到领地时在标题栏额外显示提示信息");
        TeleportTitleMessage = c.get("Global.Tp.TeleportTitleMessage", true);

        c.addComment("Global.Tp.BlockedWorlds", "不允许使用 /res tp传送的世界列表", "这只会阻止对这些世界的传送，而不是从中阻止");
        TeleportBlockedWorlds = c.get("Global.Tp.BlockedWorlds", Arrays.asList("SomeWorldNames"));
        CMIList.toLowerCase(TeleportBlockedWorlds);

        Set<World> worlds = new HashSet<World>();

        for (int i = 0; i < 3 && i < Bukkit.getWorlds().size(); i++) {
            worlds.add(Bukkit.getWorlds().get(i));
        }

        worlds.addAll(Bukkit.getWorlds());

        RTeleport.clear();
        boolean commented = false;
        if (c.getC().isConfigurationSection("Global.RandomTeleportation.Worlds")) {
            ConfigurationSection sec = c.getC().getConfigurationSection("Global.RandomTeleportation.Worlds");
            if (sec != null)
                for (String one : sec.getKeys(false)) {
                    String path = "Global.RandomTeleportation.Worlds." + one + ".";

                    boolean enabled = c.get(path + "Enabled", true);

                    if (!commented)
                        c.addComment("Global.RandomTeleportation.Worlds." + one,
                            "使用此功能的世界名称。添加带有适当名称的Anthere以启用随机传送");

                    if (!commented)
                        c.addComment(path + "MaxCoord", "最大坐标到传送，设置为1000，可以在-1000和1000坐标之间传送播放器");
                    int MaxCoord = c.get(path + "MaxCoord", 1000);

                    if (!commented)
                        c.addComment(path + "MinCoord",
                            "如果MaxCoord设置为1000，Mincoord设置为500，则可以将播放器传送到-1000至-500至500至1000坐标之间");
                    int MinCoord = c.get(path + "MinCoord", c.getC().getInt(path + "MinCord", 500));
                    int CenterX = c.get(path + "CenterX", 0);
                    int CenterZ = c.get(path + "CenterZ", 0);

                    World w = getWorld(one);

                    if (w == null) {
                        plugin.consoleMessage("&cCan't find world with (" + one + ") name");
                        continue;
                    }

                    commented = true;
                    worlds.remove(w);

                    if (!enabled)
                        continue;
                    RTeleport.add(new RandomTeleport(w, MaxCoord, MinCoord, CenterX, CenterZ));
                }
        }

        for (World one : worlds) {
            String name = one.getName();
            name = name.replace(".", "_");

            String path = "Global.RandomTeleportation.Worlds." + name + ".";
            boolean enabled = c.get(path + "Enabled", true);
            int MaxCoord = c.get(path + "MaxCoord", 1000);
            int MinCoord = c.get(path + "MinCoord", c.getC().getInt(path + "MinCord", 500));
            int CenterX = c.get(path + "CenterX", 0);
            int CenterZ = c.get(path + "CenterZ", 0);

            if (!enabled)
                continue;
            RTeleport.add(new RandomTeleport(one, MaxCoord, MinCoord, CenterX, CenterZ));
        }

        c.addComment("Global.RandomTeleportation.Cooldown", "在再次使用命令之前，要等待多长时间的播放器。");
        rtCooldown = c.get("Global.RandomTeleportation.Cooldown", 5);

        c.addComment("Global.RandomTeleportation.MaxTries", "尝试找到正确的远程位置的次数。",
            "保持较低的数字，因为玩家总是可以在延迟后重试");
        rtMaxTries = c.get("Global.RandomTeleportation.MaxTries", 20);

        c.addComment("Global.SaveInterval", "在居住区之间的间隔（几分钟）节省。");
        autoSaveInt = c.get("Global.SaveInterval", 10);
        c.addComment("Global.NewSaveMechanic", "新的保存机械师可以最大程度地减少保存文件几次，然后加速保存/加载时间一般", "更大的文件具有更大的影响");
        NewSaveMechanic = c.get("Global.NewSaveMechanic", true);

        c.addComment("Global.Backup.AutoCleanUp.Use",
            "您是否要自动从主备份文件夹中自动删除备份文件，如果它们比定义的一天数量大");
        BackupAutoCleanUpUse = c.get("Global.Backup.AutoCleanUp.Use", false);
        BackupAutoCleanUpDays = c.get("Global.Backup.AutoCleanUp.Days", 30);

        c.addComment("Global.Backup.UseZip", "您想通过在备份文件夹中的主要居住文件夹中创建zip文件来备份文件",
            "这个不会对保存文件夹中制作的常规备份文件有影响");
        UseZipBackup = c.get("Global.Backup.UseZip", true);

        BackupWorldFiles = c.get("Global.Backup.IncludeFiles.Worlds", true);
        BackupleasesFile = false;
        BackuppermlistsFile = c.get("Global.Backup.IncludeFiles.permlists", true);
        BackupflagsFile = c.get("Global.Backup.IncludeFiles.flags", true);
        BackupgroupsFile = c.get("Global.Backup.IncludeFiles.groups", true);
        BackupconfigFile = c.get("Global.Backup.IncludeFiles.config", true);

        // Auto remove old residences
        c.addComment("Global.AutoCleanUp.Use", "如果播放器离线X天，则在服务器启动上进行了高度实验性的住宅清洁。",
            "玩家可以使用Residence绕过这一点。CleanBypass权限节点");
        AutoCleanUp = c.get("Global.AutoCleanUp.Use", false);
        c.addComment("Global.AutoCleanUp.Days", "玩家离线多久后删除其领地");
        AutoCleanUpDays = c.get("Global.AutoCleanUp.Days", 60);
        c.addComment("Global.AutoCleanUp.DetailsOnUnknown", "如果无法确定居住所有者，则启用时，我们会将有关它的一些基本信息打印到控制台中");
        AutoCleanDetailsOnUnknown = c.get("Global.AutoCleanUp.DetailsOnUnknown", false);
        c.addComment("Global.AutoCleanUp.Regenerate", "该操作对服务器负担较重，重建过程中可能造成卡顿",
            "是否在清理时还原旧领地区域",
            "此功能需要安装 WorldEdit");
        AutoCleanUpRegenerate = c.get("Global.AutoCleanUp.Regenerate", false);
        c.addComment("Global.AutoCleanUp.Worlds", "将包含在支票清单中的世界");
        AutoCleanUpWorlds = c.get("Global.AutoCleanUp.Worlds", Arrays.asList(defaultWorldName));

        for (int i = 0; i < AutoCleanUpWorlds.size(); i++) {
            AutoCleanUpWorlds.set(i, AutoCleanUpWorlds.get(i).toLowerCase());
        }

        c.addComment("Global.AutoCleanUp.TrasnferToUser", "启用后，我们将住宅转移到定义的用户而不是删除用户", "定义的用户将被排除在清理操作之外");
        AutoCleanTrasnferToUser = c.get("Global.AutoCleanUp.TrasnferToUser", false);

        c.addComment("Global.AutoCleanUp.UserName", "接收删除住宅的用户名称");
        AutoCleanUserName = c.get("Global.AutoCleanUp.UserName", "Server_Land");


        c.addComment("Global.AntiGreef.RangeGaps",
            "遗体之间的距离距离",
            "这将防止住宅区被创建",
            "如果旧居住的所有者和新住所的所有者相同",
            "设置为0或一个空列表，如果要禁用此列表",
            "如果您想对所有世界使用相同的限制，请使用“全部”",
            "如果您只想在这个世界上使用此限制，请使用特定的世界名称",
            "特定的世界名称将覆盖“所有”价值");

        List<String> ls = new ArrayList<String>();
        if (c.getC().isInt("Global.AntiGreef.RangeGap")) {
            ls.add("all-" + c.getC().getInt("Global.AntiGreef.RangeGap"));
            c.get("Global.AntiGreef.RangeGaps", Arrays.asList("all-" + c.getC().getInt("Global.AntiGreef.RangeGap")));
        } else
            ls = c.get("Global.AntiGreef.RangeGaps", Arrays.asList("all-8"));

        for (String one : ls) {
            String[] split = one.split("-");
            if (split.length < 2)
                continue;
            try {
                String worldName = one.substring(0, one.length() - split[split.length - 1].length() - 1);
                int range = Integer.parseInt(split[split.length - 1]);
                AntiGreefRangeGaps.put(worldName.toLowerCase(), range);
            } catch (Throwable e) {
                continue;
            }
        }

        // TNT explosions below 63
        c.addComment("Global.AntiGreef.TNT.ExplodeBelow",
            "设置为true时，将允许带有TNT的TNT和MINECART在住宅以外的62（默认）级别爆炸",
            "这将允许使用TNT和更多香草播放开采");
        TNTExplodeBelow = c.get("Global.AntiGreef.TNT.ExplodeBelow", false);
        TNTExplodeBelowLevel = c.get("Global.AntiGreef.TNT.level", 62);
        // Creeper explosions below 63
        c.addComment("Global.AntiGreef.Creeper.ExplodeBelow", "当设置为true时，将允许爬行者在62（默认）级别以下爆炸",
            "这将提供更现实的游戏",
            "为此，您将需要在世界范围内禁用爬行者爆炸。这可以在全球世界部分下的标志文件中完成");
        CreeperExplodeBelow = c.get("Global.AntiGreef.Creeper.ExplodeBelow", false);
        CreeperExplodeBelowLevel = c.get("Global.AntiGreef.Creeper.level", 62);
        // Flow
        c.addComment("Global.AntiGreef.Flow.Level", "从中开始熔岩和水流阻滞的水平", "这在居住区没有影响");
        FlowLevel = c.get("Global.AntiGreef.Flow.Level", 63);
        c.addComment("Global.AntiGreef.Flow.NoLavaFlow", "此设置为真，熔岩流室外被阻塞");
        NoLava = c.get("Global.AntiGreef.Flow.NoLavaFlow", false);
        c.addComment("Global.AntiGreef.Flow.NoWaterFlow", "将此设置为真，住宅外部的水流被阻塞");
        NoWater = c.get("Global.AntiGreef.Flow.NoWaterFlow", false);
        NoFlowWorlds = c.get("Global.AntiGreef.Flow.Worlds", Arrays.asList(defaultWorldName));

        // Place
        c.addComment("Global.AntiGreef.Place.Level", "从哪个开始，从哪个开始熔岩和水位", "这在居住区没有影响");
        PlaceLevel = c.get("Global.AntiGreef.Place.Level", 63);
        c.addComment("Global.AntiGreef.Place.NoLavaPlace", "随着此设置为真，Playrs不能将熔岩放在住宅外");
        NoLavaPlace = c.get("Global.AntiGreef.Place.NoLavaPlace", false);
        c.addComment("Global.AntiGreef.Place.NoWaterPlace", "将此设置为真，玩家不能将水放在住所外");
        NoWaterPlace = c.get("Global.AntiGreef.Place.NoWaterPlace", false);
        NoPlaceWorlds = c.get("Global.AntiGreef.Place.Worlds", Arrays.asList(defaultWorldName));

        // Sand fall
        c.addComment("Global.AntiGreef.BlockFall.Use", "随着此设置为真，如果它们将降落在不同的区域");
        useBlockFall = c.get("Global.AntiGreef.BlockFall.Use", true);
        c.addComment("Global.AntiGreef.BlockFall.Level", "从哪个开始，从哪个开始块的跌落",
            "这在居住区或外部没有影响");
        BlockFallLevel = c.get("Global.AntiGreef.BlockFall.Level", 62);
        BlockFallWorlds = c.get("Global.AntiGreef.BlockFall.Worlds", Arrays.asList(defaultWorldName));

        if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
            // Res cleaning
            CleanBlocks.clear();
            c.addComment("Global.AntiGreef.ResCleaning.Use",
                "将此设置为真，在玩家删除其住所后，下面列出的所有块将被空气块替换",
                "防止居住在格里芬目标附近创建的有效方法，然后将其删除",
                "注意力！清理区域时，较大的居住区可能希望在服务器上创造更大的负载。因此，如果普通玩家可以使用大型住宅，请不要使用此功能。 1500万块将是最大限制");
            UseClean = c.get("Global.AntiGreef.ResCleaning.Use", false);
            c.addComment("Global.AntiGreef.ResCleaning.Level", "您要替换块的级别");
            CleanLevel = c.get("Global.AntiGreef.ResCleaning.Level", 63);
            c.addComment("Global.AntiGreef.ResCleaning.Blocks", "要替换的块列表", "默认情况下，只有水和熔岩将被更换");
            List<?> pls = c.get("Global.AntiGreef.ResCleaning.Blocks", Arrays.asList(CMIMaterial.WATER.toString(), CMIMaterial.LAVA.toString()));
            for (Object one : pls) {
                CMIMaterial mat = CMIMaterial.get(String.valueOf(one));
                if (mat != CMIMaterial.NONE && mat.getMaterial() != null && !mat.isAir())
                    CleanBlocks.add(mat.getMaterial());
            }

            CleanWorlds = c.get("Global.AntiGreef.ResCleaning.Worlds", Arrays.asList(defaultWorldName));
            for (int i = 0; i < CleanWorlds.size(); i++) {
                CleanWorlds.set(i, CleanWorlds.get(i).toLowerCase());
            }
        }

        c.addComment("Global.AntiGreef.Flags.Prevent", "通过将其设置为“列表”的真实标志，将受到更改的保护",
            "在邀请某人并将PVP标志杀死的人们中保护示例以杀死他们");
        PvPFlagPrevent = c.get("Global.AntiGreef.Flags.Prevent", true);
        FlagsList = c.get("Global.AntiGreef.Flags.list", Arrays.asList("pvp"));

        c.addComment("Global.DefaultGroup", "如果权限无法连接或您不使用权限，则使用的默认组使用。");
        defaultGroup = c.get("Global.DefaultGroup", "default");

        useLeases = false;

        DateFormat = "E yyyy.MM.dd 'at' hh:mm:ss a zzz";
        DateFormatShort = "MM.dd hh:mm";

        c.addComment("Global.TimeZone", "设置日期显示所使用的时区，当服务器位置与玩家分布地区不同时尤其有用",
            "完整时区列表可在 http://www.mkyong.com/java/java-display-list-of-timezone-with-gmt/ 查看");
        TimeZone = c.get("Global.TimeZone", Calendar.getInstance().getTimeZone().getID());

        c.addComment("Global.ResMoneyBack", "启用或禁用移除领地时返还金钱");
        ResMoneyBack = c.get("Global.ResMoneyBack", false);


        leaseCheckInterval = 0;
        leaseAutoRenew = false;

        c.addComment("Global.EnablePermissions", "是否启用权限系统");
        c.get("Global.EnablePermissions", true);

        c.addComment("Global.EnableEconomy", "启用或禁用 Residence 的经济系统（支持 Vault）");
        enableEconomy = c.get("Global.EnableEconomy", true);

        c.addComment("Global.ChargeWhen", "定义在何时收取费用，仅在启用经济系统时生效");
        c.addComment("Global.ChargeWhen.Creating", "在创建领地（/res create 与 /res auto）时收取费用");
        chargeOnCreation = c.get("Global.ChargeWhen.Creating", true);
        c.addComment("Global.ChargeWhen.Expanding", "在扩展领地面积时收取费用");
        chargeOnExpansion = c.get("Global.ChargeWhen.Expanding", true);
        c.addComment("Global.ChargeWhen.AreaAdd", "在新增区域时收取费用");
        chargeOnAreaAdd = c.get("Global.ChargeWhen.AreaAdd", true);

        c.addComment("Global.Type", "默认为 None，会通过 Vault API 尝试连接默认经济插件，如失败则寻找其他受支持的经济插件",
            "也可以自定义经济接口以直接接入经济系统", "可用值: " + EconomyType.toStringLine());
        VaultEconomy = EconomyType.getByName(c.get("Global.Type", "None"));
        if (VaultEconomy == null)

        {
            plugin.consoleMessage("&cCould not determine economy from " + c.get("Global.Type", "Vault"));
            plugin.consoleMessage("&cTrying to find suitable economy system");
            VaultEconomy = EconomyType.None;
        }

        // Rent system removed
        enableRentSystem = false;

//	TownEnabled = c.get("Global.Town.Enabled", true);
//	c.addComment("Global.Town.MinRange", "住宅之间的范围","如果所有者不属于同一城镇");
//	TownMinRange = c.get("Global.Town.MinRange", 16);


        c.addComment("Global.Rent.Schematics.SaveOnFlagChange", "此设置已停用");
        SchematicsSaveOnFlagChange = true;


        // Rent system removed, interval not used
        rentCheckInterval = 0;

        ELMessageType old = c.getC().isBoolean("Global.ActionBar.General") && c.getC().getBoolean("Global.ActionBar.General") ? ELMessageType.ActionBar
            : ELMessageType.ActionBar;
        old = c.getC().isBoolean("Global.TitleBar.EnterLeave") && c.getC().getBoolean("Global.TitleBar.EnterLeave") ? ELMessageType.TitleBar : old;

        c.addComment("Global.Messages.GeneralMessages", "定义要发送居住地的位置，请输入/离开/拒绝移动和类似消息。可能的选项： " + ELMessageType.getAllValuesAsString(),
            "titlebar可以具有％subtitle％变量来定义第二行");
        EnterLeaveMessageType = ELMessageType.getByName(c.get("Global.Messages.GeneralMessages", old.toString()));
        if (EnterLeaveMessageType == null || Version.isCurrentEqualOrLower(Version.v1_7_R4))
            EnterLeaveMessageType = ELMessageType.ActionBar;

        ActionBarOnSelection = c.get("Global.ActionBar.ShowOnSelection", true);

        c.addComment("Global.ResidenceChatColor", "居住聊天的颜色。");
        try {
            chatColor = CMIChatColor.getColor((c.get("Global.ResidenceChatColor", "DARK_PURPLE")));
        } catch (Exception ex) {
            chatColor = CMIChatColor.DARK_PURPLE;
        }
        if (chatColor == null) {
            CMIMessages.consoleMessage("&cCan't find color by name for ResidenceChatColor");
            chatColor = CMIChatColor.DARK_PURPLE;
        }


        c.addComment("Global.AdminOnlyCommands", "是否忽略通常的权限标志，仅允许使用“居住”的OP和组更改住宅。");
        adminsOnly = c.get("Global.AdminOnlyCommands", false);

        c.addComment("Global.AdminOPs", "将其设置为True使服务器OPS管理员。");
        adminOps = c.get("Global.AdminOPs", true);

        c.addComment("Global.AdminFullAccess",
            "将其设置为True Server Administration，如果它们是OP或具有Residence.Admin权限节点，则不需要使用 /resadmin命令访问admin命令。");
        AdminFullAccess = c.get("Global.AdminFullAccess", false);

        c.addComment("Global.MultiWorldPlugin", "这是您用于多世界的插件的名称，如果您没有多世界插件，则可以安全地忽略它。",
            "唯一要做的是检查在居住之前启用多世界插件，以确保适当地为其他世界加载住宅。");
        multiworldPlugin = c.get("Global.MultiWorldPlugin", "Multiverse-Core");

        c.addComment("Global.ResidenceFlagsInherit", "将其设置为真实会导致子区域从其父区域继承标志。");
        flagsInherit = c.get("Global.ResidenceFlagsInherit", true);

        c.addComment("Global.PreventRentModify", "此选项已停用，保持默认值即可");
        preventBuildInRent = c.get("Global.PreventRentModify", true);

        c.addComment("Global.PreventSubZoneRemoval", "当Subzone所有者与父级所有者不同时，将其设置为true将防止子区删除。");
        PreventSubZoneRemoval = c.get("Global.PreventSubZoneRemoval", true);

        c.addComment("Global.StopOnSaveFault", "将其设置为False，即使在保存文件中检测到错误，也会导致住宅继续加载。");
        stopOnSaveError = c.get("Global.StopOnSaveFault", true);

        c.addComment("This is the residence name filter, that filters out invalid characters.  Google 'Java RegEx' or 'Java Regular Expressions' for more info on how they work.");
        namefix = c.get("Global.ResidenceNameRegex", "[^a-zA-Z0-9\\-\\_]");

        c.addComment("Global.ShowIntervalMessages", "已停用的间隔提示设置");
        showIntervalMessages = c.get("Global.ShowIntervalMessages", false);

        c.addComment("Global.ShowNoobMessage", "将其设置为True，将胸部放在地面上时，将其发送给新玩家的教程消息。");
        ShowNoobMessage = c.get("Global.ShowNoobMessage", true);

        c.addComment("Global.NewPlayer", "如果没有任何东西，将其设置为True创建居住的胸部。", "如果他仍然没有任何住所，则只有一次重新启动");
        NewPlayerUse = c.get("Global.NewPlayer.Use", false);
        c.addComment("Global.NewPlayer.Free", "将其设置为真实，将免费创建住宅", "通过设置为虚假，如果他拥有的话，将从玩家那里拿走钱");
        NewPlayerFree = c.get("Global.NewPlayer.Free", true);
        c.addComment("Global.NewPlayer.Range", "从放置的胸部到两侧。通过设置为5，住宅总共将为5+5+1 = 11个块");
        NewPlayerRangeX = c.get("Global.NewPlayer.Range.X", 5);
        NewPlayerRangeY = c.get("Global.NewPlayer.Range.Y", 5);
        NewPlayerRangeZ = c.get("Global.NewPlayer.Range.Z", 5);

        c.addComment("Global.CustomContainers", "实验 - 以下设置是用于使用MOD时的“容器”和“使用”标志检查的块ID列表。");
        List<String> pls = c.get("Global.CustomContainers", new ArrayList<String>());
        for (String one : pls) {
            Material mat = CMILib.getInstance().getItemManager().getMaterial(one);
            if (mat != null)
                customContainers.add(mat);
        }

        pls = c.get("Global.CustomBothClick", new ArrayList<String>());
        for (String one : pls) {
            Material mat = CMILib.getInstance().getItemManager().getMaterial(one);
            if (mat != null)
                customBothClick.add(mat);
        }

        pls = c.get("Global.CustomRightClick", new ArrayList<String>());
        for (String one : pls) {
            Material mat = CMILib.getInstance().getItemManager().getMaterial(one);
            if (mat != null)
                customRightClick.add(mat);
        }

        c.addComment("Global.Visualizer.Use", "启用后，玩家将看到粒子效果以标记选择范围");
        useVisualizer = c.get("Global.Visualizer.Use", true);
        c.addComment("Global.Visualizer.Range", "绘制粒子效果的范围（以方块为单位）", "建议不要超过 30，玩家实际可见距离约为 16 格");
        VisualizerRange = c.get("Global.Visualizer.Range", 16);
        c.addComment("Global.Visualizer.ShowFor", "粒子效果显示时长，单位毫秒（5000 = 5 秒）");
        VisualizerShowFor = c.get("Global.Visualizer.ShowFor", 5000);
        c.addComment("Global.Visualizer.updateInterval", "每隔多少刻更新一次粒子效果");
        VisualizerUpdateInterval = c.get("Global.Visualizer.updateInterval", 20);
        c.addComment("Global.Visualizer.RowSpacing", "行与行之间粒子效果的间距");
        VisualizerRowSpacing = c.get("Global.Visualizer.RowSpacing", 1);
        if (VisualizerRowSpacing < 1)
            VisualizerRowSpacing = 1;
        c.addComment("Global.Visualizer.CollumnSpacing", "列与列之间粒子效果的间距");
        VisualizerCollumnSpacing = c.get("Global.Visualizer.CollumnSpacing", 1);
        if (VisualizerCollumnSpacing < 1)
            VisualizerCollumnSpacing = 1;

        c.addComment("Global.Visualizer.SkipBy", "定义跳过多少粒子来形成移动效果", "值越大可以适当降低更新频率");
        VisualizerSkipBy = c.get("Global.Visualizer.SkipBy", 2);
        if (VisualizerSkipBy < 1)
            VisualizerSkipBy = 1;

        c.addComment("Global.Visualizer.FrameCap", "每名玩家可见的最大边框粒子数量");
        VisualizerFrameCap = c.get("Global.Visualizer.FrameCap", 500);
        if (VisualizerFrameCap < 1)
            VisualizerFrameCap = 1;

        c.addComment("Global.Visualizer.SidesCap", "每名玩家可见的最大侧面粒子数量");
        VisualizerSidesCap = c.get("Global.Visualizer.SidesCap", 2000);
        if (VisualizerSidesCap < 1)
            VisualizerSidesCap = 1;

        StringBuilder effectsList = new StringBuilder();

        for (Effect one : Effect.values()) {
            if (one == null)
                continue;
            if (one.name() == null)
                continue;
            if (!effectsList.toString().isEmpty())
                effectsList.append(", ");
            effectsList.append(one.name().toLowerCase());
        }

        c.addComment("Global.Visualizer.Selected", "粒子效果名称，例如: explode、largeexplode、hugeexplosion、fireworksSpark、splash、wake、crit、magicCrit",
            " smoke、largesmoke、spell、instantSpell、mobSpell、mobSpellAmbient、witchMagic、dripWater、dripLava、angryVillager、happyVillager、townaura",
            " note、portal、enchantmenttable、flame、lava、footstep、cloud、reddust、snowballpoof、snowshovel、slime、heart、barrier",
            " droplet、take、mobappearance",
            "",
            "若使用 Spigot 服务器，可用的粒子效果还包括: " + effectsList.toString());

        // Frame
        String efname = c.get("Global.Visualizer.Selected.Frame", "happyVillager");
        SelectedFrame = CMIParticle.getCMIParticle(efname);
        if (SelectedFrame == null) {
            SelectedFrame = CMIParticle.HAPPY_VILLAGER;
            Bukkit.getConsoleSender().sendMessage("Can't find effect for Selected Frame with this name, it was set to default");
        }

        // Sides
        efname = c.get("Global.Visualizer.Selected.Sides", "reddust");
        SelectedSides = CMIParticle.getCMIParticle(efname);
        if (SelectedSides == null) {
            SelectedSides = CMIParticle.COLOURED_DUST;
            Bukkit.getConsoleSender().sendMessage("Can't find effect for Selected Sides with this name, it was set to default");
        }

        efname = c.get("Global.Visualizer.Overlap.Frame", "FLAME");
        OverlapFrame = CMIParticle.getCMIParticle(efname);
        if (OverlapFrame == null) {
            OverlapFrame = CMIParticle.FLAME;
            Bukkit.getConsoleSender().sendMessage("Can't find effect for Overlap Frame with this name, it was set to default");
        }

        efname = c.get("Global.Visualizer.Overlap.Sides", "FLAME");
        OverlapSides = CMIParticle.getCMIParticle(efname);
        if (OverlapSides == null) {
            OverlapSides = CMIParticle.FLAME;
            Bukkit.getConsoleSender().sendMessage("Can't find effect for Selected Sides with this name, it was set to default");
        }

        c.addComment("Global.Visualizer.EnterAnimation", "当玩家进入居住时，显示出粒子的效果。仅适用于主要居住区");
        EnterAnimation = c.get("Global.Visualizer.EnterAnimation", true);

        c.addComment("Global.BounceAnimation", "当玩家被推回时显示粒子效应");
        BounceAnimation = c.get("Global.BounceAnimation", true);

        c.addComment("Global.GUI.Enabled", "启用或禁用Flag Gui");
        useFlagGUI = c.get("Global.GUI.Enabled", true);

        c.addComment("Global.GUI.setTrue", "将标志设置为true时要使用的项目ID和数据");

        CMIMaterial Mat = CMIMaterial.get(c.get("Global.GUI.setTrue", "GREEN_WOOL"));
        if (Mat == null)
            Mat = CMIMaterial.GREEN_WOOL;
        guiBottonStates.put(FlagState.TRUE, Mat.newItemStack());

        c.addComment("Global.GUI.setFalse", "将标志设置为false时要使用的项目ID和数据");
        Mat = CMIMaterial.get(c.get("Global.GUI.setFalse", "RED_WOOL"));
        if (Mat == null)
            Mat = CMIMaterial.RED_WOOL;
        guiBottonStates.put(FlagState.FALSE, Mat.newItemStack());

        c.addComment("Global.GUI.setRemove", "设置标志以删除标志时要使用的项目ID和数据");
        Mat = CMIMaterial.get(c.get("Global.GUI.setRemove", "LIGHT_GRAY_WOOL"));
        if (Mat == null)
            Mat = CMIMaterial.LIGHT_GRAY_WOOL;
        guiBottonStates.put(FlagState.NEITHER, Mat.newItemStack());

        c.addComment("Global.AutoMobRemoval", "默认值= false。启用此功能，将定期从怪物中清除带有标志名称的住宅。",
            "这在服务器端很重，所以只有在您真的需要此功能的情况下才能启用");
        AutoMobRemoval = c.get("Global.AutoMobRemoval.Use", false);
        c.addComment("Global.AutoMobRemoval.Interval", "几秒钟内一次检查住宅中的怪物的频率。保持合理的数量");
        AutoMobRemovalInterval = c.get("Global.AutoMobRemoval.Interval", 5);

        enforceAreaInsideArea = c.get("Global.EnforceAreaInsideArea", false);
        spoutEnable = c.get("Global.EnableSpout", false);
        enableLeaseMoneyAccount = false;

        c.addComment("Global.Couldroncompatibility",
            "通过将其设置为真实，将启用Kcouldron服务器的部分兼容性。动作栏消息和选择可视化器将自动禁用，因为不正确的兼容性");
        Couldroncompatibility = c.get("Global.Couldroncompatibility", false);
        if (Couldroncompatibility) {
            useVisualizer = false;
            EnterLeaveMessageType = ELMessageType.ChatBox;
            ActionBarOnSelection = false;
        }

        c.addComment("DynMap.Use", "启用或禁用Dynmap支持");
        DynMapUse = c.get("DynMap.Use", true);
        c.addComment("DynMap.HideByDefault", "当设置为true时，我们将默认在Dynmap窗口上隐藏居住区", "仍然可以启用住宅，可以在左上角提供Dynmap选项");
        DynMapHideByDefault = c.get("DynMap.HideByDefault", false);
        c.addComment("DynMap.ShowFlags", "显示或隐藏领地的标志");
        DynMapShowFlags = c.get("DynMap.ShowFlags", true);
        c.addComment("DynMap.ExcludeDefaultFlags", "启用后，默认标志不会在领地概览中显示");
        DynMapExcludeDefaultFlags = c.get("DynMap.ExcludeDefaultFlags", true);
        c.addComment("DynMap.HideHidden", "若设为 true，带有 hidden 标志的领地在 dynmap 上也会被隐藏");
        DynMapHideHidden = c.get("DynMap.HideHidden", true);

        c.addComment("DynMap.Layer.3dRegions", "启用3D区域");
        DynMapLayer3dRegions = c.get("DynMap.Layer.3dRegions", true);
        c.addComment("DynMap.Layer.SubZoneDepth", "有多深入subsines展示");
        DynMapLayerSubZoneDepth = c.get("DynMap.Layer.SubZoneDepth", 2);

        c.addComment("DynMap.Border.Color", "边界的颜色。从此页面挑选颜色http://www.w3schools.com/colors/colors_picker.asp");
        DynMapBorderColor = c.get("DynMap.Border.Color", "#FF0000");
        c.addComment("DynMap.Border.Opacity", "透明度。 0.3表示只有30％的颜色可见");
        DynMapBorderOpacity = c.get("DynMap.Border.Opacity", 0.3);
        c.addComment("DynMap.Border.Weight", "边界厚度");
        DynMapBorderWeight = c.get("DynMap.Border.Weight", 3);
        DynMapFillOpacity = c.get("DynMap.Fill.Opacity", 0.3);
        DynMapFillColor = c.get("DynMap.Fill.Color", "#FFFF00");

        c.addComment("DynMap.VisibleRegions", "仅在此列表中显示区域");
        DynMapVisibleRegions = c.get("DynMap.VisibleRegions", new ArrayList<String>());
        c.addComment("DynMap.HiddenRegions", "即使没有隐藏在地图上的地图上的区域");
        DynMapHiddenRegions = c.get("DynMap.HiddenRegions", new ArrayList<String>());

        c.save();
    }

    private Color processColor(String cls) {
        try {
            if (cls.startsWith("#")) {
                cls = cls.substring(1);
                cls = CMIChatColor.colorCodePrefix + cls + CMIChatColor.colorCodeSuffix;
            }
            CMIChatColor col = CMIChatColor.getColor(cls);
            if (col != null && col.getJavaColor() != null)
                return col.getJavaColor();
        } catch (Throwable e) {
        }
        return new Color(125, 125, 125);
    }

    public void loadFlags() {
        FileConfiguration flags = YamlConfiguration.loadConfiguration(new File(plugin.dataFolder, "flags.yml"));

        if (flags.isList("Global.TotalFlagDisabling")) {
            List<String> globalDisable = flags.getStringList("Global.TotalFlagDisabling");

            // Re enabling all of them before loading flags file
            for (Flags one : Flags.values()) {
                one.setGlobalyEnabled(true);
            }

            for (String fl : globalDisable) {
                Flags flag = Flags.getFlag(fl);
                if (flag == null) {
                    continue;
                }
                flag.setGlobalyEnabled(false);
            }
        }

        globalCreatorDefaults = FlagPermissions.parseFromConfigNode("CreatorDefault", flags.getConfigurationSection("Global"));
        globalResidenceDefaults = FlagPermissions.parseFromConfigNode("ResidenceDefault", flags.getConfigurationSection("Global"));
        loadGroups();
    }

    public void loadGroups() {
        FileConfiguration groups = YamlConfiguration.loadConfiguration(new File(plugin.dataFolder, "groups.yml"));
        ConfigurationSection node = groups.getConfigurationSection("Global.GroupDefault");
        if (node != null) {
            Set<String> keys = node.getConfigurationSection(defaultGroup).getKeys(false);
            if (keys != null) {
                for (String key : keys) {
                    globalGroupDefaults.put(key, FlagPermissions.parseFromConfigNodeAsList(defaultGroup, "false"));
                }
            }
        }
    }

    public World getWorld(String name) {
        name = name.replace("_", "").replace(".", "");
        for (World one : Bukkit.getWorlds()) {
            if (one.getName().replace("_", "").replace(".", "").equalsIgnoreCase(name))
                return one;
        }
        return null;
    }

    public boolean isGlobalChatEnabled() {
        return GlobalChatEnabled;
    }

    public boolean isGlobalChatSelfModify() {
        return GlobalChatSelfModify;
    }

    public String getGlobalChatFormat() {
        return GlobalChatFormat;
    }


    public boolean isTNTExplodeBelow() {
        return TNTExplodeBelow;
    }

    public int getTNTExplodeBelowLevel() {
        return TNTExplodeBelowLevel;
    }

    public boolean isCreeperExplodeBelow() {
        return CreeperExplodeBelow;
    }

    public int getCreeperExplodeBelowLevel() {
        return CreeperExplodeBelowLevel;
    }

    public boolean useVisualizer() {
        return useVisualizer;
    }

    public int getVisualizerRange() {
        return VisualizerRange;
    }

    public int getVisualizerShowFor() {
        return VisualizerShowFor;
    }

    public int getNewPlayerRangeX() {
        return NewPlayerRangeX;
    }

    public int getNewPlayerRangeY() {
        return NewPlayerRangeY;
    }

    public int getNewPlayerRangeZ() {
        return NewPlayerRangeZ;
    }

    public int getVisualizerRowSpacing() {
        return VisualizerRowSpacing;
    }

    public int getVisualizerCollumnSpacing() {
        return VisualizerCollumnSpacing;
    }

    public int getVisualizerSkipBy() {
        return VisualizerSkipBy;
    }

    public int getVisualizerUpdateInterval() {
        return VisualizerUpdateInterval;
    }

    public CMIParticle getSelectedFrame() {
        return SelectedFrame;
    }

    public CMIParticle getSelectedSides() {
        return SelectedSides;
    }

    public CMIParticle getOverlapFrame() {
        return OverlapFrame;
    }

    public CMIParticle getOverlapSides() {
        return OverlapSides;
    }

    @Deprecated
    public CMIParticle getSelectedSpigotFrame() {
        return SelectedFrame;
    }

    @Deprecated
    public CMIParticle getSelectedSpigotSides() {
        return SelectedSides;
    }

    @Deprecated
    public CMIParticle getOverlapSpigotFrame() {
        return OverlapFrame;
    }

    @Deprecated
    public CMIParticle getOverlapSpigotSides() {
        return OverlapSides;
    }

    public int getTeleportDelay() {
        return TeleportDelay;
    }

    public boolean isTeleportTitleMessage() {
        return TeleportTitleMessage;
    }

    public String getDefaultGroup() {
        return defaultGroup;
    }

    public String getResidenceNameRegex() {
        return namefix;
    }

    public boolean enableEconomy() {
        return enableEconomy && plugin.getEconomyManager() != null;
    }

    public boolean enabledRentSystem() {
        return enableRentSystem && enableEconomy();
    }

    public boolean useLeases() {
        return useLeases;
    }

    public boolean useResMoneyBack() {
        return ResMoneyBack;
    }

    public boolean allowAdminsOnly() {
        return adminsOnly;
    }

    public boolean allowEmptyResidences() {
        return allowEmptyResidences;
    }

    public boolean isNoLava() {
        return NoLava;
    }

    public boolean isNoWater() {
        return NoWater;
    }

    public boolean isNoLavaPlace() {
        return NoLavaPlace;
    }

    public boolean isBlockFall() {
        return useBlockFall;
    }

    public boolean isNoWaterPlace() {
        return NoWaterPlace;
    }


    public boolean isUseResidenceFileClean() {
        return AutoCleanUp;
    }

    public int getResidenceFileCleanDays() {
        return AutoCleanUpDays;
    }

    public boolean isAutoCleanUpRegenerate() {
        return AutoCleanUpRegenerate;
    }

    public boolean isAutoCleanDetailsOnUnknown() {
        return AutoCleanDetailsOnUnknown;
    }

    public boolean isUseClean() {
        return UseClean;
    }

    public boolean isPvPFlagPrevent() {
        return PvPFlagPrevent;
    }

    public boolean isOverridePvp() {
        return OverridePvp;
    }

    public boolean isBlockAnyTeleportation() {
        return BlockAnyTeleportation;
    }

    @Deprecated
    public int getInfoToolID() {
        return infoTool.getId();
    }

    public CMIMaterial getInfoTool() {
        return infoTool;
    }

    public CMIMaterial getSelectionTool() {
        return selectionTool;
    }

    @Deprecated
    public int getSelectionTooldID() {
        return selectionTool.getId();
    }

    public boolean getOpsAreAdmins() {
        return adminOps;
    }

    public boolean getAdminFullAccess() {
        return AdminFullAccess;
    }

    public String getMultiworldPlugin() {
        return multiworldPlugin;
    }

    public boolean autoRenewLeases() {
        return leaseAutoRenew;
    }

    public boolean isShortInfoUse() {
        return ShortInfoUse;
    }


    public int getRentCheckInterval() {
        return rentCheckInterval;
    }

    public int getLeaseCheckInterval() {
        return leaseCheckInterval;
    }

    public int getAutoSaveInterval() {
        return autoSaveInt;
    }

    public boolean isNewSaveMechanic() {
        return NewSaveMechanic;
    }

    // backup stuff   
    public boolean BackupAutoCleanUpUse() {
        return BackupAutoCleanUpUse;
    }

    public int BackupAutoCleanUpDays() {
        return BackupAutoCleanUpDays;
    }

    public boolean UseZipBackup() {
        return UseZipBackup;
    }

    public boolean BackupWorldFiles() {
        return BackupWorldFiles;
    }


    public boolean BackupleasesFile() {
        return BackupleasesFile;
    }

    public boolean BackuppermlistsFile() {
        return BackuppermlistsFile;
    }

    public boolean BackupflagsFile() {
        return BackupflagsFile;
    }

    public boolean BackupgroupsFile() {
        return BackupgroupsFile;
    }

    public boolean BackupconfigFile() {
        return BackupconfigFile;
    }
    // backup stuff

    public int getFlowLevel() {
        return FlowLevel;
    }

    public int getPlaceLevel() {
        return PlaceLevel;
    }

    public int getBlockFallLevel() {
        return BlockFallLevel;
    }

    public int getCleanLevel() {
        return CleanLevel;
    }

    public boolean flagsInherit() {
        return flagsInherit;
    }

    public boolean isIgnoreGroupedFlagAcess() {
        return ignoreGroupedFlagAcess;
    }

    public boolean useActionBarOnSelection() {
        return ActionBarOnSelection;
    }

    public CMIChatColor getChatColor() {
        return chatColor;
    }

    public int getMinMoveUpdateInterval() {
        return minMoveUpdate;
    }


    public int getHealInterval() {
        return HealInterval;
    }

    public int getFeedInterval() {
        return FeedInterval;
    }

    public int getSafeZoneInterval() {
        return SafeZoneInterval;
    }


    public FlagPermissions getGlobalCreatorDefaultFlags() {
        return globalCreatorDefaults;
    }
    public FlagPermissions getGlobalResidenceDefaultFlags() {
        return globalResidenceDefaults;
    }

    public Map<String, FlagPermissions> getGlobalGroupDefaultFlags() {
        return globalGroupDefaults;
    }

    public String getLanguage() {
        return language;
    }

    public String getDefaultWorld() {
        return DefaultWorld;
    }

    public String getDateFormat() {
        return DateFormat;
    }

    public String getDateFormatShort() {
        return DateFormatShort;
    }

    public String getTimeZone() {
        return TimeZone;
    }

    public boolean preventRentModify() {
        return preventBuildInRent;
    }

    public boolean isPreventSubZoneRemoval() {
        return PreventSubZoneRemoval;
    }

    public boolean stopOnSaveError() {
        return stopOnSaveError;
    }

    public boolean showIntervalMessages() {
        return showIntervalMessages;
    }

    public boolean ShowNoobMessage() {
        return ShowNoobMessage;
    }

    public boolean isNewPlayerUse() {
        return NewPlayerUse;
    }

    public boolean isNewPlayerFree() {
        return NewPlayerFree;
    }

    public boolean enableSpout() {
        return spoutEnable;
    }

    public boolean AutoMobRemoval() {
        return AutoMobRemoval;
    }

    public int AutoMobRemovalInterval() {
        return AutoMobRemovalInterval;
    }

    public boolean enableLeaseMoneyAccount() {
        return enableLeaseMoneyAccount;
    }

    public boolean CouldronCompatibility() {
        return Couldroncompatibility;
    }

    public boolean debugEnabled() {
        return enableDebug;
    }

    public boolean isSelectionIgnoreY() {
        return SelectionIgnoreY;
    }

    public boolean isSelectionIgnoreYInSubzone() {
        return SelectionIgnoreYInSubzone;
    }

    public boolean isNoCostForYBlocks() {
        return NoCostForYBlocks;
    }

    public boolean versionCheck() {
        return versionCheck;
    }

    public boolean isUUIDConvertion() {
        return UUIDConvertion;
    }

    public boolean isOfflineMode() {
        return OfflineMode;
    }

    public List<Material> getCustomContainers() {
        return customContainers;
    }

    public List<Material> getCustomBothClick() {
        return customBothClick;
    }

    public List<Material> getCustomRightClick() {
        return customRightClick;
    }

    public List<Material> getCleanBlocks() {
        return CleanBlocks;
    }

    public List<String> getNoFlowWorlds() {
        return NoFlowWorlds;
    }

    public List<String> getAutoCleanUpWorlds() {
        return AutoCleanUpWorlds;
    }

    public List<String> getNoPlaceWorlds() {
        return NoPlaceWorlds;
    }

    public List<String> getBlockFallWorlds() {
        return BlockFallWorlds;
    }

    public List<String> getNegativePotionEffects() {
        return NegativePotionEffects;
    }

    public List<String> getNegativeLingeringPotionEffects() {
        return NegativeLingeringPotionEffects;
    }

    public List<String> getCleanWorlds() {
        return CleanWorlds;
    }

    public List<String> getProtectedFlagsList() {
        return FlagsList;
    }

    public boolean getEnforceAreaInsideArea() {
        return enforceAreaInsideArea;
    }

    public List<RandomTeleport> getRandomTeleport() {
        return RTeleport;
    }

    public int getrtCooldown() {
        return rtCooldown;
    }

    public Location getKickLocation() {
        return KickLocation;
    }

    public Location getFlyLandLocation() {
        return FlyLandLocation;
    }

    public int getrtMaxTries() {
        return rtMaxTries;
    }

    public boolean useFlagGUI() {
        return useFlagGUI;
    }

    public boolean BounceAnimation() {
        return BounceAnimation;
    }

    public int getVisualizerFrameCap() {
        return VisualizerFrameCap;
    }

    public int getVisualizerSidesCap() {
        return VisualizerSidesCap;
    }

    public Double getWalkSpeed1() {
        return WalkSpeed1;
    }

    public Double getWalkSpeed2() {
        return WalkSpeed2;
    }

    public int getItemPickUpDelay() {
        return ItemPickUpDelay;
    }

    public boolean isConsoleLogsShowFlagChanges() {
        return ConsoleLogsShowFlagChanges;
    }

    public EconomyType getEconomyType() {
        return VaultEconomy;
    }

    public boolean isCanTeleportIncludeOwner() {
        return CanTeleportIncludeOwner;
    }

    public ELMessageType getEnterLeaveMessageType() {
        return EnterLeaveMessageType;
    }

    public boolean isEnterAnimation() {
        return EnterAnimation;
    }


    public ItemStack getGuiBottonStates(FlagState state) {
        return guiBottonStates.get(state);
    }

    public int getSelectionNetherHeight() {
        return SelectionNetherHeight;
    }

    public boolean isInfoExcludeDFlags() {
        return InfoExcludeDFlags;
    }

    public boolean isLoadEveryWorld() {
        return LoadEveryWorld;
    }

    public boolean isARCCheckCollision() {
        return ARCCheckCollision;
    }

    public String ARCIncrementFormat() {
        return ARCIncrementFormat;
    }

    public int getARCSizePercentage() {
        return ARCSizePercentage;
    }

    public int getARCSizeMin() {
        return ARCSizeMin;
    }

    public int getARCSizeMax() {
        return ARCSizeMax;
    }

    public boolean isARCSizeEnabled() {
        return ARCSizeEnabled;
    }

    public boolean isARCOldMethod() {
        return ARCOldMethod;
    }

    public boolean isChargeOnCreation() {
        return chargeOnCreation;
    }

    public boolean isChargeOnExpansion() {
        return chargeOnExpansion;
    }

    public boolean isChargeOnAreaAdd() {
        return chargeOnAreaAdd;
    }

    public int getAntiGreefRangeGaps(String worldName) {
        if (AntiGreefRangeGaps.isEmpty() || worldName == null)
            return 0;
        Integer specific = AntiGreefRangeGaps.get(worldName.toLowerCase());
        if (specific != null)
            return specific;
        Integer all = AntiGreefRangeGaps.get("all");
        if (all != null)
            return all;
        return 0;
    }

    public boolean isARCRatioInform() {
        return ARCRatioInform;
    }

    public int getARCRatioValue() {
        return ARCRatioValue;
    }

    public boolean isARCRatioConfirmation() {
        return ARCRatioConfirmation;
    }

    public int getSignsMaxPerResidence() {
        return SignsMaxPerResidence;
    }

    public boolean isAutoCleanTrasnferToUser() {
        return AutoCleanTrasnferToUser;
    }

    public String getAutoCleanUserName() {
        return AutoCleanUserName;
    }

    public boolean isDisableResidenceCreation() {
        return DisableResidenceCreation;
    }

    public List<String> getTeleportBlockedWorlds() {
        return TeleportBlockedWorlds;
    }

//    public int getTownMinRange() {
//	return TownMinRange;
//    }
//
//    public boolean isTownEnabled() {
//	return TownEnabled;
//    }
}