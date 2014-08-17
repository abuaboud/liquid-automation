package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.randevent.RandomEvent;
import org.liquidbot.bot.utils.Utilities;

/**
 * Created by Hiasat on 8/16/14.
 */
public class StrangePlant extends RandomEvent {

	private final int[] ANIMATIONS = {348, 350};
	private final String PLANT_NAME = "Strange plant";

	@Override
	public String getName() {
		return "Strange Plant";
	}

	@Override
	public String getAuthor() {
		return "Hiasat";
	}

	@Override
	public boolean active() {
		return Game.isLoggedIn() && plant().isValid();
	}

	@Override
	public void solve() {
		NPC plant = plant();
		if(!plant.isValid())
			return;
		setStatus("Picking up fruit");
		if (plant.interact("Pick")) {
			for (int i = 0; i < 100 &&plant().isValid(); i++, Time.sleep(40, 60)) ;
		}
	}

	@Override
	public void reset() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public NPC plant() {
		return NPCs.getNearest(new Filter<NPC>() {
			@Override
			public boolean accept(NPC npc) {
				return npc.isValid() && Utilities.inArray(npc.getAnimation(), ANIMATIONS) && npc.getName().equalsIgnoreCase(PLANT_NAME) && npc.getInteracting().isValid() && npc.getInteracting().equals(Players.getLocal());
			}
		});
	}
}
