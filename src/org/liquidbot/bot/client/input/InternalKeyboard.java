package org.liquidbot.bot.client.input;

import org.liquidbot.bot.Configuration;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created on 7/29/2014.
 */
public class InternalKeyboard implements KeyListener {

	private final Component component;
	private final KeyListener keyDispatcher;

	private KeyListener[] keyboardlistener;

	public InternalKeyboard() {
		this.component = Configuration.getInstance().getCanvas().getCanvas();
		this.keyboardlistener = component.getKeyListeners();
		this.keyDispatcher = component.getKeyListeners()[0];

	}

	public final KeyEvent create(int id, int keyCode, char c) {
		return new KeyEvent(component, id, System.currentTimeMillis(), 0,
				keyCode, c, id != KeyEvent.KEY_TYPED ? KeyEvent.KEY_LOCATION_STANDARD
				: KeyEvent.KEY_LOCATION_UNKNOWN);
	}

	public void press(KeyEvent c) {
		component.dispatchEvent(c);
	}

	public void type(KeyEvent c) {
		component.dispatchEvent(c);
	}

	public void release(KeyEvent c) {
		component.dispatchEvent(c);
	}

	public void press(char c) {
		press(create(KeyEvent.KEY_PRESSED, (int) c, c));
	}

	public void type(char c) {
		component.dispatchEvent(create(KeyEvent.KEY_TYPED, 0, c));
	}

	public void release(char c) {
		component.dispatchEvent(create(KeyEvent.KEY_RELEASED, (int) c, c));
	}

	public KeyEvent createNew(KeyEvent old) {
		return new KeyEvent(component, old.getID(), old.getWhen(),
				old.getModifiers(), old.getKeyCode(), old.getKeyChar(),
				old.getKeyLocation());
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		keyDispatcher.keyPressed(createNew(arg0));
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		keyDispatcher.keyReleased(createNew(arg0));
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		keyDispatcher.keyTyped(createNew(arg0));
	}


}
