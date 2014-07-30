package org.liquidbot.bot.script.api.context;

import org.liquidbot.bot.script.api.query.NPCQuery;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class ClientContext {

    public final NPCQuery npcs;

    public ClientContext() {
        this.npcs = new NPCQuery(this);
    }

}
