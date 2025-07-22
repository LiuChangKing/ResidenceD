package com.bekvon.bukkit.residence.landcore;

/** Data for a placed land core. */
public class LandCoreData {
    private int level;
    private String residenceName;

    public LandCoreData(int level, String residenceName) {
        this.level = level;
        this.residenceName = residenceName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getResidenceName() {
        return residenceName;
    }

    public void setResidenceName(String residenceName) {
        this.residenceName = residenceName;
    }
}
