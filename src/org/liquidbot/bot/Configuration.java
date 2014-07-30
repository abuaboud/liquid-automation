package org.liquidbot.bot;

import org.liquidbot.bot.client.input.InternalKeyboard;
import org.liquidbot.bot.client.input.InternalMouse;
import org.liquidbot.bot.ui.BotFrame;
import org.liquidbot.component.Canvas;

/**
 * Created by Hiasat on 7/29/2014.
 */
public class Configuration {

    public static InternalKeyboard keyboard;
    public static InternalMouse mouse;
    public static Canvas canvas;
    public static BotFrame botFrame;

    public static boolean drawPlayers = false;
    public static boolean drawNPCs = false;
    public static boolean drawMouse = true;
    public static boolean drawCanvas = true;
}
