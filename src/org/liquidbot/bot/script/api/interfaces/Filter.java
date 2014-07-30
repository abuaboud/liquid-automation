package org.liquidbot.bot.script.api.interfaces;

/**
 * Created by Kenneth on 7/30/2014.
 */
public interface Filter<E> {

    public boolean accept(E e);

}
