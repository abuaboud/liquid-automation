package org.liquidbot.bot.script.randevent.impl;


import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.interfaces.PaintListener;
import org.liquidbot.bot.script.api.methods.data.Bank;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Timer;
import org.liquidbot.bot.script.randevent.RandomEvent;
import org.liquidbot.bot.script.randevent.RandomEventHandler;

import java.awt.*;
import java.util.ArrayList;


/**
 * Created by Hiasat on 8/3/14
 */
public class SmartBreak extends RandomEvent implements PaintListener{


    public ArrayList<Integer> breaks = new ArrayList<Integer>();
    public ArrayList<Integer> amounts = new ArrayList<Integer>();

    public Timer timer = new Timer(60 * 25 * 1000);

    public Timer restTimer = new Timer(0);

    public int breakPerHour = Random.nextInt(2, 4);

	@Override
    public void reset(){
        breaks.clear();
        amounts.clear();
    }

    @Override
    public String getAuthor() {
        return "Magorium";
    }

	@Override
	public boolean active() {
		if (amounts.size() == 0) {
			breakPerHour = Random.nextInt(2, 4);
			int[] b = getBreaksTime(breakPerHour);
			int[] a = getBreaksAmount(breakPerHour);
			for (int i = 0; i < b.length; i++) {
				breaks.add(b[i]);
				amounts.add(a[i]);
			}
			timer = new Timer(breaks.get(0));
			breaks.remove(0);
		}
		return (Configuration.getInstance().smartBreak() && !timer.isRunning() && (!Game.isLoggedIn() || (!Players.getLocal().isInCombat() && !Bank.isOpen())));
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
                restTimer = new Timer(amounts.get(0));
                amounts.remove(0);
            }
            if (!restTimer.isRunning()) {
                RandomEventHandler.enableRandom(RandomEventHandler.loginHandler, true);
                timer = new Timer(breaks.get(0));
                breaks.remove(0);
            }
        }
    }

    private static int[] getBreaksTime(int breakPerHour) {
        int hour = 3000000;
        int[] breaks = new int[breakPerHour];
        for (int i = 0; i < breakPerHour; i++) {
            if (i == (breakPerHour - 1)) {
                breaks[i] = hour;
            } else {
                int random = Random.nextInt(10, 25);
                int b = random * 60 * 1000 + Random.nextInt(0, 60) * 1000;
                hour -= b;
                breaks[i] = b;
            }
        }
        if (hour < 0) {
            return getBreaksTime(breakPerHour);
        }
        return breaks;
    }

    private static int[] getBreaksAmount(int breakPerHour) {
        int hour = Random.nextInt(8, 12) * 60 * 1000;
        int[] breaks = new int[breakPerHour];
        for (int i = 0; i < breakPerHour; i++) {
            if (i == (breakPerHour - 1)) {
                breaks[i] = hour;
            } else {
                int b = Random.nextInt(2, 5) * 60 * 1000 + Random.nextInt(0, 60) * 1000;
                hour -= b;
                breaks[i] = b;
            }
        }
        if (hour < 0) {
            return getBreaksAmount(breakPerHour);
        }
        return breaks;
    }

	@Override
	public void render(Graphics2D graphics) {

	}
}
