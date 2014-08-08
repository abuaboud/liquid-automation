package org.liquidbot.bot.script.api.wrappers;

import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.interactive.Players;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/*
 * Created by Hiasat on 8/7/14
 */
public class LocalPath extends Path {

    private int[][] flags;
    private int offX, offY;

    private final Tile start;
    private final Tile end;
    private final Tile[] tiles;


    public LocalPath(Tile start, Tile end) {
        this.start = start;
        this.end = end;
        this.tiles = findPath(start, end);
    }

    public LocalPath(Tile end) {
        this(Players.getLocal().getLocation(), end);
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

    private Tile[] findPath(Tile start, Tile end) {
        if (start.getZ() != end.getZ())
            return null;
        final int currPlane = start.getZ();
        final int baseX = Game.getBaseX(), baseY = Game.getBaseY();
        final int currX = start.getX() - baseX, currY = start.getY() - baseY;
        int destX = end.getX() - baseX, destY = end.getY() - baseY;

        final int plane = Game.getPlane();
        if (currPlane != plane)
            return null;
        flags = Walking.getCollisionFlags(plane);
        final Tile offset = Walking.getCollisionOffset(plane);
        offX = offset.getX();
        offY = offset.getY();


        if (flags == null || currX < 0 || currY < 0 || currX >= flags.length || currY >= flags.length) {
            return null;
        } else if (destX < 0 || destY < 0 || destX >= flags.length || destY >= flags.length) {
            if (destX < 0) {
                destX = 0;
            } else if (destX >= flags.length) {
                destX = flags.length - 1;
            }
            if (destY < 0) {
                destY = 0;
            } else if (destY >= flags.length) {
                destY = flags.length - 1;
            }
        }

        final HashSet<Node> open = new HashSet<Node>();
        final HashSet<Node> closed = new HashSet<Node>();
        Node curr = new Node(currX, currY, currPlane);
        final Node dest = new Node(destX, destY, currPlane);

        curr.f = heuristic(curr, dest);
        open.add(curr);

        while (!open.isEmpty()) {
            curr = lowest_f(open);
            if (curr.equals(dest)) {
                return path(curr, baseX, baseY);
            }
            open.remove(curr);
            closed.add(curr);
            for (final Node next : successors(curr)) {
                if (!closed.contains(next)) {
                    final double t = curr.g + dist(curr, next);
                    boolean use_t = false;
                    if (!open.contains(next)) {
                        open.add(next);
                        use_t = true;
                    } else if (t < next.g) {
                        use_t = true;
                    }
                    if (use_t) {
                        next.prev = curr;
                        next.g = t;
                        next.f = t + heuristic(next, dest);
                    }
                }
            }
        }
        return null;
    }

    private static double heuristic(final Node start, final Node end) {
        final double dx = Math.abs(start.x - end.x);
        final double dy = Math.abs(start.y - end.y);
        final double diag = Math.min(dx, dy);
        final double straight = dx + dy;
        return Math.sqrt(2.0) * diag + straight - 2 * diag;
    }

    private static double dist(final Node start, final Node end) {
        if (start.x != end.x && start.y != end.y) {
            return 1.41421356;
        } else {
            return 1.0;
        }
    }

    private static Node lowest_f(final Set<Node> open) {
        Node best = null;
        for (final Node t : open) {
            if (best == null || t.f < best.f) {
                best = t;
            }
        }
        return best;
    }

    private java.util.List<Node> successors(final Node t) {
        final LinkedList<Node> tiles = new LinkedList<Node>();
        final int x = t.x, y = t.y, z = t.z;
        final int f_x = x - offX, f_y = y - offY;
        final int here = flags[f_x][f_y];
        final int upper = flags.length - 1;
        if (f_y > 0 && (here & Walking.Flag.WALL_SOUTH) == 0 && (flags[f_x][f_y - 1] & Walking.Flag.BLOCKED) == 0) {
            tiles.add(new Node(x, y - 1, z));
        }
        if (f_x > 0 && (here & Walking.Flag.WALL_WEST) == 0 && (flags[f_x - 1][f_y] & Walking.Flag.BLOCKED) == 0) {
            tiles.add(new Node(x - 1, y, z));
        }
        if (f_y < upper && (here & Walking.Flag.WALL_NORTH) == 0 && (flags[f_x][f_y + 1] & Walking.Flag.BLOCKED) == 0) {
            tiles.add(new Node(x, y + 1, z));
        }
        if (f_x < upper && (here & Walking.Flag.WALL_EAST) == 0 && (flags[f_x + 1][f_y] & Walking.Flag.BLOCKED) == 0) {
            tiles.add(new Node(x + 1, y, z));
        }
        if (f_x > 0 && f_y > 0 && (here & (Walking.Flag.WALL_SOUTHWEST | Walking.Flag.WALL_SOUTH | Walking.Flag.WALL_WEST)) == 0
                && (flags[f_x - 1][f_y - 1] & Walking.Flag.BLOCKED) == 0
                && (flags[f_x][f_y - 1] & (Walking.Flag.BLOCKED | Walking.Flag.WALL_WEST)) == 0
                && (flags[f_x - 1][f_y] & (Walking.Flag.BLOCKED | Walking.Flag.WALL_SOUTH)) == 0) {
            tiles.add(new Node(x - 1, y - 1, z));
        }
        if (f_x > 0 && f_y < upper && (here & (Walking.Flag.WALL_NORTHWEST | Walking.Flag.WALL_NORTH | Walking.Flag.WALL_WEST)) == 0
                && (flags[f_x - 1][f_y + 1] & Walking.Flag.BLOCKED) == 0
                && (flags[f_x][f_y + 1] & (Walking.Flag.BLOCKED | Walking.Flag.WALL_WEST)) == 0
                && (flags[f_x - 1][f_y] & (Walking.Flag.BLOCKED | Walking.Flag.WALL_NORTH)) == 0) {
            tiles.add(new Node(x - 1, y + 1, z));
        }
        if (f_x < upper && f_y > 0 && (here & (Walking.Flag.WALL_SOUTHEAST | Walking.Flag.WALL_SOUTH | Walking.Flag.WALL_EAST)) == 0
                && (flags[f_x + 1][f_y - 1] & Walking.Flag.BLOCKED) == 0
                && (flags[f_x][f_y - 1] & (Walking.Flag.BLOCKED | Walking.Flag.WALL_EAST)) == 0
                && (flags[f_x + 1][f_y] & (Walking.Flag.BLOCKED | Walking.Flag.WALL_SOUTH)) == 0) {
            tiles.add(new Node(x + 1, y - 1, z));
        }
        if (f_x > 0 && f_y < upper && (here & (Walking.Flag.WALL_NORTHEAST | Walking.Flag.WALL_NORTH | Walking.Flag.WALL_EAST)) == 0
                && (flags[f_x + 1][f_y + 1] & Walking.Flag.BLOCKED) == 0
                && (flags[f_x][f_y + 1] & (Walking.Flag.BLOCKED | Walking.Flag.WALL_EAST)) == 0
                && (flags[f_x + 1][f_y] & (Walking.Flag.BLOCKED | Walking.Flag.WALL_NORTH)) == 0) {
            tiles.add(new Node(x + 1, y + 1, z));
        }
        return tiles;
    }

    private Tile[] path(final Node end, final int base_x, final int base_y) {
        final LinkedList<Tile> path = new LinkedList<Tile>();
        Node p = end;
        while (p != null) {
            path.addFirst(p.get(base_x, base_y));
            p = p.prev;
        }
        return path.toArray(new Tile[path.size()]);
    }

    private final class Node {
        public final int x, y, z;
        public Node prev;
        public double g, f;
        public boolean special;

        public Node(final int x, final int y, final int z) {
            this(x, y, z, false);
        }

        public Node(final int x, final int y, final int z, final boolean special) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.special = special;
            g = f = 0;
        }

        @Override
        public int hashCode() {
            return x << 4 | y;
        }

        @Override
        public boolean equals(final Object o) {
            if (o instanceof Node) {
                final Node n = (Node) o;
                return x == n.x && y == n.y && z == n.z;
            }
            return false;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }

        public Tile get(final int baseX, final int baseY) {
            return new Tile(x + baseX, y + baseY, z);
        }
    }
}
