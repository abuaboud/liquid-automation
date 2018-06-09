package org.liquidbot.bot.script.api.methods.input;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.util.Time;

import java.awt.event.KeyEvent;

/*
 * Created on 7/31/14
 */
public class Keyboard {

    public static void pressEnter() {
        press(KeyEvent.VK_ENTER);
        release(KeyEvent.VK_ENTER);
    }

    private static Configuration config = Configuration.getInstance();

    public static void press(int event) {
        config.getKeyboard().press(config.getKeyboard().create(KeyEvent.KEY_PRESSED, event, (char) event));
    }

    public static void release(int event) {
        config.getKeyboard().release(config.getKeyboard().create(KeyEvent.KEY_RELEASED, event, (char) event));
    }

    public static void type(char c) {
        config.getKeyboard().type(c);
    }

    public static void sendText(String text, boolean pressEnter, int minSleep, int maxSleep) {
        for (int i = 0; i < text.toCharArray().length; i++) {
            type(text.toCharArray()[i]);
            Time.sleep(minSleep, maxSleep);
        }
        if (pressEnter)
            pressEnter();
    }

    public static void sendText(String text, boolean pressEnter) {
        for (int i = 0; i < text.toCharArray().length; i++) {
            type(text.toCharArray()[i]);
            Time.sleep(90, 120);
        }

        if (pressEnter)
            pressEnter();
    }
}
