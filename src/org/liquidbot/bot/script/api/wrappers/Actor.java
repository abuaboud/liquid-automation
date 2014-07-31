package org.liquidbot.bot.script.api.wrappers;/*
 * Created by Hiasat on 7/30/14
 */

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Locatable;
import org.liquidbot.bot.script.api.interfaces.Nameable;

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
     *  Max health for Actor only show when health bar visible
     *
     * @return Integer Max Health
     */
    public int getMaxHealth() {
        return (int) Reflection.value("Actor.getMaxHealth()", raw);
    }
    /**
     *  Current health for Actor only show when health bar visible
     *
     * @return Integer current Health
     */
    public int getHealth() {
        return (int) Reflection.value("Actor.getHealth()", raw);
    }
    /**
     *  the Height of Actor
     *
     * @return Integer Height
     */
    public int getHeight() {
        return (int) Reflection.value("Actor.getHeight()", raw);
    }

    /**
     * return real X location
     *
     * @return Integer x Location
     */
    public int getY() {
        return ((((int) Reflection.value("Actor.getY()", raw)) >> 7) + (int) Reflection.value("Client.getBaseY()", null));
    }
    /**
     * return real Y location
     *
     * @return Integer Y Location
     */
    public int getX() {
        return ((((int) Reflection.value("Actor.getX()", raw)) >> 7) + (int) Reflection.value("Client.getBaseX()", null));
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
