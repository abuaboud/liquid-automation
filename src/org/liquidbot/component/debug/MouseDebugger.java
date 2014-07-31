package org.liquidbot.component.debug;

import java.awt.*;

/**
 * Created by Kenneth on 7/30/2014.
 */
public class MouseDebugger extends Debugger {

    private final MouseTrail trail = new MouseTrail();

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
        trail.add(config.getMouse().getLocation());
        trail.draw(graphics2D);
    }

    private final static class MouseTrail {
        private final int SIZE = 50;
        private final double ALPHA_STEP = (255.0 / SIZE);
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
            double alpha = 0;
            for (int i = index; i != (index == 0 ? SIZE - 1 : index - 1); i = (i + 1)
                    % SIZE) {
                if (points[i] != null && points[(i + 1) % SIZE] != null) {
                    Color rainbow = Color.getHSBColor((float) (alpha / 255), 1, 1);
                    g.setColor(new Color(rainbow.getRed(), rainbow.getGreen(), rainbow.getBlue(), (int) alpha));

                    g.drawLine(points[i].x, points[i].y, points[(i + 1) % SIZE].x, points[(i + 1) % SIZE].y);

                    alpha += ALPHA_STEP;
                }
            }
        }
    }
}
