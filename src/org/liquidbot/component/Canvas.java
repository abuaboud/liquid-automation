package org.liquidbot.component;

import org.liquidbot.bot.Configuration;
<<<<<<< HEAD
import org.liquidbot.bot.Constants;
import org.liquidbot.component.debug.*;
=======
import org.liquidbot.bot.client.debug.Debugger;
import org.liquidbot.bot.client.debug.MouseDebugger;
import org.liquidbot.bot.client.debug.NPCDebugger;
import org.liquidbot.bot.client.debug.PlayerDebugger;
>>>>>>> origin/master
import org.liquidbot.bot.client.parser.FieldHook;
import org.liquidbot.bot.client.parser.HookReader;
import org.liquidbot.bot.script.api.interfaces.PaintListener;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hiasat on 7/29/2014.
 */
public class Canvas extends java.awt.Canvas {

    private final java.awt.Canvas canvas;
    private final BufferedImage botBuffer = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);
    private final BufferedImage gameBuffer = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);

    private List<PaintListener> listeners = new ArrayList<>();

    /**
     * Create new instance of Canvas Class
     *
     * @param canvas
     */
    public Canvas(java.awt.Canvas canvas) {
        this.canvas = canvas;

        final Debugger[] debuggers = {
                new MouseDebugger(), new NPCDebugger(), new PlayerDebugger()
        };
        Collections.addAll(listeners, debuggers);
    }

    /**
     * Doubling Canvas Buffering & drawing PaintListeners
     *
     * @return final Graphics
     */
    @Override
    public Graphics getGraphics() {
        final Graphics graphics = botBuffer.getGraphics();

        if (Configuration.drawCanvas) {
            graphics.drawImage(gameBuffer, 0, 0, null);
            for (PaintListener listener : getPaintListeners()) {
                if (listener instanceof Debugger) {
                    final Debugger debug = (Debugger) listener;
                    if (debug.activate()) {
                        debug.render(graphics);
                    } else {
                        debug.dispose();
                    }
                } else {
                    listener.render(graphics);
                }
            }
        }
        graphics.dispose();

        final Graphics2D rend = (Graphics2D) canvas.getGraphics();
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

    public synchronized List<PaintListener> getPaintListeners() {
        return listeners;
    }

    @Override
<<<<<<< HEAD
    public void setBounds(int x, int y, int width, int height) {
        canvas.setBounds(0, 0, width, height);
    }

    @Override
    public void setSize(int width, int height) {
        canvas.setSize(Constants.APPLET_WIDTH, Constants.APPLET_WIDTH);
    }

    @Override
    public void setLocation(int x, int y) {
        canvas.setLocation(0, 0);
    }
=======
    public void setLocation(int x, int y) {
        canvas.setLocation(0, 0);
    }

>>>>>>> origin/master
}
