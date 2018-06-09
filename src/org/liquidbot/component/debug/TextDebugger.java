package org.liquidbot.component.debug;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.methods.data.Menu;
import org.liquidbot.bot.script.api.methods.data.movement.Camera;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.methods.interactive.Players;

import java.awt.*;
import java.util.ArrayList;


/**
 * Created on 7/30/2014.
 */
public class TextDebugger extends Debugger<String> {

    private final ArrayList<String> debuggedList = new ArrayList<>();
    private final Configuration config = Configuration.getInstance();

    @Override
    public String[] elements() {
        debuggedList.clear();
	    drawText(config.drawGameState(), "Game State -^^> " + Game.getGameState());
	    drawText(config.drawPlayerLocation(), "Player Location -^^> " + Players.getLocal().getLocation().toString());
	    drawText(config.drawMouseLocation(), "Mouse Location -^^> " + Mouse.getLocation().toString());
	    drawText(config.drawFloor(), "Floor -^^> " + Game.getPlane());
	    drawText(config.drawMapBase(), "Map Base -^^> [" + Game.getBaseX() + " , " + Game.getBaseY() + "]");
	    drawText(config.drawCamera(), "Camera -^^> [" + Camera.getX() + " , " + Camera.getY() + " , " + Camera.getZ() + "] Pitch: " + Camera.getPitch() + " Yaw: " + Camera.getYaw() + " Map Angle: " + Camera.getMapAngle() + "]");
	    drawText(config.drawCamera(), "Camera Angle -^^> " + Camera.getAngle());
	    drawText(config.drawMenu(), "Menu Rectangle -^^> " + Menu.getArea().toString());
	    drawText(config.drawMenu(), "Menu Open -^^> " + Menu.isOpen());
	    drawText(config.drawMenu(), "Menu");

	    java.util.List<String> actions = Menu.getActions();
	    java.util.List<String> options = Menu.getOptions();
	    for (int i = 0; i < actions.size(); i++) {
		    if (options.size() > i) {
			    drawText(config.drawMenu(), "-^^> " + actions.get(i) + " " + options.get(i));
		    }
	    }

        drawText(config.isDisplayFPS(), "FPS -^^> " + config.getFPS());
        return debuggedList.toArray(new String[debuggedList.size()]);
    }

    @Override
    public boolean activate() {
        return true;
    }

    @Override
    public void render(Graphics2D graphics) {
        int yOff = 30;
        for(String str : refresh()) {
            graphics.drawString(str, 15, yOff);
            yOff += 15;
        }
    }

    private void drawText(boolean active ,String debug){
        if(active)
            debuggedList.add(debug);
    }

}
