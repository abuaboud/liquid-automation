package org.liquidbot.bot.script.api.wrappers;

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Identifiable;
import org.liquidbot.bot.script.api.interfaces.Nameable;
import org.liquidbot.bot.script.api.wrappers.definitions.NPCDefinition;

/**
 * Created on 7/29/2014.
 */
public class NPC extends Actor implements Identifiable, Nameable {

    private NPCDefinition npcDefinition;

    public NPC(Object raw) {
        super(raw);
        if (raw != null) {
            this.npcDefinition = new NPCDefinition(Reflection.value("NPC#getNpcComposite()", raw));
        }
    }

    /**
     * This method grabs tnhe name of the NPC
     *
     * @return String: the NPC name
     */
    public String getName() {
        return npcDefinition.getName();
    }

    /**
     * Gets the ID of the NPC
     *
     * @return Integer:the NPC's ID
     */
    public int getId() {
        return npcDefinition.getId();
    }

    /**
     * Gets the combatLevel of the NPC
     *
     * @return Integer: the NPC's combat level
     */
    public int getCombatLevel() {
        return npcDefinition.getCombatLevel();
    }

    /**
     * Interact Actions of the NPC
     *
     * @return String[] : actions of the NPC
     */
    public String[] getActions() {
        return npcDefinition.getActions();
    }

    /**
     * model Ids of the NPC
     *
     * @return String[] :  model Ids of the NPC
     */
    public int[] getModelIds() {
        return npcDefinition.getModelIds();
    }
}
