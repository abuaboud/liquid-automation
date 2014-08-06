package org.liquidbot.bot.script;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.interfaces.PaintListener;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.loader.ScriptInfo;
import org.liquidbot.bot.script.randevent.RandomEventHandler;
import org.liquidbot.bot.utils.Logger;

import java.awt.*;

/*
 * Created by Hiasat on 8/2/14
 */
public class ScriptHandler implements Runnable {

    private Thread scriptThread;
    private Thread randomEventsThread;
    private AbstractScript script;
    private ScriptInfo scriptInfo;
    private State scriptState;
    private PaintListener paintListener;
    private RandomEventHandler randomEventHandler;

    private Logger logger = new Logger(getClass());

    public enum State {
        RUNNING, PAUSE, STOPPED
    }

    @Override
    public void run() {
        while (!scriptState.equals(State.STOPPED)) {
            if (script == null) {
                stop();
            } else if (scriptState.equals(State.PAUSE) || (randomEventHandler != null && randomEventHandler.isActive)) {
                Time.sleep(500);
            } else {
                int timeToSleep = script.operate();
                Time.sleep(timeToSleep);
            }
        }
    }

    public void start(AbstractScript script, ScriptInfo scriptInfo) {
        logger.info("Script Started: " + scriptInfo.name, Color.GREEN);
        this.scriptState = State.RUNNING;
        this.scriptInfo = scriptInfo;
        this.script = script;
        this.scriptThread = new Thread(this);
        this.scriptThread.start();
        this.script.onStart();
        if (randomEventHandler == null){
            randomEventHandler = new RandomEventHandler();
            Configuration.getInstance().getCanvas().getPaintListeners().add(randomEventHandler);
        }
        this.randomEventsThread = new Thread(randomEventHandler);
        this.randomEventsThread.start();
        if (script instanceof PaintListener) {
            paintListener = (PaintListener) script;
            Configuration.getInstance().getCanvas().getPaintListeners().add(paintListener);
        }
    }

    public void stop() {
        logger.info("Script Stopped: " + scriptInfo.name, Color.RED.brighter());
        this.scriptState = State.STOPPED;
        this.script.onStop();
        this.scriptThread.interrupt();
        this.randomEventsThread.interrupt();
        Configuration.getInstance().getCanvas().getPaintListeners().remove(paintListener);
        this.script = null;
        this.scriptThread = null;
        this.randomEventsThread = null;
        this.paintListener = null;
    }

    public void pause() {
        logger.info("Paused Started: " + scriptInfo.name, Color.ORANGE);
        this.scriptState = State.PAUSE;
    }

    public State getScriptState() {
        return scriptState;
    }

    public Thread getScriptThread() {
        return scriptThread;
    }

}
