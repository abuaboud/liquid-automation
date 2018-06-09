package org.liquidbot.bot.script.api.interfaces;

/**
 * Created on 7/29/2014.
 */
public interface Identifiable {

    public int getId();

    public static interface Query<Q> {
        public Q id(final int... ids);
    }

}
