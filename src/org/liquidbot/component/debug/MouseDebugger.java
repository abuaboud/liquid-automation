package org.liquidbot.component.debug;

import java.awt.*;

/**
 * Created by Kenneth on 7/30/2014.
 */
public class MouseDebugger extends Debugger {

    private final MouseTrail trail = new MouseTrail();
    private final Color trailc = new Color(Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue(), 100);

    @Override
    public Object[] elements() {
        return new Object[0]; // ignored
    }

    @Override
    public boolean activate() {
        return config.drawMouse();
    }

    @Override
    public void render(Graphics graphics) {
        final Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(trailc);
        trail.add(config.getMouse().getLocation());
        trail.draw(graphics2D);

        graphics2D.setColor(Color.WHITE);
        final Point pt = config.getMouse().getLocation();
        graphics.drawLine(pt.x, pt.y - 10, pt.x, pt.y + 10);
        graphics.drawLine(pt.x - 10, pt.y, pt.x + 10, pt.y);
    }

    private final static class MouseTrail {
        private final int SIZE = 50;
        private final Point[] points;
        private int index;

        public MouseTrail() {
            points = new Point[SIZE];
            index = 0;
        }

        public void add(final Point p) {
            points[index++] = p;
            index %= SIZE;
        }

        public void draw(final Graphics g) {
            for (int i = index; i != (index == 0 ? SIZE - 1 : index - 1); i = (i + 1) % SIZE) {
                if (points[i] != null && points[(i + 1) % SIZE] != null) {

                    g.drawLine(points[i].x, points[i].y, points[(i + 1) % SIZE].x, points[(i + 1) % SIZE].y);
                }
            }
        }
    }
}
