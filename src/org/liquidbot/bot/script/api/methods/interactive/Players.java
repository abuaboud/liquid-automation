package org.liquidbot.bot.script.api.methods.interactive;

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.wrappers.Player;

/*
 * Created by Hiasat on 7/30/14
 */
public class Players {

    public static Player getLocal(){
        return new Player(Reflection.value("Client#getMyPlayer()",null));
    }
}
