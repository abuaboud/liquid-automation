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
    private final JMenuItem  settings, widgets;
    private final JCheckBoxMenuItem gameObjects, npcs, groundItems, mouse, canvas, players;
    private final Configuration config = Configuration.getInstance();

    private final Logger log = new Logger(getClass());

    public BotPopupMenu() {
        view = new JMenu("View");

        players = new JCheckBoxMenuItem("Players");
        players.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                config.drawPlayers(!config.drawPlayers());
                log.info(config.drawPlayers() ? "Enabled player drawing." : "Disabled player drawing.");
            }
        });

        npcs = new JCheckBoxMenuItem("NPCs");
        npcs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                config.drawNPCs(!config.drawNPCs());
                log.info(config.drawNPCs() ? "Enabled npc drawing." : "Disabled npc drawing.");
            }
        });

        mouse = new JCheckBoxMenuItem("Mouse");
        mouse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                config.drawMouse(!config.drawMouse());
                log.info(config.drawMouse() ? "Enabled mouse drawing." : "Disabled mouse drawing.");
            }
        });

        canvas = new JCheckBoxMenuItem("Canvas");
        canvas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                config.drawCanvas(!config.drawCanvas());
                log.info(config.drawCanvas() ? "Enabled canvas drawing." : "Disabled canvas drawing.");
            }
        });

        groundItems = new JCheckBoxMenuItem("Ground Items");
        gameObjects = new JCheckBoxMenuItem("Game Objects");
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
