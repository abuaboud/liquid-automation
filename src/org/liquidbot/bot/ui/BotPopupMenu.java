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
    private final JMenuItem settings, widgets, console;
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
        mouse.setSelected(config.drawMouse());
        mouse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                config.drawMouse(!config.drawMouse());
                log.info(config.drawMouse() ? "Enabled mouse drawing." : "Disabled mouse drawing.");
            }
        });

        canvas = new JCheckBoxMenuItem("Canvas");
        canvas.setSelected(config.drawCanvas());
        canvas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                config.drawCanvas(!config.drawCanvas());
                log.info(config.drawCanvas() ? "Enabled canvas drawing." : "Disabled canvas drawing.");
            }
        });

        console = new JMenuItem("Display Console");
        console.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (config.getConsole().isDisplaying()) {
                    log.info("Disabling Console.");
                    config.getConsole().display(false);
                    config.getBotFrame().remove(config.getConsole());
                } else {
                    log.info("Enabled Console.");
                    config.getConsole().display(true);
                    config.getBotFrame().add(config.getConsole());
                }
                config.getBotFrame().pack();
                config.getBotFrame().revalidate();
            }
        });

        groundItems = new JCheckBoxMenuItem("Ground Items");
        groundItems.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                config.drawGroundItems(!config.drawGroundItems());
                log.info(config.drawGroundItems() ? "Enabled GroundItems drawing." : "Disabled GroundItems drawing.");
            }
        });
        gameObjects = new JCheckBoxMenuItem("GameObjects");
        gameObjects.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                config.drawGameObjects(!config.drawGameObjects());
                log.info(config.drawGameObjects() ? "Enabled GameObjects drawing." : "Disabled GameObjects drawing.");
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
        add(new JSeparator());
        add(console);
    }

}
