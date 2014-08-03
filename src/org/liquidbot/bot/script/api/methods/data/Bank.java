package org.liquidbot.bot.script.api.methods.data;

import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.wrappers.Item;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;
import org.liquidbot.bot.utils.Utilities;

import java.util.ArrayList;

/*
 * Created by Hiasat on 8/2/14
 */
public class Bank {

    private static final int BANK_INTERFACE = 12, BANK_ITEMS_PAD = 10, BANK_TITLE = 2, BANK_FULL_SPACE = 5, BANK_USED_SPACE = 3, BANK_INNER_INTERFACE = 1, BANK_CLOSE = 11;
    private static final String[] NPC_BANK_NAMES = {"Banker", "Ghost banker", "Banker tutor"};
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
                    Item item = new Item(BankI, widgetChild.getItemId(), widgetChild.getItemStack(), Item.Type.BANK, widgetChild.getArea());
                    if (item.isValid() && (filter == null || filter.accept(item))) {
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

    public static Item getItem(Filter<Item> filter) {
        Item[] allItems = getAllItems(filter);
        return allItems.length > 0 ? allItems[0] : null;
    }

    public static Item getItem(final int... ids) {
        if (ids == null)
            return null;
        return getItem(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && Utilities.inArray(item.getId(), ids);
            }
        });
    }

    public static Item getItem(final String... names) {
        if (names == null)
            return null;
        return getItem(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && item.getName() != null && Utilities.inArray(item.getName(), names);
            }
        });
    }

    public static boolean contains(final Filter<Item> filter) {
        return getItem(filter) != null;
    }

    public static boolean contains(final String... names) {
        return getItem(names) != null;
    }

    public static boolean contains(final int... ids) {
        return getItem(ids) != null;
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
        Item item = getItem(filter);
        return item != null ? item.getStackSize() : 0;
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
                return item.isValid() && item.getName() != null && Utilities.inArray(item.getName(), names);
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
        return Integer.parseInt(Widgets.get(BANK_INTERFACE, BANK_FULL_SPACE).getText());
    }

    public static int getUsedSpace() {
        if (Widgets.get(BANK_INTERFACE) == null)
            return -1;
        return Integer.parseInt(Widgets.get(BANK_INTERFACE, BANK_USED_SPACE).getText());
    }

    public static boolean isFull() {
        return !isOpen() && getUsedSpace() == getFullSpace();
    }

    public static boolean isOpen() {
        WidgetChild iFace = Widgets.get(BANK_INTERFACE, BANK_TITLE);
        if (iFace == null)
            return false;
        //Should check for Texts because it can be visible without be really visible
        return iFace.isVisible() && iFace.getText() != null && iFace.getText().toLowerCase().contains("the bank of runescape");
    }

    public static boolean close() {
        if (!isOpen())
            return true;
        WidgetChild closeButton = Widgets.get(BANK_INTERFACE, BANK_INNER_INTERFACE).getChild(BANK_CLOSE);
        return closeButton.isVisible() && closeButton.interact("Close");
    }


}
