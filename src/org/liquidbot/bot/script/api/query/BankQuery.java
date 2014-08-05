package org.liquidbot.bot.script.api.query;

import org.liquidbot.bot.script.api.methods.data.Bank;
import org.liquidbot.bot.script.api.wrappers.Item;

/**
 * Created by Kenneth on 8/4/2014.
 */
public class BankQuery extends AbstractQuery<BankQuery, Item> {
    @Override
    protected Item[] elements() {
        return Bank.getAllItems();
    }
}
