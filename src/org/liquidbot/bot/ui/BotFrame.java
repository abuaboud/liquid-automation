package org.liquidbot.bot.ui;

import org.liquidbot.bot.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class BotFrame extends JFrame {

    private final JPanel appletPanel;

    public BotFrame() {
        super(Constants.CLIENT_TITLE + " - v" + Constants.CLIENT_VERSION);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        appletPanel = new JPanel();
        appletPanel.setPreferredSize(new Dimension(Constants.APPLET_WIDTH, Constants.APPLET_HEIGHT));
        this.getContentPane().add(appletPanel);
    }
}
