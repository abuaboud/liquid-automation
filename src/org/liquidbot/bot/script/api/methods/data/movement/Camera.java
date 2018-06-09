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
 * Created on 7/31/14
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
				_pitch = getPitch();
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
	 * @param angle to change
	 * @return true if done else false
	 */
	public static boolean setAngle(int angle) {
		if (!Game.isLoggedIn())
			return false;
		
		int curAngle = getAngle(),
				diff = getDiff(curAngle, angle),
						dir = -1;
		boolean turnLeft = false;

		if (normalizeAngle(curAngle + diff) == angle) {
			turnLeft = true;
			dir = KeyEvent.VK_LEFT;
		} else {
			dir = KeyEvent.VK_RIGHT;
		}
		
		int finalRawAngle = turnLeft ? curAngle + diff : curAngle - diff,
				curRawAngle =getAngle();
		if (diff > 5) {
			// Figure out where we are
			Keyboard.press(dir);
			for (int i = 0; i < 100; i++) {
				
				if (turnLeft) {
					curRawAngle = (getAngle() >= curRawAngle ? 0 : 360) + getAngle();
					
					if (curRawAngle > finalRawAngle)
						break;
				} else {
					curRawAngle = (getAngle() <= curRawAngle ? 0 : -360) + getAngle();
					
					if (curRawAngle < finalRawAngle)
						break;
				}
				
				Time.sleep(100, 200);
			}
			Keyboard.release(dir);
		}
		return getDiff(getAngle(), angle) <= 5;
	}

	private static int getDiff(int curAngle, int angle) {
		int raw_diff = normalizeAngle(Math.abs(angle - curAngle));
		return raw_diff > 180 ? 360 - raw_diff : raw_diff;
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

	private static int normalizeAngle(int angle) {
		return angle % 360;
	}
	

}
