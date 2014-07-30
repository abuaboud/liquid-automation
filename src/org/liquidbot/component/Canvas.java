package org.liquidbot.component;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.Constants;
import org.liquidbot.bot.client.praser.FieldHook;
import org.liquidbot.bot.client.praser.HookReader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

/**
 * Created by Hiasat on 7/29/2014.
 */
public class Canvas extends java.awt.Canvas {

    private final java.awt.Canvas canvas;
    private final BufferedImage botBuffer = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);
    private final BufferedImage gameBuffer = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);

    /**
     * Create new instance of Canvas Class
     *
     * @param canvas
     */
    public Canvas(java.awt.Canvas canvas) {
        this.canvas = canvas;
    }

    /**
     * Doubling Canvas Buffering & drawing PaintListeners
     *
     * @return final Graphics
     */
    @Override
    public Graphics getGraphics() {
        Graphics graphics = botBuffer.getGraphics();
        if (Configuration.drawCanvas) {
            graphics.drawImage(gameBuffer, 0, 0, null);
            if (Configuration.drawMouse) {
                Point location = Configuration.mouse.getLocation();
                graphics.setColor(Color.WHITE);
                graphics.drawLine(location.x, 0, location.x, Constants.APPLET_HEIGHT);
                graphics.drawLine(0, location.y, Constants.APPLET_WIDTH, location.y);
            }
        }
        graphics.dispose();

        Graphics2D rend = (Graphics2D) canvas.getGraphics();
        rend.drawImage(botBuffer, 0, 0, null);

        return gameBuffer.getGraphics();
    }

    /**
     * @return Original Canvas
     */
    public java.awt.Canvas getOriginalCanvas() {
        return canvas;
    }

    /**
     * @return Original Canvas hashCode
     */
    @Override
    public int hashCode() {
        return canvas.hashCode();
    }

    /**
     * set Original field to return to this Canvas
     */
    public void set() {
        try {
            FieldHook fieldHook = HookReader.fields.get("Client#getCanvas()");
            if (fieldHook == null) {
                System.out.println("Error in Canvas Can't find Hook info");
                return;
            }
            Class<?> clazz = Configuration.botFrame.loader().loadClass(fieldHook.getClassName());
            Field field = clazz.getDeclaredField(fieldHook.getFieldName());
            field.setAccessible(true);
            field.set(null, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
