package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;
import org.liquidbot.bot.script.randevent.RandomEvent;
import org.liquidbot.bot.utils.Utilities;

/**
 * Created on 8/10/14.
 */
public class Certer extends RandomEvent {

	private final int WIDGET = 184;
	private final int WIDGET_ITEM = 7;
	private final int[] MODEL_IDS = {2807, 8828, 8829, 8832, 8833, 8834, 8835, 8836, 8837};
	private final String[] ITEM_NAMES = {"bowl", "battleaxe", "fish", "shield", "helmet", "ring", "shears", "sword", "spade"};
	private final String[] NPC_NAMES = {"Niles", "Miles", "Giles"};

	private NPC carter;

	private final Filter<NPC> NPC_FILTER = new Filter<NPC>() {
		@Override
		public boolean accept(NPC npc) {
			if (npc.isValid() && npc.getActions() != null && npc.distanceTo() < 6) {
				final String message = npc.getSpokenMessage();
				if (Utilities.inArray(npc.getName(), NPC_NAMES)) {
					if ((message != null && message.toLowerCase().contains(Players.getLocal().getName().toLowerCase()))
							|| (npc.getInteracting().isValid() && npc.getInteracting().equals(Players.getLocal()))) {
						return true;
					}
				}
			}
			return false;
		}
	};

	@Override
	public String getAuthor() {
		return "Hiasat";
	}

	@Override
	public boolean active() {
		if (!Game.isLoggedIn())
			return false;
		carter = NPCs.getNearest(NPC_FILTER);
		return Widgets.get(WIDGET).isValid() || carter.isValid();
	}

	@Override
	public String getName() {
		return "Carter";
	}


	@Override
	public void solve() {
		if (Camera.getPitch() < 70) {
			Camera.setPitch(Random.nextInt(70, 90));
		}
		if (!carter.isValid())
			return;
		if (Widgets.get(WIDGET).isValid() && Widgets.get(WIDGET, WIDGET_ITEM).isVisible()) {
			String answer = getAnswer(Widgets.get(WIDGET, WIDGET_ITEM).getModelId());
			for (int i = 1; i < 4; i++) {
				WidgetChild widgetChild = Widgets.get(WIDGET, i);
				if (widgetChild.isVisible()) {
					if (widgetChild.getText().toLowerCase().contains(answer.toLowerCase())) {
						setStatus("Selecting the answer.");
						WidgetChild child = Widgets.get(WIDGET, i + 7);
						child.click(true);
						for (int x = 0; x < 20 && Widgets.get(WIDGET, i + 7).isVisible(); x++, Time.sleep(100, 150)) ;
					}
				}
			}
		} else if (Widgets.canContinue()) {
			Widgets.clickContinue();
		} else {
			setStatus("Talking to carter.");
			carter.interact("Talk-to");
			for (int i = 0; i < 20 && !Widgets.canContinue(); i++, Time.sleep(100, 150)) ;
		}
	}

	@Override
	public void reset() {
		carter = null;
	}


	private String getAnswer(int modelId) {
		for (int i = 0; i < MODEL_IDS.length; i++) {
			if (MODEL_IDS[i] == modelId) {
				return ITEM_NAMES[i];
			}
		}

		setStatus("[Error] Can't Define: " + modelId);
		return null;
	}
}
