package org.liquidbot.bot.script.api.methods.interactive;

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.query.PlayerQuery;
import org.liquidbot.bot.script.api.wrappers.Player;
import org.liquidbot.bot.script.api.wrappers.Tile;

import java.util.ArrayList;
import java.util.List;

/*
 * Created on 7/30/14
 */
public class Players {

    private final static PlayerQuery query = new PlayerQuery();

    public static PlayerQuery query() {
        return query;
    }

    /**
     * @return Player : Your Player
     */
    public static Player getLocal() {
        return new Player(Reflection.value("Client#getMyPlayer()", null));
    }

    /**
     * @return Player[] : all players
     */
    public static Player[] getAll() {
        return getAll(null);
    }

    /**
     * Get all Players that apply to that filter
     *
     * @param filter
     * @return Player[]
     */
    public static Player[] getAll(Filter<Player> filter) {
        List<Player> list = new ArrayList<Player>();
        final Object[] objects = (Object[]) Reflection.value("Client#getLocalPlayers()", null);
        for (Object player : objects) {
            if (player != null) {
                Player wrapper = new Player(player);
                if ((filter == null || filter.accept(wrapper))) {
                    list.add(wrapper);
                }
            }
        }
        return list.toArray(new Player[list.size()]);
    }

    /**
     * get Nearest Specific Player to Specific Location
     *
     * @param location startLocation
     * @param filter   Player Filter
     * @return Player
     */
    public static Player getNearest(Tile location, Filter<Player> filter) {
        Player closet = new Player(null);
        int distance = 9999;
        for (Player player : getAll(filter)) {
            if (player.isValid() && distance > player.distanceTo(location)) {
                closet = player;
            }
        }
        return closet;
    }

    /**
     * get Nearest Specific Player to Specific Location
     *
     * @param filter Player Filter
     * @return Player
     */
    public static Player getNearest(Filter<Player> filter) {
        return getNearest(Players.getLocal().getLocation(), filter);
    }

	/**

	 * @return wrapper that have null in structure to avoid Null Pointer Exception and able to use Player#isValid instead
	 */
	public static Player nil(){
		return new Player(null);
	}

}
