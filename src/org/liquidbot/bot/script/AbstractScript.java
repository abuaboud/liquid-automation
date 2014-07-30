package org.liquidbot.bot.script;

import org.liquidbot.bot.script.api.context.ClientContext;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class AbstractScript {

    public final ClientContext ctx;

    public AbstractScript() {
        this.ctx = new ClientContext();
    }

}
