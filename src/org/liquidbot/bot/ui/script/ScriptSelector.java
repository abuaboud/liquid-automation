package org.liquidbot.bot.ui.script;

import com.google.gson.Gson;
import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.Constants;
import org.liquidbot.bot.script.loader.ScriptInfo;
import org.liquidbot.bot.script.loader.ScriptLoader;
import org.liquidbot.bot.ui.account.Account;
import org.liquidbot.bot.utils.NetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

/**
 * Created by Kenneth on 7/31/2014.
 */
public class ScriptSelector extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5378865050472191696L;
	private final File accountFile = new File(Constants.SETTING_PATH + File.separator + Constants.ACCOUNT_FILE_NAME);
	private final Gson gson = new Gson();

	private JTextField searchField;
	private JComboBox<String> accounts;

	private JPanel topPanel;
	private JPanel scriptPanel;
	private JScrollPane scrollPane;

	public ScriptSelector() {
		super("LiquidBot Script Selector");
		setResizable(false);


		searchField = new JTextField(20);
		searchField.setForeground(Color.LIGHT_GRAY);
		searchField.setText("Search");
		searchField.setMaximumSize(new Dimension(100, 30));
		searchField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				super.focusGained(e);
				searchField.setForeground(Color.BLACK);
				searchField.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);
				searchField.setForeground(Color.LIGHT_GRAY);
				searchField.setText("Search");
			}
		});
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				super.keyTyped(e);
			}
		});

		accounts = new JComboBox<>();


		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(accounts);
		topPanel.add(Box.createHorizontalGlue());
		topPanel.add(searchField);

		scriptPanel = new JPanel();
		scriptPanel.setLayout(null);

		scrollPane = new JScrollPane(scriptPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);


		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(535, 405);
	}


	public Account[] loadAccounts() {
		final java.util.List<Account> list = new ArrayList<>();
		try {
			final String[] data = NetUtils.readPage(accountFile.toURI().toURL().toString());
            if(data.length > 0) {
                final Account[] accounts = gson.fromJson(data[0], Account[].class);
                Collections.addAll(list, accounts);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list.toArray(new Account[list.size()]);
	}

	public void loadScripts() {
		scriptPanel.removeAll();
		java.util.List<ScriptInfo> scripts = ScriptLoader.getScripts();
		if(Constants.OFFLINE_MODE) {
			scripts.add(new ScriptInfo("LiquidFisher", "LiquidFisher", "Best Fishing Script Ever!", "Hiasat"));
			scripts.add(new ScriptInfo("LiquidSlayer", "LiquidSlayer", "Best Woodcutting Script Ever!", "Hiasat"));
		}
		final int width = 170;
		final int height = 115;
		final int spacing = 3;
		final int scriptPerRow = 3;
		int realIndex = 0;
		for (int scriptIndex = 0; scriptIndex < scripts.size(); scriptIndex++) {
			final ScriptInfo scriptInfo = scripts.get(scriptIndex);
			if (scriptInfo.collection) {
				final ScriptPanel panel = new ScriptPanel(scriptInfo);
				int col = realIndex / scriptPerRow;
				int row = realIndex - (col * scriptPerRow);
				int x = row * width + spacing;
				int y = col * height + spacing;
				panel.setBounds(x, y, width, height);
				panel.getButton().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Account account = null;
						for (Account acc : loadAccounts()) {
							if (acc.getEmail().contains(accounts.getSelectedItem().toString())) {
								account = acc;
								break;
							}
						}
						Configuration.getInstance().getScriptHandler().start(ScriptLoader.loadScript(scriptInfo), scriptInfo, account);
						ScriptSelector.this.dispose();
					}
				});
				scriptPanel.add(panel);
				realIndex++;
			}
		}

		searchField.setText("");
		scriptPanel.setPreferredSize(new Dimension(535, (int) (Math.ceil((Double.valueOf(scriptPanel.getComponentCount()) / 3.0)) *height )));
		Account[] loadedAccounts = loadAccounts();
		final String[] accountsList = new String[loadedAccounts.length + 1];
		accountsList[0] = "None";
		for (int x = 1; x < accountsList.length; x++) {
			accountsList[x] = loadedAccounts[x - 1].getEmail().replaceAll("\0", "");
		}
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(accountsList);
		accounts.setModel(model);
	}

}
