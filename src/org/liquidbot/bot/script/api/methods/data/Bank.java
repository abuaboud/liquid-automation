package org.liquidbot.bot.script.api.methods.data;

import java.util.ArrayList;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.interfaces.Condition;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.input.Keyboard;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.methods.interactive.GameEntities;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.query.BankQuery;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.GameObject;
import org.liquidbot.bot.script.api.wrappers.Item;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;
import org.liquidbot.bot.utils.Utilities;

/*
 * Created on 8/2/14
 */
public class Bank {

	private static final BankQuery query = new BankQuery();

	/**
	 * Gets the query instance
	 *
	 * @return the query instance
	 */
	public static BankQuery query() {
		return query;
	}

	private static final int BANK_INTERFACE = 12, BANK_ITEMS_PAD = 10,
			BANK_TITLE = 2, BANK_FULL_SPACE = 5, BANK_USED_SPACE = 3,
			BANK_INNER_INTERFACE = 1, BANK_CLOSE = 11,
			BANK_DEPOSIT_INVENTORY = 25, BANK_DEPOSIT_WORN_ITEMS = 27;
	private static final String[] NPC_BANK_NAMES = {"Banker", "Ghost banker",
			"Banker tutor", "Squire"};
	private static final String OBJECT_BANK_NAME = "Bank Booth";

	public static Item[] getAllItems(Filter<Item> filter) {
		ArrayList<Item> list = new ArrayList<Item>();
		if (!Game.isLoggedIn() || !isOpen())
			return list.toArray(new Item[list.size()]);
		WidgetChild parent = Widgets.get(BANK_INTERFACE, BANK_ITEMS_PAD);
		if (parent.isVisible()) {
			WidgetChild[] children = parent.getChildren();
			if (children != null) {
				for (int BankI = 0; BankI < children.length; BankI++) {
					WidgetChild widgetChild = children[BankI];
					Item item = new Item(widgetChild.getItemId(),
							widgetChild.getItemStack(), BankI, Item.Type.BANK,
							widgetChild.getArea());
					if (item.isValid()
							&& (filter == null || filter.accept(item))) {
						list.add(item);
					}
				}
			}
		}
		return list.toArray(new Item[list.size()]);
	}

	public static Item[] getAllItems() {
		return getAllItems(null);
	}

	public static Item nil() {
		return new Item(-1, -1, -1, Item.Type.BANK, null);
	}

	public static Item getItem(Filter<Item> filter) {
		Item[] allItems = getAllItems(filter);
		return allItems.length > 0 ? allItems[0] : nil();
	}

	public static Item getItem(final int... ids) {
		if (ids == null)
			return nil();
		return getItem(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item.isValid() && Utilities.inArray(item.getId(), ids);
			}
		});
	}

	public static Item getItem(final String... names) {
		if (names == null)
			return nil();
		return getItem(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item.isValid() && item.getName() != null
						&& Utilities.inArray(item.getName(), names);
			}
		});
	}

	public static boolean contains(final Filter<Item> filter) {
		return getItem(filter).isValid();
	}

	public static boolean contains(final String... names) {
		return getItem(names).isValid();
	}

	public static boolean contains(final int... ids) {
		return getItem(ids).isValid();
	}

	public static boolean containsAll(final int... ids) {
		if (ids == null)
			return false;
		for (int id : ids) {
			if (!contains(id))
				return false;
		}
		return true;
	}

	public static boolean containsAll(final String... names) {
		if (names == null)
			return false;
		for (String name : names) {
			if (!contains(name))
				return false;
		}

		return true;
	}

	public static int getCount(Filter<Item> filter) {
		final Item item = getItem(filter);
		return item.isValid() ? item.getStackSize() : 0;
	}

	public static int getCount() {
		Item[] items = getAllItems(null);
		if (items == null)
			return 0;
		int count = 0;
		for (Item item : items) {
			count = count + item.getStackSize();
		}
		return count;
	}

	public static int getCount(final int... ids) {
		Item[] items = getAllItems(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item.isValid() && Utilities.inArray(item.getId(), ids);
			}
		});
		if (items == null)
			return 0;
		int count = 0;
		for (Item item : items) {
			count = count + item.getStackSize();
		}
		return count;
	}

	public static int getCount(final String... names) {
		Item[] items = getAllItems(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item.isValid() && item.getName() != null
						&& Utilities.inArray(item.getName(), names);
			}
		});
		if (items == null)
			return 0;
		int count = 0;
		for (Item item : items) {
			count = count + item.getStackSize();
		}
		return count;
	}

	public static int getEmptySpace() {
		if (Widgets.get(BANK_INTERFACE) == null)
			return -1;
		return getFullSpace() - getUsedSpace();
	}

	public static int getFullSpace() {
		if (Widgets.get(BANK_INTERFACE) == null)
			return -1;
		return Integer.parseInt(Widgets.get(BANK_INTERFACE, BANK_FULL_SPACE)
				.getText());
	}

	public static int getUsedSpace() {
		if (Widgets.get(BANK_INTERFACE) == null)
			return -1;
		return Integer.parseInt(Widgets.get(BANK_INTERFACE, BANK_USED_SPACE)
				.getText());
	}

	public static boolean isFull() {
		return !isOpen() && getUsedSpace() == getFullSpace();
	}

	public static boolean isOpen() {
		WidgetChild iFace = Widgets.get(BANK_INTERFACE, BANK_TITLE);
		if (iFace == null)
			return false;
		// Should check for Texts because it can be visible without be really
		// visible
		return iFace.isVisible()
				&& iFace.getText() != null
				&& iFace.getText().toLowerCase()
				.contains("the bank of runescape");
	}

	public static boolean close() {
		if (!isOpen())
			return true;
		WidgetChild closeButton = Widgets.get(BANK_INTERFACE,
				BANK_INNER_INTERFACE).getChild(BANK_CLOSE);
		return closeButton.isVisible() && closeButton.interact("Close");
	}

	public static boolean depositInventory() {
		if (isOpen()) {
			final WidgetChild depositInventoryButton = Widgets.get(
					BANK_INTERFACE, BANK_DEPOSIT_INVENTORY);
			return depositInventoryButton != null
					&& depositInventoryButton.isVisible()
					&& depositInventoryButton.interact("Deposit inventory");
		}
		return false;
	}

	public static boolean depositWornItems() {
		if (isOpen()) {
			final WidgetChild depositWornItems = Widgets.get(BANK_INTERFACE,
					BANK_DEPOSIT_WORN_ITEMS);
			return depositWornItems != null && depositWornItems.isVisible()
					&& depositWornItems.interact("Deposit worn items");
		}
		return false;
	}

	public static boolean deposit(final int[] ids, final int amount) {
		return deposit(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item.isValid() && Utilities.inArray(item.getId(), ids);
			}
		}, amount);
	}

	public static boolean deposit(final String[] names, final int amount) {
		return deposit(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item.isValid() && item.getName() != null
						&& Utilities.inArray(item.getName(), names);
			}
		}, amount);
	}

	public static boolean deposit(final String name, final int amount) {
		return deposit(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item.isValid() && item.getName() != null
						&& item.getName().equalsIgnoreCase(name);
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
		Item item = Inventory.getItem(id);
		if (!item.isValid())
			return false;
		final int amountInInventory = Inventory.getCount(id);
		String action = "Deposit-X";
		if (amount >= amountInInventory) {
			action = "Deposit-All";
		} else {
			switch (amountInInventory) {
				case 1:
					action = "Deposit-1";
					break;
				case 5:
					action = "Deposit-5";
					break;
				case 10:
					action = "Deposit-10";
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
				return Inventory.getCount(id) == amountInInventory;
			}
		}, 3000);
		return true;
	}

	public static boolean withdraw(final int id, final int amount) {
		if (!isOpen())
			return false;
		Item item = Bank.getItem(id);
		if (!item.isValid())
			return false;
		final int amountBeforeWithdraw = Bank.getCount(id);
		String action = "Withdraw-X";
		if (amountBeforeWithdraw <= amount) {
			action = "Withdraw-All";
		} else {
			switch (amount) {
				case 1:
					action = "Withdraw-1";
					break;
				case 5:
					action = "Withdraw-5";
					break;
				case 10:
					action = "Withdraw-10";
					break;
			}
		}
		Mouse.move(item.getInteractPoint());
		Time.sleep(100, 250);
		if (Menu.contains("Withdraw-" + amount)) {
			action = "Withdraw-" + amount;
		}
		item.interact(action, item.getName());
		if (action.contains("X")) {
			Time.sleep(2000, 3000);
			Keyboard.sendText("" + amount, true);
		}
		Time.sleep(new Condition() {
			@Override
			public boolean active() {
				return Bank.getCount(id) == amountBeforeWithdraw;
			}
		}, 3000);
		return true;
	}

	public static boolean withdraw(final int[] ids, final int amount) {
		return withdraw(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item.isValid() && Utilities.inArray(item.getId(), ids);
			}
		}, amount);
	}

	public static boolean withdraw(final String[] names, final int amount) {
		return withdraw(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item.isValid() && item.getName() != null
						&& Utilities.inArray(item.getName(), names);
			}
		}, amount);
	}

	public static boolean withdraw(final String name, final int amount) {
		return withdraw(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item.isValid() && item.getName() != null
						&& item.getName().equalsIgnoreCase(name);
			}
		}, amount);
	}

	public static boolean withdraw(Filter<Item> filter, int amount) {
		Item[] items = getAllItems(filter);
		if (items == null)
			return false;
		for (Item item : items) {
			if (item.isValid()) {
				withdraw(item.getId(), amount);
			}
		}
		return true;
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
		return depositAllExcept(new Filter<Item>() {
			@Override
			public boolean accept(Item item) {
				return item.isValid() && item.getName() != null
						&& Utilities.inArray(item.getName(), names);
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

	public static boolean open() {
		if (isOpen())
			return true;
		final NPC banker = NPCs.getNearest(NPC_BANK_NAMES);
		GameObject bankBooth = GameEntities.getNearest(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject gameObject) {
				if (gameObject.isValid()
						&& gameObject.getName() != null
						&& gameObject.getName().equalsIgnoreCase(
						OBJECT_BANK_NAME)) {
					if (banker.isValid()
							&& gameObject.distanceTo(banker
							.getLocation()) < 3) {
						return true;
					}
				}
				return false;
			}
		});
		if (banker.isValid() && (Configuration.getInstance().pattern().contains("USE_BANK_BOOTH_NO") || !bankBooth.isValid())) {
			if (banker.isOnScreen()) {
				banker.interact("Bank", banker.getName());
				for (int a = 0; a < 20 && !isOpen(); a++, Time.sleep(100, 150))
					;
				return true;
			} else {
				Walking.walkTo(banker);
				banker.turnTo();
				Time.sleep(new Condition() {
					@Override
					public boolean active() {
						return Players.getLocal().isMoving();
					}
				}, 3000);
			}
		} else if (bankBooth.isValid()) {
			if (bankBooth.isOnScreen()) {
				bankBooth.interact("Bank", OBJECT_BANK_NAME);
				for (int a = 0; a < 20 && !isOpen(); a++, Time.sleep(100, 150))
					;
				return true;
			} else {
				Walking.walkTo(bankBooth);
				bankBooth.turnTo();
				Time.sleep(new Condition() {
					@Override
					public boolean active() {
						return Players.getLocal().isMoving();
					}
				}, 3000);
			}
		}
		return false;
	}
}
