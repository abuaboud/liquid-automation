package org.liquidbot.bot.script.api.wrappers;

/*
 * Created on 8/7/14
 */
public class TilePath extends Path {

    private final Tile start;
    private final Tile end;
    private final Tile[] tiles;

    public TilePath(Tile... tiles) {
        this.start = tiles[0];
        this.end = tiles[tiles.length - 1];
        this.tiles = tiles;
    }

    @Override
    public Tile getStart() {
        return start;
    }

    @Override
    public Tile getEnd() {
        return end;
    }

    @Override
    public Tile[] getTiles() {
        return tiles;
    }
}
