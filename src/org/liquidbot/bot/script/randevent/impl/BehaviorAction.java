package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.enums.Tab;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.Menu;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.util.Timer;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.api.wrappers.Player;
import org.liquidbot.bot.script.randevent.RandomEvent;
import org.liquidbot.bot.utils.Utilities;

/**
 * Created by Hiasat on 8/19/14.
 */
public class BehaviorAction extends RandomEvent {

	private Timer timer = new Timer(60 * 2 * 1000);

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
		return Game.isLoggedIn() && !timer.isRunning();
	}

	@Override
	public void solve() {
		switch (Random.nextInt(0, 30)) {
			case 5:
				Camera.setAngle(Random.nextInt(0, 360));
				break;
			case 6:
				Tab.values()[Random.nextInt(0, Tab.values().length)].open();
				if (Random.nextInt(0, 10) > 5)
					Tab.INVENTORY.open();
				break;
			case 10:
				Camera.setPitch(Random.nextInt(0, 90));
				break;
			case 11:
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
			case 16:
				Player[] examinePlayers = Players.getAll(new Filter<Player>() {
					@Override
					public boolean accept(Player player) {
						return player.isValid() && player.isOnScreen();
					}
				});
				if (examinePlayers.length > 0) {
					Player examinePlayer = examinePlayers[Random.nextInt(0, examinePlayers.length)];
					if (examinePlayer.isValid()) {
						examinePlayer.interact("Examine", examinePlayer.getName());
					}
				}
				break;
		}
		timer =  new Timer(Random.nextInt(45,60) * Random.nextInt(1,4) * 1000);

	}

	@Override
	public void reset() {

	}
}
