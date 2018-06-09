package org.liquidbot.component.debug;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.methods.data.Settings;

import java.awt.*;
import java.util.ArrayList;

/*
 * Created on 8/7/14
 */
public class SettingsDebugger extends Debugger<String> {

    private int[] cache;

    private ArrayList<String> debugger = new ArrayList<>();

    @Override
    public String[] elements() {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean activate() {
        return Configuration.getInstance().drawSettings();
    }

    @Override
    public void render(Graphics2D graphics) {
        int[] settings = Settings.getAll();
        if (cache == null)
            cache = settings;
        for (int i = 0; i < 2000; i++) {
            if (cache[i] != settings[i]) {
                debugger.add(0, "Setting " + i + " : " + settings[i] + "-->" + cache[i]);
            }
        }
        while (debugger.size() > 10) {
            debugger.remove(debugger.size() - 1);
        }
        int y = 15;
        for (String s : debugger) {
            graphics.drawString(s, 15, y);
            y += 13;
        }
        cache = Settings.getAll();
    }
}
