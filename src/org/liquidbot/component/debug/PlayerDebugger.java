package org.liquidbot.component.debug;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.wrappers.Player;

import java.awt.*;


/**
 * Created by Kenneth on 7/30/2014.
 */
public class PlayerDebugger extends Debugger<Player> {

    @Override
    public Player[] elements() {
        return new Player[0];
    }

    @Override
    public boolean activate() {
        return config.drawPlayers();
    }

    @Override
    public void render(Graphics graphics) {

    }
}
