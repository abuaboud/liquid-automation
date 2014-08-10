package org.liquidbot.bot.ui.script;

import org.liquidbot.bot.script.SkillCategory;
import org.liquidbot.bot.script.loader.ScriptInfo;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kenneth on 8/8/2014.
 */
public class ScriptPanel extends JPanel {

	private JButton button;
	private JLabel imageLabel;

	private JLabel nameLabel;

	private ScriptInfo scriptInfo;

	public ScriptPanel(final ScriptInfo scriptInfo) {
		this.scriptInfo = scriptInfo;
		setLayout(null);

		setToolTipText(scriptInfo.desc);

		button = new JButton("Start");
		button.setBounds(70, 90, 92, 20);

		imageLabel = new JLabel(scriptInfo.skillCategory.getIcon());
		imageLabel.setBounds(0,10,48,51);

		nameLabel = new JLabel(scriptInfo.name);
		nameLabel.setBounds(60, 5, 110, 40);

		add(button);
		add(nameLabel);
		add(imageLabel);

		setBorder(new EtchedBorder());
	}

	public JButton getButton(){
		return button;
	}

}
