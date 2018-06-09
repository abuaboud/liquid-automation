package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.Constants;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.util.Timer;
import org.liquidbot.bot.script.randevent.RandomEvent;
import org.liquidbot.bot.utils.Utilities;

/**
 * Created on 8/15/14.
 */
public class BotWorld extends RandomEvent {

	private int lastWorld = -1;

	@Override
	public String getAuthor() {
		return "Hiasat";
	}

	@Override
	public boolean active() {
		if (Utilities.inArray(Game.getCurrentWorld(), Constants.WORLDS) && Game.isLoggedIn()
				&& lastWorld != Game.getCurrentWorld()) {
			lastWorld = Game.getCurrentWorld();
		}
		if (Players.getLocal().isInCombat()) {
			return false;
		}
		return !Utilities.inArray(Game.getCurrentWorld(), Constants.WORLDS) && Game.isLoggedIn();
	}

	@Override
	public String getName() {
		return "BotWorld Handler";
	}

	@Override
	public void solve() {
		log.info("We are in Bot World: " + Game.getCurrentWorld());
		Timer t = new Timer(Random.nextInt(30, 50) * 1000);
		setStatus("Trying to Logout: " + Time.parse(t.getRemaining()));
		while (t.isRunning()) {
			if (Game.isLoggedIn() && !Players.getLocal().isInCombat()) {
				Game.logout();
			}
			setStatus("Trying to Logout: " + Time.parse(t.getRemaining()));
			Time.sleep(200, 250);
			if (!Game.isLoggedIn()) {
				break;
			}
		}
		if (Game.isLoggedIn()) {
			System.exit(0);
		} else {
			Timer restTimer = new Timer(Random.nextInt(45, 60) * 6 * 1000);
			while (restTimer.isRunning()) {
				setStatus("Idling for: " + Time.parse(restTimer.getRemaining()));
				Time.sleep(400, 700);
			}
		}

	}

	@Override
	public void reset() {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
