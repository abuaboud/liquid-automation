package org.liquidbot.bot.script.api.util;


/**
 * Created with IntelliJ IDEA.
 * User: Hiasat
 * Date: 7/28/13
 * Time: 5:25 PM
 */
public class Random {

    private static java.util.Random random = new java.util.Random();

    public static int nextInt(int min, int max) {
        if (min > max) {
            min = max - (min - max);
        }
        return random.nextInt(max - min) + min;
    }

}
