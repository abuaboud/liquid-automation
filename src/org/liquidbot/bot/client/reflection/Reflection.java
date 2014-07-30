package org.liquidbot.bot.client.reflection;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.client.praser.FieldHook;
import org.liquidbot.bot.client.praser.HookReader;

import java.lang.reflect.Field;
import java.util.Hashtable;

/**
 * Created by Hiasat on 7/29/2014.
 */
public class Reflection {

    private static Hashtable<String, Field> fields = new Hashtable<>();

    public static void init() {
        for (String fieldKey : HookReader.fields.keySet().toArray(new String[fields.size()])) {
            FieldHook fieldHook = HookReader.fields.get(fieldKey);
            if (fieldHook.getType().contains("("))
                continue;
            fields.put(fieldKey, field(fieldKey));
        }
    }

    /**
     * @param fieldKey
     * @param instance
     * @return field value
     */
    public static Object value(String fieldKey, Object instance) {
        try {
            return fields.get(fieldKey).get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param fieldKey
     * @return java.lang.reflect.Field
     */
    public static Field field(String fieldKey) {
        FieldHook fieldHook = HookReader.fields.get(fieldKey);
        if (fieldHook == null)
            System.out.println("Error FieldHook null");
        try {
            return Configuration.botFrame.loader().loadClass(fieldHook.getClassName()).getDeclaredField(fieldHook.getFieldName());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}
