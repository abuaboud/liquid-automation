package org.liquidbot.bot.script.api.methods.data;

import org.liquidbot.bot.script.api.interfaces.Condition;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.input.Keyboard;
import org.liquidbot.bot.script.api.methods.interactive.GameEntities;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.GameObject;
import org.liquidbot.bot.script.api.wrappers.Item;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;
import org.liquidbot.bot.utils.Utilities;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Hiasat on 8/12/14.
 */
public class DepositBox {

	private static final int WIDGET_TITLE= 60 , ITEM_PAD = 61, BANK_CLOSE = 62, WIDGET = 11;

	public static Point getLocation(int index){
	   if(index == -1 || index > 27)
		   return null;
		int row = index / 7;
		int col = index - (row * 7);
		int x = 145 + (col * 40);
		int y = 92 + (row * 40);

		int plus = Random.nextInt(0, 1);
		if (plus == 1) {
			return new Point((x + (Random.nextInt(2, 4))), y + (Random.nextInt(2, 4)));
		}
		return new Point((x - (Random.nextInt(2, 4))), y - (Random.nextInt(2, 4)));
	}

	public static Item getItem(Filter<Item> filter) {
		Item[] items = getAllItems(filter);
		if (items == null || items.length == 0)
			return nil();
		return items[0];
	}

	public static Item getItem(final int... ids) {
		return getItem(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item.isValid() && Utilities.inArray(item.getId(), ids);
			}
		});
	}

	public static Item getItem(final String... names) {
		return getItem(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item.isValid() && item.getName() != null && Utilities.inArray(item.getName(), names);
			}
		});
	}

	public static Item[] getAllItems(Filter<Item> filter) {
		java.util.List<Item> list = new ArrayList<>();
		if (!Game.isLoggedIn())
			return list.toArray(new Item[list.size()]);
		final WidgetChild child = Widgets.get(WIDGET, ITEM_PAD);
		if (!child.isVisible())
			return list.toArray(new Item[list.size()]);
		final int[] contentIds = child.getSlotContentIds();
		final int[] stackSizes = child.getStackSizes();
		if (contentIds == null || stackSizes == null)
			return list.toArray(new Item[list.size()]);
		for (int itemIndex = 0; itemIndex < contentIds.length; itemIndex++) {
			Item item = new Item(contentIds[itemIndex] - 1, stackSizes[itemIndex], itemIndex, Item.Type.DEPOSIT_BOX, new Rectangle(getLocation(itemIndex).x - 2, getLocation(itemIndex).y - 2, 4, 4));
			if (item.isValid() && (filter == null || filter.accept(item))) {
				list.add(item);
			}
		}
		return list.toArray(new Item[list.size()]);
	}

	public static Item[] getAllItems() {
		return getAllItems(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return true;
			}
		});
	}

	public static boolean isOpen() {
		WidgetChild iFace = Widgets.get(WIDGET, WIDGET_TITLE);
		if (iFace == null)
			return false;
		return iFace.isVisible() && iFace.getText() != null && iFace.getText().toLowerCase().contains("bank");
	}

	public static boolean open() {
		if (isOpen())
			return true;
		final GameObject box = GameEntities.getNearest("Bank deposit box");
		if(box.isValid()){
			if (box.isOnScreen()) {
				box.interact("Deposit");
				Time.sleep(new Condition() {
					@Override
					public boolean active() {
						return !isOpen();
					}
				}, 3000);
			} else {
				Walking.walkTo(box);
				if (Random.nextInt(0, 10) > 6)
					box.turnTo();
				Time.sleep(new Condition() {
					@Override
					public boolean active() {
						return !box.isOnScreen();
					}
				}, 3000);
			}
		}
		return isOpen();
	}

	public static boolean close() {
		if (!isOpen())
			return true;
		WidgetChild closeButton = Widgets.get(WIDGET, BANK_CLOSE);
		return closeButton.isVisible() && closeButton.interact("Close");
	}

	public static boolean depositAll() {
		return depositAllExcept(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return false;
			}
		});
	}

	public static boolean depositAllExcept(final String... names) {
		if(names == null)
			return depositAll();
		return depositAllExcept(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item.isValid() && item.getName() != null && Utilities.inArray(item.getName(), names);
			}
		});
	}

	public static boolean depositAllExcept(final int... ids) {
		return depositAllExcept(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item.isValid() && Utilities.inArray(item.getId(), ids);
			}
		});
	}

	public static boolean depositAllExcept(Filter<Item> filter) {
		boolean deposit = false;
		for (Item item : Inventory.getAllItems()) {
			if (item.isValid() && (filter == null || !filter.accept(item))) {
				deposit(item.getId(), 9999999);
				deposit = true;
			}
		}
		return deposit;
	}

	public static boolean deposit(final int[] ids, final int amount) {
		return deposit(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item != null && Utilities.inArray(item.getId(), ids);
			}
		}, amount);
	}

	public static boolean deposit(final String[] names, final int amount) {
		return deposit(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item != null && item.getName() != null && Utilities.inArray(item.getName(), names);
			}
		}, amount);
	}

	public static boolean deposit(final String name, final int amount) {
		return deposit(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item != null && item.getName() != null && item.getName().equalsIgnoreCase(name);
			}
		}, amount);
	}

	public static boolean deposit(Filter<Item> filter, int amount) {
		Item[] items = getAllItems(filter);
		if (items == null || items.length == 0)
			return false;
		for (Item item : items) {
			if (item.isValid()) {
				deposit(item.getId(), amount);
			}
		}
		return true;
	}


	public static boolean deposit(final int id, final int amount) {
		if (!isOpen())
			return false;
		final Item item = getItem(id);
		if (!item.isValid())
			return false;
		final int amountInInventory = Inventory.getCount(id);
		String action = "Deposit X";
		if (amount >= amountInInventory) {
			action = "Deposit All";
		} else {
			switch (amountInInventory) {
				case 1:
					action = "Deposit 1";
					break;
				case 5:
					action = "Deposit 5";
					break;
				case 10:
					action = "Deposit 10";
					break;
			}
		}
		item.interact(action, item.getName());
		if (action.contains("X")) {
			Time.sleep(2000, 2500);
			Keyboard.sendText("" + amount, false);
		}
		Time.sleep(new Condition() {
			@Override
			public boolean active() {
				return item.isValid();
			}
		}, 3000);
		return true;
	}
	public static Item nil() {
		return new Item(-1, -1, -1, Item.Type.DEPOSIT_BOX, null);
	}

}
