package com.bekvon.bukkit.residence.commands;

import com.bekvon.bukkit.residence.LocaleManager;
import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.CommandAnnotation;
import com.bekvon.bukkit.residence.containers.cmd;
import com.bekvon.bukkit.residence.containers.lm;
import com.bekvon.bukkit.residence.landcore.LandCoreManager;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.liuchangking.dreamengine.utils.MessageUtil;
import net.Zrips.CMILib.FileHandler.ConfigReader;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class landcore implements cmd {

    @Override
    @CommandAnnotation(simple = true, priority = 5700)
    public Boolean perform(Residence plugin, CommandSender sender, String[] args, boolean resadmin) {
        if (!(sender instanceof Player))
            return false;
        if (!plugin.getPermissionManager().isResidenceAdmin(sender)) {
            plugin.msg(sender, lm.General_NoPermission);
            return true;
        }
        if (args.length < 1 || args.length > 2)
            return false;

        LandCoreManager manager = plugin.getLandCoreManager();

        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length != 1)
                return false;

            Player player = (Player) sender;
            ClaimedResidence res = plugin.getResidenceManager().getByLoc(player.getLocation());
            if (res == null) {
                plugin.msg(player, lm.Invalid_Residence);
                return true;
            }
            Location coreLoc = manager.getCoreLocation(res.getName());
            if (coreLoc == null) {
                plugin.msg(player, "当前领地没有领地核心");
                return true;
            }
            manager.withdraw(player, coreLoc.getBlock());
            MessageUtil.notifySuccess(player, "正在收回", "领地正在回收中...");
            return true;
        }

        int level;
        try {
            level = Integer.parseInt(args[0]);
        } catch (Exception e) {
            return false;
        }
        Player target = (args.length == 2) ? Bukkit.getPlayer(args[1]) : (Player) sender;
        if (target == null) {
            plugin.msg(sender, lm.Invalid_Player, args[1]);
            return true;
        }
        ItemStack core = manager.createCoreItem(level, target);
        target.getInventory().addItem(core);
        plugin.msg(sender, "已获得等级" + level + "的领地核心");
        if (target != sender)
            plugin.msg(target, "已获得等级" + level + "的领地核心");
        return true;
    }

    @Override
    public void getLocale() {
        ConfigReader c = Residence.getInstance().getLocaleManager().getLocaleConfig();
        c.get("Description", "给予或删除领地核心");
        c.get("Info", Arrays.asList("&eUsage: &6/res landcore <level> [player]", "&eUsage: &6/res landcore remove"));
        LocaleManager.addTabCompleteMain(this, "<level>", "[playername]");
        LocaleManager.addTabCompleteSub(this, "remove");
    }
}
