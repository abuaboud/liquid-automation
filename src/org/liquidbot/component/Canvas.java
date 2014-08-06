package org.liquidbot.component;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.Constants;
import org.liquidbot.bot.client.parser.FieldHook;
import org.liquidbot.bot.client.parser.HookReader;
import org.liquidbot.bot.script.api.interfaces.PaintListener;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.component.debug.*;

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

    private final Configuration config = Configuration.getInstance();
    private final java.awt.Canvas canvas;
    private final BufferedImage botBuffer = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);
    private final BufferedImage gameBuffer = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);

    private List<PaintListener> listeners = new ArrayList<>();


    private long timeTaken = 0;
    private long beginTime = 0;
    private int chosenFPS = 10;
    private int updateRatio = (1000/chosenFPS);

    /**
     * Create new instance of Canvas Class
     *
     * @param canvas
     */
    public Canvas(java.awt.Canvas canvas) {
        this.canvas = canvas;
        final Debugger[] debuggers = {
                new MouseDebugger(), new NPCDebugger(), new PlayerDebugger(), new GroundItemDebugger(), new GameObjectDebugger(),new TextDebugger()
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

        beginTime = System.currentTimeMillis();

        final Graphics graphics = botBuffer.getGraphics();
        if (config.drawCanvas()) {
            graphics.drawImage(gameBuffer, 0, 0, null);
            for (PaintListener listener : getPaintListeners()) {
                if (listener instanceof Debugger) {
                    final Debugger debug = (Debugger) listener;
                    if (debug.activate()) {
                        debug.render((Graphics2D) graphics);
                    }
                } else {
                    listener.render((Graphics2D) graphics);
                }
            }
        }
        graphics.dispose();

        final Graphics2D rend = (Graphics2D) canvas.getGraphics();
        rend.drawImage(botBuffer, 0, 0, null);

        timeTaken = System.currentTimeMillis() - beginTime;
        if(Configuration.getInstance().lowCPU()){
            Time.sleep((int) (updateRatio - timeTaken));
        }
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
            Class<?> clazz = config.getBotFrame().loader().loadClass(fieldHook.getClassName());
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
    public void setBounds(int x, int y, int width, int height) {
        canvas.setBounds(0, 0, width, height);
    }

    @Override
    public void setSize(int width, int height) {
        canvas.setSize(Constants.APPLET_WIDTH, Constants.APPLET_HEIGHT);
    }

    @Override
    public void setLocation(int x, int y) {
        canvas.setLocation(0, 0);
        revalidate();
    }

}
