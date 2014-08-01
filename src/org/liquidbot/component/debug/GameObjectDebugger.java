package org.liquidbot.component.debug;

import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.interactive.GameEntities;
import org.liquidbot.bot.script.api.wrappers.GameObject;

import java.awt.*;

/**
 * Created by Kenneth on 7/30/2014.
 */
public class GameObjectDebugger extends Debugger<GameObject> {

    @Override
    public GameObject[] elements() {
        return GameEntities.getAll(filter);
    }

    @Override
    public boolean activate() {
        return config.drawGameObjects();
    }

    @Override
    public void render(Graphics graphics) {
            final FontMetrics metrics = graphics.getFontMetrics();
            for (GameObject gameObject : refresh()) {
                if (gameObject.isValid()){
                    if (gameObject.getType() != null) {
                        if (gameObject.getType().equals(GameObject.Type.INTERACTIVE)) {
                            graphics.setColor(Color.GREEN);
                        } else if (gameObject.getType().equals(GameObject.Type.FLOOR_DECORATION)) {
                            graphics.setColor(Color.YELLOW);
                        } else if (gameObject.getType().equals(GameObject.Type.BOUNDARY)) {
                            graphics.setColor(Color.WHITE);
                        } else if (gameObject.getType().equals(GameObject.Type.WALL_OBJECT)) {
                            graphics.setColor(Color.RED);
                        }
                        Point p = gameObject.getLocation().getPointOnScreen();
                        String format = gameObject.getId() + ((gameObject.getName() !=null && !gameObject.getName().equalsIgnoreCase("null"))? " (" + gameObject.getName() + ")" : "");
                        graphics.drawString(format,p.x - (metrics.stringWidth(format)/2),p.y);
                        graphics.fillRect(p.x, p.y, 5, 5);
                    }
                }
            }
    }

    private Filter<GameObject> filter = new Filter<GameObject>() {
        @Override
        public boolean accept(GameObject gameObject) {
            return gameObject.isValid() && gameObject.distanceTo() < 7 && gameObject.isOnScreen();
        }
    };
}
