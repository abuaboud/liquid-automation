package org.liquidbot.bot.script.randevent.impl;


import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.methods.data.Bank;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Timer;
import org.liquidbot.bot.script.randevent.RandomEvent;
import org.liquidbot.bot.script.randevent.RandomEventHandler;

import java.util.ArrayList;


/**
 * Created by Hiasat on 8/3/14
 */
public class SmartBreak extends RandomEvent {


	public Timer timer = new Timer(Random.nextInt(40, 60) * 3 * 60 * 1000);

	public Timer breakTimer = new Timer(0);

	@Override
	public String getAuthor() {
		return "Magorium";
	}

	@Override
	public boolean active() {
		return !timer.isRunning() && (!Game.isLoggedIn() || (!Players.getLocal().isInCombat() && !Bank.isOpen()));
	}

	@Override
	public String getName() {
		return "Smart Break";
	}

	@Override
	public void solve() {
		if (!timer.isRunning() && (!Game.isLoggedIn() || (!Players.getLocal().isInCombat() && !Bank.isOpen()))) {
			if (Game.isLoggedIn()) {
				Game.logout();
				breakTimer = new Timer(Random.nextInt(20, 30) * 60 * 1000);
			}
			if (!breakTimer.isRunning()) {
				RandomEventHandler.enableRandom(RandomEventHandler.loginHandler, true);
				timer = new Timer(Random.nextInt(40, 60) * 3 * 60 * 1000);
			}
		}
	}


	@Override
	public void reset() {

	}


}
