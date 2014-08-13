package org.liquidbot.component.debug;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.wrappers.Widget;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Hiasat on 8/13/14.
 */
public class WidgetDebugger extends Debugger {

	public static int x,y,height,width = -1;

	@Override
	public Object[] elements() {
		return new Object[0];
	}

	@Override
	public boolean activate() {
		return Game.getGameState() == Game.STATE_LOGGED_IN && Configuration.getInstance().drawWidgets();
	}

	@Override
	public void render(Graphics2D graphics) {

		if(width >= 0 && height >= 0){
			graphics.setColor(Color.GREEN);
			graphics.drawRect(x,y,width,height);
		}
	}
}
