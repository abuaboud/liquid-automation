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
    private final Configuration config = Configuration.getInstance();
    private final Logger log = new Logger(ScriptSelector.class);
    private final Gson gson = new Gson();

    private JScrollPane scrollPane;
    private JPanel bottomPanel, centerPanel;
    private DefaultComboBoxModel<Account> accountModel;
    private DefaultComboBoxModel<SkillCategory> skillModel;
    private JComboBox<Account> accountBox;
    private JComboBox<SkillCategory> skillBox;
    private JTextField searchField;

    public ScriptSelector() {
        super("Script Selector");
        getContentPane().setLayout(new BorderLayout());

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        accountModel = new DefaultComboBoxModel<>(loadAccounts());
        accountBox = new JComboBox<>(accountModel);

        skillModel = new DefaultComboBoxModel<>(SkillCategory.values());
        skillBox = new JComboBox<>(skillModel);

        searchField = new JTextField(15);

        bottomPanel.add(accountBox);
        bottomPanel.add(skillBox);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(searchField);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EtchedBorder());
        scrollPane = new JScrollPane(centerPanel);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        for(final ScriptInfo info : ScriptLoader.getScripts()) {
            final ScriptButton button = new ScriptButton(info);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    config.getScriptHandler().start(ScriptLoader.loadScript(info), info, config.getScriptHandler().getAccount());
                    config.setAccount((Account) accountBox.getSelectedItem());
                }
            });
            centerPanel.add(button);
        }

        pack();
        setVisible(true);
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

    public static void main(String[] args) {
        new ScriptSelector();
    }

}
