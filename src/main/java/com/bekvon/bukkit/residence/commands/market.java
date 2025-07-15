package com.bekvon.bukkit.residence.commands;

import com.bekvon.bukkit.residence.LocaleManager;
import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.*;
import net.Zrips.CMILib.FileHandler.ConfigReader;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class market implements cmd {

    @Override
    @CommandAnnotation(simple = true, priority = 2600)
    public Boolean perform(Residence plugin, CommandSender sender, String[] args, boolean resadmin) {
        plugin.msg(sender, lm.General_NoPermission);
        return true;
    }

    @Override
    public void getLocale() {
        ConfigReader c = Residence.getInstance().getLocaleManager().getLocaleConfig();

        c.get("Description", "Buy, Sell, or Rent Residences");
        c.get("Info", Arrays.asList("&eUsage: &6/res market ? for more Info"));

        c.setFullPath(c.getPath() + "SubCommands.");

        c.get("Info.Description", "Get economy Info on residence");
        c.get("Info.Info", Arrays.asList("&eUsage: &6/res market Info [residence]", "Shows rent information for the Residence."));
        LocaleManager.addTabCompleteSub(this, "Info", "[residence]");

        c.get("list.Description", "Lists rentable residences.");
        c.get("list.Info", Arrays.asList("&eUsage: &6/res market list rent"));
        LocaleManager.addTabCompleteSub(this, "list", "rent");

        c.get("list.SubCommands.rent.Description", "Lists rentable residences.");
        c.get("list.SubCommands.rent.Info", Arrays.asList("&eUsage: &6/res market list rent"));


        c.get("sign.Description", "Set market sign");
        c.get("sign.Info", Arrays.asList("&eUsage: &6/res market sign [residence]", "Sets market sign you are looking at."));
        LocaleManager.addTabCompleteSub(this, "sign", "[residence]");

    }

}
