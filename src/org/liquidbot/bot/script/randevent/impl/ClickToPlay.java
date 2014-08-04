package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;
import org.liquidbot.bot.script.randevent.RandomEvent;

/*
 * Created by Hiasat on 8/3/14
 */
public class ClickToPlay extends RandomEvent {

    private final int LOBBY_WIDGET = 378;
    private final int CLICK_TO_PLAY = 17;

    @Override
    public String getName() {
        return "ClickToPlay";
    }

    @Override
    public String getAuthor() {
        return "Hiasat";
    }

    @Override
    public boolean active() {
        return Game.getGameState() != Game.STATE_LOG_IN_SCREEN && Widgets.get(LOBBY_WIDGET).isValid();
    }

    @Override
    public void solve() {
        WidgetChild clickToPlay = Widgets.get(LOBBY_WIDGET, CLICK_TO_PLAY);
        setStatus("Clicking To Play");
        if (clickToPlay.isVisible()) {
            clickToPlay.interact("Play RuneScape");
            for(int i = 0 ; i < 25 && Widgets.get(LOBBY_WIDGET).isValid();i++, Time.sleep(100,150));
        }
    }

    @Override
    public void reset() {

    }
}
