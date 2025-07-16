package com.bekvon.bukkit.residence.commands;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.CommandAnnotation;
import com.bekvon.bukkit.residence.containers.cmd;
import com.bekvon.bukkit.residence.containers.lm;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Manual migration command to import YAML data into MySQL.
 */
public class migratedb implements cmd {

    @Override
    @CommandAnnotation(simple = false, priority = 5600)
    public Boolean perform(Residence plugin, CommandSender sender, String[] args, boolean resadmin) {
        if (!plugin.isUsingMysql()) {
            plugin.msg(sender, "MySQL\u672a\u542f\u7528");
            return true;
        }
        if (sender instanceof Player && !plugin.getPermissionManager().isResidenceAdmin(sender)) {
            plugin.msg(sender, lm.General_NoPermission);
            return true;
        }
        try {
            plugin.migrateToMysql();
            plugin.msg(sender, "\u6570\u636e\u5df2\u8f6c\u79fb\u5230MySQL");
        } catch (Exception e) {
            plugin.msg(sender, "\u8f6c\u79fb\u5931\u8d25: " + e.getMessage());
        }
        return true;
    }

    @Override
    public void getLocale() {
        // no locale entries
    }
}
