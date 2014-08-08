package org.liquidbot.bot.script.api.wrappers;

import org.liquidbot.bot.script.api.methods.data.Calculations;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.util.Time;

/*
 * Created by Hiasat on 8/3/14
 */
public abstract class Path {

    private boolean end;

    public abstract Tile getStart();

    public abstract Tile getEnd();

    public abstract Tile[] getTiles();


    /**
     * Traverse Path till reached end tile
     *
     * @return boolean: true if it done correctly else false
     */
    public boolean traverse() {
        Tile[] tiles = getTiles();
        if (tiles[tiles.length - 1].distanceTo() < 5)
            return true;
        final Tile next = getNext();
        final Tile endTile = tiles[tiles.length - 1];

        if (next.equals(endTile)) {
            if (Calculations.distanceTo(next) <= 1 || (end && Players.getLocal().isMoving())) {
                return false;
            }
            end = true;
        } else {
            end = false;
        }
        Walking.walkTo(next);
        for (int i = 0; i < 10 && Players.getLocal().isMoving(); i++, Time.sleep(300, 350)) ;
        return true;
    }

    private Tile getNext() {
        Tile[] tiles = getTiles();
        for (int i = tiles.length - 1; i >= 0; --i) {
            if (Calculations.isOnMap(tiles[i])) {
                return tiles[i];
            }
        }
        return Walking.getClosestTileOnMap(tiles[tiles.length - 1]);
    }

    public Tile[] getReversedTiles() {
        Tile[] tiles = getTiles();
        Tile[] t = new Tile[tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            t[tiles.length - 1 - i] = tiles[i];
        }
        return t;
    }

}
