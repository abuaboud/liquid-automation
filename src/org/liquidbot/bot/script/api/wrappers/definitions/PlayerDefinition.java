package org.liquidbot.bot.script.api.wrappers.definitions;

import org.liquidbot.bot.client.reflection.Reflection;

/*
 * Created by Hiasat on 7/31/14
 */
public class PlayerDefinition {

    private Object raw;

    public PlayerDefinition(Object raw) {
        this.raw = raw;
    }

    public boolean isFemale() {
        if (raw == null)
            return false;
        return (Boolean) Reflection.value("PlayerComposite#isFemale()", raw);
    }

    public int[] getEquipment() {
        if (raw == null)
            return null;
        return (int[]) Reflection.value("PlayerComposite#getEquipment()", raw);
    }

    public boolean isValid() {
        return raw != null;
    }
}
