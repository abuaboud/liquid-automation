package org.liquidbot.bot.script.api.listeners;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.ScriptHandler;
import org.liquidbot.bot.script.api.methods.data.Inventory;
import org.liquidbot.bot.script.api.wrappers.Item;
import org.liquidbot.bot.utils.Logger;
import org.liquidbot.bot.utils.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created on 8/10/2014.
 */
public class InventoryMonitor implements Runnable {

    private final Configuration config = Configuration.getInstance();
    private final List<InventoryListener> listeners = new ArrayList<>();
    private final Logger log = new Logger(ExperienceMonitor.class);

    public void addListener(InventoryListener... listeners) {
        Collections.addAll(this.listeners, listeners);
    }

    private Item[] lastItems;

    public InventoryMonitor() {
        log.info("Initialized InventoryMonitor..");
        lastItems = Inventory.getAllItems();
    }

    @Override
    public void run() {
        while(config.getScriptHandler().getScriptState() != null && config.getScriptHandler().getScriptState() != ScriptHandler.State.STOPPED) {
            final Item[] currentItems = Inventory.getAllItems();
            for(Item item : currentItems) {
                int previous = getCount(lastItems, item.getId());
                int current = getCount(currentItems, item.getId());
                if(current != previous) {
                    for(InventoryListener listener : listeners) {
                        listener.onItem(new Item(item.getId(), current - previous, item.getIndex(), Item.Type.INVENTORY, item.getArea()));
                    }
                    break;
                }
            }
            lastItems = currentItems;
            Utilities.sleep(600);
        }
    }

    private int getCount(Item[] array, int itemId) {
        int count = 0;
        for(Item item : array) {
            if(item != Inventory.nil() && itemId == item.getId()) {
                count += item.getStackSize();
            }
        }
        return count;
    }
}
