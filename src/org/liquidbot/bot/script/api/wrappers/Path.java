package org.liquidbot.bot.script.api.wrappers;

import org.liquidbot.bot.script.api.methods.data.Calculations;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.util.Time;

/*
 * Created by Hiasat on 8/3/14
 */
public class Path {

    public Tile[] tiles;
    private boolean end;

    public Path(Tile ...tiles){
        this.tiles = tiles;
    }

    public boolean traverse() {
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
        for (int i = tiles.length - 1; i >= 0; --i) {
            if (Calculations.isOnMap(tiles[i])) {
                return tiles[i];
            }
        }
        return Walking.getClosestTileOnMap(tiles[tiles.length - 1]);
    }

}
