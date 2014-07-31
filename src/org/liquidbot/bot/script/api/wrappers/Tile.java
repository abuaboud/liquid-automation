package org.liquidbot.bot.script.api.wrappers;/*
 * Created by Hiasat on 7/30/14
 */

import org.liquidbot.bot.Constants;
import org.liquidbot.bot.script.api.interfaces.Interactable;
import org.liquidbot.bot.script.api.interfaces.Locatable;
import org.liquidbot.bot.script.api.methods.data.Calculations;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;

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
     *
     * @return Integer : X Coordinate
     */
    public int getX() {
        return x;
    }
    /**
     *
     * @return Integer : Y Coordinate
     */
    public int getY() {
        return y;
    }
    /**
     *
     * @return Integer : Z Coordinate
     */
    public int getZ() {
        return z;
    }
    /**
     * @param action
     * @param option
     * @return boolean : true if interacted with this else false
     */
    @Override
    public boolean interact(String action, String option) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
    /**
     * @param action
     * @return boolean : true if interacted with this else false
     */
    @Override
    public boolean interact(String action) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
    /**
     * @param left
     * @return boolean : true if clicked on this else false
     */
    @Override
    public boolean click(boolean left) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     *
     * @return boolean : true if clicked on this else false
     */
    @Override
    public boolean click() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     *
     * @return boolean : true if this on viewPort else false
     */
    @Override
    public boolean isOnScreen() {
        return Constants.VIEWPORT.contains(getPointOnScreen());
    }

    /**
     *
     * @return Point: Point converted from WorldToScreen depend on X/Y
     */
    @Override
    public Point getPointOnScreen() {
        return Calculations.tileToScreen(this);
    }

    /**
     *
     * @return Point: Point used to interact
     */
    @Override
    public Point getInteractPoint() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int distanceTo() {
       return Calculations.distanceTo(this);
    }
    /**
     *
     * @param locatable
     * @return Integer : distance from this to locatable
     */
    @Override
    public int distanceTo(Locatable locatable) {
        return Calculations.distanceBetween(getLocation(), locatable.getLocation());
    }

    /**
     *
     * @param tile
     * @return Integer : distance from Player to this
     */
    @Override
    public int distanceTo(Tile tile) {
        return (int) Calculations.distanceBetween(getLocation(),tile);
    }

    /**
     * turn camera to this tile
     */
    @Override
    public void turnTo() {
        Camera.turnTo(this);
    }

    /**
     *
     * @return Tile : currentTile
     */
    @Override
    public Tile getLocation() {
        return this;
    }
}
