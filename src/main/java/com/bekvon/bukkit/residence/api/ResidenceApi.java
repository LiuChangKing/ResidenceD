package com.bekvon.bukkit.residence.api;

import com.bekvon.bukkit.residence.Residence;

public class ResidenceApi {

    public static ResidencePlayerInterface getPlayerManager() {
	return Residence.getInstance().getPlayerManagerAPI();
    }

    public static ResidenceInterface getResidenceManager() {
        return Residence.getInstance().getResidenceManagerAPI();
    }
}
