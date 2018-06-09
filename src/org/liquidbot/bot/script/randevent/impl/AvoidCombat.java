package org.liquidbot.bot.script.randevent.impl;

import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.api.wrappers.Path;
import org.liquidbot.bot.script.api.wrappers.Tile;
import org.liquidbot.bot.script.api.wrappers.TilePath;
import org.liquidbot.bot.script.randevent.RandomEvent;
import org.liquidbot.bot.utils.Utilities;

/*
 * Created on 8/7/14
 */
public class AvoidCombat extends RandomEvent {

	private final String ROCK_GOLEM = "Rock golem";
	private final String SWARM = "Swarm";
	private final String EVIL_CHICKEN = "Evil chicken";
	private final String RIVER_TROLL = "River troll";
	private final String DRUNKEN_DWARF = "Drunken Dwarf";
	private final String TURPENTINE = "Rick Turpentine";
	private final String SECURITY = "Security Guard";
	private final String LOST_PIRATE = "Lost Pirate";
	private final String DR_JEKYLL = "Dr Jekyll";
	private final String MR_HYDE = "Mr Hyde";
	private final String TREE_SPIRIT = "Tree Spirit";
	private final String CAP_HAND = "Cap'n Hand";
	private final String[] COMBAT_NPCS_NAMES = {ROCK_GOLEM, SWARM,
			EVIL_CHICKEN, RIVER_TROLL, DRUNKEN_DWARF, TURPENTINE, SECURITY,
			LOST_PIRATE, DR_JEKYLL, TREE_SPIRIT, CAP_HAND, MR_HYDE};

	private Tile target = null;
	private Tile startTile = null;
	private boolean wasAtTile = false;
	private Path path = null;
	private Path reversedPath = null;

	private final Filter<NPC> combatFilter = new Filter<NPC>() {
		@Override
		public boolean accept(NPC npc) {
			return npc.isValid() && npc.getName() != null && Utilities.inArray(npc.getName(), COMBAT_NPCS_NAMES)
					&& ((npc.getInteracting().isValid() && npc.getInteracting().equals(Players.getLocal()))
					|| (npc.getSpokenMessage() != null && npc.getSpokenMessage().contains(Players.getLocal().getName())));
		}
	};

	private NPC getCombatNPC() {
		return NPCs.getNearest(combatFilter);
	}

	@Override
	public String getName() {
		return "Avoid Combat";
	}

	@Override
	public String getAuthor() {
		return "Hiasat";
	}

	@Override
	public boolean active() {
		return Game.isLoggedIn()
				&& (target != null || startTile != null || (getCombatNPC().isValid() && Players.getLocal().isInCombat()));
	}

	@Override
	public void solve() {
		if (!Walking.isRunning() && Walking.getEnergy() > 20) {
			Walking.setRun(true);
		} else if ((getCombatNPC() != null && Players.getLocal().isInCombat())
				&& target == null && startTile == null) {
			startTile = Players.getLocal().getLocation();
			target = getTarget();
			path = Walking.findLocalPath(target);
			reversedPath = new TilePath(path.getReversedTiles() == null ? new Tile[]{startTile} : path.getReversedTiles());
		} else if (target != null && startTile != null) {
			if (wasAtTile) {
				if (startTile.isOnMap()) {
					Walking.walkTo(startTile);
					target = null;
					startTile = null;
					path = null;
					wasAtTile = false;
				} else {
					if (reversedPath != null) {
						reversedPath.traverse();
					} else {
						Walking.walkTo(startTile);
					}
					for (int i = 0; i < 20 && Players.getLocal().isMoving(); i++, Time.sleep(100, 150)) ;
				}
			} else {
				if (path != null) {
					if (path.getTiles() == null) {
						Walking.walkTo(target);
					} else {
						path.traverse();
					}
				} else {
					Walking.walkTo(target);
				}
				if (target.distanceTo() < 6) {
					wasAtTile = true;
				}
				for (int i = 0; i < 20 && Players.getLocal().isMoving(); i++, Time.sleep(100, 150)) ;
			}
		}
	}

	@Override
	public void reset() {
		startTile = null;
		target = null;
		path = null;
		wasAtTile = false;
	}

	private Tile getTarget() {
		final Tile base = new Tile(Game.getBaseX(), Game.getBaseY());
		Tile tile = Players.getLocal().getLocation();
		for (int x = 0; x < 104; x++) {
			for (int y = 0; y < 104; y++) {
				final Tile t = Tile.derive(base, x, y);
				if (t.isWalkable()
						&& Walking.getClosestTileOnMap(t).isWalkable() && t.isOnMap()) {
					if (t.distanceTo(Players.getLocal().getLocation()) > tile.distanceTo(Players.getLocal().getLocation()) && t.distanceTo(startTile) < 40) {
						tile = t;

					}
				}

			}
		}
		return tile;
	}
}
