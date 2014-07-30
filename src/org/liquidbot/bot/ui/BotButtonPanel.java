package org.liquidbot.bot.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class BotButtonPanel extends JPanel {

    private String[] buttons = {
        "http://i.imgur.com/6ur1b4w.png", "http://i.imgur.com/RlLK12N.png", "http://i.imgur.com/gj6TKkX.png",
            "http://i.imgur.com/XyjRPT4.png"
    };

    private Image[] buttonImages = new Image[buttons.length];

    public BotButtonPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        for(int i = 0; i < buttons.length; i++) {
            try  {
                buttonImages[i] = ImageIO.read(new URL(buttons[i]));
            } catch(IOException a) {
                a.printStackTrace();
            }
        }

        add(createButton(buttonImages[0]));
        add(createButton(buttonImages[1]));
        add(createButton(buttonImages[2]));

        add(Box.createHorizontalGlue());
        final JButton settings = createButton(buttonImages[3]);
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

    private JButton createButton(Image image) {
        final JButton button = new JButton();
        button.setIcon(new ImageIcon(image));
        button.setBorderPainted(false);
        button.setMargin(new Insets(1, 0, 1,0));
        return button;
    }

}
