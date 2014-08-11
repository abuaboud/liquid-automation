package org.liquidbot.bot.script.randevent;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.ScriptHandler;
import org.liquidbot.bot.script.api.interfaces.PaintListener;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.randevent.impl.*;
import org.liquidbot.bot.utils.Logger;

import java.awt.*;

/*
 * Created by Hiasat on 8/3/14
 */
public class RandomEventHandler implements Runnable, PaintListener {

	public Logger log = new Logger(getClass());

	public boolean isActive = false;

	public RandomEvent activeEvent;

	public final RandomEvent[] randomEvents;

	private final Color BACKGROUND_COLOR = new Color(Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), 40);

	public RandomEventHandler() {
		randomEvents = new RandomEvent[]{new Login(), new ClickToPlay(), new StrangeBox(), new SurpriseExam()
				, new Reward(), new Talker(), new AvoidCombat(), new Certer(), new DrillDemon(), new FrogCave()
				, new ScapeIsland(), new FreakyFoster(), new PrisonPete()};
	}

	@Override
	public void run() {
		while (Configuration.getInstance().getScriptHandler().getScriptState().equals(ScriptHandler.State.RUNNING)) {
			for (RandomEvent randomEvent : randomEvents) {
				if (randomEvent.isEnabled() && randomEvent.active()) {
					log.info("Started RandomEvent: " + randomEvent.getName(), Color.GREEN);
					setActive(true);
					activeEvent = randomEvent;
					while (randomEvent.active() && Configuration.getInstance().getScriptHandler().getScriptState().equals(ScriptHandler.State.RUNNING)) {
						randomEvent.solve();
						Time.sleep(500);
					}
					activeEvent = null;
					setActive(false);
					randomEvent.reset();
					log.info("Completed RandomEvent: " + randomEvent.getName(), Color.GREEN);
				}
			}
			Time.sleep(Configuration.getInstance().lowCpu() ? 1000 : 500);
		}
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}


	@Override
	public void render(Graphics2D graphics) {
		if (Configuration.getInstance().getScriptHandler().getScriptState().equals(ScriptHandler.State.RUNNING) && activeEvent != null) {
			graphics.setColor(BACKGROUND_COLOR);
			graphics.fillRect(0, 0, 765, 503);
			graphics.setColor(Color.WHITE);
			graphics.drawString("Event:" + activeEvent.getName(), 351, 20);
			graphics.drawString("Author:" + activeEvent.getAuthor(), 351, 35);
			graphics.drawString("Status:" + activeEvent.getStatus(), 351, 50);


		}
	}
}
