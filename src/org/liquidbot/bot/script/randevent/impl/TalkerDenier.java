package org.liquidbot.bot.script.randevent.impl;

import java.awt.Point;

import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.Menu;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.api.wrappers.Path;
import org.liquidbot.bot.script.api.wrappers.Player;
import org.liquidbot.bot.script.api.wrappers.Tile;
import org.liquidbot.bot.script.api.wrappers.TilePath;
import org.liquidbot.bot.script.api.wrappers.Widget;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;
import org.liquidbot.bot.script.randevent.RandomEvent;
import org.liquidbot.bot.utils.Utilities;

/*
 * Created by Hiasat on 8/7/14
 */
public class TalkerDenier extends RandomEvent {


	public final static String[] RANDOM_NAMES = new String[] {
		"Mysterious Old Man", "Genie", "Pillory Guard",
		"Postie Pete", "Evil Bob", "Dunce", "Bee Keeper",
		"Rick Turpentine", "Leo", "Capt' Arnav", "Quiz Master"
	};
	private NPC npc;

	private static WidgetChild widget;
	private static String[] contStrs = new String[] {
			"click to continue",
			"click here to continue"
	};
	private static String s;
	private static boolean done;
	
	public NPC getRandom() {
		return NPCs.getNearest(new Filter<NPC> () {

			@Override
			public boolean accept(NPC n) {
				if (n == null || !n.isValid() || n.getName() == null)
					return false;
				
				String s = n.getName();
				if (s == null)
					return false;
				
				try {
					Player p = (Player) n.getInteracting();
					String myName = Players.getLocal().getName();
					
					for (String name : RANDOM_NAMES)
						if (name != null
							&& name.equalsIgnoreCase(s)
							&& p.getName() != null
							&& p.getName().equalsIgnoreCase(myName))
							return true;
				} catch (ClassCastException e) {
					
				}
				return false;
			}
		});
	}
	
	@Override
	public boolean active() {
		return getRandom() != null && getRandom().isValid();
	}

	@Override
	public void solve() {
		NPC npc = getRandom();
		for (int i = 0; i < 100 && (canContinue() || (getRandom() != null && getRandom().isValid())); i++) {
			if (canContinue()) {
				log.info("Continuing..."+i);
				Time.sleep(500, 2000);
				clickContinue();
			} else if (getDeclineWidget().isVisible()) {
				log.info("Declining..."+i);
				getDeclineWidget().click();
				Time.sleep(500, 2000);
			} else {
				log.info("Talking to "+npc.getName()+"..."+i);
				npc.interact("Talk-to");
				for (int a = 0; a < 30 && !canContinue(); a++)
					Time.sleep(100, 200);
			}
		}
	}

	private WidgetChild getDeclineWidget() {
		return Widgets.get(228, 2);
	}

	public static boolean canContinue() {
		widget = null;
		done = false;
		for (Widget w : Widgets.get()) {
			if (done) break;
			for (WidgetChild c : w.getChildren()) {
				if (done) break;
				try {
					if (c.isVisible() && c.getText() != null) {
						s = c.getText().toLowerCase();
						if (s.isEmpty())
							continue;
						
						for (String a : contStrs) {
							if (s.contains(a)) {
								widget = c;
								done = true;
								break;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return widget != null;
	}
	
	public static void clickContinue() {
		if (canContinue() && widget != null) {
			if (!Menu.contains("Continue"))
				Mouse.move(new Point(widget.getX() + 2 + Random.nextInt(0, widget.getWidth() - 4),
						widget.getY() + 2 + Random.nextInt(0, widget.getHeight() - 4)));
			Mouse.click(true);
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Talker/Decliner";
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "Lemons";
	}

	@Override
	public void reset() {
		npc = null;
	}

}
