package org.liquidbot;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaWhiteVisionLookAndFeel;
import org.liquidbot.bot.ui.BotFrame;

import javax.swing.*;
import java.text.ParseException;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class Boot {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(new SyntheticaWhiteVisionLookAndFeel());
                } catch (UnsupportedLookAndFeelException | ParseException e) {
                    e.printStackTrace();
                }
                final BotFrame frame = new BotFrame();
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

}
