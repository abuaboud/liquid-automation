package org.liquidbot;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import org.liquidbot.bot.ui.BotFrame;

import javax.swing.*;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class Boot {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(new WindowsLookAndFeel());
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                final BotFrame frame = new BotFrame();
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

}
