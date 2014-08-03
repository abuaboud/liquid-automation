package org.liquidbot.bot.script.api.wrappers;

import org.liquidbot.bot.script.api.interfaces.Identifiable;
import org.liquidbot.bot.script.api.interfaces.Interactable;
import org.liquidbot.bot.script.api.interfaces.Nameable;
import org.liquidbot.bot.script.api.methods.data.Menu;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.definitions.ItemDefinition;

import java.awt.*;

/*
 * Created by Hiasat on 8/3/14
 */
public class Item implements Interactable, Identifiable, Nameable {

    private int id;
    private int stackSize;
    private int index;
    private Rectangle area;
    private ItemDefinition itemDefinition;
    private Type type;

    public enum Type {
        INVENTORY, BANK
    }

    public Item(int index, int id, int stackSize, Type type, Rectangle area) {
        this.type = type;
        this.index = index;
        this.id = id;
        this.stackSize = stackSize;
        this.area = area;
    }

    public Type getType() {
        return type;
    }

    public int getStackSize() {
        return stackSize;
    }

    public Rectangle getArea() {
        return area;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Point getInteractPoint() {
        Rectangle rect = area;
        return new Point(Random.nextInt(rect.x, rect.x + rect.width),
                Random.nextInt(rect.y, rect.y + rect.height));
    }

    @Override
    public boolean interact(String action, String option) {
        int menuIndex = -1;
        for (int i = 0; i < 5; i++) {
            menuIndex = Menu.index(action, option);
            Point interactPoint = getInteractPoint();
            if (menuIndex > -1)
                break;
            if (Menu.isOpen() && menuIndex == -1)
                Menu.interact("Cancel");
            Mouse.move(interactPoint);
            Time.sleep(100, 150);
        }
        return menuIndex > -1 && org.liquidbot.bot.script.api.methods.data.Menu.interact(action, option);
    }

    @Override
    public boolean interact(String action) {
        return interact(action, null);
    }

    @Override
    public boolean click(boolean left) {
        Mouse.click(getInteractPoint(), left);
        return true;
    }

    @Override
    public boolean click() {
        Mouse.click(getInteractPoint(), true);
        return true;
    }

    @Override
    public String getName() {
        if (itemDefinition == null)
            itemDefinition = new ItemDefinition(id);
        return itemDefinition.getName();
    }

    public boolean isValid() {
        return (id | stackSize) != -1;
    }
}
