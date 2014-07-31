package org.liquidbot.component.debug;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Calculations;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.wrappers.NPC;

import java.awt.*;

/**
 * Created by Kenneth on 7/30/2014.
 */
public class NPCDebugger extends Debugger<NPC> {

    @Override
    public NPC[] elements() {
        return NPCs.getAll();
    }

    @Override
    public boolean activate() {
        return Configuration.drawNPCs && Game.isLoggedIn();
    }

    @Override
    public void render(Graphics graphics) {

        final FontMetrics metrics = graphics.getFontMetrics();

        for(NPC npc : NPCs.query().refresh().within(7).filter(filter)) {
            final Point pt = Calculations.tileToScreen(npc.getLocation(), 0.5, 0.5, 0);
            final String format = npc.getName() + " [ID: " + npc.getId() + " Animation Id: " + npc.getAnimation() + "]";

            graphics.setColor(Color.PINK);
            graphics.fillRect(pt.x, pt.y, 5, 5);

            graphics.setColor(Color.WHITE);
            graphics.drawString(format, pt.x - (metrics.stringWidth(format) / 2), pt.y - 5);
        }
    }

    private Filter<NPC> filter = new Filter<NPC>() {
        @Override
        public boolean accept(NPC npc) {
            return npc.isValid() && npc.isOnScreen();
        }
    };
}
