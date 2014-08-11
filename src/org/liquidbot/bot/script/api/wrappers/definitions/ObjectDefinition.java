package org.liquidbot.bot.script.api.wrappers.definitions;

import org.liquidbot.bot.client.parser.HookReader;
import org.liquidbot.bot.client.reflection.Reflection;

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
			Object raw = Reflection.invoke("Client#getGameObjectComposite()", null, Id, HookReader.methods.get("Client#getGameObjectComposite()").getCorrectParam());
			String name = (String) Reflection.value("GameObjectComposite#getName()", raw);
			if (name == null || name.equalsIgnoreCase("null")) {
				int[] transformIds = (int[]) Reflection.value("GameObjectComposite#getTransformIds()", raw);
				if (transformIds != null) {
					int[] widgetVarps = (int[]) Reflection.value("Client#getWidgetSettings()", null);
					int transformVarpIndex = (int) Reflection.value("GameObjectComposite#getTransformVarpIndex()", raw);
					if (transformVarpIndex > -1) {
						int realId = transformIds[widgetVarps[transformVarpIndex]];
						if (realId > 0) {
							int correctParam =  HookReader.methods.get("Client#getGameObjectComposite()").getCorrectParam();
							transformedComposite = Reflection.invoke("Client#getGameObjectComposite()", raw, realId, correctParam);
						}
					}
				}
			}
			nameCache.put(Id, (String) Reflection.value("GameObjectComposite#getName()", transformedComposite == null ? raw : transformedComposite));
			actionsCache.put(Id, (String[]) Reflection.value("GameObjectComposite#getActions()", transformedComposite == null ? raw : transformedComposite));
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
