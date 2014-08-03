package org.liquidbot.bot.script.api.util;


import org.liquidbot.bot.script.api.interfaces.Condition;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {

    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * Parse Time from Long to String
     *
     * @param millis
     * @return String of Parsed time long
     */
    public static String parse(long millis) {
        long time = millis / 1000;
        String seconds = Integer.toString((int) (time % 60));
        String minutes = Integer.toString((int) ((time % 3600) / 60));
        String hours = Integer.toString((int) (time / 3600));
        String days = Integer.toString((int) (time / (3600 * 24)));

        for (int i = 0; i < 5; i++) {
            if (Integer.parseInt(hours) >= 24) {
                hours = Integer.parseInt(hours) - 24 + "";
            }
        }

        for (int i = 0; i < 2; i++) {
            if (seconds.length() < 2)
                seconds = "0" + seconds;
            if (minutes.length() < 2)
                minutes = "0" + minutes;
            if (hours.length() < 2)
                hours = "0" + hours;
        }

        if (Integer.parseInt(days) > 0)
            return days + ":" + hours + ":" + minutes + ":" + seconds;

        return hours + ":" + minutes + ":" + seconds;
    }

    /**
     * Get the current formated Date.
     *
     * @return String
     */
    public static String getDate() {
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Get the time stamp.
     *
     * @return String
     */
    public static String getTimeStamp() {
        return getDate().split(" ")[1];
    }

    /**
     * Sleep static amount
     *
     * @param i amount of sleep
     */
    public static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {

        }
    }

    /**
     * Sleep Random amount of time between min and max
     *
     * @param min
     * @param max
     */
    public static void sleep(int min, int max) {
        sleep(Random.nextInt(min, max));
    }

    /**
     * sleep while Condition is Active and amount is deadline for sleep
     *
     * @param condition
     * @param amount
     */
    public static void sleep(Condition condition, int amount) {
        Timer timer = new Timer(amount);
        while (condition.active() && timer.isRunning()) {
            Time.sleep(50, 70);
        }
    }
}
