package org.liquidbot.bot;

import org.liquidbot.bot.client.input.InternalKeyboard;
import org.liquidbot.bot.client.input.InternalMouse;
import org.liquidbot.bot.ui.BotFrame;
import org.liquidbot.bot.utils.Logger;
import org.liquidbot.component.Canvas;

/**
 * Created by Hiasat on 7/29/2014.
 */
public class Configuration {

    private static final Logger logger = new Logger(Configuration.class);

    private InternalKeyboard keyboard;
    private InternalMouse mouse;
    private Canvas canvas;
    private BotFrame botFrame;

    private boolean enableMouse = true;
    private boolean enableKeyboard = true;
    private boolean drawPlayers = false;
    private boolean drawNPCs = false;
    private boolean drawGroundItems = false;
    private boolean drawMouse = true;
    private boolean drawCanvas = true;

    private static Configuration instance = new Configuration();

    public static Configuration getInstance() {
        return instance;
    }

    public InternalKeyboard getKeyboard() {
        if (keyboard == null)
            logger.error("Keyboard isn't set!");
        return keyboard;
    }

    public void setKeyboard(InternalKeyboard keyboard) {
        this.keyboard = keyboard;
    }

    public InternalMouse getMouse() {
        if (mouse == null)
            logger.error("Mouse isn't set!");
        return mouse;
    }

    public void setMouse(InternalMouse mouse) {
        this.mouse = mouse;
    }

    public Canvas getCanvas() {
        if (canvas == null)
            logger.error("canvas isn't set!");
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public BotFrame getBotFrame() {
        if (botFrame == null)
            logger.error("botFrame isn't set!");
        return botFrame;
    }

    public void setBotFrame(BotFrame botFrame) {
        this.botFrame = botFrame;
    }

    public boolean enableMouse() {
        return enableMouse;
    }

    public void enableMouse(boolean enableMouse) {
        this.enableMouse = enableMouse;
    }

    public boolean enableKeyboard() {
        return enableKeyboard;
    }

    public void enableKeyboard(boolean enableKeyboard) {
        this.enableKeyboard = enableKeyboard;
    }

    public boolean drawPlayers() {
        return drawPlayers;
    }

    public void drawPlayers(boolean drawPlayers) {
        this.drawPlayers = drawPlayers;
    }

    public boolean drawNPCs() {
        return drawNPCs;
    }

    public void drawNPCs(boolean drawNPCs) {
        this.drawNPCs = drawNPCs;
    }

    public boolean drawGroundItems() {
        return drawGroundItems;
    }

    public void drawGroundItems(boolean drawGroundItems) {
        this.drawGroundItems = drawGroundItems;
    }

    public boolean drawMouse() {
        return drawMouse;
    }

    public void drawMouse(boolean drawMouse) {
        this.drawMouse = drawMouse;
    }

    public boolean drawCanvas() {
        return drawCanvas;
    }

    public void drawCanvas(boolean drawCanvas) {
        this.drawCanvas = drawCanvas;
    }
}
