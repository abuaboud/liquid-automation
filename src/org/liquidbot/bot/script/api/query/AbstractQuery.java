package org.liquidbot.bot.script.api.query;

import org.liquidbot.bot.script.api.interfaces.Filter;

import java.util.*;

/**
 * Created by Kenneth on 7/29/2014.
 */
@SuppressWarnings("unchecked")
public abstract class AbstractQuery<Q extends AbstractQuery, E> implements Iterable<E> {

    private final List<E> elements = new LinkedList<E>();

    protected abstract E[] elements();

    public Q select() {
        elements.clear();
        Collections.addAll(elements, elements());
        return (Q) this;
    }

    public Q filter(Filter<E> filter) {
        final List<E> copy = new LinkedList<E>(elements);
        for(E e : copy) {
            if(!filter.accept(e)) {
                elements.remove(e);
            }
        }
        return (Q) this;
    }

    public Q sort(Comparator<E> comparator) {
        Collections.sort(elements, comparator);
        return (Q) this;
    }

    public E single() {
        return !elements.isEmpty() ? elements.get(0) : null;
    }

    public Q limit(int amount) {
        final List<E> clone = new LinkedList<E>(elements);
        if(size() > amount) {
            final List<E> sub = clone.subList(0, amount);
            elements.clear();
            elements.addAll(sub);
        }
        return (Q) this;
    }

    public Q shuffle() {
        Collections.shuffle(elements);
        return (Q) this;
    }

    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<E> iterator() {
        return elements.iterator();
    }

}