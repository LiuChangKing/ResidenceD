package com.bekvon.bukkit.residence.economy;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.api.MarketBuyInterface;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.bekvon.bukkit.residence.containers.Visualizer;
import com.bekvon.bukkit.residence.containers.lm;
import com.bekvon.bukkit.residence.permissions.PermissionGroup;
import com.bekvon.bukkit.residence.permissions.PermissionManager.ResPerm;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.CuboidArea;
import net.Zrips.CMILib.Container.PageInfo;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.Map.Entry;

public class TransactionManager implements MarketBuyInterface {
    private Set<ClaimedResidence> sellAmount;
    private Residence plugin;

    public TransactionManager(Residence plugin) {
        this.plugin = plugin;
        sellAmount = new HashSet<ClaimedResidence>();
    }

    public boolean chargeEconomyMoney(Player player, double chargeamount) {
        EconomyInterface econ = plugin.getEconomyManager();
        if (econ == null) {
            plugin.msg(player, lm.Economy_MarketDisabled);
            return false;
        }
        if (!econ.canAfford(player.getName(), chargeamount)) {
            plugin.msg(player, lm.Economy_NotEnoughMoney);
            return false;
        }
        econ.subtract(player.getName(), chargeamount);
        try {
            if (chargeamount != 0D)
                plugin.msg(player, lm.Economy_MoneyCharged, plugin.getEconomyManager().format(chargeamount), econ.getName());
        } catch (Exception e) {
        }
        return true;
    }

    public boolean giveEconomyMoney(Player player, double amount) {
        if (player == null)
            return false;
        if (amount == 0)
            return true;
        EconomyInterface econ = plugin.getEconomyManager();
        if (econ == null) {
            plugin.msg(player, lm.Economy_MarketDisabled);
            return false;
        }

        econ.add(player.getName(), amount);
        plugin.msg(player, lm.Economy_MoneyAdded, plugin.getEconomyManager().format(amount), econ.getName());
        return true;
    }

    @Deprecated
    public boolean giveEconomyMoney(String playerName, double amount) {
        if (playerName == null)
            return false;
        if (amount == 0)
            return true;
        EconomyInterface econ = plugin.getEconomyManager();
        if (econ == null) {
            return false;
        }
        econ.add(playerName, amount);
        return true;
    }

    public void putForSale(String areaname, Player player, int amount, boolean resadmin) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }

    public void putForSale(ClaimedResidence res, Player player, int amount, boolean resadmin) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }

    @Override
    public boolean putForSale(String areaname, int amount) {
        return false;
    }

    public boolean putForSale(ClaimedResidence res, int amount) {
        return false;
    }

    @Override
    public void buyPlot(String areaname, Player player, boolean resadmin) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }

    public void buyPlot(ClaimedResidence res, Player player, boolean resadmin) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }

    }

    public void removeFromSale(Player player, String areaname, boolean resadmin) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }

    public void removeFromSale(Player player, ClaimedResidence res, boolean resadmin) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }

    @Override
    public void removeFromSale(String areaname) {
    }

    public void removeFromSale(ClaimedResidence res) {
    }

    public void removeFromSale(ClaimedResidence res, boolean removeSigns) {
    }

    @Override
    public boolean isForSale(String areaname) {
        return false;
    }

    public boolean isForSale(ClaimedResidence res) {
        return false;
    }

    public boolean viewSaleInfo(String areaname, Player player) {
        return false;
    }

    public boolean viewSaleInfo(ClaimedResidence res, Player player) {
        return false;
    }

    public void printForSaleResidences(Player player, int page) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }

    public void clearSales() {
        sellAmount.clear();
    }

    @Override
    public int getSaleAmount(String areaname) {
        return -1;
    }

    public int getSaleAmount(ClaimedResidence res) {
        return -1;
    }

    public void load(Map<String, Integer> root) {
    }

    @Override
    public Map<String, Integer> getBuyableResidences() {
        return new HashMap<String, Integer>();
    }

    public Map<String, Integer> save() {
        return new HashMap<String, Integer>();
    }
}