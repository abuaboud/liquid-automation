package org.liquidbot.bot;

import org.liquidbot.bot.client.input.InternalKeyboard;
import org.liquidbot.bot.client.input.InternalMouse;
import org.liquidbot.bot.client.security.encryption.AES;
import org.liquidbot.bot.script.ScriptHandler;
import org.liquidbot.bot.ui.BotConsole;
import org.liquidbot.bot.ui.BotFrame;
import org.liquidbot.bot.ui.account.Account;
import org.liquidbot.bot.ui.account.AccountManager;
import org.liquidbot.bot.ui.login.misc.User;
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
    private BotConsole console;
    private ScriptHandler scriptHandler;
    private User user;
    private AES encryption;
    private AccountManager accountManager;

    private boolean enableMouse = true;
    private boolean enableKeyboard = true;
    private boolean drawPlayers = false;
    private boolean drawPlayerLocation = false;
    private boolean drawNPCs = false;
    private boolean drawGroundItems = false;
    private boolean drawGameObjects = false;
    private boolean drawSettings = false;
    private boolean drawInventory = false;
    private boolean drawMouse = true;
    private boolean drawCanvas = true;
	private boolean drawGameState = false;
	private boolean drawMouseLocation = false;
	private boolean drawFloor = false;
	private boolean drawMapBase = false;
	private boolean drawCamera = false;
	private boolean drawMenu = false;
    private boolean lowCPU = false;

    private static Configuration instance = new Configuration();

    public void drawInventory(boolean drawInventory) {
        this.drawInventory = drawInventory;
    }

    public boolean drawInventory() {
        return this.drawInventory;
    }

	public boolean drawMenu() {
		return drawMenu;
	}

	public void drawMenu(boolean drawMenu) {
		this.drawMenu = drawMenu;
	}

	public boolean drawGameState() {
		return drawGameState;
	}

	public void drawGameState(boolean drawGameState) {
		this.drawGameState = drawGameState;
	}

	public boolean drawMouseLocation() {
		return drawMouseLocation;
	}

	public void drawMouseLocation(boolean drawMouseLocation) {
		this.drawMouseLocation = drawMouseLocation;
	}

	public boolean drawFloor() {
		return drawFloor;
	}

	public void drawFloor(boolean drawFloor) {
		this.drawFloor = drawFloor;
	}

	public boolean drawMapBase() {
		return drawMapBase;
	}

	public void drawMapBase(boolean drawMapBase) {
		this.drawMapBase = drawMapBase;
	}

	public boolean drawCamera() {
		return drawCamera;
	}

	public void drawCamera(boolean drawCamera) {
		this.drawCamera = drawCamera;
	}

    public void drawSettings(boolean drawSettings) {
        this.drawSettings = drawSettings;
    }

    public boolean drawSettings() {
        return this.drawSettings;
    }

    public static Configuration getInstance() {
        return instance;
    }

    public boolean lowCPU() {
        return lowCPU;
    }

    public void lowCPU(boolean lowCPU) {
        this.lowCPU = lowCPU;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AccountManager getAccountManager() {
        if (accountManager == null)
            accountManager = new AccountManager();
        return accountManager;
    }

    public ScriptHandler getScriptHandler() {
        if (scriptHandler == null)
            scriptHandler = new ScriptHandler();
        return scriptHandler;
    }

    public AES getEncryption() {
        if (encryption == null)
            logger.error("Encryption isn't set!");
        return encryption;
    }

    public void setEncryption(AES encryption) {
        this.encryption = encryption;
    }

    public BotConsole getConsole() {
        if (console == null)
            logger.error("Console isn't set!");
        return console;
    }

    public void setConsole(BotConsole console) {
        this.console = console;
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

    public boolean drawGameObjects() {
        return drawGameObjects;
    }

    public boolean drawPlayerLocation() {
        return drawPlayerLocation;
    }

    public void drawPlayerLocation(boolean drawPlayerLocation) {
        this.drawPlayerLocation = drawPlayerLocation;
    }

    public void drawGameObjects(boolean drawGameObjects) {
        this.drawGameObjects = drawGameObjects;
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
