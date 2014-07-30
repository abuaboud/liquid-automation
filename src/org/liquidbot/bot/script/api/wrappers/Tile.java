package org.liquidbot.bot.script.api.wrappers;/*
 * Created by Hiasat on 7/30/14
 */

import org.liquidbot.bot.script.api.interfaces.Interactable;
import org.liquidbot.bot.script.api.interfaces.Locatable;

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean interact(String action, String option) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean interact(String action) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean click(boolean left) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean click() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int distanceTo() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void turnTo() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Tile getLocation() {
        return this;
    }
}
