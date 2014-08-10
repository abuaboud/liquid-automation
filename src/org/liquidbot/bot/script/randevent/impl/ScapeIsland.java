package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.interfaces.Condition;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.Inventory;
import org.liquidbot.bot.script.api.methods.data.Menu;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.methods.interactive.GameEntities;
import org.liquidbot.bot.script.api.methods.interactive.GroundItems;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.GameObject;
import org.liquidbot.bot.script.api.wrappers.GroundItem;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.api.wrappers.Tile;
import org.liquidbot.bot.script.randevent.RandomEvent;

/**
 * Created by Hiasat on 8/10/14.
 */
public class ScapeIsland extends RandomEvent {

	private final String UNCOOKING_POT = "Uncooking Pot";
	private final String COOKED_FISH = "Fishlike thing";
	private final String UNCOOKED_FISH = "Raw Fishlike thing";
	private final String FISHING_SPOT = "Fishing spot";
	private final String NET = "Small fishing net";
	private final int WIDGET_STATE = 186;
	private final int[] ANGLES = {90, 180, 270, 360};
	private final String[] DIRECTIONS = {"E", "S", "W", "N"};
	private final Tile[] REGIONS_TILE = {new Tile(75, 50), new Tile(60, 40), new Tile(50, 50), new Tile(60, 60)};

	private String currentDirection;
	private int directionIndex;

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

		if (Inventory.contains(COOKED_FISH)) {
			final GameObject unCookingPot = GameEntities.getNearest(UNCOOKING_POT);
			if (unCookingPot.isValid()) {
				if (unCookingPot.isOnScreen()) {
					Inventory.getItem(COOKED_FISH).interact("Use");
					Mouse.move(unCookingPot.getInteractPoint());
					System.out.println(Menu.contains("Use"));
					if (Menu.contains("Use")) {
						unCookingPot.interact("Use", unCookingPot.getName());
					}
					Time.sleep(new Condition() {
						@Override
						public boolean active() {
							return !Inventory.contains(UNCOOKED_FISH);
						}
					}, 3000);
				} else {
					Walking.walkTo(unCookingPot);
					Camera.turnTo(unCookingPot);
					Time.sleep(new Condition() {
						@Override
						public boolean active() {
							return !unCookingPot.isOnScreen();
						}
					}, 3000);
				}
			}
		} else if (currentDirection != null) {
			if (!Inventory.contains(NET)) {
				setStatus("Taking up Net to fish");
				final GroundItem net = GroundItems.getNearest(getDirectionTile(REGIONS_TILE[directionIndex]), new Filter<GroundItem>() {
					@Override
					public boolean accept(GroundItem groundItem) {
						return groundItem.isValid() && groundItem.getName() != null && groundItem.getName().equalsIgnoreCase(NET);
					}
				});
				if (net.isValid()) {
					if (net.isOnScreen()) {
						net.interact("Take", net.getName());
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return !Inventory.contains(NET);
							}
						}, 3000);
					} else {
						Walking.walkTo(net);
						Camera.turnTo(net);
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return !net.isOnScreen();
							}
						}, 3000);
					}
				}
			} else {
				setStatus("Interacting with fishing spot");
				final GameObject fishingSpot = GameEntities.getNearest(getDirectionTile(REGIONS_TILE[directionIndex]), new Filter<GameObject>() {
					@Override
					public boolean accept(GameObject gameObject) {
						return gameObject.isValid() && gameObject.getName() != null && gameObject.getName().equalsIgnoreCase(FISHING_SPOT);
					}
				});
				if (fishingSpot.isValid()) {
					if (fishingSpot.isOnScreen()) {
						fishingSpot.interact("Net", fishingSpot.getName());
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return !Inventory.contains(UNCOOKED_FISH);
							}
						}, 3000);
					} else {
						Walking.walkTo(fishingSpot);
						Camera.turnTo(fishingSpot);
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return !fishingSpot.isOnScreen();
							}
						}, 3000);
					}
				}
			}
		} else {
			if (isWatching()) {
				setStatus("Finding Fishing Location");
				for (int index = 0; index < ANGLES.length; index++) {
					if (ANGLES[index] == Camera.getAngle()) {
						currentDirection = DIRECTIONS[index];
						log.info("Direction Found: " + currentDirection);
						directionIndex = index;
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

	private Tile getDirectionTile(Tile regionTile) {
		return new Tile(Game.getBaseX() + regionTile.getX(), Game.getBaseY() + regionTile.getY(), Game.getPlane());
	}

	@Override
	public void reset() {
		currentDirection = null;
	}

	private boolean isWatching() {
		return Widgets.get(WIDGET_STATE).isValid();
	}

}
