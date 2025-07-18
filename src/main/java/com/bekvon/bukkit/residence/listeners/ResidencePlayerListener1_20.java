package com.bekvon.bukkit.residence.listeners;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.containers.ResAdmin;
import com.bekvon.bukkit.residence.containers.lm;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import com.bekvon.bukkit.residence.protection.FlagPermissions.FlagCombo;
import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Items.CMIMaterial;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSignOpenEvent;

public class ResidencePlayerListener1_20 implements Listener {

    private Residence plugin;

    public ResidencePlayerListener1_20(Residence plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignWax(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (event.getItem() == null || event.getItem().getType() != Material.HONEYCOMB)
            return;

        Block block = event.getClickedBlock();
        if (block == null)
            return;

        if (!CMIMaterial.isSign(block.getType()))
            return;

        Player player = event.getPlayer();

        FlagPermissions perms = plugin.getPermsByLocForPlayer(block.getLocation(), player);

        if (perms.playerHas(player, Flags.build, true))
            return;

        plugin.msg(player, lm.Flag_Deny, Flags.build);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onSignInteract(PlayerSignOpenEvent event) {

        if (event.getPlayer() == null)
            return;
        // disabling event on world
        if (plugin.isDisabledWorldListener(event.getPlayer().getWorld()))
            return;

        Player player = event.getPlayer();
        if (player.hasMetadata("NPC"))
            return;

        FlagPermissions perms = plugin.getPermsByLocForPlayer(event.getSign().getLocation(), player);

        boolean hasuse = perms.playerHas(player, Flags.use, FlagCombo.TrueOrNone);
        boolean hasBuild = perms.playerHas(player, Flags.build, FlagCombo.TrueOrNone);

        if (hasuse && hasBuild || ResAdmin.isResAdmin(player))
            return;

        event.setCancelled(true);

        if (!hasuse)
            plugin.msg(player, lm.Flag_Deny, Flags.use);
        else
            plugin.msg(player, lm.Flag_Deny, Flags.build);

    }

    // For objects like decorative pots
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPotBreak(EntityChangeBlockEvent event) {

        // disabling event on world
        if (plugin.isDisabledWorldListener(event.getBlock().getWorld()))
            return;

        if (!(event.getEntity() instanceof Projectile))
            return;

        Projectile projectile = (Projectile) event.getEntity();

        Player player = null;

        if (projectile.getShooter() instanceof Player)
            player = (Player) projectile.getShooter();

        if (player != null && player.hasMetadata("NPC"))
            return;

        if (!ResidenceBlockListener.canBreakBlock(player, event.getBlock().getLocation(), true))
            event.setCancelled(true);
    }
}
