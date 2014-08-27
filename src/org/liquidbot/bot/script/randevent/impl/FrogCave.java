package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.interfaces.Condition;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.interactive.GameEntities;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.GameObject;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.api.wrappers.Widget;
import org.liquidbot.bot.script.randevent.RandomEvent;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Hiasat on 8/10/14.
 */
public class FrogCave extends RandomEvent {

	private final int WIDGET = 228;
	private final int[] CHILD_IDS = {1, 2};

	@Override
	public String getName() {
		return "Frog Cave";
	}

	@Override
	public String getAuthor() {
		return "Hiasat";
	}

	@Override
	public boolean active() {
		GameObject gasHole = GameEntities.getNearest("Gas Hole");
		return Game.isLoggedIn() && gasHole.isValid() && gasHole.distanceTo() < 25;
	}

	@Override
	public void solve() {
		final NPC princesFrog = getPrincesFrog();
		if (Widgets.canContinue()) {
			setStatus("Continuing Widgets");
			Widgets.clickContinue();
		} else if (continueSorry()) {
			setStatus("Saying sorry to frog");
			Time.sleep(new Condition() {
				@Override
				public boolean active() {
					return getPrincesFrog().isValid();
				}
			},3000);
		} else if (princesFrog.isValid()) {
			setStatus("Interacting with Frog");
			if (princesFrog.isOnScreen()) {
				princesFrog.interact("Talk-to");
				Time.sleep(new Condition() {
					@Override
					public boolean active() {
						return !Widgets.canContinue();
					}
				}, 3000);
			} else {
				Walking.walkTo(princesFrog);
				Camera.turnTo(princesFrog);
				Time.sleep(new Condition() {
					@Override
					public boolean active() {
						return !princesFrog.isOnScreen();
					}
				}, 3000);
			}
		}
	}

	@Override
	public void reset() {

	}

	private boolean continueSorry() {
		Widget widget = Widgets.get(WIDGET);
		if (widget.isValid()) {
			if (widget.getChild(CHILD_IDS[0]).getText() != null && widget.getChild(CHILD_IDS[0]).getText().toLowerCase().contains("sorry")) {
				return widget.getChild(CHILD_IDS[0]).interact("Continue");
			}
			if (widget.getChild(CHILD_IDS[1]).getText() != null && widget.getChild(CHILD_IDS[1]).getText().toLowerCase().contains("sorry")) {
				return widget.getChild(CHILD_IDS[1]).interact("Continue");
			}
		}
		return false;
	}

	public NPC getPrincesFrog() {
		java.util.List<Integer> ids = new ArrayList<>();
		for (NPC frog : NPCs.getAll("Frog")) {
			if (frog.isValid())
				ids.add(frog.getId());
		}
		return NPCs.getNearest(Collections.min(ids));
	}


}
