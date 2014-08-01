package org.liquidbot.bot.script.api.wrappers;

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Nameable;
import org.liquidbot.bot.script.api.query.PlayerQuery;
import org.liquidbot.bot.script.api.wrappers.definitions.PlayerDefinition;

/**
 * Created by Kenneth on 7/30/2014.
 */
public class Player extends Actor implements Nameable {

    private PlayerDefinition playerDefinition;

    public Player(Object raw) {
        super(raw);
        playerDefinition = new PlayerDefinition(raw);
    }

    /**
     * check player definition if it's null or not
     *
     * @return boolean : make sure the Player definition isn't null
     */
    public boolean isValid() {
        return getRaw() != null;
    }

    /**
     * Player name
     *
     * @return String: return the name of player
     */
    @Override
    public String getName() {
        if (getRaw() == null)
            return null;
        return (String) Reflection.value("Player#getName()", getRaw());  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Combat Level
     *
     * @return Integer: combat level of player
     */
    public int getCombatLevel() {
        if (getRaw() == null)
            return 0;
        return (int) Reflection.value("Player#getCombatLevel()", getRaw());
    }

    /**
     * Player's equipments
     *
     * @return Integer[]: player's equipments
     */
    public int[] getEquipment() {
        return playerDefinition.getEquipment();
    }

    /**
     * Check for player's if female or not
     *
     * @return boolean: player's female check
     */
    public boolean isFemale() {
        return playerDefinition.isFemale();
    }
}
