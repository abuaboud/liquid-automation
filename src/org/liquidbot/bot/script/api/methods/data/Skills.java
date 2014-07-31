package org.liquidbot.bot.script.api.methods.data;


import org.liquidbot.bot.client.reflection.Reflection;

public class Skills {

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
		return (int[]) Reflection.value("Client#getSkillExpArray()",null);
	}

    /**
     * Skill level as array
     *
     * @return int[] : return array of real skills level array
     * which is modified by Potion,Spell or anything else
     */
	private static int[] getSkillLevelArray() {
		return (int[]) Reflection.value("Client#getSkillLevelArray()",null);
	}

    /**
     * Real skill level as array
     *
     * @return int[] : return array of real skills level array
     * which isn't modified by Potion,Spell or anything else
     */
	private static int[] getRealSkillLevelArray() {
		return (int[]) Reflection.value("Client#getRealSkillLevelArray()",null);
	}

	/**
	 * Returns the real level of the specified skill that
	 * has not been modified by any spells, potions etc..
	 *
	 * @param skill
	 * @return Integer
	 */
	public static int getRealLevel(Skill skill) {
		if (getRealSkillLevelArray() == null || skill == null)
			return 99;

		return getRealSkillLevelArray()[skill.getIndex()];
	}
    /**
     * Returns the real level of the specified skill that
     * has or has not been modified by any spells, potions etc..
     *
     * @param skill
     * @return Integer
     */
	public static int getCurrentLevel(Skill skill) {
		if (getSkillLevelArray() == null || skill == null)
			return 99;

		return getSkillLevelArray()[skill.getIndex()];
	}
    /**
     * get current Experience  of specific skill
     *
     * @param skill
     * @return Integer: get current Experience  of specific skill
     */
	public static int getExperience(Skill skill) {
		if (getSkillLevelArray() == null || skill == null)
			return 99;

		return getSkillExpArray()[skill.getIndex()];
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
	public static int ExpTillNextLevel(Skill skill) {
		return getExpAtLevel(getRealLevel(skill) + 1) - getExperience(skill);
	}

	public enum Skill {
		ATTACK(0, "Attack"),
		DEFENSE(1, "Defence"),
		STRENGTH(2, "Strength"),
		CONSTITUTION(3, "Constitution"),
		RANGE(4, "Range"),
		PRAYER(5, "Prayer"),
		MAGIC(6, "Magic"),
		COOKING(7, "Cooking"),
		WOODCUTTING(8, "WoodCutting"),
		FLETCHING(9, "Fletching"),
		FISHING(10, "Fishing"),
		FIREMAKING(11, "FireMaking"),
		CRAFTING(12, "Crafting"),
		SMITHING(13, "Smithing"),
		MINING(14, "Mining"),
		HERBLORE(15, "Herblore"),
		AGILITY(16, "Agility"),
		THIEVING(17, "Thieving"),
		SLAYER(18, "Slayer"),
		FARMING(19, "Farming"),
		RUNECRAFTING(20, "Runecrafting"),
		HUNTER(21, "Hunter"),
		CONSTRUCTION(22, "Construction");

		private int index;
		private String name;

		private Skill(final int index, final String name) {
			this.index = index;
			this.name = name;
		}

		public int getIndex() {
			return this.index;
		}

		public String getName() {
			return this.name;
		}
	}
}
