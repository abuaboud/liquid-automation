package org.liquidbot.bot.script.api.interfaces;

import org.liquidbot.bot.script.api.wrappers.Tile;

import java.awt.*;

/**
 * Created by Kenneth on 7/29/2014.
 */
public interface Locatable {

    public boolean isOnScreen();

    public Point getPointOnScreen();

    public Point getInteractPoint();

    public int distanceTo();

    public int distanceTo(Locatable locatable);

    public int distanceTo(Tile tile);

    public void turnTo();

    public Tile getLocation();


}
