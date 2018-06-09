package org.liquidbot.bot.script;

import org.liquidbot.bot.utils.Logger;

/**
 * Created on 8/6/2014.
 */
public abstract class Action  {

    public final Logger log = new Logger(getClass());

    public abstract boolean activate();
    public abstract void execute();

    public int priority() {
        return 0;
    }

}
