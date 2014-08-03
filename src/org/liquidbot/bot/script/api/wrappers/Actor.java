package org.liquidbot.bot.script.api.wrappers;/*
 * Created by Hiasat on 7/30/14
 */

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.Constants;
import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Interactable;
import org.liquidbot.bot.script.api.interfaces.Locatable;
import org.liquidbot.bot.script.api.methods.data.*;
import org.liquidbot.bot.script.api.methods.data.Menu;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.utils.Utilities;

import java.awt.*;

public class Actor implements Locatable, Interactable {

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
     * @return int : 0-100% if health bar visible else 0%
     */
    public int getHealthPercent() {
        if (getHealth() == 0) return isInCombat() ? 0 : 100;
        return (int) ((double) getHealth() / getMaxHealth() * 100);
    }

    /**
     * @return boolean : if in combat return true else return false
     */
    public boolean isInCombat() {
        if (raw == null)
            return false;
        int LoopCycleStatus = ((int) Reflection.value("Client#getLoopCycle()", null)) - 130;
        int[] hitCycles = (int[]) Reflection.value("Actor#getHitCycles()", raw);
        for (final int loopCycle : hitCycles) {
            if (loopCycle > LoopCycleStatus) {
                return true;
            }
        }
        return false;
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
     * @return Polygon : returns bounds and cube around the actor
     */
    @Override
    public Polygon getBounds() {
        Polygon polygon = new Polygon();
        if (!isOnScreen())
            return null;
        int x = getLocation().x;
        int y = getLocation().y;
        int h = getHeight();
        int z = Game.getPlane();
        int tileByte = Walking.getTileFlags()[Game.getPlane()][getLocation().x - Game.getBaseX()][getLocation().y - Game.getBaseY()];
        if (this instanceof NPC) {
            NPC npc = (NPC) this;
            if (npc.getName() != null && npc.getName().toLowerCase().contains("fishing"))
                tileByte = 0;
        }
        double a = -0.25;
        double r = 0.25;
        Point pn = Calculations.tileToScreen(new Tile(x, y, z), r, r, tileByte == 1 ? 210 : 0);
        Point px = Calculations.tileToScreen(new Tile(x + 1, y, z), a, r, tileByte == 1 ? 210 : 0);
        Point py = Calculations.tileToScreen(new Tile(x, y + 1, z), r, a, tileByte == 1 ? 210 : 0);
        Point pxy = Calculations.tileToScreen(new Tile(x + 1, y + 1, z), a, a, tileByte == 1 ? 210 : 0);

        Point pnh = Calculations.tileToScreen(new Tile(x, y, z), r, r, tileByte == 1 ? 210 + h : h);
        Point pxh = Calculations.tileToScreen(new Tile(x + 1, y, z), a, r, tileByte == 1 ? 210 + h : h);
        Point pyh = Calculations.tileToScreen(new Tile(x, y + 1, z), r, a, tileByte == 1 ? 210 + h : h);
        Point pxyh = Calculations.tileToScreen(new Tile(x + 1, y + 1, z), a, a, tileByte == 1 ? 210 + h : h);

            polygon.addPoint(py.x, py.y);
            polygon.addPoint(pyh.x, pyh.y);

            polygon.addPoint(px.x, px.y);
            polygon.addPoint(pxh.x, pxh.y);

            polygon.addPoint(pxy.x, pxy.y);
            polygon.addPoint(pxyh.x, pxyh.y);

            polygon.addPoint(pn.x, pn.y);
            polygon.addPoint(pnh.x, pnh.y);
        return polygon;
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
        Polygon bounds = getBounds();
        if(bounds != null)
           return Utilities.generatePoint(bounds);
        return getPointOnScreen();
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

    @Override
    public boolean interact(String action, String option) {
        int index = Menu.index(action, option);
        for (int i = 0; i < 5; i++) {
            Point interactPoint = getInteractPoint();
            System.out.println(index);
            if (index > 0)
                break;
            if (Menu.isOpen() && index == -1)
                Menu.interact("Cancel");
            Mouse.move(interactPoint);
            Time.sleep(100, 150);
        }
        return index > 0 && Menu.interact(action, option);
    }

    @Override
    public boolean interact(String action) {
        return interact(action, null);
    }

    @Override
    public boolean click(boolean left) {
        Mouse.click(getInteractPoint(), left);
        return true;
    }

    @Override
    public boolean click() {
        Mouse.click(getInteractPoint(), true);
        return true;
    }
}
