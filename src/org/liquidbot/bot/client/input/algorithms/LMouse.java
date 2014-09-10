package org.liquidbot.bot.client.input.algorithms;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Lorex
 * Date: 12/20/13
 * Time: 8:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class LMouse implements MouseAlgorithm {



    @Override
    public Point[] makeMousePath(int x1, int y1, int x2, int y2) {
        Point start = new Point(x1,y1);
        Point end = new Point(x2,y2);
        return addM(start, end, createPath(start, end));
    }
    public Point[] createPath(Point start, Point end) {
        final ArrayList<Point> ps = new ArrayList<Point>();
        int max = (int) Math.sqrt(Math.pow(Math.abs(end.x - start.x), 2)
                + Math.pow(Math.abs(end.y - start.y), 2));
        double m = 0;
        if ((end.x - start.x) != 0) {
            m = (end.y - start.y) / (end.x - start.x);
        }
        int f1 = 0;
        if (Math.abs(end.x - start.x) != 0) {
            f1 = (end.x - start.x) / Math.abs(end.x - start.x);
        }
        int f2 = 0;
        if (Math.abs(end.y - start.y) != 0) {
            f2 = (end.y - start.y) / Math.abs(end.y - start.y);
        }
        double r1 = Math.random();
        if (r1 > 0.5) {
            r1 = 1;
        } else {
            r1 = -1;
        }
        int rn = (int) (Math.random() * max);
        int random = (int) (Math.random() * rn / 2 - rn / 4);
        int c = (int) (Math.random() * 50) + 10;
        double r4 = 0 * Math.random() * 2 * Math.PI;
        if (Math.abs(end.x - start.x) >= Math.abs(end.y - start.y)) {

            for (int i = 0; i <= Math.abs(end.x - start.x); i++) {
                if (i == c) {
                    ps.add(new Point(start.x + f1 * i, start.y + f2
                            * sin((int) r1, random, rn, max, i, r4)
                            + (int) (Math.random() * 10 - 5) + (int) (i * m)));
                    c += (int) (30 + Math.random() * 10);
                    if (c > Math.abs(end.x - start.x)) {
                        c = Math.abs(end.x - start.x);
                    }
                }
            }
        } else {
            for (int i = 0; i <= Math.abs(end.y - start.y); i++) {
                if (i == c) {
                    ps.add(new Point(start.x + f1 * sin((int) r1, random, rn, max, i, r4)
                            + (int) (Math.random() * 10 - 5), start.y + f2 * i));
                    c += (int) (30 + Math.random() * 10);
                    if (c > Math.abs(end.y - start.y)) {
                        c = Math.abs(end.y - start.y);
                    }
                }
            }
        }
        ArrayList<Point> points = new ArrayList<Point>();
        Point p = start;
        points.add(start);
        for (Point point : ps) {
            int factor = 0;
            if (Math.abs(point.x - p.x) != 0) {
                factor = (point.x - p.x) / Math.abs(point.x - p.x);

            }
            double m1 = 0;
            if (Math.abs(point.x - p.x) != 0) {
                m1 = (double) (point.y - p.y) / (double) Math.abs(point.x - p.x);
            }
            for (int i = 1; i <= Math.abs(point.x - p.x); i++) {
                points.add(new Point((int) (p.x + factor * i), (int) (p.y + i * m1)));
            }
            p = point;
        }
        return points.toArray(new Point[points.size()]);
    }

    public int sin(int r, int random, int random1, double max, double index, double r4) {
        return r * (int) (random * Math.sin((2 * index / (max + random1) * Math.PI) + r4));
    }

    public Point[] addM(final Point start, final Point end, final Point[] points) {
        if (Math.abs(end.x - start.x) >= Math.abs(end.y - start.y)) {
            int x = (end.x - start.x);
            int y = (end.y - start.y) - (points[points.length - 1].y - points[0].y);
            double m = (double) y / (double) x;
            for (int i = 0; i < points.length; i++) {
                points[i] = new Point(points[i].x, points[i].y
                        + (int) ((double) (points[i].x - start.x) * m));
            }
        } else {
            int x = (end.x - start.x) - (points[points.length - 1].x - points[0].x);
            int y = (end.y - start.y);
            double m = (double) x / (double) y;
            for (int i = 0; i < points.length; i++) {
                points[i] = new Point(points[i].x + (int) ((double) (points[i].y - start.y) * m),
                        points[i].y);
            }
        }
        return points;
    }


    @Override
    public String getName() {
        return "LMouse";
    }

}
