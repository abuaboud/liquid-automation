package org.liquidbot.bot.script.api.wrappers;

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Identifiable;
import org.liquidbot.bot.script.api.interfaces.Nameable;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class NPC extends Actor implements Identifiable, Nameable {

    public NPC(Object raw) {
        super(raw);
    }

    /**
     * Checks if the object is null
     * @return true if the object is not null
     */
    public boolean isValid() {
        return getRaw() != null;
    }

    /**
     * This method grabs the name of the NPC
     * @return the NPC name
     */
    @Override
    public String getName() {
        return "";
    }

    /**
     * Gets the ID of the NPC
     * @return the NPC's ID
     */
    @Override
    public int getId() {
        return 0;
    }
}
