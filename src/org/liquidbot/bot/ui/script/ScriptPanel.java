package org.liquidbot.bot.ui.script;

import org.liquidbot.bot.script.loader.ScriptInfo;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Created on 8/8/2014.
 */
public class ScriptPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5181188196122580695L;
	private JButton button;

    public ScriptPanel(final ScriptInfo scriptInfo) {
        setLayout(new BorderLayout());

		setToolTipText(scriptInfo.desc);

		button = new JButton("Start");
        add(button, BorderLayout.SOUTH);

        final JLabel imageLabel = new JLabel(scriptInfo.skillCategory.getIcon());
		add(imageLabel, BorderLayout.WEST);

        final JLabel nameLabel = new JLabel(scriptInfo.name);
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
		add(nameLabel, BorderLayout.NORTH);

		setBorder(new EtchedBorder());
	}

	public JButton getButton() {
		return button;
	}

}
