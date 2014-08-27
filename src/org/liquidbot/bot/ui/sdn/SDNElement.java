package org.liquidbot.bot.ui.sdn;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.Constants;
import org.liquidbot.bot.script.loader.ScriptInfo;
import org.liquidbot.bot.ui.login.misc.User;
import org.liquidbot.bot.utils.NetUtils;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kenneth on 7/31/2014.
 */
public class SDNElement extends JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -11911333152280301L;
	private JButton button;
	private JLabel imageLabel;

	private JLabel nameLabel;
	private JLabel descriptionLabel;

	private JPanel bottom;
	private JPanel center;

	private ScriptInfo scriptInfo;

	public SDNElement(final ScriptInfo scriptInfo) {
		this.scriptInfo = scriptInfo;
		setLayout(new BorderLayout());
		button = new JButton(scriptInfo.collection ? "Remove" : "Add");
		bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
		bottom.add(Box.createHorizontalGlue());
		bottom.add(button);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(SDNElement.this).start();
			}
		});
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


	@Override
	public void run() {
		button.setText(scriptInfo.collection ? "Removing" : "Adding");
		button.setEnabled(false);
		User user = Configuration.getInstance().getUser();
		String rawLine = NetUtils.readPage(Constants.SITE_URL + "/client/scripts.php?userId=" + user.getUserId() + "&scriptId=" + scriptInfo.scriptId + "&username=" + user.getDisplayName() + "&password=" + user.getHash() + "&action=" + (!scriptInfo.collection ? "add" : "remove"))[0];

		button.setText(rawLine.equalsIgnoreCase("Added") ? "Remove" : "Add");
		scriptInfo.collection = rawLine.equalsIgnoreCase("Added") ? true : false;
		button.setEnabled(true);
	}
}
