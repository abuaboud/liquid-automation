package org.liquidbot.component.debug;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.Inventory;
import org.liquidbot.bot.script.api.wrappers.Item;

import java.awt.*;

/*
 * Created by Hiasat on 8/7/14
 */
public class InventoryDebugger extends Debugger<Item> {

    @Override
    public Item[] elements() {
        return Inventory.getAllItems();
    }

    @Override
    public boolean activate() {
        return Game.isLoggedIn() && Configuration.getInstance().drawInventory();
    }

    @Override
    public void render(Graphics2D graphics) {
        for(Item item : Inventory.getAllItems()) {
            if(item != null) {
                graphics.setColor(Color.GREEN);
                Point point = item.getCentralPoint();
                graphics.drawString(item.getId() + "", point.x - 15, point.y + 4);
            }
        }
    }
}
