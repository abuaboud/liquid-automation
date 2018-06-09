package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.Widget;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;
import org.liquidbot.bot.script.randevent.RandomEvent;
import org.liquidbot.bot.utils.Utilities;

/**
 * Created on 8/13/14.
 */
public class QuizMaster extends RandomEvent {

	private final String QUIZ_MASTER = "Quiz Master";
	private final int WIDGET_ANSWER = 191, WIDGET_OPTION = 228;
	private final int[] FISH = {6190, 6189};
	private final int[] JEWELRY = {6198, 6197};
	private final int[] WEAPONS = {6191, 6192};
	private final int[] ARMORS = {6193, 6194};
	private final int[] FARMING = {6195, 6196};
	private final int[][] ANSWERS = {FISH, JEWELRY, ARMORS, WEAPONS, FARMING};

	@Override
	public String getName() {
		return "Quiz Master";
	}

	@Override
	public String getAuthor() {
		return "Hiasat";
	}

	@Override
	public boolean active() {
		return Game.isLoggedIn() && NPCs.getNearest(QUIZ_MASTER).isValid();
	}

	@Override
	public void solve() {
		final WidgetChild widgetChild = Widgets.get(WIDGET_OPTION, 1);
		final Widget answerWidget = Widgets.get(WIDGET_ANSWER);
		if (widgetChild.isVisible() && widgetChild.getText() != null) {
			widgetChild.click(true);
			for (int i = 0; i < 40 && widgetChild.isVisible(); i++, Time.sleep(100, 150)) ;
		} else if (answerWidget.isValid()) {
			for (WidgetChild widgetchild : answerWidget.getChildren()) {
				int[] answer = getModelID();
				if (answer == null) {
					setStatus("Answer == null");
				} else if (widgetchild != null && Utilities.inArray(widgetchild.getModelId(), answer)) {
					setStatus("Clicking answer: " + answer + "," + answer.length);
					widgetchild.click(true);
					for (int i = 0; i < 40 && Widgets.get(WIDGET_ANSWER).isValid(); i++, Time.sleep(100, 150)) ;
					break;
				}
			}
		}
	}

	private int[] getModelID() {
		for (int[] a : ANSWERS) {
			if (count(a) == 1) {
				return a;
			}
		}
		return null;
	}

	private int count(int[] Answers) {
		int count = 0;
		Widget widget = Widgets.get(WIDGET_ANSWER);
		if (widget.isValid()) {
			for (WidgetChild widgetchild : widget.getChildren()) {
				if (widgetchild.isVisible() && Utilities.inArray(widgetchild.getModelId(), Answers)) {
					count++;
				}
			}
		}
		return count;
	}


	@Override
	public void reset() {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
