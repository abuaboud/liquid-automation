package org.liquidbot.bot.ui;

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

        sdnButton = new BotButton("https://raw.githubusercontent.com/Ineedajob/RSBot/master/resources/images/world_link.png", "world_link.png");
        sdnButton.setToolTipText("Open the Script Repository.");
        add(sdnButton);

        startButton = new BotButton("https://copy.com/P92cjV2tjZkk/control_play.png", "control_play.png");
        startButton.setButtonRollOverIcon("https://raw.githubusercontent.com/Ineedajob/RSBot/master/resources/images/control_play_blue.png", "control_play_blue.png");
        startButton.setToolTipText("Start a script.");
        add(startButton);

        pauseButton = new BotButton("https://raw.githubusercontent.com/Ineedajob/RSBot/master/resources/images/control_pause.png", "control_pause.png");
        pauseButton.setToolTipText("Pause the currently running script.");
        add(pauseButton);

        stopButton = new BotButton("https://raw.githubusercontent.com/Ineedajob/RSBot/master/resources/images/control_stop.png", "control_stop.png");
        stopButton.setToolTipText("Stop the currently running script.");
        add(stopButton);
        add(Box.createHorizontalGlue());

        keyboardButton = new BotButton("https://raw.githubusercontent.com/Ineedajob/RSBot/master/resources/images/keyboard.png", "keyboard.png");
        keyboardButton.setToolTipText("Disable keyboard input.");
        add(keyboardButton);

        mouseButton  = new BotButton("https://raw.githubusercontent.com/Ineedajob/RSBot/master/resources/images/mouse.png", "mouse.png");
        mouseButton.setToolTipText("Disable mouse input.");
        add(mouseButton);

        settingsButton = new BotButton("https://raw.githubusercontent.com/Ineedajob/RSBot/master/resources/images/bot.png", "gear.png");
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
