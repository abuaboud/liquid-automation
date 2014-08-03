package org.liquidbot.bot.utils;

import org.liquidbot.bot.Configuration;

import java.awt.*;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class Logger {

    private final Configuration config = Configuration.getInstance();

    private String prefix = "";

    public Logger(Class<?> c) {
        this.prefix = c.getSimpleName();
    }

    public void error(String message) {
        System.out.println("[" + prefix + "] - ERROR: " + message);
        if(config.getConsole() != null)
            config.getConsole().append("[" + prefix + "] - ERROR: " + message,  Color.RED);
    }

    public void info(String message) {
        info(message, Color.WHITE);
    }

    public void info(String message, Color color) {
        System.out.println("[" + prefix + "] - INFO: " + message);
        if(config.getConsole() != null)
            config.getConsole().append("[" + prefix + "] - INFO: " + message, color);
    }
}
