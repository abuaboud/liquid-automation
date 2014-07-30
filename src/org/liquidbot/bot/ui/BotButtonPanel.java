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

    public BotButtonPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        menu = new BotPopupMenu();

        sdnButton = new BotButton("https://cdn.mediacru.sh/VJ1IrdBfIYpX.png", "sdn.png");
        sdnButton.setButtonHoverIcon("https://cdn.mediacru.sh/IQ0sCem5wazC.png", "sdn_hover.png");
        sdnButton.setToolTipText("Open the Script Repository.");
        add(sdnButton);

        startButton = new BotButton("https://cdn.mediacru.sh/TQOGhANP0b2m.png", "play.png");
        startButton.setButtonHoverIcon("https://cdn.mediacru.sh/q_BU8XQ07WBI.png", "play_hover.png");
        startButton.setToolTipText("Start a script.");
        add(startButton);

        pauseButton = new BotButton("https://cdn.mediacru.sh/5JAmySxCUoHh.png", "pause.png");
        pauseButton.setButtonHoverIcon("https://cdn.mediacru.sh/nC_f0l81-XP8.png", "pause_hover.png");
        pauseButton.setToolTipText("Pause the currently running script.");
        add(pauseButton);

        stopButton = new BotButton("https://cdn.mediacru.sh/HqAN3dcj3iZw.png", "stop.png");
        stopButton.setButtonHoverIcon("https://cdn.mediacru.sh/zlqTXZwzKScf.png", "stop_hover.png");
        stopButton.setToolTipText("Stop the currently running script.");
        add(stopButton);
        add(Box.createHorizontalGlue());

        keyboardButton = new BotButton("https://cdn.mediacru.sh/V_tmickKKdnK.png", "keyboard_enabled.png");
        keyboardButton.setButtonHoverIcon("https://cdn.mediacru.sh/unQ6sxD3tzhv.png", "keyboard_enabled_hover.png");
        keyboardButton.setToolTipText("Disable keyboard input.");
        keyboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Configuration.enableKeyboard = !Configuration.enableKeyboard;
                keyboardButton.setButtonIcon(Configuration.enableKeyboard? "https://cdn.mediacru.sh/V_tmickKKdnK.png" : "https://cdn.mediacru.sh/1E8MZolVQPKY.png",
                        Configuration.enableKeyboard ? "keyboard_enabled.png" : "keyboard_disabled.png");

                keyboardButton.setButtonHoverIcon(Configuration.enableKeyboard? "https://cdn.mediacru.sh/unQ6sxD3tzhv.png" : "https://cdn.mediacru.sh/B-PGAWpWVeyq.png",
                        Configuration.enableKeyboard ? "keyboard_enabled_hover.png" : "keyboard_disabled_hover.png");

                keyboardButton.revalidate();
            }
        });
        add(keyboardButton);

        mouseButton  = new BotButton("https://cdn.mediacru.sh/wYaSvKxsV_Uy.png", "mouse_enabled.png");
        mouseButton.setButtonHoverIcon("https://cdn.mediacru.sh/i7P4BRIjjXH9.png", "mouse_enabled_hover.png");
        mouseButton.setToolTipText("Disable mouse input.");
        mouseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Configuration.enableMouse = !Configuration.enableMouse;
                mouseButton.setButtonIcon(Configuration.enableMouse? "https://cdn.mediacru.sh/wYaSvKxsV_Uy.png" : "https://cdn.mediacru.sh/uEwATeMaLEpD.png",
                        Configuration.enableMouse ? "mouse_enabled.png" : "mouse_disabled.png");

                mouseButton.setButtonHoverIcon(Configuration.enableMouse? "https://cdn.mediacru.sh/i7P4BRIjjXH9.png" : "https://cdn.mediacru.sh/A-N_ANIdQLzw.png",
                        Configuration.enableMouse ? "mouse_enabled_hover.png" : "mouse_disabled_hover.png");

                mouseButton.revalidate();
            }
        });
        add(mouseButton);

        settingsButton = new BotButton("https://cdn.mediacru.sh/GyqaxnAQ9cu-.png", "settings.png");
        settingsButton.setButtonHoverIcon("https://cdn.mediacru.sh/jP6yg959nOwZ.png", "settings_hover.png");
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
