package org.liquidbot.bot.script.api.wrappers;

import org.liquidbot.bot.client.reflection.Reflection;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class NPC extends Actor{

    private Object raw;

    public NPC(Object raw) {
        super(raw);
        this.raw = raw;
    }

    public boolean isValid() {
        return raw != null;
    }
}
