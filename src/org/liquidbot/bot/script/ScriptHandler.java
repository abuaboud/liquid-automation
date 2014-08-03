package org.liquidbot.bot.script;

import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.loader.ScriptInfo;
import org.liquidbot.bot.utils.Logger;

import java.awt.*;

/*
 * Created by Hiasat on 8/2/14
 */
public class ScriptHandler implements Runnable {

    private Thread scriptThread;
    private AbstractScript script;
    private ScriptInfo scriptInfo;
    private State scriptState;

    private Logger logger = new Logger(getClass());

    public enum State {
        RUNNING, PAUSE, STOPPED
    }

    @Override
    public void run() {
        while (!scriptState.equals(State.STOPPED)) {
            if (scriptState.equals(State.PAUSE)) {
                Time.sleep(200, 250);
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
    }

    public void stop() {
        logger.info("Script Stopped: " + scriptInfo.name, Color.RED.brighter());
        this.scriptState = State.STOPPED;
        this.script.onStop();
        this.scriptThread.interrupt();
        this.script = null;
    }

    public void pause() {
        logger.info("Paused Started: " + scriptInfo.name, Color.ORANGE);
        this.scriptState = State.PAUSE;
    }

    public State getScriptState() {
        return scriptState;
    }
}
