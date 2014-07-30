package org.liquidbot.bot.script.api.query;

import org.liquidbot.bot.client.reflection.Reflection;
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
        Object[] objects = (Object[]) Reflection.value("Client#getLocalNpcs()",null);
        NPC[] elements = new NPC[objects.length];
        for(int i = 0 ; i < elements.length;i++){
           elements[i] = new NPC(objects[i]);
        }
        return elements;
    }
}
