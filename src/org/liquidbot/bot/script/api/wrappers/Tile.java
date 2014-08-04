package org.liquidbot.bot.script.api.wrappers;/*
 * Created by Hiasat on 7/30/14
 */

import org.liquidbot.bot.Constants;
import org.liquidbot.bot.script.api.interfaces.Interactable;
import org.liquidbot.bot.script.api.interfaces.Locatable;
import org.liquidbot.bot.script.api.methods.data.Calculations;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.util.Time;

import java.awt.*;

public class Tile implements Locatable, Interactable {
    int x;
    int y;
    int z;

    public Tile(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    /**
     * @return Integer : X Coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * @return Integer : Y Coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * @return Integer : Z Coordinate
     */
    public int getZ() {
        return z;
    }

    public void clickOnMap() {
        if (isOnMap()) {
            Mouse.click(getPointOnMap(), true);
            Time.sleep(100, 150);
        }
    }

    /**
     * @param action
     * @param option
     * @return boolean : true if interacted with this else false
     */

    @Override
    public boolean interact(String action, String option) {
        int menuIndex = -1;
        for (int i = 0; i < 5; i++) {
            menuIndex = org.liquidbot.bot.script.api.methods.data.Menu.index(action, option);
            Point interactPoint = getInteractPoint();
            if (menuIndex > -1)
                break;
            if (org.liquidbot.bot.script.api.methods.data.Menu.isOpen() && menuIndex == -1)
                org.liquidbot.bot.script.api.methods.data.Menu.interact("Cancel");
            Mouse.move(interactPoint);
            Time.sleep(100, 150);
        }
        return menuIndex > -1 && org.liquidbot.bot.script.api.methods.data.Menu.interact(action, option);
    }


    /**
     * @param action
     * @return boolean : true if interacted with this else false
     */
    @Override
    public boolean interact(String action) {
        return interact(action, null);
    }

    /**
     * @param left
     * @return boolean : true if clicked on this else false
     */
    @Override
    public boolean click(boolean left) {
        Mouse.click(left);
        return true;
    }

    /**
     * @return boolean : true if clicked on this else false
     */
    @Override
    public boolean click() {
        Mouse.click(true);
        return true;
    }

    @Override
    public Polygon getBounds() {
        if (!isOnScreen())
            return null;
        Polygon polygon = new Polygon();
        Point pn = Calculations.tileToScreen(new Tile(x, y, z), 0, 0, 0);
        Point px = Calculations.tileToScreen(new Tile(x + 1, y, z), 0, 0, 0);
        Point py = Calculations.tileToScreen(new Tile(x, y + 1, z), 0, 0, 0);
        Point pxy = Calculations.tileToScreen(new Tile(x + 1, y + 1, z), 0, 0, 0);
        polygon.addPoint(pn.x, pn.y);
        polygon.addPoint(px.x, px.y);
        polygon.addPoint(py.x, py.y);
        polygon.addPoint(pxy.x, pxy.y);
        return polygon;
    }

    /**
     * @return boolean : true if this on viewPort else false
     */
    @Override
    public boolean isOnScreen() {
        return Constants.VIEWPORT.contains(getPointOnScreen());
    }

    /**
     * @return Point: Point converted from WorldToScreen depend on X/Y
     */
    @Override
    public Point getPointOnScreen() {
        return Calculations.tileToScreen(this);
    }

    /**
     * @return Point: Point used to interact
     */
    @Override
    public Point getInteractPoint() {
        return getPointOnScreen();
    }

    @Override
    public int distanceTo() {
        return Calculations.distanceTo(this);
    }

    /**
     * @param locatable
     * @return Integer : distance from this to locatable
     */
    @Override
    public int distanceTo(Locatable locatable) {
        return Calculations.distanceBetween(getLocation(), locatable.getLocation());
    }

    /**
     * @param tile
     * @return Integer : distance from Player to this
     */
    @Override
    public int distanceTo(Tile tile) {
        return (int) Calculations.distanceBetween(getLocation(), tile);
    }

    /**
     * turn camera to this tile
     */
    @Override
    public void turnTo() {
        Camera.turnTo(this);
    }

    @Override
    public String toString() {
        return "[" + this.x + "," + this.y + "," + this.z + "]";
    }

    @Override
    public boolean equals(Object a) {
        if (a != null && a instanceof Tile) {
            Tile t = (Tile) a;
            return t.x == this.x && t.y == this.y;
        }
        return false;
    }

    /**
     * @return Tile : currentTile
     */
    @Override
    public Tile getLocation() {
        return this;
    }

    @Override
    public void draw(Graphics2D g, Color color) {
        g.setColor(color);
        g.drawPolygon(getBounds());
    }

    @Override
    public void draw(Graphics2D g) {
        draw(g, Color.WHITE);
    }

    public Point getPointOnMap() {
        return Calculations.tileToMap(this);
    }

    public boolean isOnMap() {
        return Calculations.isOnMap(this);
    }

    public boolean isWalkable() {
        int[][] flags = Walking.getCollisionFlags(Game.getPlane());
        int value = flags[(getX() - Game.getBaseX())][(getY() - Game.getBaseY())];
        return (value & 0x1280180) == 0 ^ (value & 0x1280180) == 128;
    }

    public Tile derive(int x, int y) {
        return this.derive(x, y, 0);
    }

    public Tile derive(int x, int y, int plane) {
        return new Tile(this.getX() + x, this.getY() + y, this.getZ() + plane);
    }

    public static Tile derive(Tile t, int x, int y) {
        return derive(t, x, y, 0);
    }

    public static Tile derive(Tile t, int x, int y, int plane) {
        return new Tile(t.getX() + x, t.getY() + y, t.getZ() + plane);
    }

    public void draw(final Graphics g) {
        draw(g, Color.WHITE);
    }

    public void draw(final Graphics g, final Color color) {
        if (Game.isLoggedIn() && isOnScreen()) {
            Point pn = Calculations.tileToScreen(new Tile(x, y, z), 0, 0, 0);
            Point px = Calculations.tileToScreen(new Tile(x + 1, y, z), 0, 0, 0);
            Point py = Calculations.tileToScreen(new Tile(x, y + 1, z), 0, 0, 0);
            Point pxy = Calculations.tileToScreen(new Tile(x + 1, y + 1, z), 0, 0, 0);
            if (Constants.VIEWPORT.contains(py) && Constants.VIEWPORT.contains(pxy) && Constants.VIEWPORT.contains(px) && Constants.VIEWPORT.contains(pn)) {
                g.setColor(color.darker());
                g.drawPolygon(new int[]{py.x, pxy.x, px.x, pn.x}, new int[]{py.y, pxy.y, px.y, pn.y}, 4);
                //g.setColor(color);
                g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
                g.fillPolygon(new int[]{py.x, pxy.x, px.x, pn.x}, new int[]{py.y, pxy.y, px.y, pn.y}, 4);
            }
        }
    }

    public boolean canReach(Tile loc) {
        return dijkstraDist(loc.x - Game.getBaseX(), loc.y - Game.getBaseY(), x - Game.getBaseX(), y - Game.getBaseY(), true) != -1;
    }

    public boolean canReach() {
        return canReach(Players.getLocal().getLocation());
    }

    private int dijkstraDist(final int startX, final int startY, final int destX, final int destY, final boolean isObject) {
        final int[][] prev = new int[104][104];
        final int[][] dist = new int[104][104];
        final int[] path_x = new int[4000];
        final int[] path_y = new int[4000];

        for (int xx = 0; xx < 104; xx++) {
            for (int yy = 0; yy < 104; yy++) {
                prev[xx][yy] = 0;
                dist[xx][yy] = 99999999;
            }
        }

        int curr_x = startX;
        int curr_y = startY;
        prev[startX][startY] = 99;
        dist[startX][startY] = 0;
        int path_ptr = 0;
        int step_ptr = 0;
        path_x[path_ptr] = startX;
        path_y[path_ptr++] = startY;
        final int blocks[][] = Walking.getCollisionFlags(Game.getPlane());
        final int pathLength = path_x.length;
        boolean foundPath = false;
        while (step_ptr != path_ptr) {
            curr_x = path_x[step_ptr];
            curr_y = path_y[step_ptr];

            if (isObject) {
                if (((curr_x == destX) && (curr_y == destY + 1)) ||
                        ((curr_x == destX) && (curr_y == destY - 1)) ||
                        ((curr_x == destX + 1) && (curr_y == destY)) ||
                        ((curr_x == destX - 1) && (curr_y == destY))) {
                    foundPath = true;
                    break;
                }
            } else if ((curr_x == destX) && (curr_y == destY)) {
                foundPath = true;
            }

            step_ptr = (step_ptr + 1) % pathLength;
            final int cost = dist[curr_x][curr_y] + 1;

            // south
            if ((curr_y > 0) && (prev[curr_x][curr_y - 1] == 0) && ((blocks[curr_x][curr_y - 1] & 0x1280102) == 0)) {
                path_x[path_ptr] = curr_x;
                path_y[path_ptr] = curr_y - 1;
                path_ptr = (path_ptr + 1) % pathLength;
                prev[curr_x][curr_y - 1] = 1;
                dist[curr_x][curr_y - 1] = cost;
            }
            // west
            if ((curr_x > 0) && (prev[curr_x - 1][curr_y] == 0) && ((blocks[curr_x - 1][curr_y] & 0x1280108) == 0)) {
                path_x[path_ptr] = curr_x - 1;
                path_y[path_ptr] = curr_y;
                path_ptr = (path_ptr + 1) % pathLength;
                prev[curr_x - 1][curr_y] = 2;
                dist[curr_x - 1][curr_y] = cost;
            }
            // north
            if ((curr_y < 104 - 1) && (prev[curr_x][curr_y + 1] == 0) && ((blocks[curr_x][curr_y + 1] & 0x1280120) == 0)) {
                path_x[path_ptr] = curr_x;
                path_y[path_ptr] = curr_y + 1;
                path_ptr = (path_ptr + 1) % pathLength;
                prev[curr_x][curr_y + 1] = 4;
                dist[curr_x][curr_y + 1] = cost;
            }
            // east
            if ((curr_x < 104 - 1) && (prev[curr_x + 1][curr_y] == 0) && ((blocks[curr_x + 1][curr_y] & 0x1280180) == 0)) {
                path_x[path_ptr] = curr_x + 1;
                path_y[path_ptr] = curr_y;
                path_ptr = (path_ptr + 1) % pathLength;
                prev[curr_x + 1][curr_y] = 8;
                dist[curr_x + 1][curr_y] = cost;
            }
            // south west
            if ((curr_x > 0) && (curr_y > 0) && (prev[curr_x - 1][curr_y - 1] == 0) && ((blocks[curr_x - 1][curr_y - 1] & 0x128010e) == 0) && ((blocks[curr_x - 1][curr_y] & 0x1280108) == 0) && ((blocks[curr_x][curr_y - 1] & 0x1280102) == 0)) {
                path_x[path_ptr] = curr_x - 1;
                path_y[path_ptr] = curr_y - 1;
                path_ptr = (path_ptr + 1) % pathLength;
                prev[curr_x - 1][curr_y - 1] = 3;
                dist[curr_x - 1][curr_y - 1] = cost;
            }
            // north west
            if ((curr_x > 0) && (curr_y < 104 - 1) && (prev[curr_x - 1][curr_y + 1] == 0) && ((blocks[curr_x - 1][curr_y + 1] & 0x1280138) == 0) && ((blocks[curr_x - 1][curr_y] & 0x1280108) == 0) && ((blocks[curr_x][curr_y + 1] & 0x1280120) == 0)) {
                path_x[path_ptr] = curr_x - 1;
                path_y[path_ptr] = curr_y + 1;
                path_ptr = (path_ptr + 1) % pathLength;
                prev[curr_x - 1][curr_y + 1] = 6;
                dist[curr_x - 1][curr_y + 1] = cost;
            }
            // south east
            if ((curr_x < 104 - 1) && (curr_y > 0) && (prev[curr_x + 1][curr_y - 1] == 0) && ((blocks[curr_x + 1][curr_y - 1] & 0x1280183) == 0) && ((blocks[curr_x + 1][curr_y] & 0x1280180) == 0) && ((blocks[curr_x][curr_y - 1] & 0x1280102) == 0)) {
                path_x[path_ptr] = curr_x + 1;
                path_y[path_ptr] = curr_y - 1;
                path_ptr = (path_ptr + 1) % pathLength;
                prev[curr_x + 1][curr_y - 1] = 9;
                dist[curr_x + 1][curr_y - 1] = cost;
            }
            // north east
            if ((curr_x < 104 - 1) && (curr_y < 104 - 1) && (prev[curr_x + 1][curr_y + 1] == 0) && ((blocks[curr_x + 1][curr_y + 1] & 0x12801e0) == 0) && ((blocks[curr_x + 1][curr_y] & 0x1280180) == 0) && ((blocks[curr_x][curr_y + 1] & 0x1280120) == 0)) {
                path_x[path_ptr] = curr_x + 1;
                path_y[path_ptr] = curr_y + 1;
                path_ptr = (path_ptr + 1) % pathLength;
                prev[curr_x + 1][curr_y + 1] = 12;
                dist[curr_x + 1][curr_y + 1] = cost;
            }
        }
        if (foundPath) {
            return dist[curr_x][curr_y];
        }
        return -1;
    }

}
