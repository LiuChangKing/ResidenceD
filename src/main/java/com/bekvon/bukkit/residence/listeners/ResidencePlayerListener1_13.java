package com.bekvon.bukkit.residence.listeners;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.containers.lm;
import com.bekvon.bukkit.residence.permissions.PermissionManager.ResPerm;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;
import net.Zrips.CMILib.Version.Version;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ResidencePlayerListener1_13 implements Listener {

    private Residence plugin;

    public ResidencePlayerListener1_13(Residence plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onSignInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.PHYSICAL) || !event.getClickedBlock().getType().equals(Material.TURTLE_EGG))
            return;
        if (!ResidenceBlockListener.canBreakBlock(event.getPlayer(), event.getClickedBlock(), true))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onButtonHitWithProjectile(BlockRedstoneEvent e) {

        if (tempButtonLocation == null)
            return;

        // Disabling listener if flag disabled globally
        if (!Flags.button.isGlobalyEnabled())
            return;

        if (e.getBlock() == null)
            return;

        if (plugin.isDisabledWorldListener(e.getBlock().getWorld()))
            return;

        Block block = e.getBlock();

        if (!tempButtonLocation.equals(block.getLocation()) && !tempButtonLocation.clone().add(0, 1, 0).equals(block.getLocation()))
            return;

        if (!CMIMaterial.isButton(block.getType()))
            return;

        e.setNewCurrent(0);
    }

    private Location tempButtonLocation = null;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onButtonHitWithProjectile(ProjectileHitEvent e) {

        tempButtonLocation = null;
        // Disabling listener if flag disabled globally
        if (!Flags.button.isGlobalyEnabled())
            return;

        if (e.getHitBlock() == null)
            return;

        if (plugin.isDisabledWorldListener(e.getHitBlock().getWorld()))
            return;

        if (!(e.getEntity().getShooter() instanceof Player))
            return;

        Player player = (Player) e.getEntity().getShooter();

        Block block = e.getHitBlock().getLocation().clone().add(e.getHitBlockFace().getDirection()).getBlock();

        tempButtonLocation = block.getLocation().clone();

        if (!CMIMaterial.isButton(block.getType()))
            return;

        FlagPermissions perms = plugin.getPermsByLocForPlayer(block.getLocation(), player);

        boolean hasuse = perms.playerHas(player, Flags.use, true);

        ClaimedResidence res = plugin.getResidenceManager().getByLoc(block.getLocation());

        Flags result = FlagPermissions.getMaterialUseFlagList().get(block.getType());
        if (result == null)
            return;

        if (perms.playerHas(player, result, hasuse))
            return;

        if (res != null && res.getRaid().isUnderRaid() && res.getRaid().isAttacker(player)) {
            return;
        }

        switch (result) {
        case button:
            if (ResPerm.bypass_button.hasPermission(player, 10000L))
                return;
            break;
        }
        e.setCancelled(true);

        plugin.msg(player, lm.Flag_Deny, result);

        if (e.getEntity() instanceof Arrow)
            e.getEntity().remove();

        if (Version.isCurrentHigher(Version.v1_13_R1) && CMIMaterial.isButton(block.getType()) && block.getBlockData() instanceof Switch) {
            Switch button = (Switch) block.getBlockData();
            button.setPowered(false);
            block.setBlockData(button, true);
            CMIScheduler.runAtLocationLater(plugin, block.getLocation(), () -> {
                button.setPowered(false);
                block.setBlockData(button, true);
            }, 1L);
            return;
        }

        if (Version.isCurrentHigher(Version.v1_13_R1) && e.getEntity() instanceof Trident && !block.getType().toString().contains("STONE") && block
            .getBlockData() instanceof org.bukkit.block.data.Powerable) {
            org.bukkit.block.data.Powerable powerable = (org.bukkit.block.data.Powerable) block.getBlockData();
            if (!powerable.isPowered()) {
                CMIScheduler.runAtLocation(plugin, block.getLocation(), () -> {
                    powerable.setPowered(false);
                    block.setBlockData(powerable, true);
                });
            }
        }

        return;
    }

}
