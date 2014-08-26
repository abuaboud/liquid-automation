package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.interfaces.Condition;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.interactive.GameEntities;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.GameObject;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.api.wrappers.Tile;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;
import org.liquidbot.bot.script.randevent.RandomEvent;
import org.liquidbot.bot.utils.Utilities;


/*
 * Created by Hiasat on 8/5/14
 */
public class SurpriseExam extends RandomEvent {

    private final int WIDGET_PATTREN = 103;
    private final int WIDGET_ITEM_BOARD = 559;
    private final int[] RANGED = {11539, 11540, 11541, 11614, 11615, 11633};
    private final int[] COOKING = {11526, 11529, 11545, 11549, 11550, 11555, 11560, 11563,
            11564, 11607, 11608, 11616, 11620, 11621, 11622, 11623, 11628, 11629, 11634, 11639,
            11641, 11649, 11624};
    private final int[] FISHING = {11527, 11574, 11578, 11580, 11599, 11600, 11601, 11602,
            11603, 11604, 11605, 11606, 11625};
    private final int[] COMBAT = {11528, 11531, 11536, 11537, 11579, 11591, 11592, 11593,
            11597, 11627, 11631, 11635, 11636, 11638, 11642, 11648};
    private final int[] FARMING = {11530, 11532, 11547, 11548, 11554, 11556, 11571, 11581,
            11586, 11610, 11645};
    private final int[] MAGIC = {11533, 11534, 11538, 11562, 11567, 11582};
    private final int[] FIREMAKING = {11535, 11551, 11552, 11559, 11646};
    private final int[] HATS = {11540, 11557, 11558, 11560, 11570, 11619, 11626, 11630,
            11632, 11637, 11654};
    private final int[] PIRATE = {11570, 11626, 11558};
    private final int[] JEWELLERY = {11572, 11576, 11652};
    private final int[] DRINKS = {11542, 11543, 11544, 11644, 11647};
    private final int[] WOODCUTTING = {11573, 11595};
    private final int[] BOOTS = {11561, 11618, 11650, 11651};
    private final int[] CRAFTING = {11546, 11553, 11565, 11566, 11568, 11569, 11572, 11575,
            11576, 11577, 11581, 11583, 11584, 11585, 11643, 11652, 11653};
    private final int[] MINING = {11587, 11588, 11594, 11596, 11598, 11609, 11610};
    private final int[] SMITHING = {11611, 11612, 11613};
    private final int[][] ITEMS = {RANGED, COOKING, FISHING, COMBAT, FARMING, MAGIC,
            FIREMAKING, HATS, DRINKS, WOODCUTTING, BOOTS, CRAFTING, MINING, SMITHING};

    private final Question[] simQuestions = {
            new Question("i feel like a fish out of water", FISHING),
            new Question("I never leave the house without some sort of jewellery.", JEWELLERY),
            new Question("Pretty accessories made from silver and gold", JEWELLERY),
            new Question("There is no better feeling than", JEWELLERY),
            new Question("I'm feeling dehydrated", DRINKS),
            new Question("All this work is making me thirsty", DRINKS),
            new Question("quenched my thirst", DRINKS), new Question("light my fire", FIREMAKING),
            new Question("fishy", FISHING), new Question("fishing for answers", FISHING),
            new Question("sea food", FISHING), new Question("fish out of water", DRINKS),
            new Question("strange headgear", HATS), new Question("tip my hat", HATS),
            new Question("thinking cap", HATS), new Question("wizardry here", MAGIC),
            new Question("rather mystical", MAGIC), new Question("abracada", MAGIC),
            new Question("hide one's face", HATS), new Question("shall unmask", HATS),
            new Question("hand-to-hand", COMBAT), new Question("hate melee and magic", COMBAT),
            new Question("hate ranging and magic", COMBAT), new Question("melee weapon", COMBAT),
            new Question("prefers melee", COMBAT), new Question("me hearties", PIRATE),
            new Question("puzzle for landlubbers", PIRATE), new Question("mighty pirate", PIRATE),
            new Question("mighty archer", RANGED), new Question("as an arrow", RANGED),
            new Question("RANGED attack", RANGED), new Question("shiny things", CRAFTING),
            new Question("igniting", FIREMAKING),
            new Question("sparks from my synapses.", FIREMAKING), new Question("burn", FIREMAKING),
            new Question("fire.", FIREMAKING), new Question("disguised", HATS),
            new Question("range", RANGED), new Question("arrow", RANGED),
            new Question("drink", DRINKS), new Question("logs", FIREMAKING),
            new Question("light", FIREMAKING), new Question("headgear", HATS),
            new Question("hat", HATS), new Question("cap", HATS), new Question("mine", MINING),
            new Question("mining", MINING), new Question("ore", MINING),
            new Question("fish", FISHING), new Question("fishing", FISHING),
            new Question("thinking cap", HATS), new Question("cooking", COOKING),
            new Question("cook", COOKING), new Question("bake", COOKING),
            new Question("farm", FARMING), new Question("farming", FARMING),
            new Question("cast", MAGIC), new Question("magic", MAGIC),
            new Question("craft", CRAFTING), new Question("boot", BOOTS),
            new Question("chop", WOODCUTTING), new Question("cut", WOODCUTTING),
            new Question("tree", WOODCUTTING),};


    private final int WIDGET_TEXT = 242;
    private final int WIDGET_CHILD_TEXT = 2;
    private final int WIDGET_QUESTION_TEXT = 72;
    private final int WIDGET_OKAY = 70;
    private final int WIDGET_CLOSE = 6;


    private int currentDoor = -1;

    @Override
    public String getName() {
        return "Surprise Exam";
    }

    @Override
    public String getAuthor() {
        return "Hiasat";
    }

    @Override
    public boolean active() {
        final NPC dragon = NPCs.getNearest("Mr. Mordaut");
        return Game.isLoggedIn() && dragon.isValid() && dragon.distanceTo() < 25;
    }

    @Override
    public void solve() {
        if (currentDoor != -1) {
            GameObject door = GameEntities.getNearest(currentDoor);
            if (door.isValid()) {
                if (door.isOnScreen()) {
                    setStatus("Opening Door.");
                    door.interact("Open");
                    for (int i = 0; i < 25 && active(); i++, Time.sleep(100, 150)) ;
                } else {
                    Walking.walkTo(door.getLocation());
                    for (int i = 0; i < 25 && door.isOnScreen(); i++, Time.sleep(100, 150)) ;
                }
            }
        } else {
            if (Widgets.canContinue()) {
                if (Widgets.get(WIDGET_TEXT).isValid()) {
                    String text = Widgets.get(WIDGET_TEXT, WIDGET_CHILD_TEXT).getText();
                    if (text.toLowerCase().contains("purple")) {
                        currentDoor = getDoorId(0, 11);
                    } else if (text.toLowerCase().contains("red")) {
                        currentDoor = getDoorId(-5, 0);
                    } else if (text.toLowerCase().contains("green")) {
                        currentDoor = getDoorId(5, -3);
                    } else if (text.toLowerCase().contains("blue")) {
                        currentDoor = getDoorId(-5, 8);
                    }
                    setStatus("Correct Door : " + currentDoor);
                }
                Widgets.clickContinue();
            } else if (Widgets.get(WIDGET_ITEM_BOARD).isValid()) {
                int[] pattern = new int[15];
                for (int i = 24; i < 39; i++) {
                    pattern[i - 24] = Widgets.get(WIDGET_ITEM_BOARD, i).getModelId();
                }
                String question = Widgets.get(WIDGET_ITEM_BOARD, WIDGET_QUESTION_TEXT).getText();
                int[] answers = null;
                for (Question q : simQuestions) {
                    if (question.toLowerCase().contains(q.getQuestion().toLowerCase())) {
                        answers = q.getAnswers();
                        break;
                    }
                }
                if (answers != null) {
                    int found = 0;
                    for (int i = 24; i < 39; i++) {
                        WidgetChild widgetChild = Widgets.get(WIDGET_ITEM_BOARD, i);
                        if (Utilities.inArray(widgetChild.getModelId(), answers)) {
                            widgetChild.click();
                            found++;
                            Time.sleep(1000, 2000);
                        }
                    }
                    if (found == 0) {
                        setStatus("Closing Item Board");
                        Widgets.get(WIDGET_ITEM_BOARD, WIDGET_CLOSE).click(true);
                    } else {
                        setStatus("Pressing Okay");
                        Widgets.get(WIDGET_ITEM_BOARD, WIDGET_OKAY).click(true);
                    }
                    Time.sleep(new Condition() {
                        @Override
                        public boolean active() {
                            return Widgets.get(WIDGET_ITEM_BOARD, WIDGET_OKAY).isVisible();
                        }
                    }, 3000);
                    if (Widgets.get(WIDGET_ITEM_BOARD).isValid()) {
                        setStatus("Closing Item Board");
                        Widgets.get(WIDGET_ITEM_BOARD, WIDGET_CLOSE).click(true);
                        Time.sleep(new Condition() {
                            @Override
                            public boolean active() {
                                return Widgets.get(WIDGET_ITEM_BOARD, WIDGET_OKAY).isVisible();
                            }
                        }, 3000);
                    }
                }
            } else if (Widgets.get(WIDGET_PATTREN).isValid()) {
                int[] pattern = new int[3];
                for (int i = 8; i < 11; i++) {
                    pattern[i - 8] = Widgets.get(WIDGET_PATTREN, i) != null ? Widgets.get(WIDGET_PATTREN, i).getModelId() : 0;
                }
                int matched = 0;
                int[] itemsGroup = null;
                for (int[] group : ITEMS) {
                    for (int item : group) {
                        for (int p : pattern) {
                            if (p == item) {
                                matched++;
                            }
                        }
                    }
                    if (matched == 3) {
                        itemsGroup = group;
                        break;
                    }
                }
                if (itemsGroup != null) {
                    boolean foundAnswer = false;
                    for (int i = 12; i < 16; i++) {
                        WidgetChild answer = Widgets.get(WIDGET_PATTREN, i);
                        if (answer.isVisible() && Utilities.inArray(answer.getModelId(), itemsGroup)) {
                            setStatus("Clicking on Answer");
                            answer.click();
                            foundAnswer = true;
                            break;
                        }
                    }
                    if (!foundAnswer) {
                        setStatus("Guessing the Answer");
                        Widgets.get(WIDGET_PATTREN, Random.nextInt(10, 13)).click(true);
                    }
                } else {
                    setStatus("Guessing the Answer");
                    Widgets.get(WIDGET_PATTREN, Random.nextInt(10, 13)).click(true);
                }
                Time.sleep(new Condition() {
                    @Override
                    public boolean active() {
                        return Widgets.get(WIDGET_PATTREN).isValid();
                    }
                }, 3000);
            } else {
                setStatus("Talking to Mr. Mordaut");
                final NPC dragon = NPCs.getNearest("Mr. Mordaut");
                if (dragon.isOnScreen()) {
                    dragon.interact("Talk-to");
                    for (int i = 0; i < 25 && !Widgets.canContinue(); i++, Time.sleep(100, 150)) ;
                } else {
                    Walking.walkTo(dragon.getLocation());
                }
            }
        }
    }

    private int getDoorId(int x, int y) {
        return GameEntities.getAt(new Tile(Game.getBaseX() + x + 54, Game.getBaseY() + 46 + y)).getId();
    }


    @Override
    public void reset() {
        currentDoor = -1;
    }

    private class Question {
        String Question;
        int[] items;

        private Question(String Question, int[] items) {
            this.Question = Question;
            this.items = items;
        }

        public String getQuestion() {
            return this.Question;
        }

        public int[] getAnswers() {
            return this.items;
        }
    }


}
