package org.liquidbot.bot.script.api.wrappers;/*
 * Created by Hiasat on 7/30/14
 */

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Locatable;
import org.liquidbot.bot.script.api.interfaces.Nameable;

public class Actor implements Locatable {

    private final Object raw;

    public Actor(Object raw){
       this.raw = raw;
    }

    /**
     * Returns the raw reflection object
     */
    protected Object getRaw() {
        return raw;
    }

    /**
     * This get you text Message that appear upon Actor Head
     *
     * @return Spoken Message Text that Actor Speak
     */
    public String getSpokenMessage() {
        return (String) Reflection.value("Actor#getSpokenMessage()", raw);
    }

    
}
