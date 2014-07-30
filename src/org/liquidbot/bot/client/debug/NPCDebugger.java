package org.liquidbot.bot.client.debug;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.wrappers.NPC;

import java.awt.*;
import java.util.List;


/**
 * Created by Kenneth on 7/30/2014.
 */
public class NPCDebugger extends Debugger<NPC> {

    @Override
    public NPC[] elements() {
        final List<NPC> list = Configuration.context.npcs.refresh().getList();
        return list.toArray(new NPC[list.size()]);
    }

    @Override
    public boolean activate() {
        return Configuration.drawNPCs;
    }

    @Override
    public void render(Graphics graphics) {
        for(NPC npc : elements()) {

        }
        graphics.drawString("NPCs not implemented.", 5, 20);
    }
}
