package org.liquidbot.bot.script.api.wrappers;

import com.sun.corba.se.spi.ior.IdentifiableFactory;
import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.client.parser.HookReader;
import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Identifiable;
import org.liquidbot.bot.script.api.interfaces.Interactable;
import org.liquidbot.bot.script.api.interfaces.Locatable;
import org.liquidbot.bot.script.api.interfaces.Nameable;
import org.liquidbot.bot.script.api.methods.data.Calculations;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.wrappers.definitions.ItemDefinition;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/*
 * Created by Hiasat on 7/31/14
 */
public class GroundItem implements Locatable, Interactable, Nameable, Identifiable {

    private int id;
    private int amount;
    private Tile location;
    private ItemDefinition itemDefinition;

    public GroundItem(int id , int amount, Tile tile) {
        this.location = tile;
        this.id = id;
        this.amount = amount;
        this.itemDefinition = new ItemDefinition(id);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean interact(String action, String option) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean interact(String action) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean click(boolean left) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean click() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isOnScreen() {
        return location.isOnScreen();
    }

    @Override
    public Point getPointOnScreen() {
        int realX = (getLocation().getX() - Game.getBaseX()) * 128;
        int realY = (getLocation().getY() - Game.getBaseY()) * 128;
        return Calculations.worldToScreen(realX + 66, realY + 66, 0);
    }

    @Override
    public Point getInteractPoint() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int distanceTo() {
        return Calculations.distanceTo(this);
    }

    @Override
    public int distanceTo(Locatable locatable) {
        return Calculations.distanceTo(locatable);
    }

    @Override
    public int distanceTo(Tile tile) {
        return Calculations.distanceTo(tile);
    }

    @Override
    public void turnTo() {
        Camera.turnTo(this);
    }

    @Override
    public Tile getLocation() {
        return location;
    }

    @Override
    public String getName() {
        return itemDefinition.getName();
    }

    public boolean isValid() {
        return (id | amount) != -1;
    }

    public int getStackSize() {
        return amount;
    }
}
