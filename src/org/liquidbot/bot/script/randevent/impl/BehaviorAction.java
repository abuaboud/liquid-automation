package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.client.patterns.Pattern;
import org.liquidbot.bot.script.api.enums.Tab;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.Menu;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.methods.interactive.GameEntities;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.util.Timer;
import org.liquidbot.bot.script.api.wrappers.GameObject;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.api.wrappers.Player;
import org.liquidbot.bot.script.randevent.RandomEvent;
import org.liquidbot.bot.utils.Utilities;

/**
 * Created by Hiasat on 8/19/14.
 */
public class BehaviorAction extends RandomEvent {

	private Timer timer = new Timer(60 * 2 * 1000);

	private final Pattern pattern = Configuration.getInstance().pattern();

	@Override
	public String getName() {
		return "Random Behavior";
	}

	@Override
	public String getAuthor() {
		return "Hiasat";
	}

	@Override
	public boolean active() {
		return false; /*Game.isLoggedIn() && !timer.isRunning();*/
	}

	@Override
	public void solve() {
		switch (Random.nextInt(0, 30)) {
			case 5:
				Camera.setAngle(Random.nextInt(0, 360));
				break;
			case 6:
				String tab = pattern.find("TABS_");
				for (Tab a : Tab.values()) {
					if (tab != null && tab.toLowerCase().contains(a.getName().toLowerCase())) {
						a.open();
						Time.sleep(200, 300);
					}
				}
				break;
			case 10:
				if (pattern.contains("CHANGE_PITCH_YES"))
					Camera.setPitch(Random.nextInt(0, 90));
				break;
			case 11:
				if (!pattern.contains("HOVER_PLAYERS_YES"))
					break;
				Player[] players = Players.getAll(new Filter<Player>() {
					@Override
					public boolean accept(Player player) {
						return player.isValid() && player.isOnScreen();
					}
				});
				if (players.length > 0) {
					Player player = players[Random.nextInt(0, players.length)];
					if (player.isValid()) {
						setStatus("Moving mouse over Player.");
						Mouse.move(player.getPointOnScreen());
						Time.sleep(600, 1200);

						if (Random.nextInt(0, 10) == 5) {
							setStatus("Right clicking Player.");
							Mouse.click(false);
							Time.sleep(200, 1000);

							if (Menu.isOpen()) {
								Menu.interact("Cancel");
								Time.sleep(60, 200);
							}
						}
					}
				}
				break;
			case 13:
				if (!pattern.contains("HOVER_NPCS_YES"))
					break;
				NPC[] npcs = NPCs.getAll(new Filter<NPC>() {
					@Override
					public boolean accept(NPC npc) {
						return npc.isValid() && npc.isOnScreen();
					}
				});
				if (npcs.length > 0) {
					NPC npc = npcs[Random.nextInt(0, npcs.length)];
					if (npc.isValid()) {
						setStatus("Moving mouse over NPC.");
						Mouse.move(npc.getPointOnScreen());
						Time.sleep(600, 3000);
						if (Random.nextInt(0, 10) == 5) {
							setStatus("Right clicking NPC.");
							Mouse.click(false);
							Time.sleep(200, 1000);
							if (Menu.isOpen()) {
								Menu.interact("Cancel");
								Time.sleep(60, 200);
							}
						}
					}
				}
				break;
			case 15:
				if (!pattern.contains("EXAMINE_NPCS_YES"))
					break;
				NPC[] examineNpcs = NPCs.getAll(new Filter<NPC>() {
					@Override
					public boolean accept(NPC npc) {
						return npc.isValid() && npc.isOnScreen() && npc.getActions() != null && Utilities.inArray("Examine", npc.getActions());
					}
				});
				if (examineNpcs.length > 0) {
					NPC examnieNPC = examineNpcs[Random.nextInt(0, examineNpcs.length)];
					if (examnieNPC.isValid()) {
						examnieNPC.interact("Examine", examnieNPC.getName());
					}
				}
				break;
			case 17:
				if (!pattern.contains("EXAMINE_OBJECTS_YES"))
					break;
				GameObject[] examineObjects = GameEntities.getAll(new Filter<GameObject>() {
					@Override
					public boolean accept(GameObject gameObject) {
						return gameObject.isValid() && gameObject.isOnScreen() && gameObject.getActions() != null && Utilities.inArray("Examine", gameObject.getActions());
					}
				});
				if (examineObjects.length > 0) {
					GameObject examineObject = examineObjects[Random.nextInt(0, examineObjects.length)];
					if (examineObject.isValid()) {
						examineObject.interact("Examine", examineObject.getName());
					}
				}
				break;
			case 18:
				if (!pattern.contains("HOVER_OBJECTS_YES"))
					break;
				GameObject hoverObject = GameEntities.getNext(new Filter<GameObject>() {
					@Override
					public boolean accept(GameObject gameObject) {
						return gameObject.isValid() && gameObject.isOnScreen() && gameObject.getActions() != null && Utilities.inArray("Examine", gameObject.getActions());
					}
				});
				if (hoverObject.isValid()) {
					setStatus("Moving mouse over NPC.");
					Mouse.move(hoverObject.getPointOnScreen());
					Time.sleep(600, 3000);
					if (Random.nextInt(0, 10) == 5) {
						setStatus("Right clicking NPC.");
						Mouse.click(false);
						Time.sleep(200, 1000);
						if (Menu.isOpen()) {
							Menu.interact("Cancel");
							Time.sleep(60, 200);
						}
					}
				}
				break;
			case 20:
				if (!pattern.contains("CHECKING_XP_YES"))
					break;
				if (Tab.SKILLS.open()) {
					setStatus("Opening Skills Tab.");
					Time.sleep(60, 200);

					int randomInt = Random.nextInt(1, 23);
					final int WIDGET_ID = 320;
					if (Widgets.get(WIDGET_ID) != null && Widgets.get(WIDGET_ID).getChild(randomInt) != null) {
						setStatus("Moving mouse over random skill.");
						Mouse.move(Widgets.get(WIDGET_ID).getChild(randomInt).getInteractPoint());
						Time.sleep(600, 2500);
					}

					Time.sleep(100, 200);
				}
				break;
			default:
				break;
		}
		timer = new Timer(Random.nextInt(45, 60) * Random.nextInt(1, 4) * 1000);

	}

	@Override
	public void reset() {

	}
}
