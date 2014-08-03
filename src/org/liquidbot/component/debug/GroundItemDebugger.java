package org.liquidbot.component.debug;

import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Calculations;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.interactive.GroundItems;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.util.Timer;
import org.liquidbot.bot.script.api.wrappers.GroundItem;
import org.liquidbot.bot.script.api.wrappers.NPC;

import java.awt.*;
import java.util.Hashtable;

/**
 * Created by Kenneth on 7/30/2014.
 */
public class GroundItemDebugger extends Debugger<GroundItem> {

    @Override
    public GroundItem[] elements() {
        return GroundItems.getAll(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem) {
                return true;
            }
        });
    }

    @Override
    public boolean activate() {
        return config.drawGroundItems();
    }

    @Override
    public void render(Graphics2D graphics) {
            final FontMetrics metrics = graphics.getFontMetrics();
            for (GroundItem groundItem : refresh()) {
                if (groundItem.isValid() ){
                    Point point = groundItem.getPointOnScreen();
                    graphics.setColor(Color.white);
                    graphics.fillRect((int) point.x, (int) point.y, 5, 5);
                    graphics.setColor(Color.yellow);
                    String format = groundItem.getName() + " [ID: " + groundItem.getId() + "]";
                    graphics.drawString(format, point.x - (metrics.stringWidth(format)/2),point.y);

                }
            }
    }

    private Filter<GroundItem> filter = new Filter<GroundItem>() {
        @Override
        public boolean accept(GroundItem groundItem) {
            return groundItem.isValid() && groundItem.distanceTo() < 7 && groundItem.isOnScreen();
        }
    };
}
