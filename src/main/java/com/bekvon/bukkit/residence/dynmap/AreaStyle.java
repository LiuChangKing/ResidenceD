package com.bekvon.bukkit.residence.dynmap;

import com.bekvon.bukkit.residence.Residence;

class AreaStyle {
    String strokecolor;
    String forrentstrokecolor;
    String rentedstrokecolor;
    double strokeopacity;
    int strokeweight;
    String fillcolor;
    double fillopacity;
    int y;

    AreaStyle() {
	strokecolor = Residence.getInstance().getConfigManager().DynMapBorderColor;
	forrentstrokecolor = Residence.getInstance().getConfigManager().DynMapFillForRent;
	rentedstrokecolor = Residence.getInstance().getConfigManager().DynMapFillRented;
	strokeopacity = Residence.getInstance().getConfigManager().DynMapBorderOpacity;
	strokeweight = Residence.getInstance().getConfigManager().DynMapBorderWeight;
	fillcolor = Residence.getInstance().getConfigManager().DynMapFillColor;
	fillopacity = Residence.getInstance().getConfigManager().DynMapFillOpacity;
	y = 64;
    }
}
