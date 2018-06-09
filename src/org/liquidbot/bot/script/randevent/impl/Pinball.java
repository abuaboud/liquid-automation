package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.Settings;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.methods.interactive.GameEntities;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.GameObject;
import org.liquidbot.bot.script.api.wrappers.Tile;
import org.liquidbot.bot.script.randevent.RandomEvent;

/**
 * Created on 8/15/14.
 */
public class Pinball extends RandomEvent {

	private static final Tile[] TILES = new Tile[]{new Tile(47, 54), new Tile(49, 57), new Tile(52, 58), new Tile(55, 57, 0), new Tile(57, 54)};
	private final int WIDGET_SCORE = 263;

	@Override
	public String getName() {
		return "Pinball";
	}

	@Override
	public String getAuthor() {
		return "Hiasat";
	}

	@Override
	public boolean active() {
		return Game.isLoggedIn() && NPCs.getNearest("Flippa").isValid();
	}

	@Override
	public void solve() {
		if (Widgets.canContinue()) {
			setStatus("Clicking continue.");
			Widgets.clickContinue();
		} else if (getScore() == 10) {
			setStatus("[Step]: Exiting");
			GameObject exit = GameEntities.getNearest("Cave Exit");
			if (exit.isValid()) {
				Camera.setPitch(Random.nextInt(0, 5));
				Camera.turnAngleTo(exit);
				if (exit.distanceTo() > 6) {
					exit.interact("Exit");
					for (int i = 0; i < 70 && NPCs.getNearest("Flippa").isValid(); i++, Time.sleep(100, 150)) ;
				} else {
					Camera.setPitch(Random.nextInt(0, 5));
					Camera.turnAngleTo(exit);
					for (int i = 0; i < 50 && Players.getLocal().isMoving(); i++) {
						Time.sleep(100, 150);
					}
				}
			}
		} else {
			setStatus("Tagging Pinball");
			GameObject target = post();
			if (target.isValid()) {
				if (target.isOnScreen()) {
					Mouse.move(target.getPointOnScreen());
					if (target.interact("Tag")) {
						for (int i = 0; i < 20 && !Players.getLocal().isMoving(); i++, Time.sleep(100, 150)) ;
						for (int i = 0; i < 20 && Players.getLocal().isMoving(); i++, Time.sleep(100, 150)) ;
						Time.sleep(1300, 1800);
					}
				} else {
					Camera.turnTo(target);
				}
			}
		}
	}

	private GameObject post() {
		final int index = Settings.get(727) >> 1 & 0xf;
		final Tile tile = new Tile(Game.getBaseX() + TILES[index].getX(),Game.getBaseY() + TILES[index].getY());
		return GameEntities.getNearest(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject gameObject) {
				return gameObject.isValid() && gameObject.getName() != null && gameObject.getName().equalsIgnoreCase("Pinball post") && gameObject.getLocation().equals(tile);
			}
		});
	}

	private int getScore() {
		if (!Widgets.get(WIDGET_SCORE).isValid())
			return 0;
		return Integer.parseInt(Widgets.get(WIDGET_SCORE, 1).getText().replace("Score: ", ""));
	}

	@Override
	public void reset() {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
