package org.liquidbot.bot.script.api.query;

import org.liquidbot.bot.script.api.context.ClientContext;
import org.liquidbot.bot.script.api.wrappers.NPC;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class NPCQuery extends AbstractQuery<NPCQuery, NPC> {

    public NPCQuery(ClientContext ctx) {
        super(ctx);
    }

    @Override
    protected NPC[] elements() {
        return new NPC[0];
    }
}
