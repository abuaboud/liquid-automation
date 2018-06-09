package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.interfaces.Condition;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.Inventory;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.interactive.GameEntities;
import org.liquidbot.bot.script.api.methods.interactive.GroundItems;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.*;
import org.liquidbot.bot.script.randevent.RandomEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created on 8/11/14.
 */
public class FreakyFoster extends RandomEvent {

	private final int WIDGET_CHAT_TWO = 242;
	private final int WIDGET_CHAT = 243;
	private final int WIDGET_CHAT_LEAVE = 241;
	private final int WIDGET_WARNING = 566;
	private final int WIDGET_WARNING_CHILDREN = 18;

	private final String RAW_PHEASANT = "Raw pheasant";
	private final String EXIT_PORTAL = "Exit portal";

	private int pheasantId;
	private boolean leave = false;

	private ArrayList<Integer> droppedIds = new ArrayList<>();


	@Override
	public String getName() {
		return "Freaky Foster";
	}

	@Override
	public String getAuthor() {
		return "Hiasat";
	}

	@Override
	public boolean active() {
		return Game.isLoggedIn() && NPCs.getNearest("Pheasant").isValid();
	}

	@Override
	public void solve() {
		if (leave) {
			WidgetChild warning = Widgets.get(WIDGET_WARNING, WIDGET_WARNING_CHILDREN);
			if (warning.isVisible()) {
				warning.click();
				Time.sleep(new Condition() {
					@Override
					public boolean active() {
						return NPCs.getNearest("Peasant").isValid();
					}
				},3000);
			} else {
				setStatus("Collecting dropped items.");
				final GroundItem droppedItem = GroundItems.getNearest(new Filter<GroundItem>() {
					@Override
					public boolean accept(GroundItem groundItem) {
						return groundItem.isValid() && droppedIds.size() > 0 && droppedIds.contains(groundItem.getId());
					}
				});
				if (Inventory.contains(RAW_PHEASANT)) {
					Item[] meats = Inventory.getAllItems(new Filter<Item>() {
						@Override
						public boolean accept(Item item) {
							return item.isValid() && item.getName() != null && item.getName().equalsIgnoreCase(RAW_PHEASANT);
						}
					});
					for (Item item : meats) {
						if (item.isValid()) {
							item.interact("Drop", item.getName());
							Time.sleep(100, 150);
						}
					}
				} else if (!Inventory.isFull() && droppedItem.isValid()) {
					if (droppedItem.isOnScreen()) {
						droppedItem.interact("Take");
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return droppedItem.isValid();
							}
						}, 3000);
					} else {
						Walking.walkTo(droppedItem);
						Camera.turnTo(droppedItem);
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return !droppedItem.isOnScreen();
							}
						}, 3000);
					}
				} else {
					setStatus("Leaving Freaky Foster");
					final GameObject portal = GameEntities.getNearest(EXIT_PORTAL);
					if (portal.isValid()) {
						if (portal.isOnScreen()) {
							portal.interact("Use");
							Time.sleep(new Condition() {
								@Override
								public boolean active() {
									return portal.isValid();
								}
							}, 3000);
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
		} else if (pheasantId <= 0) {
			if (Widgets.get(WIDGET_CHAT_LEAVE).isValid() && Widgets.get(WIDGET_CHAT_LEAVE, 2).getText().contains("leave")) {
				setStatus("Leave Widget.");
				leave = true;
				Widgets.clickContinue();
			} else if (Widgets.get(WIDGET_CHAT).isValid() || Widgets.get(WIDGET_CHAT_TWO).isValid()) {
				for (int i = 2; i < 5; i++) {
					if (Widgets.get(WIDGET_CHAT).isValid() && getId(Widgets.get(WIDGET_CHAT, i).getText()) != -1) {
						pheasantId = getId(Widgets.get(WIDGET_CHAT, i).getText());
						setStatus("Peasant ID: " + pheasantId);
						break;
					}
				}
				for (int i = 2; i < 4; i++) {
					if (Widgets.get(WIDGET_CHAT_TWO).isValid() && getId(Widgets.get(WIDGET_CHAT_TWO, i).getText()) != -1) {
						pheasantId = getId(Widgets.get(WIDGET_CHAT_TWO, i).getText());
						setStatus("Peasant ID: " + pheasantId);
						break;
					}
				}
				Widgets.clickContinue();
			} else if (Widgets.canContinue()) {
				setStatus("Clicking Continue");
				Widgets.clickContinue();
			} else {
				final NPC freakyFoster = NPCs.getNearest("Freaky Forester");
				if (freakyFoster.isValid()) {
					setStatus("Interacting with Freaky Foster");
					if (freakyFoster.isOnScreen()) {
						freakyFoster.interact("Talk-to");
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return !Widgets.canContinue();
							}
						}, 3000);
					} else {
						Walking.walkTo(freakyFoster);
						Camera.turnTo(freakyFoster);
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return !freakyFoster.isOnScreen();
							}
						}, 3000);
					}
				}
			}
		} else {

			if (Inventory.contains(RAW_PHEASANT)) {
				final NPC freakyFoster = NPCs.getNearest("Freaky Forester");
				if (freakyFoster.isValid()) {
					setStatus("Interacting with Freaky Foster");
					if (freakyFoster.isOnScreen()) {
						freakyFoster.interact("Talk-to");
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return Inventory.contains(RAW_PHEASANT);
							}
						}, 3000);
						if (!Inventory.contains(RAW_PHEASANT))
							leave = true;
					} else {
						Walking.walkTo(freakyFoster);
						Camera.turnTo(freakyFoster);
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return !freakyFoster.isOnScreen();
							}
						}, 3000);
					}
				}
			} else {
				final GroundItem meat = GroundItems.getNearest(RAW_PHEASANT);
				if (Inventory.isFull()) {
					final Item itemToDrop = getLowestItem();
					if (itemToDrop.isValid()) {
						itemToDrop.interact("Drop", itemToDrop.getName());
						droppedIds.add(itemToDrop.getId());
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return itemToDrop.isValid();
							}
						}, 3000);
					}
				} else if (meat.isValid()) {
					setStatus("Picking Up Raw Pheasant");
					if (meat.isOnScreen()) {
						meat.interact("Take", meat.getName());
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return meat.isValid();
							}
						}, 3000);
					} else {
						Walking.walkTo(meat);
						Camera.turnTo(meat);
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return !meat.isOnScreen();
							}
						}, 3000);
					}
				} else {
					final NPC pheasant = NPCs.getNearest(pheasantId);
					if (pheasant.isValid()) {
						setStatus("Attacking Pheasant");
						if (pheasant.isOnScreen()) {
							pheasant.interact("Attack", pheasant.getName());
							Time.sleep(new Condition() {
								@Override
								public boolean active() {
									return !Widgets.canContinue();
								}
							}, 3000);
						} else {
							Walking.walkTo(pheasant);
							Camera.turnTo(pheasant);
							Time.sleep(new Condition() {
								@Override
								public boolean active() {
									return !pheasant.isOnScreen();
								}
							}, 3000);
						}
					}
				}
			}
		}
	}

	private int getId(String message) {
		if (message.toLowerCase().contains("one-") || message.toLowerCase().contains("1 tail") || message.toLowerCase().contains("one tails") || message.toLowerCase().contains("1-")) {
			return getId(Foster.ONE_TAIL.model_ids);
		} else if (message.toLowerCase().contains("two-") || message.toLowerCase().contains("2 tail") || message.toLowerCase().contains("two tails") || message.toLowerCase().contains("2-")) {
			return getId(Foster.TWO_TAIL.model_ids);
		} else if (message.toLowerCase().contains("three-") || message.toLowerCase().contains("3 tail") || message.toLowerCase().contains("three tails") || message.toLowerCase().contains("3-")) {
			return getId(Foster.THREE_TAIL.model_ids);
		} else if (message.toLowerCase().contains("four-") || message.toLowerCase().contains("4 tail") || message.toLowerCase().contains("four tails") || message.toLowerCase().contains("4-")) {
			return getId(Foster.FOUR_TAIL.model_ids);
		}
		return -1;
	}

	private int getId(int[] model_ids) {
		for (NPC npc : NPCs.getAll()) {
			if (npc != null && npc.getModelIds() != null && Arrays.equals(npc.getModelIds(), model_ids)) {
				return npc.getId();
			}
		}
		return -1;
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


	private enum Foster {
		ONE_TAIL(new int[]{8839, 8840}, "one"), TWO_TAIL(new int[]{8839, 8841}, "two"), THREE_TAIL(new int[]{8839, 8842}, "three"), FOUR_TAIL(new int[]{8839, 8843}, "four");

		int[] model_ids;
		String name;

		Foster(final int[] model_ids, final String name) {
			this.model_ids = model_ids;
			this.name = name;
		}

	}

	@Override
	public void reset() {
		pheasantId = -1;
		leave = false;
		droppedIds.clear();
	}
}
