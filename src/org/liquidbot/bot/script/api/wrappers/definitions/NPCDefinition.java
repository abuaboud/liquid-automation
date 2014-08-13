package org.liquidbot.bot.script.api.wrappers.definitions;

import org.liquidbot.bot.client.parser.HookReader;
import org.liquidbot.bot.client.reflection.Reflection;

/*
 * Created by Hiasat on 7/31/14
 */
public class NPCDefinition {

	private Object raw;
	private Object transformedComposite;

	public NPCDefinition(Object raw) {
		this.raw = raw;
		if (raw != null) {
			String name = (String) Reflection.value("NPCComposite#getName()", raw);
			if (name == null || name.equalsIgnoreCase("null")) {
				int correctParam = HookReader.methods.get("NPCComposite#getChildComposite()").getCorrectParam();
				transformedComposite = Reflection.invoke("NPCComposite#getChildComposite()", raw, correctParam);
			}
		}
	}

	public int getId() {
		if (!isValid())
			return -1;
		return (int) Reflection.value("NPCComposite#getId()", raw);
	}

	public String getName() {
		if (raw == null)
			return null;
		return (String) Reflection.value("NPCComposite#getName()", transformedComposite != null ? transformedComposite : raw);
	}

	public String[] getActions() {
		if (raw == null)
			return null;
		return (String[]) Reflection.value("NPCComposite#getActions()", transformedComposite != null ? transformedComposite : raw);
	}

	public int getCombatLevel() {
		if (raw == null)
			return -1;
		return (int) Reflection.value("NPCComposite#getCombatLevel()", transformedComposite != null ? transformedComposite : raw);
	}

	public int[] getModelIds() {
		if (raw == null)
			return null;
		return (int[]) Reflection.value("NPCComposite#getModelIds()", transformedComposite != null ? transformedComposite : raw);
	}

	public boolean isValid() {
		return raw != null;
	}
}
