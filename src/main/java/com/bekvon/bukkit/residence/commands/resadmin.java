package com.bekvon.bukkit.residence.commands;

import com.bekvon.bukkit.residence.LocaleManager;
import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.CommandAnnotation;
import com.bekvon.bukkit.residence.containers.ResAdmin;
import com.bekvon.bukkit.residence.containers.cmd;
import com.bekvon.bukkit.residence.containers.lm;
import net.Zrips.CMILib.FileHandler.ConfigReader;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class resadmin implements cmd {

    @Override
    @CommandAnnotation(simple = false, priority = 5300)
    public Boolean perform(Residence plugin, CommandSender sender, String[] args, boolean resadmin) {
        if (!(sender instanceof Player))
            return false;
        if (args.length != 1)
            return true;

        Player player = (Player) sender;
        if (args[0].equalsIgnoreCase("on")) {
            ResAdmin.turnResAdmin(player, true);
            plugin.msg(player, lm.General_AdminToggleTurnOn);
        } else if (args[0].equalsIgnoreCase("off")) {
            ResAdmin.turnResAdmin(player, false);
            plugin.msg(player, lm.General_AdminToggleTurnOff);
        }
        return true;
    }

    @Override
    public void getLocale() {
        ConfigReader c = Residence.getInstance().getLocaleManager().getLocaleConfig();
        c.get("Description", "Enabled or disable residence admin");
        c.get("Info", Arrays.asList("&eUsage: &6/res resadmin [on/off]"));
        LocaleManager.addTabCompleteMain(this, "on%%off");
    }
}
