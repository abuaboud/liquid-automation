package org.liquidbot.bot.script.api.query;

import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.interfaces.Identifiable;
import org.liquidbot.bot.script.api.interfaces.Locatable;
import org.liquidbot.bot.script.api.interfaces.Nameable;
import org.liquidbot.bot.script.api.wrappers.Tile;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Kenneth on 7/30/2014.
 */
public abstract class BasicQuery<Q extends BasicQuery, E extends Locatable & Nameable & Identifiable> extends AbstractQuery<Q, E>
        implements Locatable.Query<Q>, Nameable.Query<Q>, Identifiable.Query<Q> {

    @Override
    public Q id(final int... ids) {
        return filter(new Filter<E>() {
            @Override
            public boolean accept(E e) {
                return e != null && Arrays.asList(ids).contains(e.getId());
            }
        });
    }

    @Override
    public Q within(final int radius) {
        return filter(new Filter<E>() {
            @Override
            public boolean accept(E e) {
                return e != null && e.distanceTo() <= radius;
            }
        });
    }

    @Override
    public Q at(final Tile tile) {
        return filter(new Filter<E>() {
            @Override
            public boolean accept(E e) {
                return e != null && e.getLocation().equals(tile);
            }
        });
    }

    @Override
    public Q nearest() {
        return sort(DISTANCE_SORT);
    }

    @Override
    public Q name(final String... names) {
        return filter(new Filter<E>() {
            @Override
            public boolean accept(E e) {
                return e != null && Arrays.asList(names).contains(e.getName());
            }
        });
    }

    private final Comparator<E> DISTANCE_SORT = new Comparator<E>() {
        @Override
        public int compare(E o1, E o2) {
            return o1.distanceTo() - o2.distanceTo();
        }
    };
}
