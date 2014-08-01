package org.liquidbot.component.debug;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Calculations;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.api.wrappers.Player;

import java.awt.*;


/**
 * Created by Kenneth on 7/30/2014.
 */
public class PlayerDebugger extends Debugger<Player> {

    @Override
    public Player[] elements() {
        return Players.getAll(filter);
    }

    @Override
    public boolean activate() {
        return config.drawPlayers() && Game.isLoggedIn();
    }

    @Override
    public void render(Graphics graphics) {
        final FontMetrics metrics = graphics.getFontMetrics();
        for (Player player : refresh()) {
            Point point = player.getPointOnScreen();
            graphics.setColor(Color.yellow);
            graphics.fillRect((int) point.x, (int) point.y, 5, 5);
            graphics.setColor(Color.red);
            String format = player.getName() + " [Hp: " + player.getHealthPercent() + " Animation: " + player.getAnimation() + "]";
            graphics.drawString(format, point.x - (metrics.stringWidth(format)/2), point.y - 5);
        }
    }

    private Filter<Player> filter = new Filter<Player>() {
        @Override
        public boolean accept(Player player) {
            return player.isValid() && player.distanceTo() < 7 && player.isOnScreen();
        }
    };
}
