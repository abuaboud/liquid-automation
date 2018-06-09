package org.liquidbot.bot.script.api.query;

import org.liquidbot.bot.script.api.methods.interactive.GameEntities;
import org.liquidbot.bot.script.api.wrappers.GameObject;

/**
 * Created on 8/4/2014.
 */
public class GameObjectQuery extends BasicQuery<GameObjectQuery, GameObject> {
    @Override
    protected GameObject[] elements() {
        return GameEntities.getAll();
    }

    @Override
    public GameObject nil() {
        return GameEntities.nil();
    }
}
