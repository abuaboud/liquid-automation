package org.liquidbot.bot.script.loader;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.AbstractScript;
import org.liquidbot.bot.script.Manifest;
import org.liquidbot.bot.script.SkillCategory;
import org.liquidbot.bot.ui.login.misc.User;
import org.liquidbot.bot.utils.Logger;
import org.liquidbot.bot.utils.NetUtils;
import org.liquidbot.bot.utils.Utilities;
import sun.security.krb5.Config;

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
        AbstractScript abstractScript = null;
        try {
            if (scriptInfo.scriptId == -1) {
                urlClassLoader = new URLClassLoader(new URL[]{Utilities.toUrl(SCRIPTS_PATH)});
                abstractScript = (AbstractScript) urlClassLoader.loadClass(scriptInfo.clazz).newInstance();
                urlClassLoader = null;
            } else {
                User user = Configuration.getInstance().getUser();
                ScriptClassLoader sc = new ScriptClassLoader(new URL("http://liquidbot.org/client/loadScript.php?userId=" + user.getUserId() + "&scriptId=" + scriptInfo.scriptId));
                if (sc.isSafe()) {
                    for (String entry : sc.entries().keySet().toArray(new String[sc.entries().keySet().size()])) {
                        try {
                            Class<?> clazz = sc.loadClass(entry);
                            if (clazz.getAnnotation(Manifest.class) != null) {
                                Object newInstance = clazz.newInstance();
                                if (newInstance instanceof AbstractScript) {
                                    abstractScript = (AbstractScript) newInstance;
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            // ignored
                        }
                    }
                } else {
                   log.error("This Script contains some Prohibited code, Please report that to Admin");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return abstractScript;
    }

    public static List<ScriptInfo> getScripts() {
        scripts.clear();
        getRepositoryScripts();
        getLocalScripts();
        return scripts;
    }

    public static List<ScriptInfo> getRepositoryScripts() {
        User user = Configuration.getInstance().getUser();

        String rawLine = NetUtils.readPage("http://liquidbot.org/client/scripts.php?userId=" + user.getUserId() + "&username=" + user.getDisplayName() + "&password=" + user.getHash() + "&action=view")[0];

        for (String script : rawLine.split("<br>")) {
            scripts.add(new ScriptInfo(script));
        }
        return scripts;
    }

    public static List<ScriptInfo> getLocalScripts() {
        urlClassLoader = new URLClassLoader(new URL[]{Utilities.toUrl(SCRIPTS_PATH)});
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
