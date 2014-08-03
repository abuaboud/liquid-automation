package org.liquidbot.bot.client.input;/*
 * Created by Hiasat on 7/30/14
 */

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.utils.Utilities;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.liquidbot.bot.client.input.algorithms.*;


public class InternalMouse implements MouseListener, MouseMotionListener {

    private final MouseListener mouseListenerDispatcher;
    private final MouseMotionListener mouseMotionDispatcher;
    private Component component;
    private final Configuration config = Configuration.getInstance();

    private MouseAlgorithm mouseAlgorithm = new Spline();
    private int clientX;
    private int clientY;
    private int clientPressX = -1;
    private int clientPressY = -1;
    private long clientPressTime = -1;
    private boolean clientPressed;
    private boolean humanInput = true;

    public InternalMouse(Applet applet) {
        this.component = applet.getComponentAt(1, 1);
        this.mouseListenerDispatcher = component.getMouseListeners()[0];
        this.mouseMotionDispatcher = component.getMouseMotionListeners()[0];

        component.addMouseListener(this);
        component.addMouseMotionListener(this);

    }


    @Override
    public void mouseEntered(MouseEvent e) {
        //Don't register this event.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //Don't register this event.
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clientX = e.getX();
        clientY = e.getY();
        mouseListenerDispatcher.mouseClicked(new MouseEvent(component, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, clientX, clientY, 1, false, e.getButton()));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        press(e.getX(), e.getY(), e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        clientPressX = e.getX();
        clientPressY = e.getY();
        clientPressTime = System.currentTimeMillis();
        clientPressed = false;
        release(e.getX(), e.getY(), e.getButton());
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
        Point p = config.getCanvas().getMousePosition();
        if (p != null && p.x > 0 && p.y > 0) {
            if (humanInput) {
                clientX = p.x;
                clientY = p.y;
                hop(clientX, clientY);
            }
        } else if (arg0.getX() > 0 && arg0.getY() > 0) {
            clientX = arg0.getX();
            clientY = arg0.getY();
            hop(clientX, clientY);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point p = config.getCanvas().getMousePosition();
        if (p != null && p.x > 0 && p.y > 0 && config.getCanvas() != null && Utilities.isPointValid(p)) {
            if (humanInput) {
                clientX = p.x;
                clientY = p.y;
                hop(clientX, clientY);
            }
        } else if (e.getX() > 0 && e.getY() > 0 && Utilities.isPointValid(new Point(e.getX(), e.getY()))) {
            clientX = e.getX();
            clientY = e.getY();
        }
        hop(clientX, clientY);
    }

    public boolean isHumanInput() {
        return humanInput;
    }

    public int getX() {
        return clientX;
    }

    public int getY() {
        return clientY;
    }

    public int getPressX() {
        return clientPressX;
    }

    public int getPressY() {
        return clientPressY;
    }

    public long getPressTime() {
        return clientPressTime;
    }

    public boolean isPressed() {
        return clientPressed;
    }

    public Point getLocation() {
        return new Point(getX(), getY());
    }

    public void hop(int x, int y) {
        clientX = x;
        clientY = y;
        mouseMotionDispatcher.mouseMoved(new MouseEvent(component, MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(), 0, x, y, 0, false));
    }

    public void click(int x, int y, int button) {
        press(x, y, button);
        Utilities.sleep(20, 100);
        release(x, y, button);
        mouseListenerDispatcher.mouseClicked(new MouseEvent(component, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, clientX, clientY, 1, false, button));
    }

    public void press(int x, int y, int button) {
        mouseListenerDispatcher.mousePressed(new MouseEvent(component,
                MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, x, y,
                1, false, button));
    }

    public void release(int x, int y, int button) {
        mouseListenerDispatcher.mouseReleased(new MouseEvent(component,
                MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, x, y,
                1, false, button));
    }

    public void click(boolean left) {
        click(clientX, clientY, (left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3));
    }

    public boolean drag(int x1, int y1, int x2, int y2) {
        hop(x1, y1);
        press(getX(), getY(), MouseEvent.BUTTON2);
        hop(x2, y2);
        release(getX(), getY(), MouseEvent.BUTTON2);
        return getX() == x2 && getY() == y2 && !isPressed();
    }

    public void move(int x, int y) {
        Point destination = new Point(x, y);
        Point mousePosition = getLocation();
        if (mousePosition.distance(destination) > 20) {
            Point[] path = mouseAlgorithm.makeMousePath(mousePosition.x, mousePosition.y, destination.x, destination.y);

            for (Point p : path) {
                mousePosition = p;
                hop(mousePosition.x, mousePosition.y);
                Time.sleep(6, 10);
            }
        } else {
            Point difference = new Point((int) (destination.getX() - mousePosition.getX()), (int) (destination.getY() - mousePosition.getY()));
            for (double Current = 0; Current < 1; Current += (4 / Math.sqrt(Math.pow(difference.getX(), 2) + Math.pow(difference.getY(), 2)))) {
                mousePosition = new Point((int) mousePosition.getX() + (int) (difference.getX() * Current), (int) mousePosition.getY() + (int) (difference.getY() * Current));
                hop(mousePosition.x, mousePosition.y);
                Time.sleep(1, Random.nextInt(4, 5));
            }
        }

        Time.sleep(40, 50);
    }


    public boolean dragMouse(int x1, int y1, int x2, int y2) {
        hop(x1, y1);
        press(getX(), getY(), MouseEvent.BUTTON2);
        hop(x2, y2);
        release(getX(), getY(), MouseEvent.BUTTON2);

        return getX() == x2 && getY() == y2 && !isPressed();
    }

}
