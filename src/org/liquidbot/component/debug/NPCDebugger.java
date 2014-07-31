package org.liquidbot.component.debug;

import org.liquidbot.bot.Configuration;
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
        for (NPC npc : refresh()) {
            if (npc.isValid() && npc.isOnScreen() && npc.distanceTo() < 7) {
                FontMetrics fontMetrics = graphics.getFontMetrics();
                Point point = Calculations.tileToScreen(npc.getLocation(), 0.5, 0.5, 0);
                graphics.setColor(Color.PINK);
                graphics.fillRect((int) point.x, (int) point.y, 5, 5);
                graphics.setColor(Color.WHITE);
                String format = npc.getName() + " [ID: " + npc.getId() + " Animation Id: " + npc.getAnimation() + "]";
                graphics.drawString(format, point.x - (fontMetrics.stringWidth(format) / 2), point.y - 5);
            }
        }
    }
}
