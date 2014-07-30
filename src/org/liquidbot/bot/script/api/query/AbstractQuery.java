package org.liquidbot.bot.script.api.query;

import org.liquidbot.bot.script.api.context.ClientAccessor;
import org.liquidbot.bot.script.api.context.ClientContext;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kenneth on 7/29/2014.
 */
public abstract class AbstractQuery<Q extends AbstractQuery, E> extends ClientAccessor {

    private final List<E> list = new LinkedList<E>();

    protected abstract E[] elements();

    public AbstractQuery(ClientContext ctx) {
        super(ctx);
    }
}
