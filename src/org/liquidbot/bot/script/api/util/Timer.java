package org.liquidbot.bot.script.api.util;


public class Timer {


    private long start;
    private long end;
    private long period;

    public Timer(long period) {
        this.start = System.currentTimeMillis();
        this.period = period;
        this.end = start + period;
    }

    public boolean isRunning() {
        return System.currentTimeMillis() < end;
    }

    public long getElapsed() {
        return System.currentTimeMillis() - start;
    }

    public long getRemaining() {
        return isRunning() ? end - System.currentTimeMillis() : 0;
    }

    public void reset() {
        this.end = System.currentTimeMillis() + period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String toElapsedString() {
        return Time.parse(getElapsed());
    }

    public String toRemainingString() {
        return Time.parse(getRemaining());
    }
}