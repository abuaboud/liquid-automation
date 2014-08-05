package org.liquidbot.bot.script.api.wrappers;

import org.liquidbot.bot.Constants;
import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Identifiable;
import org.liquidbot.bot.script.api.interfaces.Interactable;
import org.liquidbot.bot.script.api.interfaces.Locatable;
import org.liquidbot.bot.script.api.interfaces.Nameable;
import org.liquidbot.bot.script.api.methods.data.Calculations;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.definitions.ObjectDefinition;
import org.liquidbot.bot.utils.Utilities;

import java.awt.*;

/*
 * Created by Hiasat on 8/1/14
 */
public class GameObject implements Identifiable, Nameable, Locatable, Interactable {

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

    @Override
    public void draw(Graphics2D g, Color color) {
        g.setColor(color);
        g.drawPolygon(getBounds());
    }

    @Override
    public void draw(Graphics2D g) {
        draw(g, Color.WHITE);
    }

    public int getHeight() {
        Object renderable = Reflection.value(type.cato + "#getRenderable()", raw);
        if (renderable == null)
            return 20;
        return (int) Reflection.value("Renderable#getModelHeight()", renderable);
    }

    @Override
    public Polygon getBounds() {
        Polygon polygon = new Polygon();
        if (!isOnScreen())
            return null;
        int x = getX();
        int y = getY();
        int z = Game.getPlane();
        int h = getHeight();
        double a = -0.35;
        double r = 0.35;

        int tileByte = Walking.getTileFlags()[Game.getPlane()][getLocation().x - Game.getBaseX()][getLocation().y - Game.getBaseY()];

        Point pn = Calculations.tileToScreen(new Tile(x, y, z), r, r, tileByte == 1 ? 210 : 0);
        Point px = Calculations.tileToScreen(new Tile(x + 1, y, z), a, r, tileByte == 1 ? 210 : 0);
        Point py = Calculations.tileToScreen(new Tile(x, y + 1, z), r, a, tileByte == 1 ? 210 : 0);
        Point pxy = Calculations.tileToScreen(new Tile(x + 1, y + 1, z), a, a, tileByte == 1 ? 210 : 0);

        Point pnh = Calculations.tileToScreen(new Tile(x, y, z), r, r, tileByte == 1 ? 210 + h : h);
        Point pxh = Calculations.tileToScreen(new Tile(x + 1, y, z), a, r, tileByte == 1 ? 210 + h : h);
        Point pyh = Calculations.tileToScreen(new Tile(x, y + 1, z), r, a, tileByte == 1 ? 210 + h : h);
        Point pxyh = Calculations.tileToScreen(new Tile(x + 1, y + 1, z), a, a, tileByte == 1 ? 210 + h : h);

        if (Constants.VIEWPORT.contains(py)
                && Constants.VIEWPORT.contains(pyh)
                && Constants.VIEWPORT.contains(px)
                && Constants.VIEWPORT.contains(pxh)
                && Constants.VIEWPORT.contains(pxy)
                && Constants.VIEWPORT.contains(pxyh)
                && Constants.VIEWPORT.contains(pn)
                && Constants.VIEWPORT.contains(pnh)) {
            polygon.addPoint(py.x, py.y);
            polygon.addPoint(pyh.x, pyh.y);

            polygon.addPoint(px.x, px.y);
            polygon.addPoint(pxh.x, pxh.y);

            polygon.addPoint(pxy.x, pxy.y);
            polygon.addPoint(pxyh.x, pxyh.y);

            polygon.addPoint(pn.x, pn.y);
            polygon.addPoint(pnh.x, pnh.y);
        } else {
            return null;
        }
        return polygon;

    }

    @Override
    public boolean isOnScreen() {
        return Constants.VIEWPORT.contains(Calculations.tileToScreen(getLocation(),0.5,0.5,getHeight()));
    }

    @Override
    public Point getPointOnScreen() {
        return getLocation().getPointOnScreen();
    }

    /**
     * @return Point: Point used to interact
     */
    @Override
    public Point getInteractPoint() {
        Polygon bounds = getBounds();
        if (bounds != null)
            return Utilities.generatePoint(bounds);
        return Calculations.tileToScreen(getLocation(),0.5,0.5,getHeight());
    }

    @Override
    public int distanceTo() {
        return Calculations.distanceTo(this);
    }

    @Override
    public int distanceTo(Locatable locatable) {
        return Calculations.distanceBetween(tile, this.getLocation());
    }

    @Override
    public int distanceTo(Tile tile) {
        return Calculations.distanceBetween(tile, this.getLocation());
    }

    @Override
    public void turnTo() {
        Camera.turnTo(this);
    }


    @Override
    public boolean interact(String action, String option) {
        int menuIndex = -1;
        for (int i = 0; i < 5; i++) {
            menuIndex = org.liquidbot.bot.script.api.methods.data.Menu.index(action, option);
            Point interactPoint = getInteractPoint();
            if (menuIndex > -1)
                break;
            if (org.liquidbot.bot.script.api.methods.data.Menu.isOpen() && menuIndex == -1)
                org.liquidbot.bot.script.api.methods.data.Menu.interact("Cancel");
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

}
