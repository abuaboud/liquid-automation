package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.Constants;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.WorldHopper;
import org.liquidbot.bot.script.api.methods.input.Keyboard;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.randevent.RandomEvent;
import org.liquidbot.bot.ui.account.Account;
import org.liquidbot.bot.utils.Utilities;

import java.awt.*;

/*
 * Created by Hiasat on 8/3/14
 */
public class Login extends RandomEvent {

	private boolean clicked = false;

	private final Rectangle USER_EXIST = new Rectangle(398, 278, 129, 12);
	private final Rectangle USER_CANCEL = new Rectangle(397, 307, 135, 30);

	private String name = "Login";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getAuthor() {
		return "Hiasat";
	}

	@Override
	public boolean active() {
		return Game.getGameState() == Game.STATE_LOG_IN_SCREEN;
	}

	@Override
	public void solve() {
		Account account = Configuration.getInstance().getScriptHandler().getAccount();
		if (!Utilities.inArray(Game.getCurrentWorld(), Constants.WORLDS) || (WorldHopper.getSelectedWorld() > 0 && WorldHopper.getSelectedWorld() != Game.getCurrentWorld())) {
			if (!Utilities.inArray(Game.getCurrentWorld(), Constants.WORLDS)) {
				log.info("We are in Bot World");
				WorldHopper.setWorld(Constants.WORLDS[Random.nextInt(0, Constants.WORLDS.length)]);
			}

			WorldHopper.openWorldsMenu();
			WorldHopper.clickOnWorld(WorldHopper.getSelectedWorld());
			for (int i = 0; i < 30 && Game.getCurrentWorld() != WorldHopper.getSelectedWorld(); i++) {
				Time.sleep(80, 120);
			}

			if (Game.getCurrentWorld() == WorldHopper.getSelectedWorld()) {
				WorldHopper.setWorld(0);
			}
		} else if (account == null) {
			name = "Login (No Info)";
		} else if (!clicked) {
			setStatus("Clicking Cancel");
			Mouse.click(new Point(USER_CANCEL.x + Random.nextInt(0, USER_CANCEL.width), USER_CANCEL.y + Random.nextInt(0, USER_CANCEL.height)), true);

			setStatus("Clicking Existing User");
			Mouse.click(new Point(USER_EXIST.x + Random.nextInt(0, USER_EXIST.width), USER_EXIST.y + Random.nextInt(0, USER_EXIST.height)), true);
			Time.sleep(2000, 4000);
			clicked = true;
		} else {
			setStatus("Entering Username.");
			Keyboard.sendText(account.getEmail(), true, Random.nextInt(75, 95), Random.nextInt(110, 125));
			setStatus("Entering Password.");
			Keyboard.sendText(account.getPassword(), true, Random.nextInt(75, 95), Random.nextInt(110, 125));
			for (int i = 0; i < 25 && (Game.getGameState() == Game.STATE_LOG_IN_SCREEN); i++, Time.sleep(100, 150)) ;
			clicked = false;
		}
	}

	@Override
	public void reset() {
		name = "Login";
		clicked = false;
	}
}
