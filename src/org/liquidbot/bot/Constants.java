package org.liquidbot.bot;

import java.awt.*;
import java.io.File;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class Constants {

    public static final double CLIENT_VERSION = 0.01;

    public static final String CLIENT_TITLE = "LiquidBot Automation";

    public static final int APPLET_WIDTH = 765;
    public static final int APPLET_HEIGHT = 503;

    public static final Rectangle GAME_SCREEN = new Rectangle(0, 0, APPLET_WIDTH, APPLET_HEIGHT);
    public static final Rectangle VIEWPORT = new Rectangle(5, 5, 509, 332);

    public static final String HOME_PATH = (new File(System.getProperty("user.home")).exists() ? System.getProperty("user.home") : "/root") + "/LiquidBot";
    public static final String SETTING_PATH = HOME_PATH + File.separator + "settings";
    public static final String SCRIPT_PATH = HOME_PATH + File.separator + "scripts";
    public static final String ACCOUNT_FILE_NAME = "Accounts.json";
    public static final String FORUM_FILE_NAME = "Forum.ini";

    public static final String KEYBOARD_KEYS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*~";
}
