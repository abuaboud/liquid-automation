package org.liquidbot.bot.client.patterns;

import org.liquidbot.bot.Constants;

import org.liquidbot.bot.utils.FileUtils;
import org.liquidbot.bot.utils.Logger;
import org.liquidbot.bot.utils.NetUtils;
import org.liquidbot.bot.utils.Utilities;

/**
 * Created by Hiasat on 8/22/14.
 */
public class Pattern {

	private String currentPattern;

	private String[] parsedPattern;

	private Logger log = new Logger(getClass());

	public boolean contains(String text) {
		if (currentPattern == null)
			currentPattern = NetUtils.readPage(Constants.SITE_URL + "/client/generatePattern.php")[0];
		if (parsedPattern == null)
			parsedPattern = parse(currentPattern);
		return Utilities.inArray(text, parsedPattern);
	}

	public String find(String search) {
		for (String p : parsedPattern) {
			if (p.toLowerCase().contains(search.toLowerCase())) {
				return p;
			}
		}
		return null;
	}

	private String[] parse(String currentPattern) {
		log.info("Your Pattern: " + currentPattern);
		return currentPattern.split(",");
	}


	public void currentPattern(String currentPattern) {
		parsedPattern = parse(currentPattern);
		this.currentPattern = currentPattern;
	}

	public String loadPattern(String accountName) {
		String pattern = FileUtils.load(Constants.PATTERN_FILE_NAME, accountName);
		if (pattern !=null && pattern.contains("/1=")) { // Temp Code to delete old Patterns
			FileUtils.clear(Constants.SETTING_PATH,Constants.PATTERN_FILE_NAME);
			pattern = null;
		}
		if (pattern == null) {
			pattern = NetUtils.readPage(Constants.SITE_URL + "/client/generatePattern.php")[0];
			FileUtils.save(Constants.PATTERN_FILE_NAME, accountName, pattern);
		}
		return pattern;
	}
}
