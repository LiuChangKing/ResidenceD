package com.bekvon.bukkit.residence.event;

import com.bekvon.bukkit.residence.protection.CuboidArea;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.List;

public final class ResidenceSelectionVisualizationEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private List<CuboidArea> areas;
    private List<CuboidArea> errorAreas;

    public ResidenceSelectionVisualizationEvent(Player player, List<CuboidArea> areas, List<CuboidArea> errorAreas) {
	super(player);
	this.areas = areas;
	this.errorAreas = errorAreas;
    }

    public final static HandlerList getHandlerList() {
	return handlers;
    }

    @Override
    public final HandlerList getHandlers() {
	return handlers;
    }

    @Override
    public final void setCancelled(final boolean cancel) {
	this.cancel = cancel;
    }

    @Override
    public final boolean isCancelled() {
	return cancel;
    }

    public List<CuboidArea> getAreas() {
	return areas;
    }

    public List<CuboidArea> getErrorAreas() {
	return errorAreas;
    }
}
