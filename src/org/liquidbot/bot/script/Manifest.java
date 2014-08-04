package org.liquidbot.bot.script;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Kenneth on 7/30/2014.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Manifest {

    public String name();

    public String author();

    public String description() default "";

    public double version() default 1.0;

}
