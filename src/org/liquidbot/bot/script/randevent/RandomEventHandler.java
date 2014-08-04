package org.liquidbot.bot.script.randevent;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.ScriptHandler;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.randevent.impl.ClickToPlay;
import org.liquidbot.bot.script.randevent.impl.Login;
import org.liquidbot.bot.script.randevent.impl.StrangeBox;
import org.liquidbot.bot.utils.Logger;

import java.awt.*;

/*
 * Created by Hiasat on 8/3/14
 */
public class RandomEventHandler implements Runnable {

    public Logger log = new Logger(getClass());

    public boolean running = true;
    public boolean isActive = false;

    public final RandomEvent[] randomEvents;

    public RandomEventHandler() {
        randomEvents = new RandomEvent[]{new Login(), new ClickToPlay(), new StrangeBox()};
    }

    @Override
    public void run() {
        while (isRunning()) {
            if (Configuration.getInstance().getScriptHandler().getScriptState().equals(ScriptHandler.State.RUNNING)) {
                for (RandomEvent randomEvent : randomEvents) {
                    if (randomEvent.isEnabled() && randomEvent.active()) {
                        log.info("Started RandomEvent: " + randomEvent.getName(), Color.GREEN);
                        setActive(true);
                        while (randomEvent.active()) {
                            randomEvent.solve();
                            Time.sleep(500);
                        }
                        setActive(false);
                        log.info("Completed RandomEvent: " + randomEvent.getName(), Color.GREEN);
                    }
                }
            }
            Time.sleep(500);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
