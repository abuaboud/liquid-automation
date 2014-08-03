package org.liquidbot.bot.script.api.methods.data.movement;

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.wrappers.Tile;

/*
 * Created by Hiasat on 8/2/14
 */
public class Walking {

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
        final Object collisionMap = ((Object[]) Reflection.value("Client#getCollisionMaps()",null))[plane];
        return new Tile((int)Reflection.value("CollisionMap#getOffsetX()",collisionMap),(int) Reflection.value("CollisionMagetOffsetY()",collisionMap), plane);
    }

    public static int[][] getCollisionFlags(int plane) {
        final Object collisionMap = ((Object[]) Reflection.value("Client#getCollisionMaps()",null))[plane];
        return (int[][]) Reflection.value( "CollisionMap#getFlags()",collisionMap);
    }

    public static byte[][][] getTileFlags() {
        return ( byte[][][]) Reflection.value("Client#getTileFlags()",null);
    }

    public static int[][][] getTileHeights() {
        return ( int[][][]) Reflection.value("Client#getTileHeights()",null);
    }

    public static void walkTo(Tile target){
       Tile step = getClosestTileOnMap(target);
       if(step.isOnMap())
           step.clickOnMap();
    }

    public static Tile getClosestTileOnMap(Tile current){
        if (!current.isOnMap()) {
            Tile loc = Players.getLocal().getLocation();
            Tile walk = new Tile((loc.getX() + current.getX()) / 2, (loc.getY() + current.getY()) / 2);
            return walk.isOnMap() ? walk : getClosestTileOnMap(walk);
        }
        return current;
    }
}
