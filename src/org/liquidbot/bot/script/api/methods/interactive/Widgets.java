package org.liquidbot.bot.script.api.methods.interactive;

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.wrappers.Widget;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;

/*
 * Created by Hiasat on 8/2/14
 */
public class Widgets {


    public static Widget[] get() {
        Object[][] widgets = (Object[][]) Reflection.value("Client#getWidgets()", null);
        Widget[] children = new Widget[widgets.length];
        for (int i = 0; i < widgets.length; i++) {
            children[i] = new Widget(widgets[i], i);
        }
        return children;
    }

    public static Widget get(int parent) {
        Object[][] widgets = (Object[][]) Reflection.value("Client#getWidgets()", null);
        if (widgets.length == 0 || (widgets.length - 1) < parent || parent < 0)
            return new Widget(null, parent);
        return new Widget(widgets[parent], parent);
    }

    public static WidgetChild get(int parent, int child) {
        Widget widgets = get(parent);
        if (widgets == null)
            return null;

        return widgets.getChild(child);
    }
}
