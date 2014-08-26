package org.liquidbot.bot.script.api.methods.data;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;

import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

/*
 * Created by Hiasat on 8/2/14
 */
public class Menu {

	private static final Pattern htmlTags = Pattern.compile("\\<.+?\\>");

	public static int getX() {
		return (int) Reflection.value("Client#getMenuX()", null);
	}

	public static int getY() {
		return (int) Reflection.value("Client#getMenuY()", null);
	}

	public static int getWidth() {
		return (int) Reflection.value("Client#getMenuWidth()", null);
	}

	public static int getHeight() {
		return (int) Reflection.value("Client#getMenuHeight()", null);
	}

	public static int getMenuSize() {
		return (int) Reflection.value("Client#getMenuCount()", null);
	}

	public static boolean isOpen() {
		return (Boolean) Reflection.value("Client#isMenuOpen()", null);
	}

	public static Rectangle getArea() {
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	public static java.util.List<String> getActions() {
		ArrayList<String> actions = new ArrayList<>();
		String[] menuActions = (String[]) Reflection.value("Client#getMenuActions()", null);

		for (int i = Menu.getMenuSize() - 1; i >= 0; i--) {
			if (menuActions[i] != null) {
				actions.add(htmlTags.matcher(menuActions[i]).replaceAll(""));
			}
		}
		return actions;
	}

	public static java.util.List<String> getOptions() {
		ArrayList<String> options = new ArrayList<>();
		String[] menuOptions = (String[]) Reflection.value("Client#getMenuOptions()", null);
		for (int i = Menu.getMenuSize() - 1; i >= 0; i--) {
			if (menuOptions[i] != null) {
				options.add(htmlTags.matcher(menuOptions[i]).replaceAll(""));
			}
		}
		return options;
	}

	public static int index(String action) {
		return index(action, null);
	}

	public static int index(String action, String option) {
		int index = -1;
		int i = 0;
		for (String a : Menu.getActions()) {
			try {
				if (a.equalsIgnoreCase(action) && (option == null || Menu.getOptions().get(i).toLowerCase().contains(option.toLowerCase()))) {
					index = i;
					break;
				}
				i++;
			} catch (IndexOutOfBoundsException e) {

			}
		}
		return index;
	}

	public static boolean contains(String action, String option) {
		return index(action, option) != -1;
	}

	public static boolean contains(String action) {
		return index(action, null) != -1;
	}

	public static boolean interact(String action) {
		return interact(action, null, false);
	}

	public static boolean interact(String action, String option) {
		return interact(action, option, false);
	}

	public static boolean interact(String action, String option, boolean rightClick) {
		int menuIndex = index(action, option);
		if (menuIndex < 0)
			return false;
		if (menuIndex == 0 && !rightClick) {
			Mouse.click(true);
			return true;
		}
		if (!Menu.isOpen()) {
			Mouse.click(false);
			for (int i = 0; i < 20 && !isOpen(); i++, Time.sleep(40, 50)) ;
		}
		if (isOpen()) {
			Rectangle area = new Rectangle(getX(), getY() + 18 + menuIndex * 15, getWidth(), 15);
			Mouse.move((int) (area.getX() + Random.nextInt(0, (int) area.getWidth())), (int) (area.getY() + Random.nextInt(5, 10)));
			Time.sleep(100, 150);
			Mouse.click(true);
			return true;
		}
		return false;
	}
}
