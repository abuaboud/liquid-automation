package org.liquidbot.bot.script;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.utils.Logger;

/**
 * Created by Kenneth on 7/29/2014.
 */
public abstract class AbstractScript {

    public Configuration client = Configuration.getInstance();
    public Logger log = new Logger(getClass());


}
