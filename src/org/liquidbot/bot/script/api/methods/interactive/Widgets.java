package org.liquidbot.bot.script.api.methods.interactive;

import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.Widget;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;

import java.lang.reflect.Field;

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

    public static WidgetChild getWidgetWithText(String text) {
        Object[][] widgets = (Object[][]) Reflection.value("Client#getWidgets()", null);
        Field textField = Reflection.field("Widget#getText()");
        for (int parentIndex = 0; parentIndex < widgets.length; parentIndex++) {
            Object[] children = widgets[parentIndex];
            if(children == null)
                continue;
            for (int childIndex = 0; childIndex < children.length; childIndex++) {
                Object child = children[childIndex];
                if (child != null) {
                    String widgetText = (String) Reflection.value(textField, child);
                    if (widgetText != null && widgetText.equalsIgnoreCase(text)) {
                        return new WidgetChild(child, childIndex);
                    }
                }
            }
        }
        return null;
    }

    public static boolean canContinue() {
        WidgetChild widgetChild = getWidgetWithText("Click here to continue");
        return widgetChild != null && widgetChild.isVisible();
    }

    public static void clickContinue() {
        WidgetChild widgetChild = getWidgetWithText("Click here to continue");
        if (widgetChild == null || !widgetChild.isVisible())
            return;
        widgetChild.interact("Continue");
        for (int i = 0; i < 10 && widgetChild.isVisible(); i++, Time.sleep(100, 150)) ;
    }
}
