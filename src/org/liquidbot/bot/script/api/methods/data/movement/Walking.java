package org.liquidbot.bot.script.api.methods.data.movement;

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.enums.Tab;
import org.liquidbot.bot.script.api.interfaces.Condition;
import org.liquidbot.bot.script.api.interfaces.Locatable;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.Settings;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.LocalPath;
import org.liquidbot.bot.script.api.wrappers.Tile;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;

/*
 * Created on 8/2/14
 */
public class Walking {

    private static final int WIDGET_ORB = 548, WIDGET_ORB_ICON = 93, WIDGET_SETTING = 261, WIDGET_SETTING_ICON = 53;

    private static int[][] flags;
    private static int offX, offY;

    public static interface Flag {
        static final int WALL_NORTHWEST = 0x1;
        static final int WALL_NORTH = 0x2;
        static final int WALL_NORTHEAST = 0x4;
        static final int WALL_EAST = 0x8;
        static final int WALL_SOUTHEAST = 0x10;
        static final int WALL_SOUTH = 0x20;
        static final int WALL_SOUTHWEST = 0x40;
        static final int WALL_WEST = 0x80;

        static final int OBJECT_TILE = 0x100;

        static final int WALL_BLOCK_NORTHWEST = 0x200;
        static final int WALL_BLOCK_NORTH = 0x400;
        static final int WALL_BLOCK_NORTHEAST = 0x800;
        static final int WALL_BLOCK_EAST = 0x1000;
        static final int WALL_BLOCK_SOUTHEAST = 0x2000;
        static final int WALL_BLOCK_SOUTH = 0x4000;
        static final int WALL_BLOCK_SOUTHWEST = 0x8000;
        static final int WALL_BLOCK_WEST = 0x10000;

        static final int OBJECT_BLOCK = 0x20000;
        static final int DECORATION_BLOCK = 0x40000;

        static final int WALL_ALLOW_RANGE_NORTHWEST = 0x400000;
        static final int WALL_ALLOW_RANGE_NORTH = 0x800000;
        static final int WALL_ALLOW_RANGE_NORTHEAST = 0x1000000;
        static final int WALL_ALLOW_RANGE_EAST = 0x2000000;
        static final int WALL_ALLOW_RANGE_SOUTHEAST = 0x4000000;
        static final int WALL_ALLOW_RANGE_SOUTH = 0x8000000;
        static final int WALL_ALLOW_RANGE_SOUTHWEST = 0x10000000;
        static final int WALL_ALLOW_RANGE_WEST = 0x20000000;

        static final int OBJECT_ALLOW_RANGE = 0x40000000;

        static final int BLOCKED = 0x1280100;
    }

    public static Tile getCollisionOffset(final int plane) {
        return new Tile(0,0, plane);
    }

    public static int[][] getCollisionFlags(int plane) {
        final Object collisionMap = ((Object[]) Reflection.value("Client#getCollisionMaps()", null))[plane];
        return (int[][]) Reflection.value("CollisionMap#getFlags()", collisionMap);
    }

    public static byte[][][] getTileFlags() {
        return (byte[][][]) Reflection.value("Client#getTileFlags()", null);
    }

    public static int[][][] getTileHeights() {
        return (int[][][]) Reflection.value("Client#getTileHeights()", null);
    }

    public static void walkTo(Locatable target) {
        walkTo(target.getLocation());
    }

    public static void walkTo(Tile target) {
	    if(!Game.isLoggedIn())
		    return;
        Tile step = getClosestTileOnMap(target);
        if (step.isOnMap())
            step.clickOnMap();
    }

    public static Tile getClosestTileOnMap(Tile current) {
        if (!current.isOnMap()) {
            Tile loc = Players.getLocal().getLocation();
            Tile walk = new Tile((loc.getX() + current.getX()) / 2, (loc.getY() + current.getY()) / 2);
            return walk.isOnMap() ? walk : getClosestTileOnMap(walk);
        }
        return current;
    }

    /**
     * Get the running percent.
     *
     * @return Integer
     */
    public static int getEnergy() {
        if (!Game.isLoggedIn())
            return -1;
        return (int) Reflection.value("Client#getEnergy()", null);
    }

    /**
     * Check if running is enabled.
     *
     * @return Boolean
     */
    public static boolean isRunning() {
        return Settings.get(173) == 1;
    }

    /**
     * Check if orbs are enabled.
     *
     * @return Boolean
     */
    public static boolean isUsingOrb() {
        return Settings.get(1055) > 0;
    }

    /**
     * set Running option
     *
     * @param run : true if want set turn on running else false
     */
    public static void setRun(final boolean run) {
        if (isRunning() != run) {
            if (isUsingOrb()) {
                final WidgetChild widgetChild = Widgets.get(WIDGET_ORB, WIDGET_ORB_ICON);
                if (widgetChild.isVisible())
                    widgetChild.click();
            } else {
                if (!Tab.SETTINGS.isOpen())
                    Tab.SETTINGS.open();
                final WidgetChild widgetChild = Widgets.get(WIDGET_SETTING, WIDGET_SETTING_ICON);
                if (widgetChild.isVisible())
                    widgetChild.click();
            }
            Time.sleep(new Condition() {
                @Override
                public boolean active() {
                    return isRunning() != run;
                }
            }, 4000);
        }
    }

    public static LocalPath findLocalPath(Tile start, Tile end) {
        return new LocalPath(start, end);
    }

    public static LocalPath findLocalPath(Tile end) {
        return new LocalPath(Players.getLocal().getLocation(), end);
    }
}
