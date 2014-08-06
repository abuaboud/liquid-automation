package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.methods.data.Bank;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.Inventory;
import org.liquidbot.bot.script.api.methods.data.Settings;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.Item;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;
import org.liquidbot.bot.script.randevent.RandomEvent;

/*
* Created by Hiasat on 8/3/14
*/
public class Reward extends RandomEvent {
    private int currentId = 0;

    private static final int REWARD_INTERFACE = 134;

    private static final int ATTACK_ID = 3;
    private static final int STRENGTH_ID = 4;
    private static final int RANGED_ID = 5;
    private static final int MAGIC_ID = 6;
    private static final int DEFENCE_ID = 7;
    private static final int HITPOINTS_ID = 8;
    private static final int PRAYER_ID = 9;
    private static final int AGILITY_ID = 10;
    private static final int HERBLORE_ID = 11;
    private static final int THIEVING_ID = 12;
    private static final int CRAFTING_ID = 13;
    private static final int RUNECRAFT_ID = 14;
    private static final int MINING_ID = 15;
    private static final int SMITHING_ID = 16;
    private static final int FISHING_ID = 17;
    private static final int COOKING_ID = 18;
    private static final int FIREMAKING_ID = 19;
    private static final int WOODCUTTING_ID = 20;
    private static final int FLETCHING_ID = 21;
    private static final int SLAYER_ID = 22;
    private static final int FARMING_ID = 23;
    private static final int CONSTRUCTION_ID = 24;
    private static final int HUNTER_ID = 25;

    private static final int CONFIRM_ID = 26;


    private static final String BOOK_OF_KNOWLEDGE = "Book of knowledge";
    private static final String LAMP = "Lamp";

    private static final String[] REWARD_NAMES = {BOOK_OF_KNOWLEDGE, LAMP};

    @Override
    public boolean active() {
        return Game.isLoggedIn() && getReward() != null && !Bank.isOpen() && !Players.getLocal().isInCombat();
    }

    @Override
    public void solve() {
        if (currentId == 0) {
            setSkillId();
        } else {
            if (Widgets.get(REWARD_INTERFACE).isValid()) {
                if (Settings.get(261) == currentId - 2) {
                    final WidgetChild confirm = Widgets.get(REWARD_INTERFACE, CONFIRM_ID);
                    if (confirm.isVisible()) {
                        confirm.click(true);
                        for (int i = 0; i < 40 && Widgets.get(REWARD_INTERFACE).isValid(); i++) {
                            Time.sleep(40, 60);
                        }
                    }
                } else {
                    final WidgetChild skillChild = Widgets.get(REWARD_INTERFACE, currentId);
                    if (skillChild.isVisible()) {
                        skillChild.click();
                        for (int i = 0; i < 40 && Settings.get(261) != currentId - 2; i++) {
                            Time.sleep(40, 60);
                        }
                    }
                }
            } else {
                Item reward = getReward();
                if (reward != null && reward.isValid()) {
                    if (reward.interact("Read") || reward.interact("Rub")) {
                        for (int i = 0; i < 40 && !Widgets.get(REWARD_INTERFACE).isValid(); i++) {
                            Time.sleep(40, 60);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public String getName() {
        return "Reward";
    }

    @Override
    public String getAuthor() {
        return "Lorex";
    }


    private void setSkillId() {
        switch ("") {
            case "Agility":
                currentId = AGILITY_ID;
                break;
            case "Attack":
                currentId = ATTACK_ID;
                break;
            case "Construction":
                currentId = CONSTRUCTION_ID;
                break;
            case "Crafting":
                currentId = CRAFTING_ID;
                break;
            case "Cooking":
                currentId = COOKING_ID;
                break;
            case "Defence":
                currentId = DEFENCE_ID;
                break;
            case "Farming":
                currentId = FARMING_ID;
                break;
            case "Firemaking":
                currentId = FIREMAKING_ID;
                break;
            case "Fishing":
                currentId = FISHING_ID;
                break;
            case "Fletching":
                currentId = FLETCHING_ID;
                break;
            case "Herblore":
                currentId = HERBLORE_ID;
                break;
            case "Hitpoints":
                currentId = HITPOINTS_ID;
                break;
            case "Hunter":
                currentId = HUNTER_ID;
                break;
            case "Magic":
                currentId = MAGIC_ID;
                break;
            case "Mining":
                currentId = MINING_ID;
                break;
            case "Prayer":
                currentId = PRAYER_ID;
                break;
            case "Ranged":
                currentId = RANGED_ID;
                break;
            case "Runecraft":
                currentId = RUNECRAFT_ID;
                break;
            case "Slayer":
                currentId = SLAYER_ID;
                break;
            case "Smithing":
                currentId = SMITHING_ID;
                break;
            case "Strength":
                currentId = STRENGTH_ID;
                break;
            case "Thieving":
                currentId = THIEVING_ID;
                break;
            case "Woodcutting":
                currentId = WOODCUTTING_ID;
                break;
            default:
                currentId = COOKING_ID;
                break;
        }
    }

    private Item getReward() {
        Item[] items = Inventory.getAllItems();
        for (Item item : items) {
            if (item != null) {
                String itemName = item.getName();
                if (itemName != null) {
                    for (String name : REWARD_NAMES) {
                        if (itemName.toLowerCase().equals(name.toLowerCase()) || itemName.toLowerCase().equals("lamp")) {
                            return item;
                        }
                    }
                }
            }
        }
        return null;
    }
}
