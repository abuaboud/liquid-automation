package org.liquidbot.bot.ui;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.Constants;
import org.liquidbot.bot.utils.FileUtils;
import org.liquidbot.bot.utils.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class BotPopupMenu extends JPopupMenu implements ActionListener {

	private final JMenu view, lowCpuMenu;
	private final JCheckBoxMenuItem smartBreak, gameObjects, npcs, groundItems, mouse, canvas, players, inventory, gameState, playerLocation, mouseLocation, floor, mapBase, camera, menu, lowCpu, displayfps;
	private final JMenuItem settings, widgets, console, accounts;
	private final Configuration config = Configuration.getInstance();

	private final Logger log = new Logger(getClass());

	public BotPopupMenu() {
		view = new JMenu("View");

		gameState = new JCheckBoxMenuItem("Game State");
		gameState.addActionListener(this);

		playerLocation = new JCheckBoxMenuItem("Player Location");
		playerLocation.addActionListener(this);

		mouseLocation = new JCheckBoxMenuItem("Mouse Location");
		mouseLocation.addActionListener(this);

		floor = new JCheckBoxMenuItem("Floor");
		floor.addActionListener(this);

		mapBase = new JCheckBoxMenuItem("Map base");
		mapBase.addActionListener(this);

		camera = new JCheckBoxMenuItem("Camera");
		camera.addActionListener(this);

		menu = new JCheckBoxMenuItem("Menu");
		menu.addActionListener(this);

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

		displayfps = new JCheckBoxMenuItem("Display FPS");
		displayfps.addActionListener(this);

		lowCpuMenu = new JMenu("Low CPU");
		config.setFpsSlider(new JSlider());
		String lowCpuText = FileUtils.load(Constants.SETTING_FILE_NAME, "LOW_CPU");
		if (lowCpuText != null) {
			config.setCPU(Boolean.parseBoolean(lowCpuText));
		}
		config.getFpsSlider().setValue(config.lowCpu() ? 10 : 50);
		final TitledBorder border = new TitledBorder("Adjust FPS");
		border.setTitleColor(Color.WHITE);
		config.getFpsSlider().setBorder(border);
		config.getFpsSlider().setMinimum(0);
		config.getFpsSlider().setMaximum(50);
		config.getFpsSlider().setSnapToTicks(true);
		config.getFpsSlider().setPaintTicks(true);
		config.getFpsSlider().setPaintLabels(true);
		config.getFpsSlider().setForeground(Color.WHITE);
		lowCpuMenu.add(config.getFpsSlider());

		lowCpu = new JCheckBoxMenuItem("Low CPU");
		lowCpu.addActionListener(this);
		lowCpu.setSelected(Configuration.getInstance().lowCpu());
		lowCpuMenu.add(lowCpu);

		smartBreak = new JCheckBoxMenuItem("Smart Break");
		smartBreak.addActionListener(this);
		smartBreak.setSelected(true);

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
		widgets.addActionListener(this);

		accounts = new JMenuItem("Account Manager");
		accounts.addActionListener(this);




		view.add(displayfps);
		view.add(gameState);
		view.add(playerLocation);
		view.add(mouseLocation);
		view.add(floor);
		view.add(mapBase);
		view.add(camera);
		view.add(menu);
		add(new JSeparator());
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
		add(lowCpuMenu);
		add(smartBreak);
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
		} else if (e.getSource() == inventory) {
			config.drawInventory(!config.drawInventory());
			log.info(config.drawInventory() ? "Enabled InventoryDebugger drawing." : "Disabled InventoryDebugger drawing.");
		} else if (e.getSource() == gameState) {
			config.drawGameState(!config.drawGameState());
			log.info(!config.drawGameState() ? "Enabled GameState debugger." : "Disabled GameState debugger.");
		} else if (e.getSource() == mouseLocation) {
			config.drawMouseLocation(!config.drawMouseLocation());
			log.info(!config.drawMouseLocation() ? "Enabled Mouse debugger." : "Disabled Mouse debugger.");
		} else if (e.getSource() == floor) {
			config.drawFloor(!config.drawFloor());
			log.info(!config.drawFloor() ? "Enabled Floor debugger." : "Disabled Floor debugger.");
		} else if (e.getSource() == mapBase) {
			config.drawMapBase(!config.drawMapBase());
			log.info(!config.drawMapBase() ? "Enabled Map Base debugger." : "Disabled Map Base debugger.");
		} else if (e.getSource() == camera) {
			config.drawCamera(!config.drawCamera());
			log.info(!config.drawCamera() ? "Enabled Camera debugger." : "Disabled Camera debugger.");
		} else if (e.getSource() == menu) {
			config.drawMenu(!config.drawMenu());
			log.info(!config.drawMenu() ? "Enabled Menu debugger." : "Disabled Menu debugger.");
		} else if (e.getSource() == lowCpu) {
			config.getFpsSlider().setValue(config.lowCpu() ? 50 : 10);
			config.setCPU(!config.lowCpu());
			FileUtils.save(Constants.SETTING_FILE_NAME, "LOW_CPU", config.lowCpu() + "");
		} else if (e.getSource() == displayfps) {
			log.info(!config.isDisplayFPS() ? "Enabled FPS debugger." : "Disabled FPS debugger.");
			config.setDisplayFPS(!config.isDisplayFPS());
		} else if (e.getSource() == widgets) {
			new WidgetViewer();
			log.info(!config.drawWidgets() ? "Enabled Widgets debugger." : "Disabled Widgets debugger.");
			config.drawWidgets(!config.drawWidgets());
		}  else if (e.getSource() == smartBreak) {
			log.info(!config.smartBreak() ? "Enabled Smart Break." : "Disabled Smart Break.");
			config.smartBreak(!config.smartBreak());
		}

	}
}
