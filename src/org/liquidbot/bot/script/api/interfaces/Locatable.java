package org.liquidbot.bot.script.api.interfaces;

import org.liquidbot.bot.script.api.wrappers.Tile;

/**
 * Created by Kenneth on 7/29/2014.
 */
public interface Locatable {

    public int distanceTo();

    public void turnTo();

    public Tile getLocation();


}
