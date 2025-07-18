package com.bekvon.bukkit.residence.permissions;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.bekvon.bukkit.residence.containers.lm;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import com.bekvon.bukkit.residence.protection.FlagPermissions.FlagState;
import net.Zrips.CMILib.Container.CMIWorld;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PermissionGroup {
    private int xmax = 0;
    private int ymax = 0;
    private int zmax = 0;

    private int xmin = 0;
    private int ymin = 0;
    private int zmin = 0;

    protected int Subzonexmax = 0;
    protected int Subzoneymax = 0;
    protected int Subzonezmax = 0;

    protected int Subzonexmin = 0;
    protected int Subzoneymin = 0;
    protected int Subzonezmin = 0;

    protected int resmax = 0;
    private double costperarea = 0;
    protected boolean tpaccess = false;
    protected int subzonedepth = 0;
    protected int maxSubzones = 3;
    protected FlagPermissions flagPerms;
    protected Map<String, Boolean> creatorDefaultFlags;
    protected Map<String, Map<String, Boolean>> groupDefaultFlags;
    protected Map<String, Boolean> residenceDefaultFlags;

    protected Map<String, Map<String, FlagPermissions>> groupperms;
    protected Map<String, FlagPermissions> worldperms;

    protected boolean messageperms = false;
    protected String defaultEnterMessage = null;
    protected String defaultLeaveMessage = null;
    protected boolean cancreate = false;
    protected String groupname;
    protected int maxPhysical = 2;
    protected boolean unstuck = false;
    protected boolean kick = false;
    protected int minHeight = 0;
    protected int maxHeight = 256;
    protected boolean selectCommandAccess = true;
    protected boolean itemListAccess = true;
    protected int priority = 0;

    public PermissionGroup(String name) {
        flagPerms = new FlagPermissions();
        creatorDefaultFlags = new HashMap<String, Boolean>();
        residenceDefaultFlags = new HashMap<String, Boolean>();
        groupDefaultFlags = new HashMap<String, Map<String, Boolean>>();

        worldperms = new HashMap<>();
        groupperms = new HashMap<>();

        groupname = name;
    }

    public void setPriority(int number) {
        this.priority = number;
    }

    public int getPriority() {
        return this.priority;
    }

    public PermissionGroup(String name, ConfigurationSection node) {
        this(name);
        this.parseGroup(node);
    }

    public PermissionGroup(String name, ConfigurationSection node, FlagPermissions parentFlagPerms) {
        this(name, node);
        flagPerms.setParent(parentFlagPerms);
    }

    public PermissionGroup(String name, ConfigurationSection node, FlagPermissions parentFlagPerms, int priority) {
        this(name, node);
        flagPerms.setParent(parentFlagPerms);
        this.priority = priority;
    }

    public void mirrorIn(ConfigurationSection limits) {
        parseGroup(limits);
    }

    private void parseGroup(ConfigurationSection limits) {
        if (limits == null) {
            return;
        }
        if (limits.contains("Residence.CanCreate"))
            cancreate = limits.getBoolean("Residence.CanCreate", false);

        if (limits.contains("Residence.MaxResidences"))
            resmax = limits.getInt("Residence.MaxResidences", 0);

        if (limits.contains("Residence.MaxAreasPerResidence"))
            maxPhysical = limits.getInt("Residence.MaxAreasPerResidence", 2);

        if (limits.contains("Residence.MaxEastWest"))
            xmax = limits.getInt("Residence.MaxEastWest", 0);
        if (limits.contains("Residence.MinEastWest"))
            xmin = limits.getInt("Residence.MinEastWest", 0);

        xmin = Math.min(getMinX(), getMaxX());

        if (limits.contains("Residence.MaxUpDown"))
            ymax = limits.getInt("Residence.MaxUpDown", 0);
        if (limits.contains("Residence.MinUpDown"))
            ymin = limits.getInt("Residence.MinUpDown", 0);
        ymin = Math.min(ymin, ymax);

        if (Residence.getInstance().getConfigManager().isSelectionIgnoreY()) {
            // This needs to be 256 to include entire height where 255 and block 0
            ymax = CMIWorld.getMaxHeight(Bukkit.getWorlds().get(0)) + Math.abs(CMIWorld.getMinHeight(Bukkit.getWorlds().get(0)));
        }

        if (limits.contains("Residence.MaxNorthSouth"))
            zmax = limits.getInt("Residence.MaxNorthSouth", 0);
        if (limits.contains("Residence.MinNorthSouth"))
            zmin = limits.getInt("Residence.MinNorthSouth", 0);
        zmin = Math.min(zmin, zmax);

        if (limits.contains("Residence.MinHeight"))
            minHeight = limits.getInt("Residence.MinHeight", 0);
        // This needs to be 256 to include entire height where 255 and block 0
        if (limits.contains("Residence.MaxHeight"))
            maxHeight = limits.getInt("Residence.MaxHeight", CMIWorld.getMaxHeight(Bukkit.getWorlds().get(0)));

        if (limits.contains("Residence.CanTeleport"))
            tpaccess = limits.getBoolean("Residence.CanTeleport", false);

        if (limits.contains("Residence.MaxSubzonesInArea"))
            maxSubzones = limits.getInt("Residence.MaxSubzonesInArea", 3);

        if (limits.contains("Residence.SubzoneDepth"))
            subzonedepth = limits.getInt("Residence.SubzoneDepth", 0);

        if (limits.contains("Residence.SubzoneMaxEastWest"))
            Subzonexmax = limits.getInt("Residence.SubzoneMaxEastWest", getMaxX());
        else
            Subzonexmax = getMaxX();
        Subzonexmax = Math.min(getMaxX(), Subzonexmax);

        if (limits.contains("Residence.SubzoneMinEastWest"))
            Subzonexmin = limits.getInt("Residence.SubzoneMinEastWest", 0);
        Subzonexmin = Math.min(Subzonexmin, Subzonexmax);

        if (limits.contains("Residence.SubzoneMaxUpDown"))
            Subzoneymax = limits.getInt("Residence.SubzoneMaxUpDown", ymax);
        else
            Subzoneymax = getMaxYSize();

        Subzoneymax = Math.min(ymax, Subzoneymax);
        if (limits.contains("Residence.SubzoneMinUpDown"))
            Subzoneymin = limits.getInt("Residence.SubzoneMinUpDown", 0);
        Subzoneymin = Math.min(Subzoneymin, Subzoneymax);

        if (limits.contains("Residence.SubzoneMaxNorthSouth"))
            Subzonezmax = limits.getInt("Residence.SubzoneMaxNorthSouth", zmax);
        else
            Subzonezmax = getMaxZ();

        if (Residence.getInstance().getConfigManager().isSelectionIgnoreYInSubzone()) {
            // This needs to be 256 to include entire height where 255 and block 0
            Subzonezmax = CMIWorld.getMaxHeight(Bukkit.getWorlds().get(0)) + Math.abs(CMIWorld.getMinHeight(Bukkit.getWorlds().get(0)));
        }

        Subzonezmax = Math.min(zmax, Subzonezmax);

        if (limits.contains("Residence.SubzoneMinNorthSouth"))
            Subzonezmin = limits.getInt("Residence.SubzoneMinNorthSouth", 0);
        Subzonezmin = Math.min(Subzonezmin, Subzonezmax);

        if (limits.contains("Messaging.CanChange"))
            messageperms = limits.getBoolean("Messaging.CanChange", false);
        if (limits.contains("Messaging.DefaultEnter"))
            defaultEnterMessage = limits.getString("Messaging.DefaultEnter", null);
        if (limits.contains("Messaging.DefaultLeave"))
            defaultLeaveMessage = limits.getString("Messaging.DefaultLeave", null);
        if (limits.contains("Economy.BuyCost"))
            costperarea = limits.getDouble("Economy.BuyCost", 0);


        if (limits.isBoolean("Residence.Unstuck"))
            unstuck = limits.getBoolean("Residence.Unstuck", false);
        if (limits.contains("Residence.Kick"))
            kick = limits.getBoolean("Residence.Kick", false);
        if (limits.contains("Residence.SelectCommandAccess"))
            selectCommandAccess = limits.getBoolean("Residence.SelectCommandAccess", true);
        if (limits.contains("Residence.ItemListAccess"))
            itemListAccess = limits.getBoolean("Residence.ItemListAccess", true);
        ConfigurationSection node = limits.getConfigurationSection("Flags.Permission");
        Set<String> flags = null;
        if (node != null) {
            flags = node.getKeys(false);
        }
        if (flags != null) {
            Iterator<String> flagit = flags.iterator();
            while (flagit.hasNext()) {
                String flagname = flagit.next();
                boolean access = limits.getBoolean("Flags.Permission." + flagname, false);
                flagPerms.setFlag(flagname, access ? FlagState.TRUE : FlagState.FALSE);
            }
        }
        node = limits.getConfigurationSection("Flags.CreatorDefault");
        if (node != null) {
            flags = node.getKeys(false);
        }
        if (flags != null) {
            Iterator<String> flagit = flags.iterator();
            while (flagit.hasNext()) {
                String flagname = flagit.next();
                boolean access = limits.getBoolean("Flags.CreatorDefault." + flagname, false);
                creatorDefaultFlags.put(flagname, access);
            }
        }

//	if (node == null) {
//	    }
//	}
//	if (node != null) {
//	    flags = node.getKeys(false);
//	    if (flags != null) {
//		Iterator<String> flagit = flags.iterator();
//		while (flagit.hasNext()) {
//		    String flagname = flagit.next();
//		}
//	    }
//	}

        node = limits.getConfigurationSection("Flags.Default");
        if (node != null) {
            flags = node.getKeys(false);
        }
        if (flags != null) {
            Iterator<String> flagit = flags.iterator();
            while (flagit.hasNext()) {
                String flagname = flagit.next();
                boolean access = limits.getBoolean("Flags.Default." + flagname, false);
                residenceDefaultFlags.put(flagname, access);
            }
        }
        node = limits.getConfigurationSection("Flags.GroupDefault");
        Set<String> groupDef = null;
        if (node != null) {
            groupDef = node.getKeys(false);
        }
        if (groupDef != null) {
            Iterator<String> groupit = groupDef.iterator();
            while (groupit.hasNext()) {
                String name = groupit.next();
                Map<String, Boolean> gflags = new HashMap<String, Boolean>();
                flags = limits.getConfigurationSection("Flags.GroupDefault." + name).getKeys(false);
                Iterator<String> flagit = flags.iterator();
                while (flagit.hasNext()) {
                    String flagname = flagit.next();
                    boolean access = limits.getBoolean("Flags.GroupDefault." + name + "." + flagname, false);
                    gflags.put(flagname, access);
                }
                groupDefaultFlags.put(name, gflags);
            }
        }
    }

    public String getGroupName() {
        return groupname;
    }

    public int getMaxX() {
        return xmax;
    }

    public int getMaxYSize() {
        return ymax;
    }

    public int getMinYSize() {
        return ymin;
    }

    @Deprecated
    public int getMaxY() {
        return getMaxYSize();
    }

    public int getMaxZ() {
        return zmax;
    }

    public int getMinX() {
        return xmin;
    }

    @Deprecated
    public int getMinY() {
        return getMinYSize();
    }

    public int getMinZ() {
        return zmin;
    }

    public int getSubzoneMaxX() {
        return Subzonexmax;
    }

    public int getSubzoneMaxY() {
        return Subzoneymax;
    }

    public int getSubzoneMaxZ() {
        return Subzonezmax;
    }

    public int getSubzoneMinX() {
        return Subzonexmin;
    }

    public int getSubzoneMinY() {
        return Subzoneymin;
    }

    public int getSubzoneMinZ() {
        return Subzonezmin;
    }

    @Deprecated
    public int getMinHeight() {
        return getLowestYAllowed();
    }

    public int getLowestYAllowed() {
        return minHeight;
    }

    public int getHighestYAllowed() {
        return maxHeight;
    }

    @Deprecated
    public int getMaxHeight() {
        return getHighestYAllowed();
    }

    public int getMaxZones() {
        return resmax;
    }

    public double getCostPerBlock() {
        return costperarea;
    }


    public boolean hasTpAccess() {
        return tpaccess;
    }

    public int getMaxSubzoneDepth() {
        return subzonedepth;
    }

    public int getMaxSubzones() {
        return maxSubzones;
    }

    public boolean canSetEnterLeaveMessages() {
        return messageperms;
    }

    public String getDefaultEnterMessage() {
        if (defaultEnterMessage == null)
            return "";
        return defaultEnterMessage;
    }

    public String getDefaultLeaveMessage() {
        if (defaultLeaveMessage == null)
            return "";
        return defaultLeaveMessage;
    }



    public boolean hasUnstuckAccess() {
        return unstuck;
    }

    public boolean hasKickAccess() {
        return kick;
    }

    public int getMaxPhysicalPerResidence() {
        return maxPhysical;
    }

    public Set<Entry<String, Boolean>> getDefaultResidenceFlags() {
        return residenceDefaultFlags.entrySet();
    }

    public Set<Entry<String, Boolean>> getDefaultCreatorFlags() {
        return creatorDefaultFlags.entrySet();
    }


    public Set<Entry<String, Map<String, Boolean>>> getDefaultGroupFlags() {
        return groupDefaultFlags.entrySet();
    }

    public boolean canCreateResidences() {
        return cancreate;
    }

    public boolean hasFlagAccess(Flags flag) {
        return flagPerms.has(flag, false);
    }

    @Deprecated
    public boolean hasFlagAccess(String flag) {
        return flagPerms.has(flag, false);
    }

//    public boolean inLimits(CuboidArea area) {
//	if (area.getXSize() > xmax || area.getYSize() > ymax || area.getZSize() > zmax) {
//	    return false;
//	}
//	return true;
//    }
//
//    public boolean inLimitsSubzone(CuboidArea area) {
//	if (area.getXSize() > Subzonexmax || area.getYSize() > Subzoneymax || area.getZSize() > Subzonezmax) {
//	    return false;
//	}
//	return true;
//    }

    public boolean selectCommandAccess() {
        return selectCommandAccess;
    }

    public boolean itemListAccess() {
        return itemListAccess;
    }

    public void printLimits(CommandSender player, OfflinePlayer target, boolean resadmin) {

        ResidencePlayer rPlayer = Residence.getInstance().getPlayerManager().getResidencePlayer(target);
        rPlayer.getGroup(true);
        PermissionGroup group = rPlayer.getGroup();

        Residence.getInstance().msg(player, lm.General_Separator);
        Residence.getInstance().msg(player, lm.Limits_PGroup, Residence.getInstance().getPermissionManager().getPermissionsGroup(target.getName(),
            target.isOnline() ? Bukkit.getPlayer(target.getName()).getWorld().getName() : Residence.getInstance().getConfigManager().getDefaultWorld()));
        Residence.getInstance().msg(player, lm.Limits_RGroup, group.getGroupName());
        if (target.isOnline() && resadmin)
            Residence.getInstance().msg(player, lm.Limits_Admin, Residence.getInstance().getPermissionManager().isResidenceAdmin(player));
        Residence.getInstance().msg(player, lm.Limits_CanCreate, group.canCreateResidences());
        Residence.getInstance().msg(player, lm.Limits_MaxRes, rPlayer.getMaxRes());
        Residence.getInstance().msg(player, lm.Limits_NumberOwn, rPlayer.getResAmount());
        Residence.getInstance().msg(player, lm.Limits_MaxEW, group.getMinX() + "-" + rPlayer.getMaxX());
        Residence.getInstance().msg(player, lm.Limits_MaxNS, group.getMinZ() + "-" + rPlayer.getMaxZ());
        Residence.getInstance().msg(player, lm.Limits_MaxUD, group.getMinY() + "-" + group.getMaxY());
        Residence.getInstance().msg(player, lm.Limits_MinMax, group.getMinHeight(), group.getMaxHeight());
        Residence.getInstance().msg(player, lm.Limits_MaxSubzones, rPlayer.getMaxSubzones());
        Residence.getInstance().msg(player, lm.Limits_MaxSubDepth, rPlayer.getMaxSubzoneDepth());
        Residence.getInstance().msg(player, lm.Limits_EnterLeave, group.messageperms);
        if (Residence.getInstance().getEconomyManager() != null) {
            Residence.getInstance().msg(player, lm.Limits_Cost, group.costperarea);
        }
        Residence.getInstance().msg(player, lm.Limits_Flag, group.flagPerms.listFlags());
        Residence.getInstance().msg(player, lm.General_Separator);
    }

    public double getCostperarea() {
        return costperarea;
    }


    @Deprecated
    public int getXmin() {
        return xmin;
    }

    @Deprecated
    public int getXmax() {
        return xmax;
    }

    @Deprecated
    public int getZmin() {
        return zmin;
    }

    @Deprecated
    public int getYmin() {
        return getMinYSize();
    }

    @Deprecated
    public int getYmax() {
        return getMaxYSize();
    }

    @Deprecated
    public int getZmax() {
        return zmax;
    }

}
