package org.liquidbot.bot.script.randevent.impl;


import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Bank;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;
import org.liquidbot.bot.script.randevent.RandomEvent;
import org.liquidbot.bot.utils.Utilities;

/*
 * Created on 8/5/14
 */
public class Talker extends RandomEvent {

	private final String DRUNKEN_DWARF = "Drunken Dwarf";
	private final String GENIE = "Genie";
	private final String SECURITY_GUARD = "Security Guard";
	private final String DR_JEKYLL = "Dr Jekyll";
	private final String RICK_TURPENTINE = "Rick Turpentine";
	private final String CAPN_HAND = "Cap'n Hand";
	private final String MYSTERIOUS_OLD_MAN = "Mysterious Old Man";
	private final String MR_HYDE = "Mr Hyde";
	private final String LOST_PIRATE = "Lost Pirate";


	private final String[] NPC_NAMES = {CAPN_HAND, DRUNKEN_DWARF, GENIE,
			SECURITY_GUARD, DR_JEKYLL, RICK_TURPENTINE, MYSTERIOUS_OLD_MAN,
			MR_HYDE, LOST_PIRATE};

	private NPC talkingNPC;
	private String npcName = null;
	private boolean startedTalking;

	@Override
	public String getAuthor() {
		return "Hiasat";
	}

	@Override
	public String getName() {
		return "Talker";
	}


	@Override
	public boolean active() {
		if (Game.isLoggedIn() && !Players.getLocal().isInCombat() && !Bank.isOpen()) {
			WidgetChild chatWidget =  Widgets.get(243,1);
			if (chatWidget.isVisible()) {
				String text = chatWidget.getText();
				if (text != null && Utilities.inArray(text, NPC_NAMES))
					return true;
			}
			talkingNPC = getTalkingNPC();
			if (talkingNPC.isValid()) {
				if (!startedTalking) {
					startedTalking = true;
					npcName = talkingNPC.getName();
					log.info("[Random] TalkerHandler: " + npcName
							+ " detected");
				}
				return true;
			}
		}
		if (startedTalking) {
			log.info("[Random] TalkerHandler: " + npcName + " completed");
			startedTalking = false;
			npcName = null;
		}
		return false;
	}

	@Override
	public void solve() {
		if (Camera.getPitch() < 70) {
			Camera.setPitch(Random.nextInt(70, 90));
		}
		if (talkingNPC !=null && talkingNPC.isValid() && !Widgets.canContinue()) {
			if(!talkingNPC.isOnScreen()){
				talkingNPC.turnTo();
			}
			setStatus("Talking to NPC");
			talkingNPC.interact("Talk-to", talkingNPC.getName());
			for (int i = 0; i < 20 && talkingNPC.isValid() && !Widgets.canContinue(); i++, Time.sleep(100, 150)) ;
		}
		if (Widgets.canContinue()) {
			while (Game.isLoggedIn() && Widgets.canContinue()) {
				setStatus("Continuing Widgets");
				Widgets.clickContinue();
				for (int i = 0; i < 40 && talkingNPC.isValid(); i++, Time.sleep(100, 150)) ;
			}
		}
	}

	@Override
	public void reset() {
		talkingNPC = null;
	}

	/**
	 * Get the nearest talker
	 *
	 * @return NPC
	 */
	private NPC getTalkingNPC() {
		return NPCs.getNearest(new Filter<NPC>() {
			@Override
			public boolean accept(NPC n) {
				if (n.isValid() && n.distanceTo() < 14) {
					if (n.getName() != null && Utilities.inArray(n.getName(), NPC_NAMES)) {
						if ((n.getSpokenMessage() != null
								&& n.getSpokenMessage().toLowerCase().contains(Players.getLocal().getName().toLowerCase()))
								|| (n.getInteracting().isValid() && n.getInteracting().equals(Players.getLocal())))
							return true;
					}

				}
				return false;
			}
		});
	}


}