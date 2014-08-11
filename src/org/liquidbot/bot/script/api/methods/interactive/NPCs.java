package org.liquidbot.bot.script.api.methods.interactive;

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.query.NPCQuery;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.api.wrappers.Tile;
import org.liquidbot.bot.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Hiasat on 7/30/14
 */
public class NPCs {

	private static NPCQuery npcQuery = new NPCQuery();

	/**
	 * Gets the Query instance
	 *
	 * @return the query instance
	 */
	public static NPCQuery query() {
		return npcQuery;
	}

	/**
	 * static Method to get all Npcs with any of these names
	 *
	 * @param names
	 * @return NPC[]
	 */
	public static NPC[] getAll(final String... names) {
		if (names == null)
			return getAll();
		return getAll(new Filter<NPC>() {
			@Override
			public boolean accept(NPC npc) {
				return npc.isValid() && npc.getName() != null && Utilities.inArray(npc.getName(), names);
			}
		});
	}

	/**
	 * static Method to get all Npcs that return true in Filter
	 *
	 * @param filter
	 * @return NPC[]
	 */
	public static NPC[] getAll(Filter<NPC> filter) {
		List<NPC> list = new ArrayList<NPC>();
		if (!Game.isLoggedIn())
			return list.toArray(new NPC[list.size()]);
		final Object[] objects = (Object[]) Reflection.value("Client#getLocalNpcs()", null);
		for (Object npc : objects) {
			if (npc != null) {
				NPC wrapper = new NPC(npc);
				if ((filter == null || filter.accept(wrapper))) {
					list.add(wrapper);
				}
			}
		}
		return list.toArray(new NPC[list.size()]);
	}

	/**
	 * Static Method to get all Npcs
	 *
	 * @return NPC[]
	 */
	public static NPC[] getAll() {
		return getAll(new Filter<NPC>() {
			@Override
			public boolean accept(NPC npc) {
				return true;
			}
		});
	}

	/**
	 * get Nearest Specific NPC to Specific Location
	 *
	 * @param filter npc Filter
	 * @return NPC
	 */
	public static NPC getNearest(Filter<NPC> filter) {
		return getNearest(Players.getLocal().getLocation(), filter);
	}

	/**
	 * get Nearest Specific NPC to Specific Location
	 *
	 * @param location startLocation
	 * @param filter   npc Filter
	 * @return NPC
	 */
	public static NPC getNearest(Tile location, Filter<NPC> filter) {
		NPC closet = new NPC(null);
		int distance = 9999;
		for (NPC npc : getAll(filter)) {
			if (npc.isValid() && distance > npc.distanceTo(location)) {
				closet = npc;
				distance = npc.distanceTo(location);
			}
		}
		return closet;
	}

	/**
	 * Get closet NPC that has that Id or one of ids
	 *
	 * @param ids target NPC Id or Ids
	 * @return NPC
	 */
	public static NPC getNearest(final int... ids) {
		if (!Game.isLoggedIn())
			return new NPC(null);
		return getNearest(Players.getLocal().getLocation(), new Filter<NPC>() {
			@Override
			public boolean accept(NPC npc) {
				return npc.isValid() && Utilities.inArray(npc.getId(), ids);
			}
		});
	}

	/**
	 * Get closet NPC that has that name or one of names
	 *
	 * @param names target NPC name or names
	 * @return NPC
	 */
	public static NPC getNearest(final String... names) {
		if (!Game.isLoggedIn())
			return new NPC(null);
		return getNearest(Players.getLocal().getLocation(), new Filter<NPC>() {
			@Override
			public boolean accept(NPC npc) {
				return npc.isValid() && Utilities.inArray(npc.getName(), names);
			}
		});
	}

	/**
	 * @return wrapper that have null in structure to avoid Null Pointer Exception and able to use NPC#isValid instead
	 */
	public static NPC nil() {
		return new NPC(null);
	}
}
