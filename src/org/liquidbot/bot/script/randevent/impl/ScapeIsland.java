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
import org.liquidbot.bot.script.api.wrappers.*;
import org.liquidbot.bot.script.randevent.RandomEvent;

import java.util.ArrayList;
import java.util.Collections;

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
	private final int WIDGET_WARNING = 566;
	private final int WIDGET_NO = 18;
	private final int[] ANGLES = {90, 180, 270, 360};
	private final String[] DIRECTIONS = {"E", "S", "W", "N"};
	private final Tile[] REGIONS_TILE = {new Tile(75, 50), new Tile(60, 40), new Tile(50, 50), new Tile(60, 60)};

	private String currentDirection;
	private int directionIndex;
	private boolean shouldLeave = false;

	private ArrayList<Integer> droppedItemIds = new ArrayList<>();

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
		if (!Game.isLoggedIn())
			return false;
		NPC servant = NPCs.getNearest("Servant");
		NPC evilBob = NPCs.getNearest("Evil Bob");
		return servant.isValid() && servant.distanceTo() < 40 && evilBob.isValid() && evilBob.distanceTo() < 40;
	}

	@Override
	public void solve() {
		final NPC servant = NPCs.getNearest("Servant");
		if (shouldLeave) {
			setStatus("Leaving the random.");
			final GroundItem item = GroundItems.getNearest(new Filter<GroundItem>() {
				@Override
				public boolean accept(GroundItem groundItem) {
					return groundItem.isValid() && droppedItemIds.contains(groundItem.getId());
				}
			});
			if (item.isValid()) {
				if (item.isValid()) {
					if (item.isOnScreen()) {
						item.interact("Take", item.getName());
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return item.isValid();
							}
						}, 3000);
					} else {
						Walking.walkTo(item);
						Camera.turnTo(item);
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return !item.isOnScreen();
							}
						}, 3000);
					}
				}
			} else {
				final WidgetChild warn = Widgets.get(WIDGET_WARNING, WIDGET_NO);
				if (warn.isVisible()) {
					warn.click();
					Time.sleep(new Condition() {
						@Override
						public boolean active() {
							return warn.isVisible();
						}
					}, 3000);
				} else {
					final GameObject portal = GameEntities.getNearest("Portal");
					if (portal.isValid()) {
						if (portal.isOnScreen()) {
							portal.interact("Enter");
							Time.sleep(new Condition() {
								@Override
								public boolean active() {
									return servant.isValid() || !warn.isVisible();
								}
							}, 3000);
							WidgetChild widgetChild = Widgets.getWidgetWithText("nowhere");
							if (widgetChild !=null && Widgets.getWidgetWithText("nowhere").isVisible()) {
								shouldLeave = false;
							}
						} else {
							Walking.walkTo(portal);
							Camera.turnTo(portal);
							Time.sleep(new Condition() {
								@Override
								public boolean active() {
									return !portal.isOnScreen();
								}
							}, 3000);
						}
					}
				}
			}
		} else if (Inventory.contains(UNCOOKED_FISH)) {
			setStatus("Feeding Evil Bob");
			final NPC evilBob = NPCs.getNearest("Evil Bob");
			if (evilBob.isValid()) {
				if (evilBob.isOnScreen()) {
					Inventory.getItem(UNCOOKED_FISH).interact("Use");
					Mouse.move(evilBob.getInteractPoint());
					Time.sleep(400, 800);
					if (Menu.contains("Use")) {
						evilBob.interact("Use", evilBob.getName());
					}
					Time.sleep(new Condition() {
						@Override
						public boolean active() {
							return Inventory.contains(UNCOOKED_FISH);
						}
					}, 3000);
					if (!Inventory.contains(UNCOOKED_FISH))
						shouldLeave = true;

				} else {
					Walking.walkTo(evilBob);
					Camera.turnTo(evilBob);
					Time.sleep(new Condition() {
						@Override
						public boolean active() {
							return !evilBob.isOnScreen();
						}
					}, 3000);
				}
			}
		} else if (Inventory.contains(COOKED_FISH)) {
			setStatus("Uncooking the Fish");
			final GameObject unCookingPot = GameEntities.getNearest(UNCOOKING_POT);
			if (unCookingPot.isValid()) {
				if (unCookingPot.isOnScreen()) {
					Inventory.getItem(COOKED_FISH).interact("Use");
					Mouse.move(unCookingPot.getInteractPoint());
					Time.sleep(400, 800);
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
			final Item lowestItem = getLowestItem();
			if (Inventory.isFull() && lowestItem.isValid()) {
				lowestItem.interact("Drop");
				Time.sleep(new Condition() {
					@Override
					public boolean active() {
						return Inventory.contains(lowestItem.getId());
					}
				}, 3000);
				if (!Inventory.contains(lowestItem.getId()))
					droppedItemIds.add(lowestItem.getId());
			} else if (!Inventory.contains(NET)) {
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
					if (fishingSpot.isOnScreen() && fishingSpot.distanceTo() < 7) {
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
				WidgetChild fallenSleep = Widgets.get(64, 2);
				if (fallenSleep.isVisible() && fallenSleep.getText().contains("Evil Bob has fallen asleep")) {
					shouldLeave = true;
				}
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

	private Item getLowestItem() {
		ArrayList<Integer> count = new ArrayList<Integer>();
		for (Item item : Inventory.getAllItems()) {
			if (item != null && item.getId() != 995) {
				count.add(Inventory.getCount(item.getId()));
			}
		}
		return Inventory.getAllItems()[count.indexOf(Collections.min(count))];
	}

	@Override
	public void reset() {
		currentDirection = null;
		directionIndex = -1;
		droppedItemIds.clear();
		shouldLeave = false;
	}

	private boolean isWatching() {
		return Widgets.get(WIDGET_STATE).isValid();
	}

}
