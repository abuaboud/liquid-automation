package org.liquidbot.bot.loader;

import org.liquidbot.bot.script.AbstractScript;
import org.liquidbot.bot.script.Manifest;
import org.liquidbot.bot.utils.Logger;
import org.liquidbot.bot.utils.Utilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kenneth on 8/2/2014.
 */
public class ScriptLoader {

    private static final Logger log = new Logger(ScriptLoader.class);
    private static final List<AbstractScript> scripts = new ArrayList<>();

    public static List<AbstractScript> getLocalScripts() {
        scripts.clear();
        findScripts(new File(Utilities.getContentDirectory() + "scripts/"));
        log.info("Found " + scripts.size() + " scripts!");
        return scripts;
    }

    private static void findScripts(final File parent) {
        for(File child : parent.listFiles()) {
            if(child.isDirectory()) {
                findScripts(child);
            } else {
                if(child.getName().endsWith(".class") && !child.getName().contains("$")) {

                    log.info("Potential script found! - " + child.getAbsolutePath());
                    final Manifest manifest = child.getClass().getAnnotation(Manifest.class);
                    if(manifest == null) {
                        log.error("Manifest is null");
                    }

                    try {

                        AbstractScript script = (AbstractScript) Class.forName(getPath(child.getAbsolutePath())).newInstance();
                        log.info("Script: "+ script);
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }


                }
            }
        }
    }

    public static String getPath(String path) {
        String[] a = path.replace(File.separator, " ").split(" ");
        boolean found = false;
        String pack = "";
        for (String s : a) {
            if (found) {
                pack = pack + s + (a[a.length - 1].equalsIgnoreCase(s) ? "" : ".");
            }
            if (s.equalsIgnoreCase("scripts")) {
                found = true;
            }
        }
        return pack.replace(".class", "");
    }

}
