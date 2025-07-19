package com.bekvon.bukkit.residence.commands;

import com.bekvon.bukkit.residence.LocaleManager;
import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.CommandAnnotation;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.bekvon.bukkit.residence.containers.cmd;
import com.bekvon.bukkit.residence.permissions.PermissionManager.ResPerm;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.liuchangking.dreamengine.api.CrossPlatformMenu;
import com.liuchangking.dreamengine.api.CrossUI;
import net.Zrips.CMILib.Container.CMIWorld;
import net.Zrips.CMILib.FileHandler.ConfigReader;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class list implements cmd {

    @Override
    @CommandAnnotation(simple = true, priority = 300)
    public Boolean perform(Residence plugin, CommandSender sender, String[] args, boolean resadmin) {
        int page = 1;
        World world = null;
        String target = null;

        if (args.length == 0 && sender instanceof Player) {
            openMenu(plugin, (Player) sender, resadmin);
            return true;
        }

        for (int i = 0; i < args.length; i++) {

            if (target == null) {
                ResidencePlayer resP = ResidencePlayer.get(args[i]);
                if (resP != null) {
                    target = resP.getName();
                    continue;
                }
            }

            try {
                page = Integer.parseInt(args[i]);
                if (page < 1)
                    page = 1;
                continue;
            } catch (Exception ex) {
            }

            if (world == null) {
                World tempW = CMIWorld.getWorld(args[i]);
                if (tempW.getName().equalsIgnoreCase(args[i])) {
                    world = tempW;
                    continue;
                }
            }

            target = args[i];
        }

        if (target != null && !sender.getName().equalsIgnoreCase(target) && !ResPerm.command_$1_others.hasPermission(sender, this.getClass().getSimpleName()))
            return true;

        plugin.getResidenceManager().listResidences(sender, target, page, false, false, resadmin, world);

        return true;
    }

    @Override
    public void getLocale() {
        ConfigReader c = Residence.getInstance().getLocaleManager().getLocaleConfig();
        c.get("Description", "List Residences");
        c.get("Info", Arrays.asList("&eUsage: &6/res list <player> <page> <worldName>",
            "Lists all the residences a player owns (except hidden ones).",
            "If listing your own residences, shows hidden ones as well.",
            "To list everyones residences, use /res listall."));
        LocaleManager.addTabCompleteMain(this, "[playername]", "[worldname]");
    }

    private void openMenu(Residence plugin, Player player, boolean resadmin) {
        java.util.TreeMap<String, ClaimedResidence> owned = plugin.getPlayerManager()
                .getResidencesMap(player.getName(), true, false, null);
        owned.putAll(plugin.getPlayerManager().getTrustedResidencesMap(player.getName(), true, false, null));

        if (owned.isEmpty()) {
            plugin.msg(player, lm.Residence_DontOwn, player.getName());
            return;
        }

        CrossPlatformMenu<String> menu = CrossUI.stringMenu(player);
        menu.title("领地列表");
        for (ClaimedResidence res : owned.values()) {
            String worldName = res.getWorldName();
            String serverId = plugin.getWorldServerId(worldName);
            String serverName = com.liuchangking.dreamengine.api.DreamServerAPI.getServerName(serverId);
            String label = res.getName() + " - " + serverName;
            menu.button(label, res.getName());
        }
        menu.onClick(ev -> {
            ClaimedResidence res = plugin.getResidenceManager().getByName(ev.getPayload());
            if (res != null) {
                res.tpToResidence(player, player, resadmin);
            }
        });
        menu.open(player);
    }
}
