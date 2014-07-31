package org.liquidbot.bot.script;

/**
 * Created by Kenneth on 7/30/2014.
 */
public @interface Manifest {

    public String name();
    public String author();
    public String description() default "";
    public double version() default 1.0;

}
