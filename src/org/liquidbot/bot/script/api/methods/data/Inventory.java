package org.liquidbot.bot.script.api.methods.data;

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.wrappers.Item;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;
import org.liquidbot.bot.utils.Utilities;

import java.awt.*;
import java.util.ArrayList;

/*
 * Created by Hiasat on 8/2/14
 */
public class Inventory {

    private static final int WIDGET_INVENTORY_INDEX = 149;
    private static final int WIDGET_INVENTORY_SLOTS = 0;

    public static Item[] getAllItems(Filter<Item> filter) {
        java.util.List<Item> list = new ArrayList<>();
        final WidgetChild child = Widgets.get(WIDGET_INVENTORY_INDEX, WIDGET_INVENTORY_SLOTS);
        if (!child.isVisible())
            return list.toArray(new Item[list.size()]);
        final int[] contentIds = child.getSlotContentIds();
        final int[] stackSizes = child.getStackSizes();
        if (contentIds == null || stackSizes == null)
            return list.toArray(new Item[list.size()]);
        for (int itemIndex = 0; itemIndex < contentIds.length; itemIndex++) {
            Item item = new Item(itemIndex, contentIds[itemIndex], stackSizes[itemIndex], Item.Type.INVENTORY, new Rectangle(getLocation(itemIndex).x - 2, getLocation(itemIndex).y - 2, 4, 4));
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

    public static Item getItem(Filter<Item> filter) {
        Item[] items = getAllItems(filter);
        if (items == null)
            return null;
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

    public static String getSelectedItemName() {
        return (String) Reflection.value("Client.getSelectedItem()", null);
    }

    public static boolean isItemSelected() {
        return getSelectedItemName() != null;
    }

    public static int getUsedSpace() {
        return getAllItems().length;
    }

    public static int getFreeSpace() {
        return 28 - getAllItems().length;
    }

    public static boolean isFull() {
        return getUsedSpace() == 28;
    }

    public static boolean isEmpty() {
        return getUsedSpace() == 0;
    }

    public static int getCount(boolean countStackSize, Filter<Item> filter) {
        int count = 0;
        for (Item item : getAllItems(filter)) {
            count = count + (countStackSize ? item.getStackSize() : 1);
        }
        return count;
    }

    public static int getCount(boolean countStackSize, final String... names) {
        if (names == null)
            return 0;
        return getCount(countStackSize, new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && item.getName() != null && Utilities.inArray(item.getName(), names);
            }
        });
    }

    public static int getCount(boolean countStackSize, final int... ids) {
        if (ids == null)
            return 0;
        return getCount(countStackSize, new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && Utilities.inArray(item.getId(), ids);
            }
        });
    }

    public static int getCount(final int... ids) {
        return getCount(false, ids);
    }

    public static int getCount(final String... names) {
        return getCount(false, names);
    }

    public static int getCount(Filter<Item> filter) {
        return getCount(false, filter);
    }

    public static boolean contains(Filter<Item> filter) {
        return getItem(filter) != null;
    }

    public static boolean contains(int... ids) {
        return getItem(ids) != null;
    }

    public static boolean contains(String... names) {
        return getItem(names) != null;
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

    public static Point getLocation(int slot) {
        int col = (slot % 4);
        int row = (slot / 4);
        int x = 580 + (col * 42);
        int y = 228 + (row * 36);
        return new Point(x, y);
    }
}
