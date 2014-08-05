package org.liquidbot.component.debug;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.wrappers.Player;

import java.awt.*;
import java.util.ArrayList;


/**
 * Created by Kenneth on 7/30/2014.
 */
public class TextDebugger extends Debugger<String> {

    private static ArrayList<String> debuggedList = new ArrayList<String>();

    @Override
    public String[] elements() {
        debuggedList.clear();
        drawText(Configuration.getInstance().drawPlayerLocation(), "Player Location ^^> " + Players.getLocal().getLocation().toString());
        return debuggedList.toArray(new String[debuggedList.size()]);
    }

    @Override
    public boolean activate() {
        return true;
    }

    @Override
    public void render(Graphics2D graphics) {
        java.util.List<String> list = refresh();
        graphics.setColor(Color.WHITE);
        for(int i = 0 ; i < list.size();i++){
            graphics.drawString(list.get(i), 15, 30 + (i * 15));
        }
    }

    private void drawText(boolean active ,String debug){
        if(active)
            debuggedList.add(debug);
    }
}
