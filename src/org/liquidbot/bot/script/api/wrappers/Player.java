package org.liquidbot.bot.script.api.wrappers;

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Nameable;

/**
 * Created by Kenneth on 7/30/2014.
 */
public class Player extends Actor implements Nameable {

    public Player(Object raw) {
        super(raw);
    }

    @Override
    public String getName() {
        if(getRaw() == null)
            return null;
        return (String) Reflection.value("Player.getName()", getRaw());
    }
}
