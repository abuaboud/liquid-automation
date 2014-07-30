package org.liquidbot;

import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;
import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.client.parser.HookReader;
import org.liquidbot.bot.ui.BotFrame;
import org.liquidbot.bot.utils.NetUtils;
import org.liquidbot.bot.utils.Utilities;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class Boot {

    public static void main(String[] args) {
        JPopupMenu.setDefaultLightWeightPopupEnabled(true);
        HookReader.init();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                try {
                    UIManager.setLookAndFeel(new SyntheticaAluOxideLookAndFeel());
                } catch (UnsupportedLookAndFeelException | ParseException e) {
                    e.printStackTrace();
                }

                NetUtils.downloadFile("https://cdn.mediacru.sh/GwN6tZ72iQre.png", Utilities.getContentDirectory() + "/resources/liquidicon.png");
                final Image iconImage = Utilities.getLocalImage(Utilities.getContentDirectory() + "/resources/liquidicon.png");

                Configuration.botFrame = new BotFrame();
                Configuration.botFrame.setIconImage(iconImage);
                Configuration.botFrame.pack();
                Configuration.botFrame.setVisible(true);
            }
        });
    }

}
