package org.liquidbot.bot.script.api.query;

import org.liquidbot.bot.script.api.context.ClientContext;
import org.liquidbot.bot.script.api.interfaces.Identifiable;
import org.liquidbot.bot.script.api.interfaces.Locatable;
import org.liquidbot.bot.script.api.interfaces.Nameable;
import org.liquidbot.bot.script.api.wrappers.Tile;

/**
 * Created by Kenneth on 7/30/2014.
 */
public abstract class MobileIdNameQuery<Q extends MobileIdNameQuery, E extends Locatable & Nameable & Identifiable> extends AbstractQuery<Q, E> implements Locatable.Query<Q>, Nameable.Query<Q>, Identifiable.Query<Q> {

    public MobileIdNameQuery(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public Q id(int... ids) {
        return null;
    }

    @Override
    public Q within(int radius) {
        return null;
    }

    @Override
    public Q at(Tile tile) {
        return null;
    }

    @Override
    public Q nearest() {
        return null;
    }

    @Override
    public Q name(String... names) {
        return null;
    }
}
