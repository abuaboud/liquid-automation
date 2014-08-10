package org.liquidbot.bot.script.api.listeners;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.ScriptHandler;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.utils.Logger;
import org.liquidbot.bot.utils.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kenneth on 8/10/2014.
 */
public class AnimationMonitor implements Runnable {

    private final Configuration config = Configuration.getInstance();
    private final List<AnimationListener> listeners = new ArrayList<>();
    private final Logger log = new Logger(ExperienceMonitor.class);
    private int last = -1;

    public AnimationMonitor() {
        log.info("Initialized AnimationListener..");
        last = Players.getLocal().getAnimation();
    }

    public void addListener(AnimationListener... listeners) {
        Collections.addAll(this.listeners, listeners);
    }

    @Override
    public void run() {
        while(config.getScriptHandler().getScriptState() != null && config.getScriptHandler().getScriptState() != ScriptHandler.State.STOPPED) {
            final int current = Players.getLocal().getAnimation();
            if(current != last) {
                for(AnimationListener listener : listeners) {
                    listener.onAnimation(last, current);
                }
            }
            last = current;
            Utilities.sleep(600);
        }
        log.info("AnimationListener stopped!");
    }
}
