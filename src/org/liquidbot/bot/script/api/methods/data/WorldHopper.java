package org.liquidbot.bot.script.api.methods.data;

import org.liquidbot.bot.Constants;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;

import java.awt.*;

public class WorldHopper {

	private static int selectedWorld = 0;

	public static int getSelectedWorld() {
		return selectedWorld;
	}

	public static void setWorld(int world) {
		selectedWorld = world;
	}

	public static void hop(int world) {
		selectedWorld = world;
		Game.logout();
	}

	public static void clickOnWorld(final int id) {
		final Rectangle rectangle = getRect(id);
		final Point point = new Point(Random.nextInt(rectangle.x, rectangle.x + rectangle.width),
				Random.nextInt(rectangle.y, rectangle.y + rectangle.height));

		Mouse.click(point, true);
	}

	public static void openWorldsMenu() {
		final Point point = new Point(Random.nextInt(5, 105), Random.nextInt(463, 498));
		Mouse.click(point, true);
		Time.sleep(1200, 1500);
	}

	private static Rectangle getRect(int id) {
		int index = 0;
		for (int x = 0; x < Constants.WORLDS.length; x++) {
			if (Constants.WORLDS[x] == id) {
				index = x;
				break;
			}
		}
		int xCount = index / 17;
		int yCount = index % 17;
		int xMulti = 93;
		int yMulti = 24;

		return new Rectangle(xMulti * xCount + 199, yCount * yMulti + 61, 87, 18);
	}


}
