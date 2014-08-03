package org.liquidbot.bot.script.api.methods.interactive;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.client.parser.FieldHook;
import org.liquidbot.bot.client.parser.HookReader;
import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.wrappers.GroundItem;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.api.wrappers.Tile;
import org.liquidbot.bot.utils.Utilities;

import java.lang.reflect.Field;
import java.util.ArrayList;

/*
 * Created by Hiasat on 7/31/14
 */
public class GroundItems {

    /**
     * @param filter
     * @return GroundItem[] : return all groundsitems in your region
     *         that applied to that filter
     */
    public static GroundItem[] getAll(Filter<GroundItem> filter) {
        ArrayList<GroundItem> groundItems = new ArrayList<GroundItem>();
        Field groundArray = Reflection.field("Client#getGroundArray()");
        Object[][][] groundArrayObjects = (Object[][][]) Reflection.value(groundArray, null);
        Field head = Reflection.field("DequeList#getHead()");
        Field next = Reflection.field("Node#getNext()");
        int z = Game.getPlane();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                Object nl = groundArrayObjects[z][x][y];
                if (nl != null) {
                    Object holder = Reflection.value(head, nl);
                    Object curNode = Reflection.value(next, holder);
                    while (curNode != null && curNode != holder && curNode != Reflection.value(head, nl)) {
                        GroundItem groundItem = new GroundItem(curNode, new Tile(Game.getBaseX() + x, Game.getBaseY() + y, Game.getPlane()));
                        if (filter == null || filter.accept(groundItem)) {
                            groundItems.add(groundItem);
                        }
                        curNode = Reflection.value(next, curNode);
                    }
                }
            }
        }
        return groundItems.toArray(new GroundItem[groundItems.size()]);
    }

    /**
     * Return all groundItems that have one of these names
     *
     * @param names
     * @return GroundItem[] : Get all gorunditems with any of these names
     */
    public static GroundItem[] getAll(final String... names) {
        return getAll(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem) {
                return groundItem.isValid() && groundItem.getName() != null && Utilities.inArray(groundItem.getName(), names);
            }
        });
    }

    /**
     * Return all groundItems that have one of these ids
     *
     * @param ids
     * @return GroundItem[] : Get all gorunditems with any of these ids
     */
    public static GroundItem[] getAll(final int... ids) {
        return getAll(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem) {
                return groundItem.isValid() && Utilities.inArray(groundItem.getId(), ids);
            }
        });
    }

    /**
     * @return GroundItem[]: return all grounditems in your region 104x104
     */
    public static GroundItem[] getAll() {
        return getAll(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem) {
                return true;
            }
        });
    }

    /**
     * @param filter
     * @return GroundItem : nearest groundItem to Local Player
     *         that apply to that filter
     */
    public static GroundItem getNearest(Filter<GroundItem> filter) {
        return getNearest(Players.getLocal().getLocation(), filter);
    }

    /**
     * @param filter
     * @return GroundItem : nearest groundItem to start tile
     *         that apply to that filter
     */
    public static GroundItem getNearest(Tile start, Filter<GroundItem> filter) {
        GroundItem closet = new GroundItem(null,null);
        int distance = 255;
        for (GroundItem groundItem : getAll(filter)) {
            if (groundItem.isValid() && distance > groundItem.distanceTo(start)) {
                closet = groundItem;
                distance = groundItem.distanceTo(start);
            }
        }
        return closet;
    }

    /**
     * Get closet GroundItem that has that Id or one of ids
     *
     * @param ids target GroundItem Id or Ids
     * @return GroundItem
     */
    public static GroundItem getNearest(final int... ids) {
        return getNearest(Players.getLocal().getLocation(), new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem) {
                return groundItem.isValid() && Utilities.inArray(groundItem.getId(), ids);
            }
        });
    }

    /**
     * Get closet GroundItem that has that name or one of names
     *
     * @param names target GroundItem name or names
     * @return GroundItem
     */
    public static GroundItem getNearest(final String... names) {
        return getNearest(Players.getLocal().getLocation(), new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem) {
                return groundItem.isValid() && Utilities.inArray(groundItem.getName(), names);
            }
        });
    }

    /**
     * @param tile
     * @return GroundItem : ground item that is in specific tile
     *         if there isn't it will return null
     */
    public static GroundItem getAt(final Tile tile) {
        return getNearest(Players.getLocal().getLocation(), new Filter<GroundItem>() {

            @Override
            public boolean accept(GroundItem obj) {
                return obj != null && tile.equals(obj.getLocation());
            }
        });
    }

}
