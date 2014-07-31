package org.liquidbot.bot.script.api.wrappers;/*
 * Created by Hiasat on 7/30/14
 */

import org.liquidbot.bot.Constants;
import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Locatable;
import org.liquidbot.bot.script.api.interfaces.Nameable;
import org.liquidbot.bot.script.api.methods.data.Calculations;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;

import java.awt.*;

public class Actor implements Locatable {

    private final Object raw;

    public Actor(Object raw) {
        this.raw = raw;
    }

    /**
     * Returns the raw reflection object
     */
    protected Object getRaw() {
        return raw;
    }

    /**
     * This get you text Message that appear upon Actor Head
     *
     * @return Spoken Message Text that Actor Speak
     */
    public String getSpokenMessage() {
        return (String) Reflection.value("Actor#getSpokenMessage()", raw);
    }

    /**
     * This you get animation Id of Actor
     *
     * @return animation Id
     */
    public int getAnimation() {
        return (int) Reflection.value("Actor#getAnimation()", raw);
    }

    /**
     * Max health for Actor only show when health bar visible
     *
     * @return Integer Max Health
     */
    public int getMaxHealth() {
        return (int) Reflection.value("Actor#getMaxHealth()", raw);
    }

    /**
     * Current health for Actor only show when health bar visible
     *
     * @return Integer current Health
     */
    public int getHealth() {
        return (int) Reflection.value("Actor#getHealth()", raw);
    }

    /**
     * the Height of Actor
     *
     * @return Integer Height
     */
    public int getHeight() {
        return (int) Reflection.value("Actor#getHeight()", raw);
    }

    /**
     * return real Y location
     *
     * @return Integer Y Location
     */
    public int getY() {
        return ((((int) Reflection.value("Actor#getY()", raw)) >> 7) + (int) Reflection.value("Client#getBaseY()", null));
    }

    /**
     * return real X location
     *
     * @return Integer X Location
     */
    public int getX() {
        return ((((int) Reflection.value("Actor#getX()", raw)) >> 7) + (int) Reflection.value("Client#getBaseX()", null));
    }

    /**
     * Check if Actor is on screen
     *
     * @return Boolean: true if it's on Viewport else false
     */
    @Override
    public boolean isOnScreen() {
        return getLocation().isOnScreen();
    }

    /**
     * @return Point: Point converted from WorldToScreen depend on X/Y
     */
    @Override
    public Point getPointOnScreen() {
        return getLocation().getPointOnScreen();
    }

    /**
     * @return Point: Point used to interact
     */
    @Override
    public Point getInteractPoint() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * distance from local player
     *
     * @return Integer
     */
    @Override
    public int distanceTo() {
        return Calculations.distanceTo(getLocation());
    }

    /**
     * distance to specific Locatable
     *
     * @return Integer
     */
    @Override
    public int distanceTo(Locatable locatable) {
        return (int) Calculations.distanceBetween(getLocation(), locatable.getLocation());
    }

    /**
     * distance to specific tile
     *
     * @return Integer
     */
    @Override
    public int distanceTo(Tile tile) {
        return (int) Calculations.distanceBetween(getLocation(), tile);
    }

    /**
     * turn camera to this Actor
     */
    @Override
    public void turnTo() {
        Camera.turnTo(this);
    }

    /**
     * current Tile of this Actor
     *
     * @return Tile
     */
    @Override
    public Tile getLocation() {
        return new Tile(getX(), getY());  //To change body of implemented methods use File | Settings | File Templates.
    }
}
