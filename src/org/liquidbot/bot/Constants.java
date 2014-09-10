package org.liquidbot.bot;

import java.awt.*;
import java.io.File;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class Constants {

	public static final double CLIENT_VERSION = 0.14;

	public static final String CLIENT_TITLE = "LiquidBot Automation";

	public static final int APPLET_WIDTH = 763;
	public static final int APPLET_HEIGHT = 504;


	public static final Rectangle GAME_SCREEN = new Rectangle(0, 0, APPLET_WIDTH, APPLET_HEIGHT);
	public static final Rectangle VIEWPORT = new Rectangle(5, 5, 509, 332);

	public static final String HOME_PATH = (new File(System.getProperty("user.home")).exists() ? System.getProperty("user.home") : "/root") + "/LiquidBot";
	public static final String SETTING_PATH = HOME_PATH + File.separator + "settings";
	public static final String ACCOUNT_FILE_NAME = "Accounts.json";
	public static final String FORUM_FILE_NAME = "Forum.ini";
	public static final String SETTING_FILE_NAME = "Settings.ini";
    public static final String PATTERN_FILE_NAME = "Patterns.ini";

	public static final String SITE_URL = "http://www.liquidbot.org";

	public static final int[] WORLDS = {301, 302, 303, 304, 305, 306, 308, 309, 310,
			311, 312, 313, 314, 316, 317, 318, 319, 320, 321, 322, 325, 326, 327, 328, 329, 330,
			333, 334, 335, 336, 337, 338, 341, 342, 343, 344, 345, 346, 349, 350, 351, 352, 353,
			354, 357, 358, 359, 360, 361, 362, 365, 366, 367, 368, 369, 370, 373, 374, 375, 376,
			377, 378, 381, 382, 383, 384, 393, 394};

	public static final String KEYBOARD_KEYS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*~";
}
