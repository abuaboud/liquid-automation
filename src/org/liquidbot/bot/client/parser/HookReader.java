package org.liquidbot.bot.client.parser;

import org.liquidbot.bot.Constants;
import org.liquidbot.bot.utils.NetUtils;
import java.util.Hashtable;

/**
 * Created by Hiasat on 7/29/2014.
 */
public class HookReader {

	public static Hashtable<String, FieldHook> fields = new Hashtable<>();
	public static Hashtable<String, MethodHook> methods = new Hashtable<>();

	private static final String HOOKS_URL = Constants.SITE_URL + "/client/hooks.html";

	public static double VERSION = 0.0;

	/**
	 * Parse Html File to get hooks info
	 */
	public static void init() {
		final String[] sourceCode = NetUtils.readPage(HOOKS_URL);
		for (String source : sourceCode) {
			if(source.contains("VERSION"))
				VERSION = Double.parseDouble(source.replace("VERSION ",""));
			if (!source.contains("#"))
				continue;
			String type = source.split(" ")[2];
			if (type.contains("(")) {
				final MethodHook methodHook = new MethodHook(source);
				methods.put(methodHook.getMethodKey(), methodHook);
			} else {
				final FieldHook fieldHook = new FieldHook(source);
				fields.put(fieldHook.getFieldKey(), fieldHook);
			}
		}

}


}