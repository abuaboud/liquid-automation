package org.liquidbot.bot.script.api.enums;

import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;

/*
 * Created by Hiasat on 8/6/14
 */
public enum Tab {

    COMBAT("Combat Options"),
    SKILLS("Stats"),
    QUEST("Quest List"),
    INVENTORY("Inventory"),
    EQUIPMENT("Worn Equipment"),
    PRAYER("Prayer"),
    MAGIC("Magic"),
    CLAN_CHAT("Clan Chat"),
    FRIEND_LIST("Friends List"),
    IGNORE_LIST("Ignore List"),
    LOGOUT("Logout"),
    SETTINGS("Options"),
    EMOTES("Emotes"),
    MUSIC("Music Player");

    String name;

    private Tab(String name) {
        this.name = name;
    }

    /**
     * Get the name of the specified Tab.
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Open the specified Tab.
     *
     * @return Boolean
     */
    public boolean open() {
        if (!Game.isLoggedIn() || isOpen() || getWidgetChild() == null)
            return true;
        getWidgetChild().click(true);

        for (int i = 0; i < 20 && !isOpen(); i++, Time.sleep(100, 150)) ;

        return isOpen();
    }

    /**
     * Check if this tab is open.
     *
     * @return Boolean
     */
    public boolean isOpen() {
        Tab current = Game.getCurrentTab();
        return Game.isLoggedIn() && current != null && current.getName() != null && getName() != null && current.getName().equals(getName());
    }

    public WidgetChild getWidgetChild() {
        if (!Game.isLoggedIn())
            return null;
        for (WidgetChild p : Widgets.get(548).getChildren()) {
            if (p.getActions() != null) {
                for (String s : p.getActions()) {
                    if (getName().equalsIgnoreCase(s)) {
                        return p;
                    }
                }
            }
        }
        return null;
    }



}
