package org.liquidbot.bot.client.parser;

import org.liquidbot.bot.utils.NetUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;

/**
 * Created by Hiasat on 7/29/2014.
 */
public class HookReader {

    public static Hashtable<String, FieldHook> fields = new Hashtable<>();
    public static Hashtable<String, MethodHook> methods = new Hashtable<>();

    private static final String HOOKS_URL = "http://liquidbot.org/client/hooks.html";

    /**
     * Parse Html File to get hooks info
     */
    public static void init() {
        final String[] sourceCode = NetUtils.readPage(HOOKS_URL);
        for (String source : sourceCode) {
            if (!source.contains("#"))
                continue;
            String type = source.split(" ")[2];
            if (type.contains("(")) {
                final MethodHook methodHook = new MethodHook(source);
                methods.put(methodHook.getMethodKey(), methodHook);
            } else {
                final FieldHook fieldHook = new FieldHook(source);
                fields.put(fieldHook.getFieldKey(), fieldHook);
            }
        }
    }


}
