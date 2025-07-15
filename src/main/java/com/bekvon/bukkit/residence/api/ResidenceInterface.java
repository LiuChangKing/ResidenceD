package com.bekvon.bukkit.residence.api;

import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public interface ResidenceInterface {
    public ClaimedResidence getByLoc(Location loc);

    public ClaimedResidence getByName(String name);

    public String getSubzoneNameByRes(ClaimedResidence res);


    public boolean addResidence(String name, Location loc1, Location loc2);

    public boolean addResidence(String name, String owner, Location loc1, Location loc2);

    public boolean addResidence(Player player, String name, Location loc1, Location loc2, boolean resadmin);
    
    public boolean addResidence(Player player, String name, boolean resadmin);
}
