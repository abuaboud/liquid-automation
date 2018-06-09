package org.liquidbot.bot.script;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.listeners.*;
import org.liquidbot.bot.script.api.interfaces.PaintListener;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.loader.ScriptInfo;
import org.liquidbot.bot.script.randevent.RandomEvent;
import org.liquidbot.bot.script.randevent.RandomEventHandler;
import org.liquidbot.bot.ui.account.Account;
import org.liquidbot.bot.utils.Logger;

import java.awt.*;

/*
 * Created on 8/2/14
 */
public class ScriptHandler implements Runnable {

	private Thread scriptThread;
	private Thread randomEventsThread;
	private LoopScript script;
	private ScriptInfo scriptInfo;
	private State scriptState = State.STOPPED;
	private Account account;
	private PaintListener paintListener;
	public RandomEventHandler randomEventHandler;
	private ExperienceMonitor experienceMonitor;
	private AnimationMonitor animationMonitor;
	private InventoryMonitor inventoryMonitor;

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

	public void start(LoopScript script, ScriptInfo scriptInfo, Account account) {
		if (script == null)
			return;
		logger.info("Script Started: " + scriptInfo.name, Color.GREEN);
		this.scriptState = State.RUNNING;
		this.scriptInfo = scriptInfo;
		this.script = script;
		this.account = account;

		if (account != null)
			Configuration.getInstance().pattern().currentPattern(Configuration.getInstance().pattern().loadPattern(account.getEmail()));

		this.scriptThread = new Thread(this);

		this.animationMonitor = new AnimationMonitor();
		if (script instanceof AnimationListener) {
			animationMonitor.addListener((AnimationListener) script);
			new Thread(animationMonitor).start();
		}


		this.experienceMonitor = new ExperienceMonitor();
		if (script instanceof ExperienceListener) {
			experienceMonitor.addListener((ExperienceListener) script);
			new Thread(experienceMonitor).start();
		}


		this.inventoryMonitor = new InventoryMonitor();
		if (script instanceof InventoryListener) {
			inventoryMonitor.addListener((InventoryListener) script);
			new Thread(inventoryMonitor).start();
		}

		if (randomEventHandler == null) {
			randomEventHandler = new RandomEventHandler();
			Configuration.getInstance().getCanvas().getPaintListeners().add(randomEventHandler);
		}
		for (RandomEvent randomEvent : randomEventHandler.randomEvents) {
			if (randomEvent != null)
				randomEvent.setEnabled(true);
		}
		this.script.onStart();
		this.scriptThread.start();
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
		this.experienceMonitor = null;
		this.randomEventsThread = null;
		this.paintListener = null;
	}

	public void pause() {
		logger.info("Paused Started: " + scriptInfo.name, Color.ORANGE);
		this.scriptState = State.PAUSE;
	}

	public void renew() {
		this.scriptThread = new Thread(this);
	}

	public void setScriptState(State scriptState) {
		this.scriptState = scriptState;
	}

	public State getScriptState() {
		return scriptState;
	}

	public Thread getScriptThread() {
		return scriptThread;
	}

	public RandomEventHandler getRandomEventHandler() {
		return randomEventHandler;
	}

	public Account getAccount() {
		return account;
	}

}
