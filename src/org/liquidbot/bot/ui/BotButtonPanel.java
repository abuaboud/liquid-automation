package org.liquidbot.bot.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class BotButtonPanel extends JPanel {

    private String[] buttons = {
        "http://i.imgur.com/6ur1b4w.png", "http://i.imgur.com/RlLK12N.png", "http://i.imgur.com/gj6TKkX.png"
    };

    private Image[] buttonImages = new Image[buttons.length];

    public BotButtonPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        for(int i = 0; i < buttons.length; i++) {
            try  {
                buttonImages[i] = ImageIO.read(new URL(buttons[i]));
            } catch(IOException a) {
                a.printStackTrace();
            }
        }

        for(Image image : buttonImages) {
            add(createButton(image));
        }

    }

    private JButton createButton(Image image) {
        final JButton button = new JButton();
        button.setIcon(new ImageIcon(image));
        button.setBorder(null);
        button.setBorderPainted(false);
        return button;
    }

}
