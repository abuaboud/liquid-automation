package org.liquidbot.bot.script.api.methods.data;

import org.liquidbot.bot.script.api.wrappers.Tile;

import java.awt.*;

/*
 * Created by Hiasat on 7/30/14
 */
public class Calculations {

    public Calculations() {

    }

    public double distanceBetween(int x, int y, int x1, int y1) {
        return Math.sqrt(Math.pow(x1 - x, 2) + Math.pow(y1 - y, 2));
    }

    public double distanceBetween(Point a, Point b) {
        return distanceBetween(a.x, a.y, b.x, b.y);
    }
    public double distanceBetween(Tile a, Tile b) {
        return distanceBetween(a.getX(), a.getY(), b.getX(), b.getY());
    }


}
