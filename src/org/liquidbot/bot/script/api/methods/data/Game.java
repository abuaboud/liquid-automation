package org.liquidbot.bot.script.api.methods.data;

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;

/*
 * Created by Hiasat on 7/31/14
 */
public class Game {

    public static final int STATE_LOGGED_IN = 30;
    public static final int STATE_LOG_IN_SCREEN = 10;

    private static final int IN_GAME_WIDGET = 378;
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
        return getGameState() == STATE_LOGGED_IN
                && (Widgets.get(IN_GAME_WIDGET) == null || !Widgets.get(IN_GAME_WIDGET).isValid());
    }
}
