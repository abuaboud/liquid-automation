package org.liquidbot.bot.script.api.methods.data;

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.enums.Tab;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;

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
     * @return Integer : Current floor player is on
     */
    public static int getPlane() {
        return (int) Reflection.value("Client#getPlane()", null);
    }

    /**
     * @return true if logged in else false
     */
    public static boolean isLoggedIn() {
        return getGameState() == STATE_LOGGED_IN
                && (Widgets.get(IN_GAME_WIDGET) == null || !Widgets.get(IN_GAME_WIDGET).isValid());
    }

    /**
     * @return Tab: depend on which tab is open
     */
    public static Tab getCurrentTab() {
        final int WIDGET_PARENT = 548;
        final WidgetChild[] children = Widgets.get(WIDGET_PARENT).getChildren();
        if (children == null || children.length == 0)
            return null;
        for (WidgetChild p : children) {
            if (p.getTextureId() != -1 && p.getActions() != null) {
                String[] actions = p.getActions();
                for (Tab tab : Tab.values()) {
                    for (String action : actions) {
                        if (tab.getName().equalsIgnoreCase(action)) {
                            return tab;
                        }
                    }
                }
            }
        }
        return null;
    }

	public static boolean logout() {
		if(!Game.isLoggedIn())
			 return true;
		final WidgetChild widgetChild = Widgets.get(182,6);
		if(Tab.LOGOUT.open()) {
			widgetChild.click(true);
			for (int i = 0; i < 10 && Game.isLoggedIn(); i++, Time.sleep(100, 150));
		}
		return !Game.isLoggedIn();
	}


	public static int getCurrentWorld() {
		return (int) Reflection.value("Client#getCurrentWorld()",null);
	}

}
