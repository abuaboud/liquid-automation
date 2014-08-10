package org.liquidbot.bot.ui.script;

import com.google.gson.Gson;
import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.Constants;
import org.liquidbot.bot.script.SkillCategory;
import org.liquidbot.bot.script.loader.ScriptInfo;
import org.liquidbot.bot.script.loader.ScriptLoader;
import org.liquidbot.bot.ui.account.Account;
import org.liquidbot.bot.utils.Logger;
import org.liquidbot.bot.utils.NetUtils;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kenneth on 8/8/2014.
 */
public class ScriptSelector extends JFrame {

	private final File accountFile = new File(Constants.SETTING_PATH + File.separator + Constants.ACCOUNT_FILE_NAME);
	private final Logger log = new Logger(ScriptSelector.class);
	private final Gson gson = new Gson();

	private JScrollPane scrollPane;
	private JPanel scriptPanel;
	private DefaultComboBoxModel<Account> accountModel;
	private DefaultComboBoxModel<SkillCategory> skillModel;
	private JComboBox<Account> accountBox;
	private JComboBox<SkillCategory> skillBox;
	private JTextField searchField;

	private JLabel accountLabel;
	private JLabel searchLabel;

	public ScriptSelector() {
		super("Script Selector");
		setResizable(false);

		getContentPane().setLayout(null);

		accountModel = new DefaultComboBoxModel<>(loadAccounts());
		accountBox = new JComboBox<>(accountModel);

		skillModel = new DefaultComboBoxModel<>(SkillCategory.values());
		skillBox = new JComboBox<>(skillModel);

		searchField = new JTextField(15);

		scriptPanel = new JPanel();
		scrollPane = new JScrollPane(scriptPanel);
		scrollPane.setLayout(null);
		scrollPane.setBounds(0, 35, 535, 351);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		accountLabel = new JLabel("Account:");
		accountLabel.setBounds(5, 5, 55, 25);

		accountBox = new JComboBox<>(loadAccounts());
		accountBox.setBounds(60, 5, 200, 25);

		searchLabel = new JLabel("Search:");
		searchLabel.setBounds(280, 5, 55, 25);

		searchField = new JTextField();
		searchField.setBounds(325, 5, 180, 25);

		getContentPane().add(searchField);
		getContentPane().add(searchLabel);
		getContentPane().add(scrollPane);
		getContentPane().add(accountBox);
		getContentPane().add(accountLabel);

		pack();
		setSize(535, 386);
		setLocationRelativeTo(getOwner());
	}

	public Account[] loadAccounts() {
		final List<Account> list = new ArrayList<>();
		try {
			final String[] data = NetUtils.readPage(accountFile.toURI().toURL().toString());
			final Account[] accounts = gson.fromJson(data[0], Account[].class);
			Collections.addAll(list, accounts);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return list.toArray(new Account[list.size()]);
	}

	public void loadScripts() {

		scrollPane.removeAll();
		for (final ScriptInfo info : ScriptLoader.getScripts()) {
			final ScriptPanel panel = new ScriptPanel(info);
			panel.setBounds(0, 0, 170, 115);
			panel.getButton().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					Configuration.getInstance().getScriptHandler().start(ScriptLoader.loadScript(info), info, (Account) accountBox.getSelectedItem());
					ScriptSelector.this.dispose();
				}
			});
			scrollPane.add(panel);
		}
		searchField.setText("");
	}

}
