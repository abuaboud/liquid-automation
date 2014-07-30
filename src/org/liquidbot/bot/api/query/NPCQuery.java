package org.liquidbot.bot.api.query;

import org.liquidbot.bot.api.context.ClientContext;
import org.liquidbot.bot.api.wrappers.NPC;

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
