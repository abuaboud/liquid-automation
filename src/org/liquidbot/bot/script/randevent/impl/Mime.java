package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.interfaces.Condition;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;
import org.liquidbot.bot.script.randevent.RandomEvent;

/**
 * Created on 8/16/14.
 */
public class Mime extends RandomEvent {

	private final int BOW_ANIMATION = 858;
	private final int CHATBOX_EMOTE = 188;
	private final String MIME = "Mime";

	private Emote currentEmote = null;


	@Override
	public String getName() {
		return "Mime";
	}

	@Override
	public String getAuthor() {
		return "Hiasat";
	}

	@Override
	public boolean active() {
		return Game.isLoggedIn() && NPCs.getNearest(MIME).isValid();
	}

	@Override
	public void solve() {
		NPC mime = NPCs.getNearest(MIME);
		if (!mime.isValid())
			return;
		if (mime.getAnimation() > 0 && mime.getAnimation() != BOW_ANIMATION && currentEmote == null) {
			currentEmote = emote(mime.getAnimation());
			setStatus("Mime emote: " + currentEmote.emote);
		} else if (currentEmote != null && Widgets.get(CHATBOX_EMOTE).isValid()) {
			WidgetChild widgetChild = widget(currentEmote);
			if (widgetChild != null && widgetChild.isVisible()) {
				setStatus("Clicking emote: " + currentEmote.emote);
				widgetChild.click(true);
				Time.sleep(new Condition() {
					@Override
					public boolean active() {
						return Widgets.get(CHATBOX_EMOTE).isValid();
					}
				}, 2000);
			}
			currentEmote = null;
		}
	}

	@Override
	public void reset() {
		currentEmote = null;
	}

	enum Emote {
		THINK(857, "Think"),
		CRY(860, "Cry"),
		LAUGH(861, "Laugh"),
		Dance(866, "Dance"),
		GLASS_WALL(1128, "Glass Wall"),
		LEAN(1129, "Lean"),
		ROPE(1130, "Climb Rope"),
		GLASS_BOX(1131, "Glass Box");

		int animation;
		String emote;

		Emote(int animation, String emote) {
			this.animation = animation;
			this.emote = emote;
		}
	}

	private Emote emote(int animation) {
		for (Emote emote : Emote.values()) {
			if (emote.animation == animation) {
				return emote;
			}
		}

		return null;
	}

	private WidgetChild widget(Emote emote) {
		for (WidgetChild child : Widgets.get(CHATBOX_EMOTE).getChildren()) {
			if (child.getText() != null && child.getText().contains(emote.emote)) {
				return child;
			}
		}
		return null;
	}

}
