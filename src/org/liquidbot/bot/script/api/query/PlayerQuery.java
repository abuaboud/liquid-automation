package org.liquidbot.bot.script.api.query;

import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.interfaces.Locatable;
import org.liquidbot.bot.script.api.interfaces.Nameable;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.wrappers.Player;
import org.liquidbot.bot.script.api.wrappers.Tile;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Kenneth on 7/30/2014.
 */
public class PlayerQuery extends AbstractQuery<PlayerQuery, Player> implements Locatable.Query<PlayerQuery>, Nameable.Query<PlayerQuery> {

    public Player local() {
        return Players.getLocal();
    }

    @Override
    protected Player[] elements() {
        return Players.getAll();
    }

    @Override
    public PlayerQuery within(final int radius) {
        return filter(new Filter<Player>() {
            @Override
            public boolean accept(Player e) {
                return e != null && e.distanceTo() <= radius;
            }
        });
    }

    @Override
    public PlayerQuery at(final Tile tile) {
        return filter(new Filter<Player>() {
            @Override
            public boolean accept(Player e) {
                return e != null && e.getLocation().equals(tile);
            }
        });
    }

    @Override
    public PlayerQuery nearest() {
        return sort(DISTANCE_SORT);
    }

    @Override
    public PlayerQuery name(final String... names) {
        return filter(new Filter<Player>() {
            @Override
            public boolean accept(Player e) {
                return e != null && Arrays.asList(names).contains(e.getName());
            }
        });
    }

    private final Comparator<Player> DISTANCE_SORT = new Comparator<Player>() {
        @Override
        public int compare(Player o1, Player o2) {
            return o1.distanceTo() - o2.distanceTo();
        }
    };
}
