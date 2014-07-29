package org.liquidbot.bot.utils;

import java.io.File;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class Utilities {

    /**
     * Used to change the loading of all files
     *
     * @return the directory which files load from
     */
    public static String getContentDirectory() {
        return System.getProperty("user.home") + File.separator + "LiquidBot" + File.separator;
    }

}
