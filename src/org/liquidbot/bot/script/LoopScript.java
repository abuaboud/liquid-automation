package org.liquidbot.bot.script;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.utils.Logger;

/**
 * Created by Kenneth on 7/29/2014.
 */
public abstract class LoopScript {

    private final long startTime = System.currentTimeMillis();

    public Logger log = new Logger(getClass());

    public abstract void onStart();

    public abstract int operate();

    public abstract void onStop();

    public final void stop() {
        Configuration.getInstance().getScriptHandler().stop();
    }

    public long getRuntime() {
        return System.currentTimeMillis() - startTime;
    }
}
