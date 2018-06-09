package org.liquidbot.bot.script.api.wrappers;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.Constants;
import org.liquidbot.bot.client.injection.callback.ModelCallBack;
import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Identifiable;
import org.liquidbot.bot.script.api.interfaces.Interactable;
import org.liquidbot.bot.script.api.interfaces.Locatable;
import org.liquidbot.bot.script.api.interfaces.Nameable;
import org.liquidbot.bot.script.api.methods.data.Calculations;
import org.liquidbot.bot.script.api.methods.data.Menu;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.definitions.ObjectDefinition;
import java.awt.*;

/*
 * Created on 8/1/14
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

	public String[] getActions() {
		if (objectDefinition == null) {
			objectDefinition = new ObjectDefinition(getId());
		}
		return objectDefinition.getActions();
	}

	public Type getType() {
		return type;
	}

	@Override
	public int getId() {
		if (raw == null)
			return 0;
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
		Model model = getModel();
		if (model == null)
			return;
		model.draw(g, color);
	}

	@Override
	public void draw(Graphics2D g) {
		draw(g, Color.WHITE);
	}

	public int getHeight() {
		if (raw == null)
			return 20;
		Object renderable = Reflection.value(type.cato + "#getRenderable()", raw);
		if (renderable == null)
			return 20;
		return (int) Reflection.value("Renderable#getModelHeight()", renderable);
	}


	@Override
	public boolean isOnScreen() {
		return Constants.VIEWPORT.contains(Calculations.tileToScreen(getLocation(), 0.5, 0.5, getHeight()));
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
		Model bounds = getModel();
		if (bounds != null)
			return bounds.getRandomPoint();
		return Calculations.tileToScreen(getLocation(), 0.5, 0.5, getHeight());
	}

	@Override
	public int distanceTo() {
		return Calculations.distanceTo(this);
	}

	@Override
	public int distanceTo(Locatable locatable) {
		return Calculations.distanceBetween(tile, locatable.getLocation());
	}

	@Override
	public int distanceTo(Tile tile) {
		return Calculations.distanceBetween(tile, getLocation());
	}

	@Override
	public void turnTo() {
		Camera.turnTo(this);
	}


	@Override
	public boolean interact(String action, String option) {
		int menuIndex = -1;
		for (int i = 0; i < 5; i++) {
			menuIndex = Menu.index(action, option);
			Point interactPoint = getInteractPoint();
			Model bounds = getModel();
			if (menuIndex > -1 && (bounds == null || bounds.contains(Mouse.getLocation())))
				break;
			if (Menu.isOpen() && menuIndex == -1)
				Menu.interact("Cancel");
			Mouse.move(interactPoint);
			Time.sleep(100, 150);
		}
		return menuIndex > -1 && Menu.interact(action, option, Configuration.getInstance().pattern().contains("RIGHT_CLICK_OBJECT_ALWAYS"));
	}

	@Override
	public boolean interact(String action) {
		return interact(action, getName());
	}

	@Override
	public boolean click(boolean left) {
		Point interactingPoint = this.getInteractPoint();
		Model bounds = getModel();
		for (int i = 0; i < 3; i++) {
			if (bounds == null || bounds.contains(Mouse.getLocation())) {
				Mouse.click(left);
				return true;
			}
			if (bounds == null || !bounds.contains(interactingPoint)) {
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


	public Model getModel() {
		int gridX = (int) Reflection.value(type.cato + "#getX()", raw);
		int gridY = (int) Reflection.value(type.cato + "#getY()", raw);
		int tileByte = Walking.getTileFlags()[Game.getPlane()][getLocation().x - Game.getBaseX()][getLocation().y - Game.getBaseY()];
		if (isValid() && getName() != null && getName().toLowerCase().contains("fishing"))
			tileByte = 0;
		int z = tileByte == 1 ? 210 : 0;
		Object[] renderable = new Object[]{Reflection.value(type.cato + "#getRenderable()", raw), null};
		if (instanceOf(renderable[0])) {
			return new Model(new Model(renderable[0]), 0, gridX, gridY, z);
		}
		if (instanceOf(renderable[1])) {
			return new Model(new Model(renderable[1]), 0, gridX, gridY, z);
		}

		return renderable[0] != null && ModelCallBack.get(renderable[0]) != null ? new Model(ModelCallBack.get(renderable[0]), 0, gridX, gridY, z): null;
	}


	public boolean instanceOf(Object first) {
		if (first == null)
			return false;
		try {
			Reflection.value("Model#getVerticesX()", first);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

}
