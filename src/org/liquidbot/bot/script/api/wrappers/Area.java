package org.liquidbot.bot.script.api.wrappers;

import org.liquidbot.bot.script.api.interfaces.Locatable;

import java.awt.*;
import java.util.ArrayList;

/*
 * Created by Hiasat on 8/2/14
 */
public class Area {


    public Polygon polygon;
    public int plane;
    private Tile[] tileCached = null;

    public Area(final Tile t1, final Tile t2) {
        this(new Tile(t1.x, t1.y, t1.getZ()), new Tile(t1.x, t2.y, t1.getZ()), new Tile(t2.x, t2.y, t2.getZ()), new Tile(t2.x, t1.y, t2.getZ()));
    }

    public Area(final Tile... bounds) {
        polygon = new Polygon();
        for (Tile t : bounds) {
            if (t != null) {
                polygon.addPoint(t.x, t.y);
            }
        }

        if (bounds.length > -1)
            this.plane = bounds[0].getZ();
    }

    public Rectangle getBounds() {
        return polygon.getBounds();
    }

    public Tile[] getTileArray() {
        if (tileCached != null)
            return tileCached;

        final Rectangle r = getBounds();
        final int x = r.x, y = r.y, width = r.width, height = r.height;
        final ArrayList<Tile> tiles = new ArrayList<Tile>();
        int xMax = x + width + 1, yMax = y + height + 1;
        for (int xx = x; xx < xMax; xx++) {
            for (int yy = y; yy < yMax; yy++) {
                tiles.add(new Tile(xx, yy, plane));
            }
        }

        tileCached = tiles.toArray(new Tile[tiles.size()]);
        return tileCached;
    }

    public Tile[] getBoundingTiles() {
        Tile[] bounds = new Tile[polygon.npoints];
        for (int i = 0; i < polygon.npoints; i++) {
            bounds[i] = new Tile(polygon.xpoints[i], polygon.ypoints[i], plane);
        }

        return bounds;
    }

    public boolean contains(final int x, final int y, final int plane) {
        return polygon.contains(x, y) && plane == this.plane;
    }

    public boolean contains(Tile t) {
        return contains(t.getX(), t.getY(), t.getZ());
    }

    public boolean contains(Locatable t) {
        return contains(t.getLocation());
    }

    public void draw(final Graphics g, final Color color) {
        tileCached = getTileArray();

        for (Tile tile : tileCached) {
            if (tile.isOnScreen())
                tile.draw(g, color);
        }
    }

    public void draw(final Graphics g) {
        draw(g, Color.white);
    }

}
