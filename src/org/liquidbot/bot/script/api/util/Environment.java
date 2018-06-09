package org.liquidbot.bot.script.api.util;

import org.liquidbot.bot.Configuration;


/*
 * Created on 8/3/14
 */
public class Environment {

    public static int getUserId() {
        return Configuration.getInstance().getUser().getUserId();
    }

    public static String getDisplayName() {
        return Configuration.getInstance().getUser().getDisplayName();
    }

}
