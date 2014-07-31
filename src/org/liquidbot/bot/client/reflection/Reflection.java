package org.liquidbot.bot.client.reflection;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.client.parser.FieldHook;
import org.liquidbot.bot.client.parser.HookReader;
import org.liquidbot.bot.client.parser.MethodHook;
import org.liquidbot.bot.utils.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;

/**
 * Created by Hiasat on 7/29/2014.
 */
public class Reflection {

    private static final Hashtable<String, Field> fields = new Hashtable<>();
    private static final Hashtable<String, Method> methods = new Hashtable<>();
    private static final Logger logger = new Logger(Reflection.class);
    private static final Configuration config = Configuration.getInstance();

    public static void init() {
        for (String fieldKey : HookReader.fields.keySet().toArray(new String[fields.size()])) {
            FieldHook fieldHook = HookReader.fields.get(fieldKey);
            fields.put(fieldKey, field(fieldKey));
        }
        for (String methodKey : HookReader.methods.keySet().toArray(new String[methods.size()])) {
            MethodHook methodHook = HookReader.methods.get(methodKey);
            methods.put(methodKey, method(methodKey));
        }
    }

    /**
     * @param fieldKey
     * @param instance
     * @return field value
     */
    public static Object value(String fieldKey, Object instance) {
        try {
            FieldHook fieldHook = HookReader.fields.get(fieldKey);
            if (fieldHook == null)
                logger.error("FieldHook null " + fieldKey);
            if (fieldHook != null && fieldHook.getType().equalsIgnoreCase("I"))
                return (int) fields.get(fieldKey).get(instance) * fieldHook.getMultiplier();
            return fields.get(fieldKey).get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * @param methodKey
     * @param instance
     * @return field value
     */
    public static Object invoke(String methodKey, Object instance,Object ...objects) {
        try {
            MethodHook methodHook = HookReader.methods.get(methodKey);
            if (methodHook == null)
                logger.error("MethodHook null " + methodKey);
            return methods.get(methodKey).invoke(instance,objects);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * @param methodKey
     * @return java.lang.reflect.Method
     */
    public static Method method(String methodKey) {
        MethodHook methodHook = HookReader.methods.get(methodKey);
        if (methodHook == null)
            logger.error("MethodHook null " + methodKey);
        for (Method method : config.getBotFrame().loader().loadClass(methodHook.getClassName()).getDeclaredMethods()) {
            if (method.getName().equalsIgnoreCase(methodHook.getMethodName())) {
                method.setAccessible(true);
                return method;
            }
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
            logger.error("FieldHook null " + fieldKey);
        try {
            Field field = config.getBotFrame().loader().loadClass(fieldHook.getClassName()).getDeclaredField(fieldHook.getFieldName());
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}
