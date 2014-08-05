package org.liquidbot.bot.script.api.methods.data;


import org.liquidbot.bot.client.reflection.Reflection;

public class Skills {

    public static int NUM_SKILLS = 23;

    private static final int[] XP_TABLE = {0, 0, 83, 174, 276, 388, 512, 650,
            801, 969, 1154, 1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523,
            3973, 4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031,
            13363, 14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408,
            33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127,
            83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636,
            184040, 203254, 224466, 247886, 273742, 302288, 333804, 368599,
            407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445,
            899257, 992895, 1096278, 1210421, 1336443, 1475581, 1629200,
            1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594,
            3597792, 3972294, 4385776, 4842295, 5346332, 5902831, 6517253,
            7195629, 7944614, 8771558, 9684577, 10692629, 11805606, 13034431,
            14391160, 15889109, 17542976, 19368992, 21385073, 23611006,
            26068632, 28782069, 31777943, 35085654, 38737661, 42769801,
            47221641, 52136869, 57563718, 63555443, 70170840, 77474828,
            85539082, 94442737, 104273167};

    /**
     * Exp skills exp as array
     *
     * @return int[] : return array of skills exp array
     */
    private static int[] getSkillExpArray() {
        return (int[]) Reflection.value("Client#getSkillExpArray()", null);
    }

    /**
     * Skill level as array
     *
     * @return int[] : return array of real skills level array
     * which is modified by Potion,Spell or anything else
     */
    private static int[] getSkillLevelArray() {
        return (int[]) Reflection.value("Client#getSkillLevelArray()", null);
    }

    /**
     * Real skill level as array
     *
     * @return int[] : return array of real skills level array
     * which isn't modified by Potion,Spell or anything else
     */
    private static int[] getRealSkillLevelArray() {
        return (int[]) Reflection.value("Client#getRealSkillLevelArray()", null);
    }

    /**
     * Returns the real level of the specified skill that
     * has not been modified by any spells, potions etc..
     *
     * @param skill
     * @return Integer
     */
    public static int getRealLevel(int skill) {
        if (getRealSkillLevelArray() == null)
            return 99;

        return getRealSkillLevelArray()[skill];
    }

    /**
     * Returns the real level of the specified skill that
     * has or has not been modified by any spells, potions etc..
     *
     * @param skill
     * @return Integer
     */
    public static int getCurrentLevel(int skill) {
        if (getSkillLevelArray() == null)
            return 99;

        return getSkillLevelArray()[skill];
    }

    /**
     * get current Experience  of specific skill
     *
     * @param skill
     * @return Integer: get current Experience  of specific skill
     */
    public static int getExperience(int skill) {
        if (getSkillLevelArray() == null)
            return 99;

        return getSkillExpArray()[skill];
    }

    /**
     * Get Exp when reach specific level
     *
     * @param level
     * @return Integer: Get Exp when reach specific level
     */
    public static int getExpAtLevel(int level) {
        if (level > 120)
            return -1;

        return XP_TABLE[level];
    }

    /**
     * Get level at specific amount of Exp
     *
     * @param xp
     * @return Integer: exp to next level
     */
    public static int getLevelAtExp(int xp) {
        for (int i = 0; i < XP_TABLE.length; i++) {
            if (xp - XP_TABLE[i] > 0) {
                return i;
            }
        }

        return XP_TABLE[XP_TABLE.length - 1];
    }

    /**
     * Get Exp left to next level
     *
     * @param skill
     * @return Integer: exp to next level
     */
    public static int ExpTillNextLevel(int skill) {
        return getExpAtLevel(getRealLevel(skill) + 1) - getExperience(skill);
    }

    public static int ATTACK = 0;
    public static int DEFENSE = 1;
    public static int STRENGTH = 2;
    public static int CONSTITUTION = 3;
    public static int RANGE = 4;
    public static int PRAYER = 5;
    public static int MAGIC = 6;
    public static int COOKING = 7;
    public static int WOODCUTTING = 8;
    public static int FLETCHING = 9;
    public static int FISHING = 10;
    public static int FIREMAKING = 11;
    public static int CRAFTING = 12;
    public static int SMITHING = 13;
    public static int MINING = 14;
    public static int HERBLORE = 15;
    public static int AGILITY = 16;
    public static int THIEVING = 17;
    public static int SLAYER = 18;
    public static int FARMING = 19;
    public static int RUNECRAFTING = 20;
    public static int HUNTER = 21;
    public static int CONSTRUCTION = 22;
}
