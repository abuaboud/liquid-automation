package org.liquidbot.bot.script.api.methods.interactive;

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
        Field head = Reflection.field("DequeList#getHead()");
        Field next = Reflection.field("Node#getNext()");
        Field id = Reflection.field("Item#getId()");
        Field stackSize = Reflection.field("Item#getStackSize()");
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                Object nl = ((Object[][][]) Reflection.value(groundArray, null))[Game.getPlane()][x][y];
                if (nl != null) {
                    Object holder = Reflection.value(head, nl);
                    Object curNode = Reflection.value(next, holder);

                    while (curNode != null && curNode != holder && curNode != Reflection.value(head, nl)) {

                        GroundItem groundItem = new GroundItem((int)Reflection.value("Item#getId()",id,curNode),(int)Reflection.value("Item#getStackSize()",stackSize,curNode), new Tile(Game.getBaseX() + x, Game.getBaseY() + y, Game.getPlane()));
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
     * @return GroundItem[]: return all grounditems in your region 104x104
     */
    public static GroundItem[] getAll() {
        return getAll(null);
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
        GroundItem closet = null;
        int distance = 255;
        for (GroundItem groundItem : getAll(filter)) {
            if (groundItem.isValid() && distance > groundItem.distanceTo(groundItem.getLocation())) {
                closet = groundItem;
            }
        }
        return closet;
    }

    /**
     * Get closet GroundItem that has that Id or one of ids
     *
     * @param ids target GroundItem Id or Ids
     * @return NPC
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
