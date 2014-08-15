package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.interfaces.Condition;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;
import org.liquidbot.bot.script.randevent.RandomEvent;

/**
 * Created by Hiasat on 8/13/14.
 */
public class SandwichLady extends RandomEvent {

	private final int WIDGET = 297;
	private final int WIDGET_CHILD_TEXT = 8;

	private NPC lady;

	@Override
	public String getName() {
		return "Sandwich lady";
	}

	@Override
	public String getAuthor() {
		return "Hiasat";
	}

	@Override
	public boolean active() {
		if (!Game.isLoggedIn())
			return false;
		lady = NPCs.getNearest(new Filter<NPC>() {
			@Override
			public boolean accept(NPC npc) {
				return npc.isValid() && npc.getName() != null && npc.getName().equalsIgnoreCase("sandwich lady") &&
						((npc.getSpokenMessage() != null && npc.getSpokenMessage().toLowerCase().contains(Players.getLocal().getName().toLowerCase()))
								|| (npc.getInteracting().isValid() && npc.getInteracting().equals(Players.getLocal())));
			}
		});
		return lady.isValid();
	}

	@Override
	public void solve() {
		if(Camera.getPitch() < 70){
			Camera.setPitch(Random.nextInt(70,90));
		}
		WidgetChild sandwichWidget = Widgets.get(WIDGET, WIDGET_CHILD_TEXT);
		if (sandwichWidget.isVisible() && sandwichWidget.getText() != null) {
			setStatus("Clicking on the Sandwich.");
			String whatSandwich = sandwichWidget.getText();
			WidgetChild sandwich = getSandwichComponent(sandwich(whatSandwich).getId());
			if (sandwich != null) {
				WidgetChild widgetChild = getSandwichComponent(sandwich(whatSandwich).getId());
				widgetChild.click(true);
				for (int i = 0; i < 20 && !Widgets.get(WIDGET).isValid(); i++, Time.sleep(100, 150)) ;
			}
		} else if (Widgets.canContinue()) {
			setStatus("Continuing Widgets");
			Widgets.clickContinue();
		} else if (lady.isValid()) {
			setStatus("Interacting with Sandwich lady");
			if (lady.isOnScreen()) {
				lady.interact("Talk-to");
				Time.sleep(new Condition() {
					@Override
					public boolean active() {
						return !Widgets.canContinue();
					}
				}, 3000);
			} else {
				Walking.walkTo(lady);
				Camera.turnTo(lady);
				Time.sleep(new Condition() {
					@Override
					public boolean active() {
						return !lady.isOnScreen();
					}
				}, 3000);
			}
		}

	}

	private enum Sandwich {
		SQUARE(10731, "square"),
		ROLL(10727, "roll"),
		CHOCOLATE(10728, "chocolate"),
		BAGUETTE(10726, "baguette"),
		TRIANGLE(10732, "triangle"),
		KEBAB(10729, "kebab"),
		PIE(10730, "pie");

		private final int modelId;
		private final String name;

		Sandwich(int m, String n) {
			this.modelId = m;
			this.name = n;
		}

		public int getId() {
			return modelId;
		}

		public String getMessage() {
			return name;
		}


	}

	public Sandwich sandwich(String nm) {
		for (Sandwich e : Sandwich.values()) {
			if (nm.toLowerCase().contains(e.getMessage().toLowerCase())) {
				return e;
			}
		}

		return Sandwich.BAGUETTE;
	}

	public WidgetChild getSandwichComponent(int mid) {
		for (int i = 1; i < 8; i++) {
			WidgetChild sandwich = Widgets.get(297, i);

			if (sandwich != null) {
				if (sandwich.getModelId() == mid) {
					return sandwich;
				}
			}
		}

		return null;
	}

	@Override
	public void reset() {
		lady = null;
	}
}
