package org.liquidbot.bot.ui;

import org.liquidbot.bot.utils.Utilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class BotButton extends JButton {

    public BotButton(String imageURL, String imageName) {

        BufferedImage image = null;
        final File imageLocation = new File(Utilities.getContentDirectory() + "/resources/" + imageName);
        try {
            if(!imageLocation.exists()) {
                image = ImageIO.read(new URL(imageURL));
                ImageIO.write(image, ".png", imageLocation.getAbsoluteFile());

            } else {
                image = ImageIO.read(imageLocation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        setIcon(new ImageIcon(image));
        setBorderPainted(false);
        setMargin(new Insets(1, 0, 1, 0));
    }

}
