package org.liquidbot.bot.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class BotButtonPanel extends JPanel {

    private String[] buttons = {
            "http://i.imgur.com/6ur1b4w.png", "http://i.imgur.com/RlLK12N.png", "http://i.imgur.com/gj6TKkX.png",
            "http://i.imgur.com/XyjRPT4.png"
    };

    public BotButtonPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(new BotButton(buttons[0], "control_play.png"));
        add(new BotButton(buttons[1], "control_pause.png"));
        add(new BotButton(buttons[2], "control_stop.png"));

        add(Box.createHorizontalGlue());

        final BotButton settings = new BotButton(buttons[3], "gear.png");
        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = ((JButton) e.getSource()).getX();
                int y = ((JButton) e.getSource()).getY();
                final JPopupMenu menu = createPopupMenu();
                menu.show(BotButtonPanel.this, x, y);
                System.out.println("Displaying panel");
            }
        });
        add(settings);

    }

    public JPopupMenu createPopupMenu() {
        final JPopupMenu popup = new JPopupMenu();
        JMenu view = new JMenu("View");
        view.add(new JMenuItem("GameObjects"));
        view.add(new JMenuItem("NPCs"));
        view.add(new JMenuItem("GroundItems"));
        popup.add(view);
        popup.add(new JSeparator());
        popup.add(new JMenuItem("Settings Explorer"));
        popup.add(new JMenuItem("Widget Explorer"));
        return popup;
    }

}
