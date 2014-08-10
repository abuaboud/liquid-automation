package org.liquidbot.bot.script.api.query;

import org.liquidbot.bot.script.api.methods.interactive.GroundItems;
import org.liquidbot.bot.script.api.wrappers.GroundItem;

/**
 * Created by Kenneth on 8/4/2014.
 */
public class GroundItemQuery extends BasicQuery<GroundItemQuery, GroundItem> {
    @Override
    protected GroundItem[] elements() {
        return GroundItems.getAll();
    }

    @Override
    public GroundItem nil() {
        return GroundItems.nil();
    }
}
