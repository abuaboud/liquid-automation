package org.liquidbot.bot.ui;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.utils.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class BotPopupMenu extends JPopupMenu {

    private final JMenu view;
    private final JMenuItem gameObjects, npcs, groundItems, mouse, canvas, settings, widgets, players;

    private final Logger log = new Logger(getClass());

    public BotPopupMenu() {
        view = new JMenu("View");

        players = new JMenuItem("Players");
        players.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Configuration.drawPlayers = !Configuration.drawPlayers;
                log.info(Configuration.drawPlayers ? "Enabled player drawing." : "Disabled player drawing.");
            }
        });


        gameObjects = new JMenuItem("Game Objects");
        npcs = new JMenuItem("NPCs");
        npcs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Configuration.drawNPCs = !Configuration.drawNPCs;
                log.info(Configuration.drawNPCs ? "Enabled NPC drawing." : "Disabled NPC drawing.");
            }
        });
        groundItems = new JMenuItem("Ground Items");
        mouse = new JMenuItem("Mouse");
        mouse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Configuration.drawMouse = !Configuration.drawMouse;
                log.info(Configuration.drawMouse ? "Enabled mouse drawing." : "Disabled mouse drawing.");
            }
        });

        canvas = new JMenuItem("Canvas");
        canvas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Configuration.drawCanvas = !Configuration.drawCanvas;
                log.info(Configuration.drawCanvas ? "Enabled canvas drawing." : "Disabled canvas drawing.");
            }
        });

        settings = new JMenuItem("Settings Explorer");
        widgets = new JMenuItem("Widget Explorer");

        view.add(players);
        view.add(gameObjects);
        view.add(npcs);
        view.add(groundItems);
        view.add(mouse);
        view.add(canvas);

        add(view);
        add(new JSeparator());
        add(settings);
        add(widgets);
    }

}
