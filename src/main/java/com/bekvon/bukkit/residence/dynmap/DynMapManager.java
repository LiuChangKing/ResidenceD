package com.bekvon.bukkit.residence.dynmap;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.lm;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.CuboidArea;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;
import com.bekvon.bukkit.residence.utils.GetTime;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;
import net.Zrips.CMILib.Version.Schedulers.CMITask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DynMapManager {
    Residence plugin;

    public DynmapAPI api;
    MarkerAPI markerapi;
    MarkerSet set;
    private Map<String, AreaMarker> resareas = new HashMap<String, AreaMarker>();
    private CMITask scheduler = null;

    public DynMapManager(Residence plugin) {
        this.plugin = plugin;
    }

    public MarkerSet getMarkerSet() {
        return set;
    }

    public void fireUpdateAdd(final ClaimedResidence res, final int deep) {
        if (api == null || set == null)
            return;
        if (res == null)
            return;

        if (scheduler != null) {
            scheduler.cancel();
        }

        scheduler = CMIScheduler.runTaskLater(plugin, () -> handleResidenceAdd(res.getName(), res, deep), 10L);
    }

    public void fireUpdateRemove(final ClaimedResidence res, final int deep) {
        if (api == null || set == null)
            return;
        if (res == null)
            return;

        handleResidenceRemove(res.getName(), res, deep);
    }

    private String formatInfoWindow(String resid, ClaimedResidence res, String resName) {
        if (res == null)
            return null;
        if (res.getName() == null)
            return null;
        if (res.getOwner() == null)
            return null;
        String v =
            "<div class=\"regioninfo\"><div class=\"infowindow\"><span style=\"font-size:140%;font-weight:bold;\">%regionname%</span><br /> "
                + CMIChatColor.stripColor(plugin.msg(lm.General_Owner, "")) + "<span style=\"font-weight:bold;\">%playerowners%</span><br />";

        if (plugin.getConfigManager().DynMapShowFlags) {

            ResidencePermissions residencePermissions = res.getPermissions();
            FlagPermissions gRD = Residence.getInstance().getConfigManager().getGlobalResidenceDefaultFlags();

            StringBuilder flgs = new StringBuilder();
            for (Entry<String, Boolean> one : residencePermissions.getFlags().entrySet()) {
                if (Residence.getInstance().getConfigManager().DynMapExcludeDefaultFlags && gRD.isSet(one.getKey()) && gRD.getFlags().get(one.getKey()).equals(one.getValue())) {
                    continue;
                }
                if (!flgs.toString().isEmpty())
                    flgs.append("<br/>");
                flgs.append(one.getKey() + ": " + one.getValue());
            }

            if (!flgs.toString().isEmpty()) {
                v += CMIChatColor.stripColor(plugin.msg(lm.General_ResidenceFlags, "")) + "<br /><span style=\"font-weight:bold;\">%flags%</span>";
                v = v.replace("%flags%", flgs.toString());
            }
        }

        v += "</div></div>";




        v = v.replace("%regionname%", resName);
        v = v.replace("%playerowners%", res.getOwner());
        String m = res.getEnterMessage();
        v = v.replace("%entermsg%", (m != null) ? m : "");
        m = res.getLeaveMessage();
        v = v.replace("%leavemsg%", (m != null) ? m : "");

        v = v.replace("%isrented%", "false");


        return v;
    }

    private boolean isVisible(String id, String worldname) {
        List<String> visible = plugin.getConfigManager().DynMapVisibleRegions;
        List<String> hidden = plugin.getConfigManager().DynMapHiddenRegions;
        if (visible != null && !visible.isEmpty() && !visible.contains(id) && !visible.contains("world:" + worldname))
            return false;

        if (hidden != null && !hidden.isEmpty() && (hidden.contains(id) || hidden.contains("world:" + worldname)))
            return false;

        return true;
    }

    private void addStyle(String resid, AreaMarker m) {
        AreaStyle as = new AreaStyle();
        int sc = 0xFF0000;
        int fc = 0xFF0000;
        try {
            sc = Integer.parseInt(as.strokecolor.substring(1), 16);
            fc = Integer.parseInt(as.fillcolor.substring(1), 16);
        } catch (NumberFormatException nfx) {
        }
        m.setLineStyle(as.strokeweight, as.strokeopacity, sc);
        m.setFillStyle(as.fillopacity, fc);
        m.setRangeY(as.y, as.y);
    }

    private void handleResidenceAdd(String resid, ClaimedResidence res, int depth) {

        if (res == null)
            return;

        boolean hidden = res.getPermissions().has("hidden", false);
        if (hidden && plugin.getConfigManager().DynMapHideHidden) {
            fireUpdateRemove(res, depth);
            return;
        }

        for (Entry<String, CuboidArea> oneArea : res.getAreaMap().entrySet()) {

            String id = oneArea.getKey() + "." + resid;

            String name = res.getName();
            double[] x = new double[4];
            double[] z = new double[4];

            String resName = res.getName();

            if (res.getAreaMap().size() > 1) {
                resName = res.getName() + " (" + oneArea.getKey() + ")";
            }

            String desc = formatInfoWindow(resid, res, resName);

            if (!isVisible(resid, res.getWorld()))
                return;

            Location l0 = oneArea.getValue().getLowLocation();
            Location l1 = oneArea.getValue().getHighLocation();

//	    x[0] = l0.getX();
//	    z[0] = l0.getZ();
//	    x[1] = l1.getX() + 1.0;
//	    z[1] = l1.getZ() + 1.0;

            x[0] = l0.getX();
            z[0] = l0.getZ();
            x[1] = l0.getX();
            z[1] = l1.getZ() + 1.0;
            x[2] = l1.getX() + 1.0;
            z[2] = l1.getZ() + 1.0;
            x[3] = l1.getX() + 1.0;
            z[3] = l0.getZ();

            AreaMarker marker = null;

            if (resareas.containsKey(id)) {
                marker = resareas.get(id);
                resareas.remove(id);
                marker.deleteMarker();
            }

            marker = set.createAreaMarker(id, name, true, res.getWorld(), x, z, true);
            if (marker == null)
                return;

            if (plugin.getConfigManager().DynMapLayer3dRegions)
                marker.setRangeY(l1.getY(), l0.getY());

            marker.setDescription(desc);
            addStyle(resid, marker);
            resareas.put(id, marker);

            if (depth <= plugin.getConfigManager().DynMapLayerSubZoneDepth) {
                List<ClaimedResidence> subids = res.getSubzones();
                for (ClaimedResidence one : subids) {
                    handleResidenceAdd(one.getName(), one, depth + 1);
                }
            }
        }
    }

    public void handleResidenceRemove(String resid, ClaimedResidence res, int depth) {

        if (resid == null)
            return;
        if (res == null)
            return;

        for (Entry<String, CuboidArea> oneArea : res.getAreaMap().entrySet()) {
            String id = oneArea.getKey() + "." + resid;
            if (resareas.containsKey(id)) {
                AreaMarker marker = resareas.remove(id);
                marker.deleteMarker();
            }
            if (depth <= plugin.getConfigManager().DynMapLayerSubZoneDepth + 1) {
                List<ClaimedResidence> subids = res.getSubzones();
                for (ClaimedResidence one : subids) {
                    handleResidenceRemove(one.getName(), one, depth + 1);
                }
            }
        }
    }

    public void activate() {
        try {
            markerapi = api.getMarkerAPI();
        } catch (Exception e) {
        }
        if (markerapi == null) {
            Bukkit.getConsoleSender().sendMessage("[Residence] Error loading dynmap marker API!");
            return;
        }

        if (set != null) {
            set.deleteMarkerSet();
            set = null;
        }
        set = markerapi.getMarkerSet("residence.markerset");
        if (set == null)
            set = markerapi.createMarkerSet("residence.markerset", "Residence", null, false);
        else
            set.setMarkerSetLabel("Residence");

        if (set == null) {
            Bukkit.getConsoleSender().sendMessage("Error creating marker set");
            return;
        }
        set.setLayerPriority(1);
        set.setHideByDefault(plugin.getConfigManager().DynMapHideByDefault);

        CMIMessages.consoleMessage(Residence.getInstance().getPrefix() + " DynMap residence activated!");

        for (Entry<String, ClaimedResidence> one : plugin.getResidenceManager().getResidences().entrySet()) {
            plugin.getDynManager().fireUpdateAdd(one.getValue(), one.getValue().getSubzoneDeep());
            handleResidenceAdd(one.getValue().getName(), one.getValue(), one.getValue().getSubzoneDeep());
        }
    }
}
