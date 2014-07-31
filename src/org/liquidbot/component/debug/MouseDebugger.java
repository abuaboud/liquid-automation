package org.liquidbot.component.debug;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.Constants;

import java.awt.*;

/**
 * Created by Kenneth on 7/30/2014.
 */
public class MouseDebugger extends Debugger{

    @Override
    public Object[] elements() {
        return new Object[0]; // ignored
    }

    @Override
    public boolean activate() {
        return config.enableMouse();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.drawLine(config.getMouse().getX(), 0, config.getMouse().getX(), Constants.APPLET_HEIGHT);
        graphics.drawLine(0, config.getMouse().getY(), Constants.APPLET_WIDTH, config.getMouse().getY());
    }
}
