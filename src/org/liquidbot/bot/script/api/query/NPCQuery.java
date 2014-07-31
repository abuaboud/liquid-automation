package org.liquidbot.bot.script.api.query;

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.context.ClientContext;
import org.liquidbot.bot.script.api.wrappers.NPC;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class NPCQuery extends MobileIdNameQuery<NPCQuery, NPC>  {

    public NPCQuery(ClientContext ctx) {
        super(ctx);
    }

    @Override
    protected NPC[] elements() {
        final Object[] objects = (Object[]) Reflection.value("Client#getLocalNpcs()",null);
        final List<NPC> elements = new ArrayList<>();
        for(int i = 0 ; i < objects.length;i++){
           elements.add(new NPC(objects[i]));
        }
        return elements.toArray(new NPC[elements.size()]);
    }
}
