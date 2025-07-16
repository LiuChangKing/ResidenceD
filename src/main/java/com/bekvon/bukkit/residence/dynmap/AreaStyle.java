package com.bekvon.bukkit.residence.dynmap;

import com.bekvon.bukkit.residence.Residence;

class AreaStyle {
    String strokecolor;
    double strokeopacity;
    int strokeweight;
    String fillcolor;
    double fillopacity;
    int y;

    AreaStyle() {
	strokecolor = Residence.getInstance().getConfigManager().DynMapBorderColor;
	strokeopacity = Residence.getInstance().getConfigManager().DynMapBorderOpacity;
	strokeweight = Residence.getInstance().getConfigManager().DynMapBorderWeight;
	fillcolor = Residence.getInstance().getConfigManager().DynMapFillColor;
	fillopacity = Residence.getInstance().getConfigManager().DynMapFillOpacity;
	y = 64;
    }
}
