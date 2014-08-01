package org.liquidbot.bot.script.api.wrappers;

import com.sun.corba.se.spi.ior.IdentifiableFactory;
import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Identifiable;
import org.liquidbot.bot.script.api.interfaces.Interactable;
import org.liquidbot.bot.script.api.interfaces.Locatable;
import org.liquidbot.bot.script.api.interfaces.Nameable;
import org.liquidbot.bot.script.api.methods.data.Calculations;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.wrappers.definitions.ObjectDefinition;

import java.awt.*;

/*
 * Created by Hiasat on 8/1/14
 */
public class GameObject implements Nameable, Locatable, Interactable, Identifiable {

    public enum Type {
        INTERACTIVE("GameObject"), BOUNDARY("Boundary"), FLOOR_DECORATION("FloorDecoration"), WALL_OBJECT("WallObject");

        String cato;

        Type(String cato) {
            this.cato = cato;
        }
    }

    private Type type;
    private int id;
    private Object raw;
    private ObjectDefinition objectDefinition;

    public GameObject(Object raw, Type type) {
        if (raw != null) {
            int hash = (int) Reflection.value(type.cato + "#getId()", raw);
            this.id = ((hash >> 14) & 0x7FFF);
            this.raw = raw;
            this.type = type;
            this.objectDefinition = new ObjectDefinition(id);
        }
    }

    public Type getType() {
        return type;
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
        return getLocation().isOnScreen();
    }

    @Override
    public Point getPointOnScreen() {
        return getLocation().getPointOnScreen();
    }

    @Override
    public Point getInteractPoint() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int distanceTo() {
        return Calculations.distanceTo(this);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int distanceTo(Locatable locatable) {
        return Calculations.distanceBetween(this.getLocation(), locatable.getLocation());
    }

    @Override
    public int distanceTo(Tile tile) {
        return Calculations.distanceBetween(getLocation(), tile);
    }

    @Override
    public void turnTo() {
        Camera.turnTo(this);
    }

    @Override
    public Tile getLocation() {
        return new Tile(getX(), getY());
    }

    @Override
    public String getName() {
        return objectDefinition.getName();
    }

    public String[] getActions() {
        return objectDefinition.getActions();
    }

    public boolean isValid() {
        return id > 0;
    }

    public int getX() {
        return (((int) Reflection.value(type.cato + "#getId()", raw)) & 0x7F) + Game.getBaseX();
    }

    public int getY() {
        return (((int) Reflection.value(type.cato + "#getId()", raw)) >> 7 & 0x7F) + Game.getBaseY();
    }

}
