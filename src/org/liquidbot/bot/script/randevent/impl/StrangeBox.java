package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.methods.data.Bank;
import org.liquidbot.bot.script.api.methods.data.Inventory;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.Widget;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;
import org.liquidbot.bot.script.randevent.RandomEvent;
import org.liquidbot.bot.utils.Utilities;

/*
 * Created by Hiasat on 8/3/14
 */
public class StrangeBox extends RandomEvent {

    private final String BOX_NAME = "Strange box";
    private final int WIDGET_BOX = 190;
    private final int WIDGET_BOX_QUESTION = 6;

    String[] shape_name = {"circle", "pentagon", "square", "star", "triangle"};

    @Override
    public String getName() {
        return "Strange Box";
    }

    @Override
    public String getAuthor() {
        return "Hiasat";
    }

    @Override
    public boolean active() {

        return Inventory.contains(BOX_NAME) && !Players.getLocal().isInCombat() && !Bank.isOpen();
    }

    @Override
    public void solve() {
        Widget boxWidget = Widgets.get(WIDGET_BOX);
        if (boxWidget.isValid()) {
            WidgetChild widgetChild = findWidgetWithAnswer(findAnswer(boxWidget.getChild(WIDGET_BOX_QUESTION).getText()));
            if (widgetChild != null && widgetChild.isVisible()) {
                Time.sleep(1000, 2000); //Humans need sometime to think!
                widgetChild.interact("Ok");
                for (int i = 0; i < 50 && Widgets.get(WIDGET_BOX).isValid(); i++, Time.sleep(100, 150)) ;
            }
        } else {
            Inventory.getItem(BOX_NAME).interact("Open");
            for (int i = 0; i < 50 && !Widgets.get(WIDGET_BOX).isValid(); i++, Time.sleep(100, 150)) ;
        }
    }

    private String findAnswer(String question) {
        question = question.toLowerCase();
        final int[] circle = {7005, 7020, 7035};
        final int[] pentagon = {7006, 7021, 7036};
        final int[] square = {7007, 7022, 7037};
        final int[] star = {7008, 7023, 7038};
        final int[] triangle = {7009, 7024, 7039};

        final int[][] shape_id = {circle, pentagon, square, star, triangle};
        final String[] shape_name = {"circle", "pentagon", "square", "star", "triangle"};

        final int[] n0 = {7010, 7025, 7040};
        final int[] n1 = {7011, 7026, 7041};
        final int[] n2 = {7012, 7027, 7042};
        final int[] n3 = {7013, 7028, 7043};
        final int[] n4 = {7014, 7029, 7044};
        final int[] n5 = {7015, 7030, 7045};
        final int[] n6 = {7016, 7031, 7046};
        final int[] n7 = {7017, 7032, 7047};
        final int[] n8 = {7018, 7033, 7048};
        final int[] n9 = {7019, 7034, 7049};

        final int[][] number_id = {n0, n1, n2, n3, n4, n5, n6, n7, n8, n9};
        final String[] number_name = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        String[] number = {"", "", ""};
        String[] shape = {"", "", ""};

        for (int i = 0; i < 3; i++) {
            for (int a = 0; a < shape_id.length; a++) {
                if (Utilities.inArray(Widgets.get(WIDGET_BOX, i).getModelId(), shape_id[a])) {
                    shape[i] = shape_name[a];
                }
            }
            for (int a = 0; a < number_id.length; a++) {
                if (Utilities.inArray(Widgets.get(WIDGET_BOX, i + 3).getModelId(), number_id[a])) {
                    number[(i)] = number_name[a];
                }
            }
        }

        if (question.contains("shape has")) {
            for (int i = 0; i < number.length; i++) {
                if (question.toLowerCase().contains(number[i].toLowerCase())) {
                    return shape[i];
                }
            }
        } else {
            for (int i = 0; i < shape.length; i++) {
                if (question.toLowerCase().contains(shape[i].toLowerCase())) {
                    return number[i];
                }
            }
        }
        return null;
    }


    private WidgetChild findWidgetWithAnswer(String answer) {
        for (int i = 10; i < 13; i++) {
            if (Widgets.get(WIDGET_BOX, i).getText().toLowerCase().contains(answer.toLowerCase())) {
                return Widgets.get(WIDGET_BOX, i - 3);
            }
        }
        return null;
    }

    @Override
    public void reset() {

    }
}
