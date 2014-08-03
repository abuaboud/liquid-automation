package org.liquidbot.bot.client.input.algorithms;

import java.awt.*;

/**
 * This class contains methods that are the basis for spline movement.
 * To use this class, consider the following pseudocode.
 * <code>
 * Point[] controls = Spline.generateControls(0,0,500,500,50,50);
 * Point[] spline = Spline.generateSpline(controls);
 * int time = Spline.fittsLaw(Math.sqrt(Math.pow(500 - 0, 2) + Math.pow(500 - 0, 2)), 10);
 * Point[] path = Spline.applyDynamism(spline, time, 10);
 * for (int i = 0; i < path.length; i++) {
 * setMousePos(path[i].x, path[i].y);
 * wait(10);
 * }
 * </code>
 * Simply put, that pseudocode represents moving the mouse from (0,0) to a area
 * with a radius of 10 at (500, 500) with a 10ms "sample rate", or resolution.
 * The radius of the area to be moved to could be the average of the width and
 * height of the square to be moved to, and should be object-specific, i.e. not
 * the same for every movement. The <code>setMousePos(int x, int y)</code> method
 * should be written by the user of this class, along with <code>wait(int ms)</code>
 * The resolution of the motion is user specific, the lower the better within
 * reason. It would be changed in the argument to <code>wait</code>, as well as
 * in the argument to <code>applyDynamism</code>. Common sense should say that
 * the positions are user specific, as well as the setting. Personaly I like the
 * look of the spline when the <code>ctrlSpacing</code> and <code>ctrlVariance</code>
 * arguments, to <code>generateSpline</code>, are both set at 50.
 * <p/>
 * Version 2 Changes:
 * - Ensured accuracy in ending point
 * - Added gaussian acceleration to <code>applyDynamism</code>
 * - Corrected error in <code>generateControls</code> relating to small movements
 * - Fixed error in gaussian values
 * - Corrected ArrayIndexOutOfBounds exception in applyDynamism
 * - Second revision of <code>generateControls</code>, more controls for smaller
 * distances as well as less variance relating to distance of movement
 *
 * @author Benjamin J. Land, aka BenLand100
 * @version 2.7
 *          <p/>
 *          By using this class or code based on this class you agree to give me credit for the
 *          time and work put into it. ;)
 */

public class Spline implements MouseAlgorithm {
    public int ex, ey, ctrlSpacing, ctrlVariance;


    public Spline() {
        this(500, 500, 50, 50);
    }

    /**
     * This constructor allows the script writer to specify thier own controls.
     *
     * @param ex
     * @param ey
     * @param ctrlSpacing
     * @param ctrlVariance
     */
    public Spline(int ex, int ey, int ctrlSpacing, int ctrlVariance) {
        this.ex = ex;
        this.ey = ey;
        this.ctrlSpacing = ctrlSpacing;
        this.ctrlVariance = ctrlVariance;
    }

    /**
     * This constructs a path from x1,y1 to x2,y2 using the bezier spline algorithim.
     *
     * @param x1 First x coord.
     * @param y1 First y coord.
     * @param x2 Second x coord.
     * @param y2 Second y coord.
     * @return The path to follow in the form of a point array.
     */
    public Point[] makeMousePath(int x1, int y1, int x2, int y2) {
        Point[] controls = Spline.generateControls(x1, y1, x2, y2, ctrlSpacing, ctrlVariance);
        Point[] spline = Spline.generateSpline(controls);
        int time = (int) Spline.fittsLaw(Math.sqrt(Math.pow(500, 2) + Math.pow(500, 2)), 10);
        return Spline.applyDynamism(spline, time, 10);
    }

    /**
     * The ammount of time (in ms) it takes an average mouse user to realise the mouse needs to be moved
     */
    public static final int reactionTime = 490;

    /**
     * The ammount of time (in ms) it takes per bit of dificulty (look up Fitts Law) to move the mouse
     */
    public static final int msPerBit = 140;

    /**
     * Creates random control points for a spline
     *
     * @param sx           Begining X position
     * @param sy           Begining Y position
     * @param ex           Begining X position
     * @param ey           Begining Y position
     * @param ctrlSpacing  Distance between control origins
     * @param ctrlVariance Max X or Y variance of each control point from its origin
     * @return An array of Points that represents the control points of the spline
     */
    public static Point[] generateControls(int sx, int sy, int ex, int ey, int ctrlSpacing, int ctrlVariance) {
        double dist = Math.sqrt((sx - ex) * (sx - ex) + (sy - ey) * (sy - ey));
        double angle = Math.atan2(ey - sy, ex - sx);
        int ctrlPoints = (int) Math.floor(dist / ctrlSpacing);
        ctrlPoints = ctrlPoints * ctrlSpacing == dist ? ctrlPoints - 1 : ctrlPoints;
        if (ctrlPoints <= 1) {
            ctrlPoints = 2;
            ctrlSpacing = (int) dist / 3;
            ctrlVariance = (int) dist / 2;
        }
        Point[] result = new Point[ctrlPoints + 2];
        result[0] = new Point(sx, sy);
        for (int i = 1; i < ctrlPoints + 1; i++) {
            double radius = ctrlSpacing * i;
            Point cur = new Point((int) (sx + radius * Math.cos(angle)), (int) (sy + radius * Math.sin(angle)));
            double percent = 1D - ((double) (i - 1) / (double) ctrlPoints);
            percent = percent > 0.5 ? percent - 0.5 : percent;
            percent += 0.25;
            int curVariance = (int) (ctrlVariance * percent);
            cur.x = (int) (cur.x + curVariance * 2 * Math.random() - curVariance);
            cur.y = (int) (cur.y + curVariance * 2 * Math.random() - curVariance);
            result[i] = cur;
        }
        result[ctrlPoints + 1] = new Point(ex, ey);
        return result;
    }

    /**
     * Factorial "n!"
     */
    private static double fact(int n) {
        double result = 1;
        for (int i = 1; i <= n; i++)
            result *= i;
        return result;
    }

    /**
     * Binomial Coefficient "n choose k"
     */
    private static double nCk(int n, int k) {
        return fact(n) / (fact(k) * fact(n - k));
    }

    /**
     * Applys a midpoint algorithm to the Vector of points to ensure pixel to pixel movement
     *
     * @param points The vector of points to be manipulated
     */
    private static void adaptiveMidpoints(java.util.Vector<Point> points) {
        int i = 0;
        while (i < points.size() - 1) {
            Point a = points.get(i++);
            Point b = points.get(i);
            if (Math.abs(a.x - b.x) > 1 || Math.abs(a.y - b.y) > 1) {
                if (Math.abs(a.x - b.x) != 0) {
                    double slope = (double) (a.y - b.y) / (double) (a.x - b.x);
                    double incpt = a.y - slope * a.x;
                    for (int c = a.x < b.x ? a.x + 1 : b.x - 1; a.x < b.x ? c < b.x : c > a.x; c += a.x < b.x ? 1 : -1)
                        points.add(i++, new Point(c, (int) Math.round(incpt + slope * c)));
                } else {
                    for (int c = a.y < b.y ? a.y + 1 : b.y - 1; a.y < b.y ? c < b.y : c > a.y; c += a.y < b.y ? 1 : -1)
                        points.add(i++, new Point(a.x, c));
                }
            }
        }
    }

    /**
     * Generates a spline that moves no more then one pixel at a time
     * TIP: For most movements, this spline is not good, use <code>applyDynamism</code>
     *
     * @param controls An array of control points
     * @return An array of Points that represents the spline
     */
    public static Point[] generateSpline(Point[] controls) {
        double degree = controls.length - 1;
        java.util.Vector<Point> spline = new java.util.Vector<Point>();
        boolean lastFlag = false;
        for (double theta = 0; theta <= 1; theta += 0.01) {
            double x = 0;
            double y = 0;
            for (double index = 0; index <= degree; index++) {
                double probPoly = nCk((int) degree, (int) index) * Math.pow(theta, index) * Math.pow(1D - theta, degree - index);
                x += probPoly * controls[(int) index].x;
                y += probPoly * controls[(int) index].y;
            }
            Point temp = new Point((int) x, (int) y);
            try {
                if (!temp.equals(spline.lastElement()))
                    spline.add(temp);
            } catch (Exception e) {
                spline.add(temp);
            }
            lastFlag = theta != 1.0;
        }
        if (lastFlag) {
            spline.add(new Point(controls[(int) degree].x, controls[(int) degree].y));
        }
        adaptiveMidpoints(spline);
        return spline.toArray(new Point[0]);
    }


    /**
     * Satisfies Integral[gaussian(t),t,0,1] == 1D
     * Therefore can distribute a value as a bell curve over the intervel 0 to 1
     *
     * @param t = A value, 0 to 1, representing a percent along the curve
     * @return The value of the gaussian curve at this position
     */
    private static double gaussian(double t) {
        t = 10D * (t / 1D) - 5D;
        return (1D / (Math.sqrt(5D) * Math.sqrt(2D * Math.PI))) * Math.exp((-t * t) / 20D);
    }

    /**
     * Returns an array of gaussian values that add up to 1 for the number of steps
     * Solves the problem of having using an intergral to distribute values
     *
     * @param steps Number of steps in the distribution
     * @return An array of values that contains the percents of the distribution
     */
    private static double[] gaussTable(int steps) {
        double[] table = new double[steps];
        double step = 1D / (double) steps;
        double sum = 0;
        for (int i = 0; i < steps; i++)
            sum += gaussian(i * step);
        for (int i = 0; i < steps; i++) {
            table[i] = gaussian(i * step) / sum;
        }
        return table;
    }

    /**
     * Omits points along the spline in order to move in steps rather then pixel by pixel
     * Adds gaussian distributed acceleration (starts slow, speeds up, ends slow)
     * TIP: msForMove should be a value from fittsLaw
     *
     * @param spline    The pixel by pixel spline
     * @param msForMove The ammount of time taken to traverse the spline
     * @param msPerMove The ammount of time per each move
     * @return The dynamic spline
     */
    public static Point[] applyDynamism(Point[] spline, int msForMove, int msPerMove) {
        Point[] result = null;
        int numPoints = spline.length;
        double msPerPoint = (double) msForMove / (double) numPoints;
        int undistStep = (int) Math.round((double) msPerMove / msPerPoint);

        if (numPoints > undistStep) {
            try {
                int steps = (int) Math.floor(numPoints / undistStep);
                result = new Point[steps];
                double[] gaussValues = gaussTable(result.length);
                double currentPercent = 0;
                for (int i = 0; i < steps; i++) {
                    currentPercent += gaussValues[i];
                    int nextIndex = (int) Math.floor((double) numPoints * currentPercent);
                    if (nextIndex < numPoints) {
                        result[i] = spline[nextIndex];
                    } else {
                        result[i] = spline[numPoints - 1];
                    }
                }
                if (currentPercent < 1D) result[steps - 1] = spline[numPoints - 1];
            } catch (Exception e) {
                return spline;
            }
        } else {
            return spline;
        }
        return result;
    }

    /**
     * Calculates the ammount of time a movement should take based on Fitts' Law
     * TIP: Do not add/subtract random values from this result, rather varry the targetSize value
     * or do not move the same distance each time ;)
     *
     * @param targetDist The distance from the current position to the center of the target
     * @param targetSize The maximum distence from the center of the target within which the end point could be
     * @return the ammount of time (in ms) the movement should take
     */
    public static long fittsLaw(double targetDist, double targetSize) {
        return (long) (reactionTime + msPerBit * (Math.log10(targetDist / targetSize + 1) / Math.log10(2)));
    }


    @Override
    public String getName() {
        return "Spline";  //To change body of implemented methods use File | Settings | File Templates.
    }

}