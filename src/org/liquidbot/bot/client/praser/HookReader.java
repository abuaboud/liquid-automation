package org.liquidbot.bot.client.praser;

import org.liquidbot.bot.utils.NetUtils;

import java.util.Hashtable;

/**
 * Created by Hiasat on 7/29/2014.
 */
public class HookReader {

    public static Hashtable<String, FieldHook> fields = new Hashtable<>();

    private static final String HOOKS_URL = "http://pastebin.com/raw.php?i=NMmd9zsQ";

    /**
     * Parse Html File to get hooks info
     */
    public static void init() {
        final String[] sourceCode = NetUtils.readPage(HOOKS_URL);
        for (String source : sourceCode) {
            if(!source.contains("#"))
                continue;
            final FieldHook fieldHook = new FieldHook(source);
            fields.put(fieldHook.getFieldKey(), fieldHook);
        }
    }


}
