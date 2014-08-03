package org.liquidbot.bot.script.loader;

import org.liquidbot.bot.script.AbstractScript;
import org.liquidbot.bot.script.Manifest;
import org.liquidbot.bot.script.SkillCategory;
import org.liquidbot.bot.utils.Logger;
import org.liquidbot.bot.utils.Utilities;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kenneth on 8/2/2014.
 */
public class ScriptLoader {

    private static final Logger log = new Logger(ScriptLoader.class);
    private static final List<ScriptInfo> scripts = new ArrayList<>();

    public static final String USER_HOME = System.getProperty("user.home");
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String SCRIPTS_PATH = ("file:" + USER_HOME + FILE_SEPARATOR + "LiquidBot"
            + FILE_SEPARATOR + "scripts" + FILE_SEPARATOR).replaceAll(" ", "%20");

    private static URLClassLoader urlClassLoader;

    public static AbstractScript loadScript(ScriptInfo scriptInfo) {
        urlClassLoader = URLClassLoader.newInstance(new URL[]{Utilities.toUrl(SCRIPTS_PATH)});
        AbstractScript abstractScript = null;
        try {
            abstractScript = (AbstractScript) urlClassLoader.loadClass(scriptInfo.clazz).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        urlClassLoader = null;
        return abstractScript;
    }

    public static List<ScriptInfo> getLocalScripts() {
        scripts.clear();
        urlClassLoader = URLClassLoader.newInstance(new URL[]{Utilities.toUrl(SCRIPTS_PATH)});
        findScripts(new File(Utilities.getContentDirectory() + "scripts/"));
        urlClassLoader = null;
        log.info("Found " + scripts.size() + " scripts!");
        return scripts;
    }

    private static void findScripts(final File parent) {
        try {
            for (File child : parent.listFiles()) {
                if (child.isDirectory()) {
                    findScripts(child);
                } else {
                    if (child.getName().endsWith(".class") && !child.getName().contains("$")) {

                        log.info("Potential script found! - " + getClassPath(child.getAbsolutePath()));
                        Class<?> clazz = urlClassLoader.loadClass(getClassPath(child.getAbsolutePath()));

                        final Manifest manifest = clazz.getAnnotation(Manifest.class);
                        if (manifest == null) {
                            log.error("Manifest is null");
                        }
                        ScriptInfo scriptInfo = new ScriptInfo(getClassPath(child.getAbsolutePath()), manifest.name(), manifest.description(), manifest.author(), SkillCategory.MISC);
                        log.info("Script: " + scriptInfo.name);
                        scripts.add(scriptInfo);

                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getClassPath(String path) {
        String[] a = path.replace(FILE_SEPARATOR, " ").split(" ");
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
