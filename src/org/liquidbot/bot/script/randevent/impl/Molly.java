package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.interfaces.Condition;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.interfaces.PaintListener;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.interactive.GameEntities;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.*;
import org.liquidbot.bot.script.randevent.RandomEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Hiasat on 8/16/14.
 */
public class Molly extends RandomEvent implements PaintListener {

	private final int WIDGET_CONTROL_INTERFACEGROUP = 240;
	private final int WIDGET_CONTROLS_GRAB = 28;
	private final int WIDGET_CONTROLS_UP = 29;
	private final int WIDGET_CONTROLS_DOWN = 30;
	private final int WIDGET_CONTROLS_LEFT = 31;
	private final int WIDGET_CONTROLS_RIGHT = 32;
	private final int OPTION_WIDGET = 228;

	private int[] targetModels = null;
	private Tile targetTile = null;

	private boolean finish = false;

	@Override
	public String getName() {
		return "Molly";
	}

	@Override
	public String getAuthor() {
		return "Hiasat";
	}

	@Override
	public boolean active() {
		if (!Game.isLoggedIn())
			return false;
		NPC molly = NPCs.getNearest("Molly");
		NPC suspect = NPCs.getNearest("Suspect");
		if ((molly.isValid() && molly.distanceTo() < 25) || (suspect.isValid() && suspect.distanceTo() < 25)) {
			final GameObject door = GameEntities.getNearest("Door");
			if (door.isValid() && door.distanceTo() < 10) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void solve() {
		final int suspectsSize = NPCs.getAll("Suspect").length;
		if (Widgets.canContinue()) {
			setStatus("Clicking Continuing");
			Widgets.clickContinue();
		} else {
			if (isInControlRoom()) {
				if (suspectsSize > 1) {
					NPC suspect = NPCs.getNearest(new Filter<NPC>() {
						@Override
						public boolean accept(NPC npc) {
							return npc.isValid() && npc.getName() != null && npc.getName().equalsIgnoreCase("Suspect") && npc.getModelIds() != null && Arrays.equals(npc.getModelIds(), targetModels);
						}
					});
					final Widget parent = Widgets.get(WIDGET_CONTROL_INTERFACEGROUP);
					if (parent.isValid()) {
						if (suspect.isValid()) {
							final Tile clawLoc = GameEntities.getNearest("evil claw").getLocation();
							targetTile = suspect.getLocation();
							final ArrayList<Integer> options = new ArrayList<Integer>();
							if (targetTile.getX() > clawLoc.getX()) {
								options.add(WIDGET_CONTROLS_LEFT);
							}
							if (targetTile.getX() < clawLoc.getX()) {
								options.add(WIDGET_CONTROLS_RIGHT);
							}
							if (targetTile.getY() > clawLoc.getY()) {
								options.add(WIDGET_CONTROLS_DOWN);
							}
							if (targetTile.getY() < clawLoc.getY()) {
								options.add(WIDGET_CONTROLS_UP);
							}
							if (options.isEmpty()) {
								options.add(WIDGET_CONTROLS_GRAB);
							}
							final WidgetChild widgetChild = parent.getChild(options.get(Random.nextInt(0, options.size())));
							if (widgetChild.isVisible()) {
								widgetChild.click();
								Time.sleep(new Condition() {
									@Override
									public boolean active() {
										return clawLoc.equals(GameEntities.getNearest("evil claw").getLocation());
									}
								}, 3000);
							}
						}
					} else {
						final GameObject controlPanel = GameEntities.getNearest("Control panel");
						if (controlPanel.isValid()) {
							if (controlPanel.isOnScreen()) {
								if (suspect.getAnimation() == -1) {
									controlPanel.interact("Use");
									Time.sleep(new Condition() {
										@Override
										public boolean active() {
											return !parent.isValid();
										}
									}, 2000);
								}
							} else {
								controlPanel.turnTo();
								Time.sleep(new Condition() {
									@Override
									public boolean active() {
										return !controlPanel.isOnScreen();
									}
								}, 3000);
							}
						}
					}
				} else {
					if (!finish)
						finish = true;
					final GameObject door = GameEntities.getNearest("Door");
					if (door.isValid()) {
						if (door.isOnScreen()) {
							door.turnTo();
							door.interact("Open");
							Time.sleep(new Condition() {
								@Override
								public boolean active() {
									return !isInControlRoom();
								}
							}, 2000);
						} else {
							door.turnTo();
							Time.sleep(new Condition() {
								@Override
								public boolean active() {
									return door.isOnScreen();
								}
							}, 3000);
						}
					}
				}
			} else {
				final NPC molly = NPCs.getNearest("Molly");
				if (molly.isValid()) {
					final WidgetChild option = Widgets.get(OPTION_WIDGET, 1);
					if (option.isVisible()) {
						option.interact("Continue");
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return option.isVisible();
							}
						}, 3000);
						targetModels = molly.getModelIds();
						setStatus("Target Model Found: " + (targetModels != null));
					} else if (targetModels != null && !finish) {
						final GameObject door = GameEntities.getNearest("Door");
						if (door.isValid()) {
							if (door.isOnScreen()) {
								door.turnTo();
								door.interact("Open");
								Time.sleep(new Condition() {
									@Override
									public boolean active() {
										return !isInControlRoom();
									}
								}, 2000);
							} else {
								door.turnTo();
								Time.sleep(new Condition() {
									@Override
									public boolean active() {
										return !door.isOnScreen();
									}
								}, 3000);
							}
						}
					} else if (molly.isOnScreen()) {
						molly.interact("Talk-To");
						Time.sleep(new Condition() {
							@Override
							public boolean active() {
								return !Widgets.canContinue();
							}
						}, 2000);
					} else {
						molly.turnTo();
					}
				}
			}
		}
	}

	private boolean isInControlRoom() {
		final GameObject o = GameEntities.getNearest("door");
		if (o.isValid() && Players.getLocal().getLocation().getX() == o.getX())
			return false;
		return o.isValid() && o.getLocation().getX() < Players.getLocal().getLocation().getX();
	}

	@Override
	public void reset() {
		targetModels = null;
		targetTile = null;
		finish = true;
	}

	@Override
	public void render(Graphics2D graphics) {

		if (targetTile != null) {
			targetTile.draw(graphics, Color.WHITE);
		}
	}
}
