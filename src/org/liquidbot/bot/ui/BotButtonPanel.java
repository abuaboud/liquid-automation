package org.liquidbot.bot.ui;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.utils.NetUtils;
import org.liquidbot.bot.utils.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class BotButtonPanel extends JPanel {

    private final Color color = new Color(92, 98, 106);
    private final Color colorDark = color.darker();

    private BotButton startButton, pauseButton, stopButton, keyboardButton, mouseButton, settingsButton, sdnButton;
    private BotPopupMenu menu;

    private final Configuration config = Configuration.getInstance();

    public BotButtonPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        menu = new BotPopupMenu();

        sdnButton = new BotButton("sdn.png");
        sdnButton.setButtonHoverIcon("sdn_hover.png");
        sdnButton.setToolTipText("Open the Script Repository.");
        add(sdnButton);

        startButton = new BotButton("play.png");
        startButton.setButtonHoverIcon("play_hover.png");
        startButton.setToolTipText("Start a script.");
        add(startButton);

        pauseButton = new BotButton("pause.png");
        pauseButton.setButtonHoverIcon("pause_hover.png");
        pauseButton.setToolTipText("Pause the currently running script.");
        add(pauseButton);

        stopButton = new BotButton("stop.png");
        stopButton.setButtonHoverIcon("stop_hover.png");
        stopButton.setToolTipText("Stop the currently running script.");
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

        mouseButton  = new BotButton("mouse_enabled.png");
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
    public void paintComponent(Graphics g)  {
        final Graphics2D graphics2D = (Graphics2D) g;
        final GradientPaint gradient = new GradientPaint(getX(), getY(), colorDark, getWidth(), getY(), color);
        graphics2D.setPaint(gradient);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
