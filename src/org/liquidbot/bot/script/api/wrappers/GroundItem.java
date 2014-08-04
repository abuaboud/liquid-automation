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
import org.liquidbot.bot.script.api.wrappers.definitions.ItemDefinition;
import org.liquidbot.bot.utils.Utilities;

import java.awt.*;

/*
 * Created by Hiasat on 7/31/14
 */
public class GroundItem implements Locatable, Identifiable, Nameable, Interactable {

    private int id;
    private int amount;
    private Tile location;
    private ItemDefinition itemDefinition;

    public GroundItem(Object raw, Tile tile) {
        if (raw != null) {
            location = tile;
            id = (int) Reflection.value("Item#getId()", raw);
            amount = (int) Reflection.value("Item#getStackSize()", raw);
            itemDefinition = new ItemDefinition(id);
        } else {
            id = -1;
            amount = -1;
        }
    }

    @Override
    public int getId() {
        return id;
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

    @Override
    public Polygon getBounds() {
        Polygon polygon = new Polygon();
        if (!isOnScreen())
            return null;
        int x = getLocation().x;
        int y = getLocation().y;
        int h = 20;
        int z = Game.getPlane();
        int tileByte = Walking.getTileFlags()[Game.getPlane()][getLocation().x - Game.getBaseX()][getLocation().y - Game.getBaseY()];
        double a = -0.25;
        double r = 0.25;
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
        return location.isOnScreen();
    }

    @Override
    public Point getPointOnScreen() {
        int realX = (getLocation().getX() - Game.getBaseX()) * 128;
        int realY = (getLocation().getY() - Game.getBaseY()) * 128;
        return Calculations.worldToScreen(realX + 66, realY + 66, 0);
    }

    /**
     * @return Point: Point used to interact
     */
    @Override
    public Point getInteractPoint() {
        Polygon bounds = getBounds();
        if (bounds != null)
            return Utilities.generatePoint(bounds);
        return getPointOnScreen();
    }

    @Override
    public int distanceTo() {
        return Calculations.distanceTo(this);
    }

    @Override
    public int distanceTo(Locatable locatable) {
        return Calculations.distanceBetween(getLocation(), locatable.getLocation());
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
        return location;
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
