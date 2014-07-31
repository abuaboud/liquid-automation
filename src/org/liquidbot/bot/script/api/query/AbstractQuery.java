package org.liquidbot.bot.script.api.query;

import org.liquidbot.bot.script.api.context.ClientAccessor;
import org.liquidbot.bot.script.api.context.ClientContext;
import org.liquidbot.bot.script.api.interfaces.Filter;

import java.util.*;

/**
 * Created by Kenneth on 7/29/2014.
 */
public abstract class AbstractQuery<Q extends AbstractQuery, E> extends ClientAccessor {

    private final List<E> list = new LinkedList<E>();

    protected abstract E[] elements();

    public AbstractQuery(ClientContext ctx) {
        super(ctx);
    }

    public Q refresh() {
        list.clear();
        for(E element : elements()) {
            if(element != null) {
                list.add(element);
            }
        }
        return (Q) this;
    }

    public Q filter(Filter<E> filter) {
        final List<E> clone = new ArrayList(list);
        for(E e : elements()) {
            if(e != null && !filter.accept(e)) {
                list.remove(e);
            }
        }
        return (Q) this;
    }

    public Q sort(Comparator<E> comparator) {
        Collections.sort(list, comparator);
        return (Q) this;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public List<E> getList() {
        return list;
    }

    public E poll() {
        return list.isEmpty() ? null : list.get(0);
    }
}
