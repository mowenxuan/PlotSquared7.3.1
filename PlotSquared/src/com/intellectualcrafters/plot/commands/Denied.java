/*
 * Copyright (c) IntellectualCrafters - 2014. You are not allowed to distribute
 * and/or monetize any of our intellectual property. IntellectualCrafters is not
 * affiliated with Mojang AB. Minecraft is a trademark of Mojang AB.
 * 
 * >> File = Denied.java >> Generated by: Citymonstret at 2014-08-09 01:41
 */

package com.intellectualcrafters.plot.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.C;
import com.intellectualcrafters.plot.PlayerFunctions;
import com.intellectualcrafters.plot.Plot;
import com.intellectualcrafters.plot.UUIDHandler;
import com.intellectualcrafters.plot.database.DBFunc;
import com.intellectualcrafters.plot.events.PlayerPlotDeniedEvent;

/**
 * Created by Citymonstret on 2014-08-03.
 */
@SuppressWarnings("deprecated")
public class Denied extends SubCommand {

	public Denied() {
		super(Command.DENIED, "Manage plot helpers", "denied {add|remove} {player}", CommandCategory.ACTIONS, true);
	}

	@Override
	public boolean execute(Player plr, String... args) {
		if (args.length < 2) {
			PlayerFunctions.sendMessage(plr, C.DENIED_NEED_ARGUMENT);
			return true;
		}
		if (!PlayerFunctions.isInPlot(plr)) {
			PlayerFunctions.sendMessage(plr, C.NOT_IN_PLOT);
			return true;
		}
		Plot plot = PlayerFunctions.getCurrentPlot(plr);
		if ((plot.owner == null) || !plot.hasRights(plr)) {
			PlayerFunctions.sendMessage(plr, C.NO_PERMISSION);
			return true;
		}
		if (args[0].equalsIgnoreCase("add")) {
			if (args[1].equalsIgnoreCase("*")) {
				UUID uuid = DBFunc.everyone;
				if (!plot.denied.contains(uuid)) {
					plot.addDenied(uuid);
					DBFunc.setDenied(plr.getWorld().getName(), plot, Bukkit.getOfflinePlayer(args[1]));
					PlayerPlotDeniedEvent event = new PlayerPlotDeniedEvent(plr, plot, uuid, true);
					Bukkit.getPluginManager().callEvent(event);
				}
				PlayerFunctions.sendMessage(plr, C.DENIED_ADDED);
				return true;
			}
			/*
			 * if (!hasBeenOnServer(args[1])) { PlayerFunctions.sendMessage(plr,
			 * C.PLAYER_HAS_NOT_BEEN_ON); return true; } UUID uuid = null; if
			 * ((Bukkit.getPlayer(args[1]) != null)) { uuid =
			 * Bukkit.getPlayer(args[1]).getUniqueId(); } else { uuid =
			 * Bukkit.getOfflinePlayer(args[1]).getUniqueId(); } if (uuid ==
			 * null) { PlayerFunctions.sendMessage(plr,
			 * C.PLAYER_HAS_NOT_BEEN_ON); return true; }
			 */
			UUID uuid = UUIDHandler.getUUID(args[1]);
			if (!plot.denied.contains(uuid)) {
				plot.addDenied(uuid);
				DBFunc.setDenied(plr.getWorld().getName(), plot, Bukkit.getOfflinePlayer(args[1]));
				PlayerPlotDeniedEvent event = new PlayerPlotDeniedEvent(plr, plot, uuid, true);
				Bukkit.getPluginManager().callEvent(event);
			}
			PlayerFunctions.sendMessage(plr, C.DENIED_ADDED);
			if ((Bukkit.getPlayer(uuid) != null) && Bukkit.getPlayer(uuid).isOnline()) {
				Plot pl = PlayerFunctions.getCurrentPlot(Bukkit.getPlayer((uuid)));
				if (pl.id == plot.id) {
					PlayerFunctions.sendMessage(Bukkit.getPlayer(uuid), C.YOU_BE_DENIED);
					Bukkit.getPlayer(uuid).teleport(Bukkit.getPlayer(uuid).getWorld().getSpawnLocation());
				}
			}
		}
		else
			if (args[0].equalsIgnoreCase("remove")) {
				if (args[1].equalsIgnoreCase("*")) {
					UUID uuid = DBFunc.everyone;
					if (!plot.denied.contains(uuid)) {
						PlayerFunctions.sendMessage(plr, C.WAS_NOT_ADDED);
						return true;
					}
					plot.removeDenied(uuid);
					DBFunc.removeDenied(plr.getWorld().getName(), plot, Bukkit.getOfflinePlayer(args[1]));
					PlayerFunctions.sendMessage(plr, C.DENIED_REMOVED);
					return true;
				}
				/*
				 * if (!hasBeenOnServer(args[1])) {
				 * PlayerFunctions.sendMessage(plr, C.PLAYER_HAS_NOT_BEEN_ON);
				 * return true; } UUID uuid = null; if
				 * (Bukkit.getPlayer(args[1])!=null) { uuid =
				 * Bukkit.getPlayer(args[1]).getUniqueId(); } else { uuid =
				 * Bukkit.getOfflinePlayer(args[1]).getUniqueId(); } if
				 * (!plot.denied.contains(uuid)) {
				 * PlayerFunctions.sendMessage(plr, C.WAS_NOT_ADDED); return
				 * true; } if (uuid == null) { PlayerFunctions.sendMessage(plr,
				 * C.PLAYER_HAS_NOT_BEEN_ON); return true; }
				 */
				UUID uuid = UUIDHandler.getUUID(args[1]);
				plot.removeDenied(uuid);
				DBFunc.removeDenied(plr.getWorld().getName(), plot, Bukkit.getOfflinePlayer(args[1]));
				PlayerPlotDeniedEvent event = new PlayerPlotDeniedEvent(plr, plot, uuid, false);
				Bukkit.getPluginManager().callEvent(event);
				PlayerFunctions.sendMessage(plr, C.DENIED_REMOVED);
			}
			else {
				PlayerFunctions.sendMessage(plr, C.DENIED_NEED_ARGUMENT);
				return true;
			}
		return true;
	}
}
