package org.liquidbot.bot.client.debug;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.wrappers.NPC;

import java.awt.*;

/**
 * Created by Kenneth on 7/30/2014.
 */
public class NPCDebugger extends Debugger<NPC> {

    @Override
    public NPC[] elements() {
        return new NPC[0];
    }

    @Override
    public boolean activate() {
        return Configuration.drawNPCs;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawString("NPCs not implemented.", 5, 20);
    }
}
