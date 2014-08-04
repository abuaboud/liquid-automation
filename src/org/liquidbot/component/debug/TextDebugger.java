package org.liquidbot.component.debug;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.methods.interactive.Players;

import java.awt.*;
import java.util.ArrayList;


/**
 * Created by Kenneth on 7/30/2014.
 */
public class TextDebugger extends Debugger<String> {

    private final ArrayList<String> debuggedList = new ArrayList<>();
    private final Configuration config = Configuration.getInstance();

    @Override
    public String[] elements() {
        debuggedList.clear();
        drawText(config.drawPlayerLocation(), "Player Location ^^> " + Players.getLocal().getLocation().toString());
        return debuggedList.toArray(new String[debuggedList.size()]);
    }

    @Override
    public boolean activate() {
        return true;
    }

    @Override
    public void render(Graphics2D graphics) {
        int yOff = 30;
        for(String str : refresh()) {
            graphics.drawString(str, 15, yOff);
            yOff += 15;
        }
    }

    private void drawText(boolean active ,String debug){
        if(active)
            debuggedList.add(debug);
    }
}
