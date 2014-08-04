package org.liquidbot.bot.script.api.wrappers;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Hiasat on 8/2/14
 */
public class Widget {

    private Object[] raw;
    private int index;

    public Widget(Object[] raw, int index) {
        this.raw = raw;
        this.index = index;
    }

    public WidgetChild[] getChildren() {
        List<WidgetChild> list = new ArrayList<>();
        if(raw == null)
            return list.toArray(new WidgetChild[list.size()]);
        for (int i = 0; i < raw.length; i++) {
            list.add(new WidgetChild(raw[i], i));
        }
        return list.toArray(new WidgetChild[list.size()]);
    }

    public WidgetChild getChild(int index) {
        if (raw == null || raw.length <= index) {
            return new WidgetChild(null, index);
        }
        return new WidgetChild(raw[index], index);
    }

    public boolean isValid() {
        return raw != null;
    }

    public int getIndex() {
        return index;
    }
}
