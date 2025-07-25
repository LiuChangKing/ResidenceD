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
            plugin.msg(sender, "MySQL未启用");
            return true;
        }
        if (sender instanceof Player && !plugin.getPermissionManager().isResidenceAdmin(sender)) {
            plugin.msg(sender, lm.General_NoPermission);
            return true;
        }
        try {
            plugin.migrateToMysql();
            plugin.msg(sender, "数据已转移到MySQL");
        } catch (Exception e) {
            plugin.msg(sender, "转移失败: " + e.getMessage());
        }
        return true;
    }

    @Override
    public void getLocale() {
        // no locale entries
    }
}
