package org.liquidbot.bot.ui.sdn;

import org.liquidbot.bot.script.loader.ScriptInfo;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Created by Kenneth on 7/31/2014.
 */
public class SDNElement extends JPanel {

    private JButton button;
    private JLabel imageLabel;

    private JLabel nameLabel;
    private JLabel descriptionLabel;

    private JPanel bottom;
    private JPanel center;

    private ScriptInfo scriptInfo;

    public SDNElement(String data) {
        scriptInfo = new ScriptInfo(data);

        setLayout(new BorderLayout());
        button = new JButton(scriptInfo.collection ? "Remove" : "Add");
        bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
        bottom.add(Box.createHorizontalGlue());
        bottom.add(button);

        center = new JPanel();
        center.setLayout(new BorderLayout());

        imageLabel = new JLabel(scriptInfo.skillCategory.getIcon());
        nameLabel = new JLabel(scriptInfo.name);
        descriptionLabel = new JLabel("<html><center>" + scriptInfo.desc + "</center></html>");
        center.add(descriptionLabel, BorderLayout.CENTER);
        center.add(nameLabel, BorderLayout.NORTH);
        center.add(imageLabel, BorderLayout.WEST);

        setBorder(new EtchedBorder());
        setSize(250, 150);
        add(bottom, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);
    }

    public ScriptInfo getScriptInfo() {
        return scriptInfo;
    }


}
