package org.liquidbot.bot.script.api.util;

import org.liquidbot.bot.Configuration;

import java.io.File;


/*
 * Created by Hiasat on 8/3/14
 */
public class Environment {

    public static int getUserId() {
        return Configuration.getInstance().getUser().getUserId();
    }

    public static String getDisplayName() {
        return Configuration.getInstance().getUser().getDisplayName();
    }

    public static File getStorageDirectory() {
        final File file = new File(System.getProperty("user.home")  + File.separator + "LiquidBot" + File.separator + "Script Settings");
        if(!file.exists())
            file.mkdirs();
        return file;
    }
}
