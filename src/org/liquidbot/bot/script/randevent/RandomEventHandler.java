package org.liquidbot.bot.script.randevent;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.ScriptHandler;
import org.liquidbot.bot.script.api.interfaces.PaintListener;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.util.Timer;
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

	public static final int botWorldHandler = 0,
			loginHandler = 1,
			lobbyHandler = 2, 
			behaviorAction = 3,
			smartBreak = 4,
			rewardHandler = 5, 
			certerHandler = 6,
			talkerHandler = 7,
			sandwichHandler = 8;

	private final Font FONT_VERDANA = new Font("Verdana", 1, 10);

	public RandomEventHandler() {
		randomEvents = new RandomEvent[]{
				new BotWorld(), new Login(), new ClickToPlay(), new BehaviorAction(), new SmartBreak(),
				new Reward(), new Certer(), new TalkerDenier(), new SandwichLady()
		};
	}

	@Override
	public void run() {
		while (Configuration.getInstance().getScriptHandler().getScriptState().equals(ScriptHandler.State.RUNNING)) {
			try {
				for (RandomEvent randomEvent : randomEvents) {
					if (randomEvent != null && randomEvent.isEnabled() && randomEvent.active()) {
						if (!randomEvent.getName().equals("Random Behavior")) {
							log.info("Started RandomEvent: " + randomEvent.getName(), Color.GREEN);
						}
						setActive(true);
						activeEvent = randomEvent;
						Configuration.getInstance().getScriptHandler().getScriptThread().interrupt();
						while (randomEvent.active() && Configuration.getInstance().getScriptHandler().getScriptState().equals(ScriptHandler.State.RUNNING)) {

							randomEvent.solve();
							Time.sleep(500);
						}
						activeEvent = null;
						setActive(false);
						randomEvent.reset();
						if (!randomEvent.getName().equals("Random Behavior")) {
							log.info("Completed RandomEvent: " + randomEvent.getName(), Color.GREEN);
						}
					}
				}
				Time.sleep(Configuration.getInstance().lowCpu() ? 1000 : 500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		SmartBreak aBreak = (SmartBreak) randomEvents[smartBreak];
		aBreak.timer = new Timer(60 * 3 * 60 * 1000);
	}


	public void setActive(boolean active) {
		isActive = active;
	}


	@Override
	public void render(Graphics2D graphics) {
		if (Configuration.getInstance().getScriptHandler().getScriptState().equals(ScriptHandler.State.RUNNING)) {
			if (activeEvent != null && !activeEvent.getName().equals("Random Behavior")) {
				graphics.setColor(BACKGROUND_COLOR);
				graphics.fillRect(0, 0, 765, 503);
				graphics.setColor(Color.WHITE);
				graphics.drawString("Event:" + activeEvent.getName(), 351, 20);
				graphics.drawString("Author:" + activeEvent.getAuthor(), 351, 35);
				graphics.drawString("Status:" + activeEvent.getStatus(), 351, 50);

				if (activeEvent instanceof PaintListener) {
					((PaintListener) activeEvent).render(graphics);
				}
			}
			SmartBreak aBreak = (SmartBreak) randomEvents[smartBreak];
			if (Configuration.getInstance().smartBreak()) {
				graphics.setFont(FONT_VERDANA);
				graphics.setColor(Color.WHITE);
				graphics.drawString("Break", 690, 140);
				graphics.drawString("In: ", 666, 152);
				graphics.drawString("For: ", 666, 164);

				graphics.setColor(Color.ORANGE);
				graphics.drawString(Time.parse(aBreak.timer.getRemaining()), 692, 152);
				graphics.drawString(Time.parse(aBreak.breakTimer.getRemaining()), 692, 164);
			}

		}
	}

	public static void enableRandom(int i, boolean enable) {
		RandomEvent randomEvent = Configuration.getInstance().getScriptHandler().getRandomEventHandler().randomEvents[i];
		Configuration.getInstance().getScriptHandler().getRandomEventHandler().log.info(enable ? "Enabled " : "Disabled " + randomEvent.getName());
		randomEvent.setEnabled(enable);
	}
}
