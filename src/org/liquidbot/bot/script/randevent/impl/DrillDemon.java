package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.interfaces.Condition;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.Settings;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.interactive.GameEntities;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.GameObject;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.api.wrappers.Tile;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;
import org.liquidbot.bot.script.randevent.RandomEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Magorium
 * Date: 8/10/14
 * Time: 1:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class DrillDemon extends RandomEvent {

	private Exercise currentExercise;

	private final int SETTING_ID = 531;
	private final int WIDGET_CHAT = 519;
	private final int WIDGET_CHAT_EXECRISE_NAME = 1;

	private int sign1 = 1;
	private int sign2 = 2;
	private int sign3 = 3;
	private int sign4 = 4;

	@Override
	public String getName() {
		return "Drill Demon";
	}

	@Override
	public String getAuthor() {
		return "Hiasat";
	}

	@Override
	public boolean active() {
		if (!Game.isLoggedIn())
			return false;
		for (NPC g : NPCs.getAll(new Filter<NPC>() {
			@Override
			public boolean accept(NPC npc) {
				return npc.isValid() && npc.getName() != null && npc.getName().equalsIgnoreCase("Sergeant Damien");
			}
		})) {
			if (g != null) {
				if (GameEntities.getNearest("Exercise mat").isValid())
					return true;
				if (g.distanceTo() < 7 && ((g.getSpokenMessage() != null && g.getSpokenMessage().contains(Players.getLocal().getName()))))
					return true;
			}
		}
		return false;
	}

	@Override
	public void solve() {
		NPC damien = NPCs.getNearest("Sergeant Damien");
		if (Camera.getPitch() < 70) {
			Camera.setPitch(Random.nextInt(70, 90));
		} else if (currentExercise != null) {
			setStatus("Doing Exercise");
			final GameObject exercise = gameObject(currentExercise);
			if (exercise.isValid()) {
				if (exercise.isOnScreen()) {
					if (exercise.interact("Use")) {
						for (int i = 0; i < 50 && Players.getLocal().getAnimation() == -1; i++, Time.sleep(100, 150)) ;
						for (int i = 0; i < 80 && Players.getLocal().getAnimation() != -1; i++, Time.sleep(100, 150)) ;
						for (int i = 0; i < 20 && !Widgets.canContinue(); i++, Time.sleep(100, 150)) ;
						currentExercise = null;
					}
				} else {
					if (exercise.distanceTo() < 7) {
						Walking.walkTo(exercise.getLocation());
					} else {
						Camera.turnTo(exercise);
					}
					Time.sleep(new Condition() {
						@Override
						public boolean active() {
							return !exercise.isOnScreen();
						}
					}, 3000);
				}
			}
		} else if (Widgets.canContinue()) {
			setStatus("Continuing widgets");
			WidgetChild child = Widgets.get(WIDGET_CHAT, WIDGET_CHAT_EXECRISE_NAME);
			if (child.isVisible()) {
				currentExercise = getExercise(child.getText());
				log.info("Exercise: " + currentExercise.getName());
			}
			Widgets.clickContinue();
		} else if (damien.isValid()) {
			setStatus("Talking to Damien");
			if (damien.isOnScreen()) {
				damien.interact("Talk-to");
				Time.sleep(new Condition() {
					@Override
					public boolean active() {
						return !Widgets.canContinue();
					}
				}, 3000);
			} else {
				Walking.walkTo(damien);
				Time.sleep(new Condition() {
					@Override
					public boolean active() {
						return Players.getLocal().isMoving();
					}
				}, 3000);
			}
		}
	}

	private GameObject gameObject(Exercise exercise) {
		final Mat mat = getMat(exercise);
		return GameEntities.getNearest(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject gameObject) {
				return gameObject.getType().equals(GameObject.Type.FLOOR_DECORATION) && gameObject.getLocation().equals(mat.tile());
			}
		});

	}

	private Mat getMat(Exercise exercise) {
		updateSettings();
		if (sign1 == exercise.id) {
			return Mat.values()[0];
		} else if (sign2 == exercise.id) {
			return Mat.values()[1];
		} else if (sign3 == exercise.id) {
			return Mat.values()[2];
		} else if (sign4 == exercise.id) {
			return Mat.values()[3];
		}
		log.error("Can't find correct Mat guessing it");
		return Mat.values()[0];
	}


	@Override
	public void reset() {
		currentExercise = null;
	}

	enum Mat {
		ONE(new Tile(48, 51)),
		TWO(new Tile(50, 51)),
		THREE(new Tile(52, 51)),
		FOUR(new Tile(54, 51));

		private Tile regionTile;

		Mat(final Tile regionTile) {
			this.regionTile = regionTile;
		}

		public Tile tile() {
			return new Tile(Game.getBaseX() + regionTile.getX(), Game.getBaseY() + regionTile.getY());
		}
	}

	private enum Exercise {
		JUMPING_JACKS(1, "Star jumps"), PUSH_UPS(2, "Push ups"), SIT_UPS(3, "Sit ups"), JOGGING(4, "Jog");
		int id;
		String name;

		Exercise(int id, String name) {
			this.id = id;
			this.name = name;
		}

		private String getName() {
			return name;
		}
	}

	private Exercise getExercise(String text) {
		for (Exercise e : Exercise.values()) {
			if (text.toLowerCase().contains(e.getName().toLowerCase()))
				return e;
		}
		return null;
	}

	public void updateSettings() {
		switch (Settings.get(531)) {
			case 668:
				sign1 = 1;
				sign2 = 2;
				sign3 = 3;
				sign4 = 4;
				break;
			case 675:
				sign1 = 2;
				sign2 = 1;
				sign3 = 3;
				sign4 = 4;
				break;
			case 724: //
				sign1 = 1;
				sign2 = 3;
				sign3 = 2;
				sign4 = 4;
				break;
			case 738: //
				sign1 = 3;
				sign2 = 1;
				sign3 = 2;
				sign4 = 4;
				break;
			case 787: //
				sign1 = 2;
				sign2 = 3;
				sign3 = 1;
				sign4 = 4;
				break;
			case 794:
				sign1 = 3;
				sign2 = 2;
				sign3 = 1;
				sign4 = 4;
				break;
			case 1116: //
				sign1 = 1;
				sign2 = 2;
				sign3 = 4;
				sign4 = 3;
				break;
			case 1123:
				sign1 = 2;
				sign2 = 1;
				sign3 = 4;
				sign4 = 3;
				break;
			case 1228: //
				sign1 = 1;
				sign2 = 4;
				sign3 = 2;
				sign4 = 3;
				break;
			case 1249: //
				sign1 = 4;
				sign2 = 1;
				sign3 = 2;
				sign4 = 3;
				break;
			case 1291:
				sign1 = 2;
				sign2 = 4;
				sign3 = 1;
				sign4 = 3;
				break;
			case 1305:
				sign1 = 4;
				sign2 = 2;
				sign3 = 1;
				sign4 = 3;
				break;
			case 1620: //
				sign1 = 1;
				sign2 = 3;
				sign3 = 4;
				sign4 = 2;
				break;
			case 1634: //
				sign1 = 3;
				sign2 = 1;
				sign3 = 4;
				sign4 = 2;
				break;
			case 1676:
				sign1 = 1;
				sign2 = 4;
				sign3 = 3;
				sign4 = 2;
				break;
			case 1697: //
				sign1 = 4;
				sign2 = 1;
				sign3 = 3;
				sign4 = 2;
				break;
			case 1802: //
				sign1 = 3;
				sign2 = 4;
				sign3 = 1;
				sign4 = 2;
				break;
			case 1809: //
				sign1 = 4;
				sign2 = 3;
				sign3 = 1;
				sign4 = 2;
				break;
			case 2131: //
				sign1 = 2;
				sign2 = 3;
				sign3 = 4;
				sign4 = 1;
				break;
			case 2138: //
				sign1 = 3;
				sign2 = 2;
				sign3 = 4;
				sign4 = 1;
				break;
			case 2187: //
				sign1 = 2;
				sign2 = 4;
				sign3 = 3;
				sign4 = 1;
				break;
			case 2201: //
				sign1 = 4;
				sign2 = 2;
				sign3 = 3;
				sign4 = 1;
				break;
			case 2250: //
				sign1 = 3;
				sign2 = 4;
				sign3 = 2;
				sign4 = 1;
				break;
			case 2257: //
				sign1 = 4;
				sign2 = 3;
				sign3 = 2;
				sign4 = 1;
				break;
		}
	}
}
