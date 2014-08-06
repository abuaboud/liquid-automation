package org.liquidbot.bot.script.api.methods.data;

import org.liquidbot.bot.client.reflection.Reflection;

/*
 * Created by Hiasat on 8/2/14
 */
public class Settings {

    public static int[] getAll() {
        int[] playerSettings = (int[]) Reflection.value("Client#getPlayerSettings()", null);
        int[] widgetSettings = (int[]) Reflection.value("Client#getWidgetSettings()", null);
        int[] allSettings = new int[playerSettings.length + widgetSettings.length];
        System.arraycopy(playerSettings, 0, allSettings, 0, playerSettings.length);
        System.arraycopy(widgetSettings, 0, allSettings, playerSettings.length, widgetSettings.length);
        return allSettings;
    }

    public static int get(int a) {
        int[] settings = getAll();
        if (settings.length <= a)
            return 0;
        return settings[a];
    }

}
