package org.liquidbot.bot.script.api.query;

import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.wrappers.NPC;

/**
 * Created on 7/29/2014.
 */
public class NPCQuery extends BasicQuery<NPCQuery, NPC> {

    @Override
    protected NPC[] elements() {
        return NPCs.getAll();
    }

    @Override
    public NPC nil() {
        return NPCs.nil();
    }
}
