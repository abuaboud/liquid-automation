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
public class GameObject implements Identifiable, Nameable {

    public enum Type {
        INTERACTIVE("GameObject"), BOUNDARY("Boundary"), FLOOR_DECORATION("FloorDecoration"), WALL_OBJECT("WallObject");

        String cato;

        Type(String cato) {
            this.cato = cato;
        }
    }


    private Object raw;
    private Type type;
    private Tile tile;
    private int id;
    private ObjectDefinition objectDefinition;

    public GameObject(Object raw, Type type, int x, int y, int z) {
        this.raw = raw;
        this.type = type;
        this.tile = new Tile(x, y, z);
    }

    @Override
    public String getName() {
        if (objectDefinition == null) {
            objectDefinition = new ObjectDefinition(getId());
        }
        return objectDefinition.getName();
    }


    public Type getType() {
        return type;
    }

    @Override
    public int getId() {
        if (id == 0) {
            id = ((int) Reflection.value(type.cato + "#getId()", raw) >> 14) & 0x7FFF;
        }
        return id;
    }

    public int getX() {
        return tile.getX();
    }

    public int getY() {
        return tile.getY();
    }

    public boolean isValid() {
        return raw != null;
    }

    public Tile getLocation() {
        return tile;
    }

  /*  @Override
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
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int distanceTo(Locatable locatable) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int distanceTo(Tile tile) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void turnTo() {
        //To change body of implemented methods use File | Settings | File Templates.
    }*/

}
