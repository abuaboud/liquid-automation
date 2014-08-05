package org.liquidbot.component.debug;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.interfaces.PaintListener;
import org.liquidbot.bot.script.api.util.Timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kenneth on 7/30/2014.
 */
public abstract class Debugger<E> implements PaintListener {

    public Configuration config = Configuration.getInstance();
    protected List<E> list = new ArrayList<E>();
    private Timer refreshRate = new Timer(1000);

    public abstract E[] elements();

    public abstract boolean activate();

    public List<E> refresh() {
        if (!refreshRate.isRunning()) {
            list = Arrays.asList(elements());
        }
        return list;
    }

}
