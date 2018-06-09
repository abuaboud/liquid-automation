package org.liquidbot.bot.script.loader.scanner;

import org.liquidbot.bot.utils.Logger;
import org.objectweb.asm.tree.ClassNode;

/*
 * Created on 8/3/14
 */
public abstract class CodeScanner {

    public Logger log = new Logger(getClass());

    public abstract boolean scan(ClassNode classNode);

    public void report(String name) {
        log.error("Found " + name + " Code in Script, Please Report that to Staff , if you think that wrong");
    }

}
