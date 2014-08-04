package org.liquidbot.bot.script.api.query;

import org.liquidbot.bot.script.api.methods.data.Inventory;
import org.liquidbot.bot.script.api.wrappers.Item;

/**
 * Created by Kenneth on 8/4/2014.
 */
public class ItemQuery extends AbstractQuery<ItemQuery, Item> {

    @Override
    protected Item[] elements() {
        return Inventory.getAllItems();
    }
}
