package org.liquidbot.bot.script.api.methods.data.movement;

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Locatable;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.input.Keyboard;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.util.Timer;
import org.liquidbot.bot.script.api.wrappers.Player;

import java.awt.event.KeyEvent;

/*
 * Created by Hiasat on 7/31/14
 */
public class Camera {

	/**
	 * X coordinate of Camera
	 *
	 * @return Integer
	 */
	public static int getX() {
		return (int) Reflection.value("Client#getCameraX()", null);
	}

	/**
	 * Y coordinate of Camera
	 *
	 * @return Integer
	 */
	public static int getY() {
		return (int) Reflection.value("Client#getCameraY()", null);
	}

	/**
	 * Z coordinate of Camera
	 *
	 * @return Integer
	 */
	public static int getZ() {
		return (int) Reflection.value("Client#getCameraZ()", null);
	}

	/**
	 * Minimap angel
	 *
	 * @return Integer
	 */
	public static int getMapAngle() {
		return (int) Reflection.value("Client#getMapAngle()", null);
	}

	/**
	 * Client Pitch View Angle Integer
	 *
	 * @return Integer 0-90
	 */
	public static int getPitch() {
		return (int) (((int) (Reflection.value("Client#getPitch()", null)) - 128) / 2.83);
	}

	/**
	 * Client Yaw View Angle Integer
	 *
	 * @return Integer 0-2048
	 */
	public static int getYaw() {
		return (int) Reflection.value("Client#getYaw()", null);
	}

	/**
	 * Camera Angel
	 *
	 * @return Integer 0-360
	 */
	public static int getAngle() {
		return Math.abs((int) (getYaw() / 5.68) - 360);
	}

	/**
	 * angel to specific degrees
	 *
	 * @param degrees
	 * @return Integer of angel
	 */
	public static int getAngleTo(int degrees) {
		int ca = getAngle();
		if (ca < degrees) {
			ca += 360;
		}
		int da = ca - degrees;
		if (da > 180) {
			da -= 360;
		}
		return da;
	}

	/**
	 * View angel to specific Locatable
	 *
	 * @param locatable such as NPC/GameObject/Tile
	 * @return Integer 0-360
	 */
	public static int getAngleTo(final Locatable locatable) {
		final Player local = Players.getLocal();
		double degree = 360 - Math.toDegrees(Math.atan2(locatable.getLocation().getX()
				- local.getLocation().getX(), local.getLocation().getY()
				- locatable.getLocation().getY()));
		if (degree >= 360) {
			degree -= 360;
		}
		return (int) degree;
	}

	/**
	 * set Pitch to given Pitch
	 *
	 * @param pitch
	 * @return Boolean : true if it done correctly else False
	 */
	public static boolean setPitch(int pitch) {
		if (!Game.isLoggedIn())
			return false;

		int _pitch = getPitch();
		if (_pitch == pitch || Math.abs(_pitch - pitch) <= 5) {
			return true;
		} else if (_pitch < pitch) {
			Keyboard.press(KeyEvent.VK_UP);
			Timer t = new Timer(5000);

			while (_pitch < pitch && Math.abs(_pitch - pitch) > 5 && t.isRunning()) {
				_pitch = Camera.getPitch();
				Time.sleep(59, 100);
			}

			Keyboard.release(KeyEvent.VK_UP);
		} else if (_pitch > pitch) {
			Keyboard.press(KeyEvent.VK_DOWN);
			Timer t = new Timer(5000);

			while (_pitch > pitch && Math.abs(_pitch - pitch) > 5 && t.isRunning()) {
				_pitch = getPitch();
				Time.sleep(59, 100);
			}

			Keyboard.release(KeyEvent.VK_DOWN);
		}

		return Math.abs(_pitch - pitch) <= 5;
	}

	/**
	 * @param degrees to change
	 * @return true if done else false
	 */
	public static boolean setAngle(int degrees) {
		if (!Game.isLoggedIn())
			return false;
		if (degrees > 360 || degrees < 0)
			return false;
		if (getAngleTo(degrees) > 5) {
			Keyboard.press((char) KeyEvent.VK_LEFT);
			while (getAngleTo(degrees) > 5 && Game.isLoggedIn()) {
				Time.sleep(25);
			}
			Keyboard.release((char) KeyEvent.VK_LEFT);
		} else if (getAngleTo(degrees) < -5) {
			Keyboard.press((char) KeyEvent.VK_RIGHT);
			while (getAngleTo(degrees) < -5 && Game.isLoggedIn()) {
				Time.sleep(25);
			}
			Keyboard.release((char) KeyEvent.VK_RIGHT);
		}
		return Math.abs(degrees - getAngle()) < 5;
	}


	/**
	 * Change Pitch/Angel to be able to see locatable
	 *
	 * @param locatable
	 */
	public static void turnTo(final Locatable locatable) {
		turnPitchTo(locatable);
		turnAngleTo(locatable);
	}

	/**
	 * Change Pitch depend on distance between Player and locatable , if 0 > Pitch > 90 it will return random 5,10
	 *
	 * @param locatable
	 */
	public static void turnPitchTo(final Locatable locatable) {
		int pitch = 90 - (locatable.distanceTo() * 5);
		int factor = Random.nextInt(0, 1) == 0 ? -1 : 1;
		pitch = pitch + factor * Random.nextInt(5, 10);

		if (pitch > 90) {
			pitch = 90;
		} else if (pitch < 0) {
			pitch = Random.nextInt(5, 10);
		}

		setPitch(pitch);
	}

	/**
	 * Change angel to locatable angel so it's get on screen
	 *
	 * @param locatable
	 */
	public static void turnAngleTo(final Locatable locatable) {
		int set = getAngleTo(locatable);

		if (set > 180) {
			set -= 180;
		} else {
			set += 180;
		}

		setAngle(set);
	}


}
