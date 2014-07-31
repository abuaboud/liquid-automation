package org.liquidbot.bot.script.api.methods.data;

import org.liquidbot.bot.client.reflection.Reflection;

/*
 * Created by Hiasat on 7/31/14
 */
public class Game {

    /**
     * current Game State E.g Logged , Lobby , Loading
     *
     * @return Integer of state
     */
    public static int getGameState() {
        return (int) Reflection.value("Client#getGameState()", null);
    }

    /**
     * current X of current Region
     *
     * @return Integer Region X
     */
    public static int getBaseX() {
        return (int) Reflection.value("Client#getBaseX()", null);
    }

    /**
     * current Y of current Region
     *
     * @return Integer Region Y
     */
    public static int getBaseY() {
        return (int) Reflection.value("Client#getBaseY()", null);
    }

    /**
     *
     * @return Integer : Current floor player is on
     */
    public static int getPlane(){
        return (int) Reflection.value("Client#getPlane()",null);
    }
    /**
     * @return true if logged in else false
     */
    public static boolean isLoggedIn() {
        return true;
    }
}
