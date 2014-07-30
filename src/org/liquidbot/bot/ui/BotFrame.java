package org.liquidbot.bot.ui;

import org.liquidbot.bot.Constants;
import org.liquidbot.bot.client.RSLoader;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class BotFrame extends JFrame {

    private final RSLoader loader;
    private final BotButtonPanel buttonPanel;

    public BotFrame() {
        super(Constants.CLIENT_TITLE + " - v" + Constants.CLIENT_VERSION);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.buttonPanel = new BotButtonPanel();
        this.getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.getContentPane().add(buttonPanel);

        this.loader = new RSLoader();
        this.getContentPane().add(loader);
    }

    public RSLoader loader(){
        return loader;
    }
}
