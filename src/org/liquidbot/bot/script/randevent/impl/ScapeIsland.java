package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.interfaces.Condition;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.randevent.RandomEvent;

/**
 * Created by Hiasat on 8/10/14.
 */
public class ScapeIsland extends RandomEvent {


	private final int WIDGET_STATE = 186;

	private String currentDirection;

	@Override
	public String getName() {
		return "ScapeIsland";
	}

	@Override
	public String getAuthor() {
		return "Hiasat";
	}

	@Override
	public boolean active() {
		NPC servant = NPCs.getNearest("Servant");
		NPC evilBob = NPCs.getNearest("Evil Bob");
		return servant.isValid() && servant.distanceTo() < 25 && evilBob.isValid() && evilBob.distanceTo() < 25;
	}

	@Override
	public void solve() {
		final NPC servant = NPCs.getNearest("Servant");
		if (currentDirection != null) {

		} else {
			if (isWatching()) {
				int[] angels = {90, 180, 270, 360};
				String[] directions = {"E", "S", "W", "N"};
				for (int index = 0; index < angels.length; index++) {
					if (angels[index] == Camera.getAngle()) {
						currentDirection = directions[index];
						break;
					}
				}
			} else if (Widgets.canContinue()) {
				setStatus("Continuing Widgets");
				Widgets.clickContinue();
			} else if (servant.isValid()) {
				setStatus("Interacting with Servant");
				if (servant.isOnScreen()) {
					servant.interact("Talk-to");
					Time.sleep(new Condition() {
						@Override
						public boolean active() {
							return !Widgets.canContinue();
						}
					}, 3000);
				} else {
					Walking.walkTo(servant);
					Camera.turnTo(servant);
					Time.sleep(new Condition() {
						@Override
						public boolean active() {
							return !servant.isOnScreen();
						}
					}, 3000);
				}
			}
		}
	}

	@Override
	public void reset() {
		currentDirection = null;
	}

	private boolean isWatching() {
		return Widgets.get(WIDGET_STATE).isValid();
	}

}
