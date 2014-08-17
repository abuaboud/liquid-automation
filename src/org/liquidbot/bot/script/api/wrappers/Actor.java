package org.liquidbot.bot.script.api.wrappers;/*
 * Created by Hiasat on 7/30/14
 */

import org.liquidbot.bot.Constants;
import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Interactable;
import org.liquidbot.bot.script.api.interfaces.Locatable;
import org.liquidbot.bot.script.api.methods.data.Calculations;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.Menu;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.utils.Utilities;

import java.awt.*;

public class Actor implements Locatable, Interactable {

    private final Object raw;

    public Actor(Object raw) {
        this.raw = raw;
    }


    /**
     * Returns the raw reflection object
     */
    protected Object getRaw() {
        return raw;
    }

    /**
     * This get you text Message that appear upon Actor Head
     *
     * @return Spoken Message Text that Actor Speak
     */
    public String getSpokenMessage() {
        return (String) Reflection.value("Actor#getSpokenMessage()", raw);
    }

    /**
     * This you get animation Id of Actor
     *
     * @return animation Id
     */
    public int getAnimation() {
        if (raw == null)
            return -1;
        return (int) Reflection.value("Actor#getAnimation()", raw);
    }

    /**
     * Max health for Actor only show when health bar visible
     *
     * @return Integer Max Health
     */
    public int getMaxHealth() {
        if (raw == null)
            return -1;
        return (int) Reflection.value("Actor#getMaxHealth()", raw);
    }

    /**
     * Current health for Actor only show when health bar visible
     *
     * @return Integer current Health
     */
    public int getHealth() {
        if (raw == null)
            return -1;
        return (int) Reflection.value("Actor#getHealth()", raw);
    }

    /**
     * @return int : 0-100% if health bar visible else 0%
     */
    public int getHealthPercent() {
        if (getHealth() == 0) return isInCombat() ? 0 : 100;
        return (int) ((double) getHealth() / getMaxHealth() * 100);
    }

    /**
     * @return boolean : if in combat return true else return false
     */
    public boolean isInCombat() {
        if (raw == null)
            return false;
        int LoopCycleStatus = ((int) Reflection.value("Client#getLoopCycle()", null)) - 130;
        int[] hitCycles = (int[]) Reflection.value("Actor#getHitCycles()", raw);
        for (final int loopCycle : hitCycles) {
            if (loopCycle > LoopCycleStatus) {
                return true;
            }
        }
        return false;
    }

    /**
     * the Height of Actor
     *
     * @return Integer Height
     */
    public int getHeight() {
        if (raw == null)
            return 0;
        return (int) Reflection.value("Renderable#getModelHeight()", raw);
    }


    /**
     * check if actor moving or running
     *
     * @return true if so , else false if not
     */
    public boolean isMoving() {
        return getQueueSize() > 0;
    }

    /**
     * @return Integer: QueueSize how many tiles queue there in map
     */
    public int getQueueSize() {
        if (raw == null)
            return 0;
        return (int) Reflection.value("Actor#getQueueSize()", raw);
    }

    /**
     * return real X location
     *
     * @return Integer X Location
     */
    public int getX() {
        if (!isValid())
            return -1;
        return ((((int) Reflection.value("Actor#getX()", raw)) >> 7) + (int) Reflection.value("Client#getBaseX()", null));
    }

	/**
	 * return real Y location
	 *
	 * @return Integer Y Location
	 */
	public int getY() {
		if (!isValid())
			return -1;
		return ((((int) Reflection.value("Actor#getY()", raw)) >> 7) + (int) Reflection.value("Client#getBaseY()", null));
	}

    /**
     * @return Polygon : returns bounds and cube around the actor
     */
    @Override
    public Polygon getBounds() {
        Polygon polygon = new Polygon();
        if (!isOnScreen())
            return null;
        int x = getLocation().x;
        int y = getLocation().y;
        int h = getHeight();
        int z = Game.getPlane();
        int tileByte = Walking.getTileFlags()[Game.getPlane()][getLocation().x - Game.getBaseX()][getLocation().y - Game.getBaseY()];
        if (this instanceof NPC) {
            NPC npc = (NPC) this;
            if (npc.getName() != null && npc.getName().toLowerCase().contains("fishing"))
                tileByte = 0;
        }
        double a = -0.35;
        double r = 0.35;
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

    /**
     * Check if Actor is on screen
     *
     * @return Boolean: true if it's on Viewport else false
     */
    @Override
    public boolean isOnScreen() {
        return getLocation().isOnScreen();
    }

    /**
     * @return Point: Point converted from WorldToScreen depend on X/Y
     */
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
        return getPointOnScreen();
    }

    /**
     * distance from local player
     *
     * @return Integer
     */
    @Override
    public int distanceTo() {
        return Calculations.distanceTo(getLocation());
    }

    /**
     * distance to specific Locatable
     *
     * @return Integer
     */
    @Override
    public int distanceTo(Locatable locatable) {
        return (int) Calculations.distanceBetween(getLocation(), locatable.getLocation());
    }

    /**
     * distance to specific tile
     *
     * @return Integer
     */
    @Override
    public int distanceTo(Tile tile) {
        return (int) Calculations.distanceBetween(getLocation(), tile);
    }

    /**
     * turn camera to this Actor
     */
    @Override
    public void turnTo() {
        Camera.turnTo(this);
    }

    /**
     * current Tile of this Actor
     *
     * @return Tile
     */
    @Override
    public Tile getLocation() {
        return new Tile(getX(), getY());  //To change body of implemented methods use File | Settings | File Templates.
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
    public boolean interact(String action, String option) {
        int menuIndex = -1;
        for (int i = 0; i < 5; i++) {
            menuIndex = Menu.index(action, option);
            Point interactPoint = getInteractPoint();
	        Polygon bounds = getBounds();
	        if (menuIndex > -1 && (bounds == null || bounds.contains(Mouse.getLocation())))
                break;
            if (Menu.isOpen() && menuIndex == -1)
                Menu.interact("Cancel");
            Mouse.move(interactPoint);
            Time.sleep(100, 150);
        }
        return menuIndex > -1 && Menu.interact(action, option);
    }

    @Override
    public boolean interact(String action) {
	    String name = null;
	    if(this instanceof NPC){
		    name = ((NPC) this).getName();
	    } else if(this instanceof Player){
		    name = ((Player) this).getName();
	    }
        return interact(action, name);
    }


	@Override
	public boolean click(boolean left) {
		Point interactingPoint = this.getInteractPoint();
		Polygon bounds = getBounds();
		for(int i = 0; i < 3; i++){
			if(bounds == null || bounds.contains(Mouse.getLocation())){
				Mouse.click(left);
				return true;
			}
			if(bounds == null || !bounds.contains(interactingPoint)){
				interactingPoint = this.getInteractPoint();
			}
			Mouse.move(interactingPoint);
		}
		return false;
	}

	@Override
	public boolean click() {
		return click(true);
	}

    /**
     * Checks if the object is null
     *
     * @return true if the object is not null
     */
    public boolean isValid() {
        return getRaw() != null;
    }

    /**
     * @return NPC/Player: return the actor that this actor interacting with
     */
    public Actor getInteracting() {
        if (raw == null)
            return null;
        int interactingIndex = (int) Reflection.value("Actor#getInteractingIndex()", raw);
        if (interactingIndex == -1)
            return new Actor(null);
        if (interactingIndex < 32768) {
            Object[] localNpcs = (Object[]) Reflection.value("Client#getLocalNpcs()", null);
            if (localNpcs.length > interactingIndex)
                return new NPC(localNpcs[interactingIndex]);
        } else {
            interactingIndex -= 32768;
            int playerIndex = (int) Reflection.value("Client#getPlayerIndex()", null);
            if (interactingIndex == playerIndex) {
                return Players.getLocal();
            }
            Object[] localPlayers = (Object[]) Reflection.value("Client#getLocalPlayers()", null);
            if (localPlayers.length > interactingIndex)
                return new Player(localPlayers[interactingIndex]);
        }
        return new Actor(null);
    }

    @Override
    public boolean equals(Object a) {
        if (a != null && a instanceof Actor) {
            Actor t = (Actor) a;
            boolean x = this.getLocation().equals(t.getLocation()) && this.getAnimation() == t.getAnimation() && this.getHealthPercent() == this.getHealthPercent() && this.getMaxHealth() == this.getMaxHealth();
            if (t instanceof Player && this instanceof Player) {
                Player j = (Player) t;
                return x & j.getName().equals(((Player) this).getName());
            } else if (t instanceof NPC && this instanceof NPC) {
                NPC j = (NPC) t;
                return x & j.getId() == (((NPC) this).getId());
            }
            return false;
        }
        return false;
    }

}
