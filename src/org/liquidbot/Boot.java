package org.liquidbot;

import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel;
import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.client.parser.HookReader;
import org.liquidbot.bot.ui.BotConsole;
import org.liquidbot.bot.ui.BotFrame;
import org.liquidbot.bot.ui.login.IPBLogin;
import org.liquidbot.bot.utils.Logger;
import org.liquidbot.bot.utils.NetUtils;
import org.liquidbot.bot.utils.Utilities;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class Boot {

    private static final Logger log = new Logger(Boot.class);
    private static final Configuration config = Configuration.getInstance();

    public static void main(String[] args) {
        JPopupMenu.setDefaultLightWeightPopupEnabled(true);
        config.setConsole(new BotConsole());
        log.info("Parsing hooks..");
        HookReader.init();

        try {
            UIManager.setLookAndFeel(new SyntheticaAluOxideLookAndFeel());
        } catch (UnsupportedLookAndFeelException | ParseException e) {
            e.printStackTrace();
        }

        final IPBLogin login = new IPBLogin();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                login.pack();
                login.setVisible(true);
            }
        });
        thread.start();
        Utilities.sleep(500);
        while(login.isVisible()) {
            Utilities.sleep(200, 300);
        }


        log.info("Lauching client..");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {



                final Image iconImage = Utilities.getLocalImage("/resources/liquidicon.png");
                final BotFrame frame = new BotFrame();
                config.setBotFrame(frame);
                frame.setIconImage(iconImage);
                frame.pack();
                frame.setVisible(true);


            }
        });
    }


}