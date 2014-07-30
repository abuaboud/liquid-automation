package org.liquidbot;

import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;
import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.client.parser.HookReader;
import org.liquidbot.bot.ui.BotFrame;
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

    public static void main(String[] args) {
        JPopupMenu.setDefaultLightWeightPopupEnabled(true);

        log.info("Downloading images..");
        for(int i = 0; i < IMAGE_INFO.length; i++) {
            NetUtils.downloadFile(IMAGE_INFO[i][0], Utilities.getContentDirectory() + "/resources/" + IMAGE_INFO[i][1]);
        }

        log.info("Parsing hooks..");
        HookReader.init();

        log.info("Lauching client..");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                try {
                    UIManager.setLookAndFeel(new SyntheticaAluOxideLookAndFeel());
                } catch (UnsupportedLookAndFeelException | ParseException e) {
                    e.printStackTrace();
                }

                final Image iconImage = Utilities.getLocalImage("/resources/liquidicon.png");

                Configuration.botFrame = new BotFrame();
                Configuration.botFrame.setIconImage(iconImage);
                Configuration.botFrame.pack();
                Configuration.botFrame.setVisible(true);
            }
        });
    }

    private static final String[][] IMAGE_INFO = {
            {"http://i.imgbox.com/XkluyPhU.png", "liquidicon.png"},
            {"http://i.imgbox.com/czMSE5ZQ.png", "keyboard_disabled_hover.png"},
            {"http://i.imgbox.com/czMSE5ZQ.png", "keyboard_disabled.png"},
            {"http://i.imgbox.com/GdNcrzav.png", "keyboard_enabled.png"},
            {"http://i.imgbox.com/REVkNdHo.png", "keyboard_enabled_hover.png"},
            {"http://i.imgbox.com/ye5khDdX.png", "mouse_disabled.png"},
            {"http://i.imgbox.com/bCbgr8Cl.png", "mouse_disabled_hover.png"},
            {"http://i.imgbox.com/JRVQYZ5H.png", "mouse_enabled.png"},
            {"http://i.imgbox.com/UtvHyVNd.png", "mouse_enabled_hover.png"},
            {"http://i.imgbox.com/qeSgl2uF.png", "pause.png"},
            {"http://i.imgbox.com/T2ro1Rno.png", "pause_hover.png"},
            {"http://i.imgbox.com/QFfVFuqb.png", "play.png"},
            {"http://i.imgbox.com/SiLp4J5Z.png", "play_hover.png"},
            {"http://i.imgbox.com/ljhYvxNU.png", "sdn.png"},
            {"http://i.imgbox.com/QTg9Hv4E.png", "sdn_hover.png"},
            {"http://i.imgbox.com/Oy7bflOh.png", "settings.png"},
            {"http://i.imgbox.com/PU7Sf6Qw.png", "settings_hover.png"},
            {"http://i.imgbox.com/dTsNttxS.png", "stop.png"},
            {"http://i.imgbox.com/VgZ0gHnW.png", "stop_hover.png"},
    };

}
