package org.liquidbot;

import de.javasoft.plaf.synthetica.SyntheticaWhiteVisionLookAndFeel;
import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.client.praser.HookReader;
import org.liquidbot.bot.ui.BotFrame;

import javax.swing.*;
import java.text.ParseException;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class Boot {

    public static void main(String[] args) {
        HookReader.init();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(new SyntheticaWhiteVisionLookAndFeel());
                } catch (UnsupportedLookAndFeelException | ParseException e) {
                    e.printStackTrace();
                }
                Configuration.botFrame = new BotFrame();
                Configuration.botFrame.pack();
                Configuration.botFrame.setVisible(true);
            }
        });
    }

}
