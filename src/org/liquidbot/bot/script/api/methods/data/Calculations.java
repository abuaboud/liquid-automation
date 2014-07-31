package org.liquidbot.bot.script.api.methods.data;

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Locatable;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.wrappers.Tile;

import java.awt.*;

/*
 * Created by Hiasat on 7/30/14
 */
public class Calculations {

    /**
     * The SIN values of a given set of angles.
     */
    public static int[] SINE = new int[2048];
    /**
     * The COS values of a given set of angles.
     */
    public static int[] COSINE = new int[2048];


    static {
        for (int i = 0; i < SINE.length; i++) {
            SINE[i] = (int) (65536.0D * Math.sin((double) i * 0.0030679615D));
            COSINE[i] = (int) (65536.0D * Math.cos((double) i * 0.0030679615D));
        }
    }

    /**
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @return distance between two Coordinate
     */
    public static int distanceBetween(int x, int y, int x1, int y1) {
        return  (int) Math.sqrt(Math.pow(x1 - x, 2) + Math.pow(y1 - y, 2));
    }

    /**
     * @param a first point
     * @param b second point
     * @return distance between two Coordinate
     */
    public static int distanceBetween(Point a, Point b) {
        return (int) distanceBetween(a.x, a.y, b.x, b.y);
    }

    /**
     * @param a first tile
     * @param b second tile
     * @return distance between two Coordinate
     */
    public static int distanceBetween(Tile a, Tile b) {
        return (int) distanceBetween(a.getX(), a.getY(), b.getX(), b.getY());
    }

    /**
     * @param a tile
     * @return current distance between player and specific tile
     */
    public static int distanceTo(Tile a) {
        final Tile loc = Players.getLocal().getLocation();
        return (int) distanceBetween(a.getX(), a.getY(), loc.getX(), loc.getY());
    }

    /**
     * @param a locatable
     * @return current distance between player and specific Actor/Object/Tile
     */
    public static int distanceTo(Locatable a) {
        final Tile loc = Players.getLocal().getLocation();
        return distanceBetween(a.getLocation().getX(), a.getLocation().getY(), loc.getX(), loc.getY());
    }

    /**
     * @param tile
     * @param dX     value to increase X for tile
     * @param dY     value to increase Y for tile
     * @param height
     * @return Point: return Point on screen if it's valid else return new Point(-1,-1)
     */
    public static Point tileToScreen(Tile tile, double dX, double dY, int height) {
        return worldToScreen((int) ((tile.getX() - Game.getBaseX() + dX) * 128), (int) ((tile.getY() - Game.getBaseY() + dY) * 128), height);
    }

    /**
     * @param tile
     * @return boolean : true if distance to tile less than 17 else false
     */
    public static boolean isOnMap(Tile tile) {
        return Calculations.distanceTo(tile) < 17;
    }

    /**
     * @param tile
     * @return Point: return tile point on screen
     */
    public static Point tileToScreen(Tile tile) {
        return worldToScreen((int) ((tile.getX() - Game.getBaseX() + 0.5) * 128), (int) ((tile.getY() - Game.getBaseY() + 0.5) * 128), 0);
    }

    /**
     * @param plane
     * @param x
     * @param y
     * @return Integer: check tile height
     */
    public static int getTileHeight(int plane, int x, int y) {
        int xx = x >> 7;
        int yy = y >> 7;
        if (xx < 0 || yy < 0 || xx > 103 || yy > 103) {
            return 0;
        }
        int[][][] tileHeights = (int[][][]) Reflection.value("Client#getTileHeights()", null);
        int aa = tileHeights[plane][xx][yy] * (128 - (x & 0x7F))
                + tileHeights[plane][xx + 1][yy] * (x & 0x7F) >> 7;

        int ab = tileHeights[plane][xx][yy + 1]
                * (128 - (x & 0x7F))
                + tileHeights[plane][xx + 1][yy + 1]
                * (x & 0x7F) >> 7;
        return aa * (128 - (y & 0x7F)) + ab * (y & 0x7F) >> 7;
    }

    /**
     *
     * @param regionX
     * @param regionY
     * @param height
     * @return Point : Convert from tile to point on screen
     */
    public static Point worldToScreen(int regionX, int regionY, int height) {
        if (regionX < 128 || regionY < 128 || regionX > 13056 || regionY > 13056) {
            return new Point(-1, -1);
        }

        int z = getTileHeight(Game.getPlane(), regionX, regionY) - height;
        regionX -= Camera.getX();
        z -= Camera.getZ();
        regionY -= Camera.getY();

        int yaw = (int) Reflection.value("Client#getYaw()", null);
        int pitch = (int) Reflection.value("Client#getPitch()", null);

        int pitch_sin = SINE[pitch];
        int pitch_cos = COSINE[pitch];
        int yaw_sin = SINE[yaw];
        int yaw_cos = COSINE[yaw];

        int _angle = regionY * yaw_sin + regionX * yaw_cos >> 16;

        regionY = regionY * yaw_cos - regionX * yaw_sin >> 16;
        regionX = _angle;
        _angle = z * pitch_cos - regionY * pitch_sin >> 16;
        regionY = z * pitch_sin + regionY * pitch_cos >> 16;


        if (regionY >= 50) {
            return new Point(258 + (regionX << 9) / regionY, (_angle << 9) / regionY + 170);
        }

        return new Point(-1, -1);
    }

    /**
     *
     * @param regionX
     * @param regionY
     * @return Point : Convert from tile to point on map
     */
    public static Point worldToMap(int regionX, int regionY) {
        int mapScale = (int)  Reflection.value("Client#getMapScale()",null);
        int mapOffset = (int)  Reflection.value("Client#getMapOffset()",null);
        int angle = Camera.getMapAngle() + mapScale & 0x7FF;
        int j = regionX * regionX + regionY * regionY;

        if (j > 6400)
            return new Point(-1, -1);

        int sin = Calculations.SINE[angle] * 256 / (mapOffset + 256);
        int cos = Calculations.COSINE[angle] * 256 / (mapOffset + 256);

        int xMap = regionY * sin + regionX * cos >> 16;
        int yMap = regionY * cos - regionX * sin >> 16;

        return new Point(644 + xMap, 80 - yMap);
    }

    /**
     *
     * @param tile
     * @return Point : convert Local player coordinates to point on map
     */
    public static Point tileToMap(Tile tile) {
        int xMapTile = tile.getX() - Game.getBaseX();
        int yMapTile = tile.getY() - Game.getBaseY();
        Object myPlayer = Reflection.value("Client#getMyPlayer()",null);
        return worldToMap((xMapTile * 4 + 2) - (int) Reflection.value("Actor#getX()",myPlayer) / 32, (yMapTile * 4 + 2) - (int) Reflection.value("Actor#getY()",myPlayer) / 32);
    }
}
