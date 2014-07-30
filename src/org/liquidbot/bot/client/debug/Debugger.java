package org.liquidbot.bot.client.debug;

import org.liquidbot.bot.script.api.interfaces.PaintListener;

/**
 * Created by Kenneth on 7/30/2014.
 */
public abstract class Debugger<E> implements PaintListener {

    public abstract E[] elements();
    public abstract boolean activate();

}
