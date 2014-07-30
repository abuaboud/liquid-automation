package org.liquidbot.bot.client.debug;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.Constants;

import java.awt.*;

/**
 * Created by Kenneth on 7/30/2014.
 */
public class MouseDebugger extends Debugger {

    @Override
    public Object[] elements() {
        return new Object[0]; // ignored
    }

    @Override
    public boolean activate() {
        return Configuration.drawMouse;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.drawLine(Configuration.mouse.getX(), 0, Configuration.mouse.getX(), Constants.APPLET_HEIGHT);
        graphics.drawLine(0, Configuration.mouse.getY(), Constants.APPLET_WIDTH, Configuration.mouse.getY());
    }
}
