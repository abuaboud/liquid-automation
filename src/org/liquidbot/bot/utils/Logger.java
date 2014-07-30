package org.liquidbot.bot.utils;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class Logger {

    private String prefix = "";

    public Logger(Class<?> c) {
        this.prefix = c.getSimpleName();
    }

    public void error(String message) {
        System.out.println("[" + prefix + "] - ERROR: " + message);
    }

    public void info(String message) {
        System.out.println("[" + prefix + "] - INFO: " + message);
    }
}
