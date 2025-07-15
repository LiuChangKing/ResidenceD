package com.bekvon.bukkit.residence.economy.rent;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.api.MarketRentInterface;
import com.bekvon.bukkit.residence.containers.lm;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class RentManager implements MarketRentInterface {
    private final Residence plugin;

    public RentManager(Residence plugin) {
        this.plugin = plugin;
    }

    @Override
    public Set<ClaimedResidence> getRentableResidences() {
        return Collections.emptySet();
    }

    @Override
    public Set<ClaimedResidence> getCurrentlyRentedResidences() {
        return Collections.emptySet();
    }

    @Override
    public RentedLand getRentedLand(String landName) {
        return null;
    }

    @Override
    public List<String> getRentedLands(String playerName) {
        return Collections.emptyList();
    }

    @Override
    public void setForRent(Player player, String landName, int amount, int days, boolean allowRenewing, boolean resadmin) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }

    @Override
    public void setForRent(Player player, String landName, int amount, int days, boolean allowRenewing, boolean stayInMarket, boolean resadmin) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }

    @Override
    public void setForRent(Player player, String landName, int amount, int days, boolean allowRenewing, boolean stayInMarket, boolean allowAutoPay, boolean resadmin) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }

    @Override
    public void rent(Player player, String landName, boolean repeat, boolean resadmin) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }

    @Override
    public void removeFromForRent(Player player, String landName, boolean resadmin) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }

    @Override
    public void unrent(Player player, String landName, boolean resadmin) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }

    @Override
    public void removeFromRent(String landName) {
    }

    @Override
    public void removeRentable(String landName) {
    }

    @Override
    public boolean isForRent(String landName) {
        return false;
    }

    @Override
    public boolean isRented(String landName) {
        return false;
    }

    @Override
    public String getRentingPlayer(String landName) {
        return null;
    }

    @Override
    public int getCostOfRent(String landName) {
        return 0;
    }

    @Override
    public boolean getRentableRepeatable(String landName) {
        return false;
    }

    @Override
    public boolean getRentedAutoRepeats(String landName) {
        return false;
    }

    @Override
    public int getRentDays(String landName) {
        return 0;
    }

    @Override
    public void checkCurrentRents() {
    }

    @Override
    public void setRentRepeatable(Player player, String landName, boolean value, boolean resadmin) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }

    @Override
    public void setRentedRepeatable(Player player, String landName, boolean value, boolean resadmin) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }

    @Override
    public int getRentCount(String playerName) {
        return 0;
    }

    @Override
    public int getRentableCount(String playerName) {
        return 0;
    }

    // Additional methods used elsewhere in the plugin
    public void payRent(Player player, String landName, boolean resadmin) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }

    public void printRentInfo(Player player, String landName) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }

    public void printRentableResidences(Player player, int page) {
        if (player != null)
            plugin.msg(player, lm.General_NoPermission);
    }
}

