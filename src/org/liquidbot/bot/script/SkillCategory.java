package org.liquidbot.bot.script;

import org.liquidbot.bot.utils.Utilities;

import javax.swing.*;

/**
 * Created on 7/31/2014.
 */
public enum SkillCategory {

	AGILITY("Agility", new String[]{"Agility"}, "/resources/skills/Agility.png", new int[]{1}),
	COMBAT("Combat", new String[]{"Combat", "Fighter", "Killer","Kill"}, "/resources/skills/Combat.png", new int[]{2}),
	CONSTRUCTION("Construction", new String[]{"Construction"}, "/resources/skills/Construction.png", new int[]{3}),
	COOKING("Cooking", new String[]{"Cooking"}, "/resources/skills/Cooking.png", new int[]{4}),
	CRAFTING("Crafting", new String[]{"Crafting"}, "/resources/skills/Crafting.png", new int[]{5}),
	FARMING("Farming", new String[]{"Farming"}, "/resources/skills/Farming.png", new int[]{6}),
	FIREMAKING("Firemaking", new String[]{"Firemaking", "FM"}, "/resources/skills/Firemaking.png", new int[]{7}),
	FISHING("Fishing", new String[]{"Fishing","Fisher"}, "/resources/skills/Fishing.png", new int[]{8}),
	FLETCHING("Fletching", new String[]{"Fletching"}, "/resources/skills/Fletching.png", new int[]{9}),
	HERBLORE("Herblore", new String[]{"Herblore"}, "/resources/skills/Herblore.png", new int[]{11}),
	HUNTER("Hunter", new String[]{"Hunter"}, "/resources/skills/Hunter.png", new int[]{12}),
	MAGIC("Magic", new String[]{"Magic"}, "/resources/skills/Magic.png", new int[]{13}),
	MINING("Mining", new String[]{"Mining", "Ore"}, "/resources/skills/Mining.png", new int[]{14}),
	MINI_GAMES("Mini-games", new String[]{"Mini-games"}, "/resources/skills/minigames.png", new int[]{15}),
	PRAYER("Prayer", new String[]{"Prayer"}, "/resources/skills/Prayer.png", new int[]{17}),
	RANGED("Ranged", new String[]{"Ranged", "Range"}, "/resources/skills/Ranged.png", new int[]{18}),
	RUNECRAFTING("Runecrafting", new String[]{"Runecrafting", "Runes"}, "/resources/skills/Runecrafting.png", new int[]{19}),
	SLAYER("Slayer", new String[]{"Slayer"}, "/resources/skills/Slayer.png", new int[]{20}),
	SMITHING("Smithing", new String[]{"Smithing"}, "/resources/skills/Smithing.png", new int[]{21}),
	THEIVING("Thieving", new String[]{"Thieving"}, "/resources/skills/Thieving.png", new int[]{22}),
	WOODCUTTING("Woodcutting", new String[]{"Woodcutting,woodcut,chop,chopper"}, "/resources/skills/woodcutting.png", new int[]{23}),
	MONEY_MAKING("Money Making", new String[]{"Money Making", "Cash"}, "/resources/skills/moneymaking.png", new int[]{16}),
	MISC("Misc", new String[]{"Misc"}, "/resources/skills/Misc.png", new int[]{0, 24});

	private String skill;
	private String[] filters;
	private ImageIcon icon;
	private int[] ids;

	SkillCategory(String skill, String[] filters, String icon, int[] ids) {
		this.skill = skill;
		this.filters = filters;
		this.icon = new ImageIcon(Utilities.getLocalImage(icon));
		this.ids = ids;
	}

	public String getName() {
		return this.skill;
	}

	public String[] getFiltersWord() {
		return this.filters;
	}

	public int[] getIds(){
		return ids;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public static SkillCategory getCategory(int id) {
		for (SkillCategory skillCategory : SkillCategory.values()) {
			for (int a : skillCategory.ids) {
				if (a == id) {
					return skillCategory;
				}
			}
		}
		return SkillCategory.MISC;
	}

	public static SkillCategory detect(final String name, final String desc) {
		for (SkillCategory skillCategory : SkillCategory.values()) {
			for (String filter : skillCategory.filters) {
				if (name.toLowerCase().contains(filter.toLowerCase()) || desc.toLowerCase().contains(filter.toLowerCase())) {
					return skillCategory;
				}
			}
		}
		return SkillCategory.MISC;
	}

	@Override
	public String toString() {
		return name().charAt(0) + name().substring(1).replaceAll("_", " ").toLowerCase();
	}

}
