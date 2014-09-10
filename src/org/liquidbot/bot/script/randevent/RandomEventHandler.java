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

	public static final int botWorldHandler = 0;
	public static final int loginHandler = 1;
	public static final int lobbyHandler = 2;
	public static final int pinballHandler = 3;
	public static final int certerHandler = 4;
	public static final int avoidCombatHandler = 5;
	public static final int drillDemonHandler = 6;
	public static final int freakyFosterHandler = 7;
	public static final int frogCaveHandler = 8;
	public static final int lostAndFoundHandler = 9;
	public static final int mazeHandler = 10;
	public static final int mimeHandler = 11;
	public static final int mollyHandler = 12;
	public static final int pilloryHandler = 13;
	public static final int prisonHandler = 14;
	public static final int quizMasterHandler = 15;
	public static final int sandwichLadyHandler = 16;
	public static final int scapeIslandHandler = 18;
	public static final int strangePlantHandler = 18;
	public static final int surpriseExamHandler = 19;
	public static final int talkerHandler = 20;
	public static final int rewardHandler = 21;
	public static final int reportHandler = 22;
	public static final int strangeBoxHandler = 23;
	public static final int bankpinHandler = 24;
	public static final int systemUpdate = 25;
	public static final int skillMenuHandler = 26;
	public static final int graveHandler = 27;
	public static final int behaviorAction = 28;
	public static final int smartBreak = 30;

	private final Font FONT_VERDANA = new Font("Verdana", 1, 10);

	public RandomEventHandler() {
		randomEvents = new RandomEvent[]{new BotWorld(),
				new Login(), new ClickToPlay(), new Pinball(), new Certer(), new AvoidCombat(), new DrillDemon(), new FreakyFoster()
				, new FrogCave(), /*new LostAndFoundHandler()*/null, new Maze(),
				new Mime(), new Molly(), null /*new PilloryHandler()*/, new PrisonPete(), new QuizMaster(), null, new SandwichLady()
				, new ScapeIsland(), new StrangePlant(), new SurpriseExam(), new Talker(), new Reward()
				,/* new ReportHandler()*/null, new StrangeBox(), null/* new BankPinHandler()*/, null
				/*  new SystemUpdate()*/, null/* new SkillMenuHandler()*/, new GraveRandom(), new BehaviorAction(), new SmartBreak()
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
