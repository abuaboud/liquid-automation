package org.liquidbot.bot.ui;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.ScriptHandler;
import org.liquidbot.bot.script.loader.ScriptInfo;
import org.liquidbot.bot.script.loader.ScriptLoader;
import org.liquidbot.bot.ui.account.Account;
import org.liquidbot.bot.ui.script.ScriptPanel;
import org.liquidbot.bot.ui.script.ScriptSelector;
import org.liquidbot.bot.ui.sdn.SDNFrame;
import org.liquidbot.bot.utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class BotButtonPanel extends JPanel {

	private final Color color = new Color(92, 98, 106);
	private final Color colorDark = color.darker();

	private BotButton startButton, pauseButton, stopButton, keyboardButton, mouseButton, settingsButton, sdnButton;
	private BotPopupMenu menu;
	private SDNFrame sdnFrame;
	private ScriptSelector scriptSelector;

	private final Logger log = new Logger(BotButtonPanel.class);
	private final Configuration config = Configuration.getInstance();

	public BotButtonPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		menu = new BotPopupMenu();
		sdnFrame = new SDNFrame();
		scriptSelector = new ScriptSelector();

		sdnButton = new BotButton("sdn.png");
		sdnButton.setButtonHoverIcon("sdn_hover.png");
		sdnButton.setToolTipText("Open the Script Repository.");
		sdnButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (config.getUser() == null || config.getUser().getDisplayName() == null) {
					log.error("SDN not available under guest account!");
				} else {
					log.info(sdnFrame.isVisible() ? "Hiding SDN panel." : "Displaying SDN panel.");
					if (!sdnFrame.isVisible())
						sdnFrame.loadScripts();
					sdnFrame.setLocationRelativeTo(sdnFrame.getOwner());
					sdnFrame.setVisible(!sdnFrame.isVisible());
				}
			}
		});
		add(sdnButton);

		startButton = new BotButton("play.png");
		startButton.setButtonHoverIcon("play_hover.png");
		startButton.setToolTipText("Start a script.");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (config.getScriptHandler().getScriptState().equals(ScriptHandler.State.PAUSE)) {
					config.getScriptHandler().setScriptState(ScriptHandler.State.RUNNING);
				} else if (!config.getScriptHandler().getScriptState().equals(ScriptHandler.State.RUNNING) && !scriptSelector.isVisible()) {
					scriptSelector.loadScripts();
					scriptSelector.setLocationRelativeTo(scriptSelector.getOwner());
					scriptSelector.setVisible(!scriptSelector.isVisible());
				}

			}
		});
		add(startButton);

		pauseButton = new BotButton("pause.png");
		pauseButton.setButtonHoverIcon("pause_hover.png");
		pauseButton.setToolTipText("Pause the currently running script.");
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (config.getScriptHandler().getScriptState() != null && config.getScriptHandler().getScriptState().equals(ScriptHandler.State.RUNNING)) {
					config.getScriptHandler().pause();
				} else {
					log.error("There is no script currently running!");
				}
			}
		});
		add(pauseButton);

		stopButton = new BotButton("stop.png");
		stopButton.setButtonHoverIcon("stop_hover.png");
		stopButton.setToolTipText("Stop the currently running script.");
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (config.getScriptHandler().getScriptState() != null && !config.getScriptHandler().getScriptState().equals(ScriptHandler.State.STOPPED)) {
					config.getScriptHandler().stop();
				} else {
					log.error("There is no script currently running!");
				}
			}
		});
		add(stopButton);
		add(Box.createHorizontalGlue());

		keyboardButton = new BotButton("keyboard_enabled.png");
		keyboardButton.setButtonHoverIcon("keyboard_enabled_hover.png");
		keyboardButton.setToolTipText("Disable keyboard input.");
		keyboardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				config.enableKeyboard(!config.enableKeyboard());
				keyboardButton.setButtonIcon(config.enableKeyboard() ? "keyboard_enabled.png" : "keyboard_disabled.png");
				keyboardButton.setButtonHoverIcon(config.enableKeyboard() ? "keyboard_enabled_hover.png" : "keyboard_disabled_hover.png");
				keyboardButton.revalidate();

			}
		});
		add(keyboardButton);

		mouseButton = new BotButton("mouse_enabled.png");
		mouseButton.setButtonHoverIcon("mouse_enabled_hover.png");
		mouseButton.setToolTipText("Disable mouse input.");
		mouseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				config.enableMouse(!config.enableMouse());
				mouseButton.setButtonIcon(config.enableMouse() ? "mouse_enabled.png" : "mouse_disabled.png");
				mouseButton.setButtonHoverIcon(config.enableMouse() ? "mouse_enabled_hover.png" : "mouse_disabled_hover.png");
				mouseButton.revalidate();
			}
		});
		add(mouseButton);

		settingsButton = new BotButton("settings.png");
		settingsButton.setButtonHoverIcon("settings_hover.png");
		settingsButton.setToolTipText("Display the client settings.");
		settingsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JButton button = (JButton) e.getSource();
				menu.show(BotButtonPanel.this, button.getX(), button.getY());
			}
		});
		add(settingsButton);
	}

	@Override
	public void paintComponent(Graphics g) {
		final Graphics2D graphics2D = (Graphics2D) g;
		final GradientPaint gradient = new GradientPaint(getX(), getY(), colorDark, getWidth(), getY(), color);
		graphics2D.setPaint(gradient);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
