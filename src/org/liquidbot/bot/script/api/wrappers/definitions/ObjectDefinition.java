package org.liquidbot.bot.script.api.wrappers.definitions;

import org.liquidbot.bot.client.injection.callback.ObjectDefinitionCallBack;
import org.liquidbot.bot.client.parser.HookReader;
import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.methods.data.Settings;

import java.util.Hashtable;

/*
 * Created by Hiasat on 7/31/14
 */
public class ObjectDefinition {

	private static Hashtable<Integer, String> nameCache = new Hashtable<>();
	private static Hashtable<Integer, String[]> actionsCache = new Hashtable<>();

	private String name;
	private String[] actions;

	public ObjectDefinition(int Id) {
		if (nameCache.get(Id) == null) {
			Object transformedComposite = null;
			Object raw = ObjectDefinitionCallBack.get(Id);
			if (raw != null) {
				String name = (String) Reflection.value("GameObjectComposite#getName()", raw);
				if (name == null || name.equalsIgnoreCase("null")) {
					int[] transformIds = (int[]) Reflection.value("GameObjectComposite#getTransformIds()", raw);
					if (transformIds != null && transformIds.length > 0) {
						int varpIndex = (int) Reflection.value("GameObjectComposite#getTransformVarpIndex()", raw);
						if (varpIndex > 0) {
							int realId = transformIds[Settings.get(varpIndex)];
							if (realId > 0) {
								transformedComposite = ObjectDefinitionCallBack.get(realId);
							}
						}
					}
					if (transformedComposite == null) {
						int correctParam =HookReader.methods.get("GameObjectComposite#getChildComposite()").getCorrectParam();
						transformedComposite = Reflection.invoke("GameObjectComposite#getChildComposite()", raw, correctParam);
					}
				}
				nameCache.put(Id, (String) Reflection.value("GameObjectComposite#getName()", transformedComposite == null ? raw : transformedComposite));
				actionsCache.put(Id, (String[]) Reflection.value("GameObjectComposite#getActions()", transformedComposite == null ? raw : transformedComposite));
			}
		}
		if (nameCache.containsKey(Id)) {
			name = nameCache.get(Id);
			actions = actionsCache.get(Id);
		}
	}

	/**
	 * Object Name
	 *
	 * @return String: Object Name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Object Actions
	 *
	 * @return String[] : Object Interact Actions
	 */
	public String[] getActions() {
		return actions;
	}

	/**
	 * check if Object composite is null or not
	 *
	 * @return Boolean : return true if not null else false
	 */
	public boolean isValid() {
		return name != null;
	}
}
