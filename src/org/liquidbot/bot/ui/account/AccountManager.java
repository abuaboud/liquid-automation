package org.liquidbot.bot.ui.account;

import com.google.gson.Gson;
import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;
import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.interfaces.Condition;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.utils.Logger;
import org.liquidbot.bot.utils.NetUtils;
import org.liquidbot.bot.utils.Utilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

/**
 * Created by Kenneth on 8/5/2014.
 */
public class AccountManager extends JFrame {

    private final Logger log = new Logger(AccountManager.class);
    private final Configuration config = Configuration.getInstance();
    private final File accountFile = new File(Utilities.getContentDirectory() + "accounts.json");
    private final Gson gson = new Gson();

    private final DefaultListModel<Account> model;
    private final JList<Account> accounts;
    private final JButton add, remove;

    private final JTextField userField;
    private final JPasswordField passField;
    private final JSpinner pinSpinner;
    private final JComboBox<Account.Reward> rewardsBox;

    public AccountManager() {
        super("Account Manager");
        getContentPane().setLayout(new BorderLayout());
        this.model = new DefaultListModel<>();
        this.accounts = new JList<>(model);
        this.accounts.setPreferredSize(new Dimension(400, 200));
        this.add = new JButton("Add account");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final Account.Reward reward = (Account.Reward) rewardsBox.getSelectedItem();
                model.addElement(new Account(userField.getText(), new String(passField.getPassword()), String.valueOf(pinSpinner.getValue()), reward.name()));
                saveAccounts();
            }
        });
        this.remove = new JButton("Remove account");
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(Account acc : accounts.getSelectedValuesList()) {
                    model.removeElement(acc);
                }
                saveAccounts();
            }
        });

        userField = new JTextField(15);
        passField = new JPasswordField(15);
        pinSpinner = new JSpinner();
        pinSpinner.setPreferredSize(new Dimension(60, 25));
        rewardsBox = new JComboBox<>(Account.Reward.values());

        final JPanel center = new JPanel();
        center.setBorder(new TitledBorder("Add a new account."));
        center.setLayout(new FlowLayout());
        center.add(userField);
        center.add(passField);
        center.add(pinSpinner);
        center.add(rewardsBox);


        final JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout());
        bottom.add(add);
        bottom.add(remove);

        getContentPane().add(center, BorderLayout.CENTER);
        getContentPane().add(accounts, BorderLayout.NORTH);
        getContentPane().add(bottom, BorderLayout.SOUTH);

        try {
            loadAccounts();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        pack();
        setLocationRelativeTo(getOwner());
    }

    private Account[] getLoadedAccounts() {
        final Account[] acc = new Account[model.size()];
        for(int i = 0; i < acc.length; i++) {
            acc[i] = model.getElementAt(i);
        }
        return acc;
    }

    public void saveAccounts() {
        final String json = gson.toJson(getLoadedAccounts());
        try {
            final FileWriter writer = new FileWriter(accountFile);
            writer.append(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error saving accounts!");
        }
    }

    public void loadAccounts() throws MalformedURLException {
        if(accountFile.exists()) {
            final Account[] accounts = gson.fromJson(NetUtils.readPage(accountFile.toURI().toURL().toString())[0], Account[].class);
            for(Account acc : accounts) {
                model.addElement(acc);
            }
            log.info("Successfully loaded " + model.size() + " accounts!", Color.GREEN);
        }
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new SyntheticaAluOxideLookAndFeel());
        } catch (UnsupportedLookAndFeelException | ParseException e) {
            e.printStackTrace();
        }

        final AccountManager manager = new AccountManager();
        manager.setVisible(true);
    }
}
