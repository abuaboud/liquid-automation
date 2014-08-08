package org.liquidbot.bot.ui;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.utils.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class BotPopupMenu extends JPopupMenu implements ActionListener {

    private final JMenu view;
    private final JCheckBoxMenuItem lowCpu, gameObjects, npcs, groundItems, mouse, canvas, players, playerLocation,inventory;
    private final JMenuItem settings, widgets, console, accounts;
    private final Configuration config = Configuration.getInstance();

    private final Logger log = new Logger(getClass());

    public BotPopupMenu() {
        view = new JMenu("View");

        playerLocation = new JCheckBoxMenuItem("Player Location");
        playerLocation.addActionListener(this);

        players = new JCheckBoxMenuItem("Players");
        players.addActionListener(this);

        npcs = new JCheckBoxMenuItem("NPCs");
        npcs.addActionListener(this);

        mouse = new JCheckBoxMenuItem("Mouse");
        mouse.setSelected(config.drawMouse());
        mouse.addActionListener(this);

        canvas = new JCheckBoxMenuItem("Canvas");
        canvas.setSelected(config.drawCanvas());
        canvas.addActionListener(this);

        lowCpu = new JCheckBoxMenuItem("Low CPU");
        lowCpu.addActionListener(this);

        console = new JMenuItem("Display Console");
        console.addActionListener(this);

        inventory = new JCheckBoxMenuItem("Inventory");
        inventory.addActionListener(this);

        groundItems = new JCheckBoxMenuItem("Ground Items");
        groundItems.addActionListener(this);

        gameObjects = new JCheckBoxMenuItem("GameObjects");
        gameObjects.addActionListener(this);

        settings = new JMenuItem("Settings Explorer");
        settings.addActionListener(this);
        widgets = new JMenuItem("Widget Explorer");

        accounts = new JMenuItem("Account Manager");
        accounts.addActionListener(this);

        view.add(playerLocation);
        view.add(inventory);
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
        add(accounts);
        add(new JSeparator());
        add(lowCpu);
        add(console);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == canvas) {
            config.drawCanvas(!config.drawCanvas());
            log.info(config.drawCanvas() ? "Enabled canvas drawing." : "Disabled canvas drawing.");
        } else if (e.getSource() == mouse) {
            config.drawMouse(!config.drawMouse());
            log.info(config.drawMouse() ? "Enabled mouse drawing." : "Disabled mouse drawing.");
        } else if (e.getSource() == npcs) {
            config.drawNPCs(!config.drawNPCs());
            log.info(config.drawNPCs() ? "Enabled npc drawing." : "Disabled npc drawing.");
        } else if (e.getSource() == players) {
            config.drawPlayers(!config.drawPlayers());
            log.info(config.drawPlayers() ? "Enabled player drawing." : "Disabled player drawing.");
        } else if (e.getSource() == playerLocation) {
            config.drawPlayerLocation(!config.drawPlayerLocation());
            log.info(config.drawPlayerLocation() ? "Enabled player Location drawing." : "Disabled player Location drawing.");
        } else if (e.getSource() == gameObjects) {
            config.drawGameObjects(!config.drawGameObjects());
            log.info(config.drawGameObjects() ? "Enabled GameObjects drawing." : "Disabled GameObjects drawing.");
        } else if (e.getSource() == groundItems) {
            config.drawGroundItems(!config.drawGroundItems());
            log.info(config.drawGroundItems() ? "Enabled GroundItems drawing." : "Disabled GroundItems drawing.");
        } else if (e.getSource() == lowCpu) {
            config.lowCPU(!config.lowCPU());
            log.info(config.lowCPU() ? "Enabled Low CPU." : "Disabled Low CPU.");
        } else if (e.getSource() == console) {
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
        } else if (e.getSource() == accounts) {
            log.info(config.getAccountManager().isVisible() ? "Closing account manager." : "Displaying account manager.");
            config.getAccountManager().setVisible(!config.getAccountManager().isVisible());
        } else if (e.getSource() == settings) {
            log.info(!config.drawSettings() ? "Enabled Setting debugger." : "Disabled Setting debugger.");
            config.drawSettings(!config.drawSettings());
        }  else if (e.getSource() == inventory) {
            config.drawInventory(!config.drawInventory());
            log.info(config.drawInventory() ? "Enabled InventoryDebugger drawing." : "Disabled InventoryDebugger drawing.");
        }
    }
}
