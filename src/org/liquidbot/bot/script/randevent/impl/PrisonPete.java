package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.interfaces.Condition;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.Inventory;
import org.liquidbot.bot.script.api.methods.data.Settings;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.methods.interactive.GameEntities;
import org.liquidbot.bot.script.api.methods.interactive.GroundItems;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.*;
import org.liquidbot.bot.script.randevent.RandomEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Hiasat on 8/11/14.
 */
public class PrisonPete extends RandomEvent {


	private final String PRISON_PETE = "Prison pete";
	private final String LEVER = "Lever";

	private final int SETTING_BALLOONS_POPPED = 638;
	private final int WIDGET_BALLOON = 273;
	private final int WIDGET_BALLOON_MODEL_ID = 3;
	private final int WIdGET_BALLOON_CLOSE = 6;
	private final int WIDGET_WARNING = 566;
	private final int WIDGET_NO = 18;

	private final Tile EXIT_REGION_TILE = new Tile(72, 50);

	private Balloon currentBalloon;

	private ArrayList<Integer> droppedItemIds = new ArrayList<>();

	@Override
	public String getName() {
		return "Prison Pete";
	}

	@Override
	public String getAuthor() {
		return "Hiasat";
	}

	@Override
	public boolean active() {
		return Game.isLoggedIn() && NPCs.getNearest(PRISON_PETE).isValid() && NPCs.getNearest("Balloon Animal").isValid();
	}

	@Override
	public void solve() {
		if (Camera.getPitch() < 70) {
			Camera.setPitch(Random.nextInt(70, 90));
		} else if (Settings.get(SETTING_BALLOONS_POPPED) == 96) {
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
					Walking.walkTo(new Tile(Game.getBaseX() + EXIT_REGION_TILE.getX(), Game.getBaseY() + EXIT_REGION_TILE.getY()));
					Time.sleep(new Condition() {
						@Override
						public boolean active() {
							return NPCs.getNearest(PRISON_PETE).isValid();
						}
					}, 3000);
				}
			}
		} else if (Widgets.canContinue()) {
			setStatus("Continuing Widgets");
			Widgets.clickContinue();
			currentBalloon = null;
		} else if (Inventory.contains("Prison Key")) {
			Inventory.getItem("Prison Key").interact("Return");
			Time.sleep(new Condition() {
				@Override
				public boolean active() {
					return Inventory.contains("Prison Key");
				}
			}, 3000);
			currentBalloon = null;
		} else if (currentBalloon == null) {
			final WidgetChild balloon = Widgets.get(WIDGET_BALLOON, WIDGET_BALLOON_MODEL_ID);
			if (balloon.isVisible()) {
				setStatus("Finding Balloon to Pop");
				currentBalloon = deterBalloon(balloon.getModelId());
				Widgets.get(WIDGET_BALLOON, WIdGET_BALLOON_CLOSE).click();
				Time.sleep(new Condition() {
					@Override
					public boolean active() {
						return !Widgets.canContinue();
					}
				}, 3000);
				if (Inventory.contains("Prison Key")) {
					currentBalloon = null;
				}
			} else {
				final GameObject leaver = GameEntities.getNearest(LEVER);
				setStatus("Operating Leaver");
				if (leaver.isValid()) {
					if (leaver.isOnScreen()) {
						leaver.turnTo();
						Mouse.move(leaver.getPointOnScreen());
						leaver.interact("Pull");
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return !balloon.isVisible();
							}
						}, 3000);
					} else {
						Walking.walkTo(leaver);
						leaver.turnTo();
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return !leaver.isOnScreen();
							}
						}, 3000);
					}
				}
			}
		} else {
			final NPC balloon = NPCs.getNearest(new Filter<NPC>() {
				@Override
				public boolean accept(NPC npc) {
					return npc.isValid() && npc.getName() != null && npc.getName().equalsIgnoreCase("Balloon Animal") && npc.getModelIds() != null && Arrays.equals(npc.getModelIds(), currentBalloon.model_ids);
				}
			});
			if (balloon.isValid()) {
				if (Inventory.isFull()) {
					setStatus("Dropping lowest value item.");
					final Item lowestItem = getLowestItem();
					lowestItem.interact("Drop");
					Time.sleep(new Condition() {
						@Override
						public boolean active() {
							return Inventory.contains(lowestItem.getId());
						}
					}, 3000);
					if (!Inventory.contains(lowestItem.getId()))
						droppedItemIds.add(lowestItem.getId());
				} else if (balloon.isOnScreen()) {
					setStatus("Popping Balloon.");
					Mouse.move(balloon.getPointOnScreen());
					balloon.interact("Pop");
					Time.sleep(new Condition() {
						@Override
						public boolean active() {
							return !Widgets.canContinue();
						}
					}, 3000);
				} else {
					setStatus("Walking to Balloon.");
					Walking.walkTo(balloon);
					balloon.turnTo();
					Time.sleep(new Condition() {
						@Override
						public boolean active() {
							return !balloon.isOnScreen();
						}
					}, 3000);
				}
			}
		}
	}

	private Item getLowestItem() {
		ArrayList<Integer> countList = new ArrayList<Integer>();
		for (Item item : Inventory.getAllItems()) {
			if (item != null && item.getId() != 995) {
				countList.add(Inventory.getCount(item.getId()));
			}
		}
		return Inventory.getAllItems()[countList.indexOf(Collections.min(countList))];
	}


	private Balloon deterBalloon(final int modelId) {
		for (Balloon balloon : Balloon.values()) {
			if (balloon.model_id == modelId) {
				return balloon;
			}
		}
		log.error("Can't Find Balloon , Guessing it");
		return Balloon.SKINNY_BOBBED_TAIL;
	}

	@Override
	public void reset() {
		currentBalloon = null;
		droppedItemIds.clear();
	}

	private enum Balloon {

		SKINNY_STRAIGHT_TAIL(10750, 10736),
		FAT_NO_HORNS(10751, 10737),
		SKINNY_BOBBED_TAIL(11028, 16034),
		FAT_WITH_HORNS(11034, 27098);

		private int model_id;
		private int[] model_ids;

		private Balloon(int model_id, int... model_ids) {
			this.model_id = model_id;
			this.model_ids = model_ids;
		}

		@Override
		public String toString() {
			return name().substring(0, 1) + name().substring(1).toLowerCase();
		}

	}
}
