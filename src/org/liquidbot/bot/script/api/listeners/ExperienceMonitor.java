package org.liquidbot.bot.script.api.listeners;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.ScriptHandler;
import org.liquidbot.bot.script.api.util.SkillData;
import org.liquidbot.bot.utils.Logger;
import org.liquidbot.bot.utils.Utilities;

import java.util.*;

/**
 * Created on 8/10/2014.
 */
public class ExperienceMonitor implements Runnable {

    private final Configuration config = Configuration.getInstance();
    private final List<ExperienceListener> listeners = new ArrayList<>();
    private final Logger log = new Logger(ExperienceMonitor.class);
    private final SkillData sd = new SkillData();
    private final Map<Integer, Integer> experiences = new HashMap<>();

    public ExperienceMonitor() {
        log.info("Initialized ExperienceMonitor..");
    }

    public void addListener(ExperienceListener... listeners) {
        Collections.addAll(this.listeners, listeners);
    }

    @Override
    public void run() {
        while(config.getScriptHandler().getScriptState() != null && config.getScriptHandler().getScriptState() != ScriptHandler.State.STOPPED) {
            for(int i = 0; i < 24; i++) {
                final int currXp = sd.experience(i);
                final int lastXp = experiences.containsKey(i) ? experiences.get(i) : 0;
                if(currXp > lastXp) {
                    for(ExperienceListener listener : listeners) {
                        listener.onExperience(i, currXp - lastXp);
                    }
                    experiences.put(i, currXp);
                }
            }
            Utilities.sleep(600);
        }
        log.info("ExperienceMonitor stopped!");
    }
}
