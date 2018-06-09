package org.liquidbot.bot.script.api.wrappers.definitions;

import org.liquidbot.bot.client.parser.HookReader;
import org.liquidbot.bot.client.reflection.Reflection;

import java.util.Hashtable;

/*
 * Created on 7/31/14
 */
public class ItemDefinition {

    private static Hashtable<Integer, String> cache = new Hashtable<>();

    private String name;

    public ItemDefinition(int Id) {
        if (cache.get(Id) == null) {
            Object raw = Reflection.invoke("Client#getItemComposite()", null, Id, HookReader.methods.get("Client#getItemComposite()").getCorrectParam());
            cache.put(Id, (String) Reflection.value("ItemComposite#getName()", raw));
        }
        name = cache.get(Id);
    }

    /**
     * Item Name
     *
     * @return String: Item Name
     */
    public String getName() {
        return name;
    }

    /**
     * check if item composite is null or not
     *
     * @return Boolean : return true if not null else false
     */
    public boolean isValid() {
        return name != null;
    }
}
