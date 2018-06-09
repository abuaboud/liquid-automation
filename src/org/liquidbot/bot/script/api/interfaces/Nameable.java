package org.liquidbot.bot.script.api.interfaces;

/**
 * Created on 7/29/2014.
 */
public interface Nameable {

    public String getName();

    public static interface Query<Q> {
        public Q name(final String... names);
    }

}
