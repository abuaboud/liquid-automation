package org.liquidbot.bot.script.api.methods.input;

import org.liquidbot.bot.Configuration;

import java.awt.*;

/*
 * Created by Hiasat on 8/2/14
 */
public class Mouse {


	public static void move(int x, int y) {
		Configuration.getInstance().getMouse().move(x, y);
	}

	public static void move(Point p) {
		if (p == null)
			return;
		move(p.x, p.y);
	}

	public static int getPressX() {
		return Configuration.getInstance().getMouse().getPressX();
	}

	public static int getPressY() {
		return Configuration.getInstance().getMouse().getPressY();
	}

	public static long getPressTime() {
		return Configuration.getInstance().getMouse().getPressTime();
	}

	public static boolean isPressed() {
		return Configuration.getInstance().getMouse().isPressed();
	}

	public static Point getLocation() {
		return Configuration.getInstance().getMouse().getLocation();
	}

	public static void hop(int x, int y) {
		Configuration.getInstance().getMouse().hop(x, y);
	}

	public static void hop(Point p) {
		hop(p.x, p.y);
	}

	public static void click(boolean left) {
		Configuration.getInstance().getMouse().click(left);
	}

	public static void click(Point p, boolean left) {
		move(p);
		click(left);
	}

	public static void press(int x, int y, int button) {
		Configuration.getInstance().getMouse().press(x, y, button);
	}

	public static void release(int x, int y, int button) {
		Configuration.getInstance().getMouse().press(x, y, button);
	}

	public static boolean dragMouse(Point p1, Point p2) {
		return dragMouse(p1.getLocation().x, p1.getLocation().y, p2.getLocation().x, p2.getLocation().y);
	}

	public static boolean dragMouse(int x, int y, int x1, int y1) {
		return Configuration.getInstance().getMouse().dragMouse(x, y, x1, y1);
	}
}
