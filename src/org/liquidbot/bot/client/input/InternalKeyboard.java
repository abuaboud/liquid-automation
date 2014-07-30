package org.liquidbot.bot.client.input;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Hiasat on 7/29/2014.
 */
public class InternalKeyboard  {

	private final Component component;

	public InternalKeyboard(Applet applet) {
		this.component = applet.getComponentAt(1, 1);
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


}
