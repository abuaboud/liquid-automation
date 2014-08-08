package org.liquidbot.bot.ui.account;

import com.google.gson.Gson;
import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;
import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.Constants;
import org.liquidbot.bot.script.api.interfaces.Condition;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.utils.Logger;
import org.liquidbot.bot.utils.NetUtils;
import org.liquidbot.bot.utils.Utilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Kenneth on 8/5/2014.
 */
public class AccountManager extends JFrame {

    private final Logger log = new Logger(AccountManager.class);
    private final File accountFile = new File(Constants.SETTING_PATH + File.separator + Constants.ACCOUNT_FILE_NAME);
    private final Gson gson = new Gson();

    private final Configuration config = Configuration.getInstance();
    private final DefaultTableModel model;
    private final JTable accounts;
    private final JScrollPane scrollPane;
    private final JButton add, remove;

    private final JTextField userField;
    private final JLabel userLabel;

    private final JComboBox<Account.Reward> rewardsBox;

    public AccountManager() {
        super("Account Manager");
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                saveAccounts();
            }
        });
        this.userLabel = new JLabel();
        this.userLabel.setText("Email Address:");

        this.model = new DefaultTableModel(loadAccounts(), new String[]{
                "Username", "Password", "Bank Pin", "Lamp Skill"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        this.accounts = new JTable();
        this.accounts.setModel(model);

        this.scrollPane = new JScrollPane();
        this.scrollPane.setViewportView(accounts);
        this.scrollPane.setPreferredSize(new Dimension(400, 200));

        this.add = new JButton("Add account");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userField.getText().length() > 0) {
                    final Account.Reward reward = (Account.Reward) rewardsBox.getSelectedItem();
                    model.addRow(new String[]{userField.getText(), "", "", "Cooking"});

                    saveAccounts();
                }
            }
        });
        this.remove = new JButton("Remove account");
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = accounts.getSelectedRow();
                if (selectedRow != -1) {
                    if (accounts.getSelectedRow() >= 0) {
                        model.removeRow(accounts.getSelectedRow());

                    }
                }
            }
        });

        userField = new JTextField(15);
        rewardsBox = new JComboBox<>(Account.Reward.values());

        final JPanel center = new JPanel();
        center.setBorder(new TitledBorder("Add a new account."));
        center.setLayout(new FlowLayout());
        center.add(userLabel);
        center.add(userField);

        final JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout());
        bottom.add(add);
        bottom.add(remove);

        TableColumnModel cm = accounts.getColumnModel();
        cm.getColumn(cm.getColumnIndex("Password")).setCellEditor(new PasswordCellEditor());
        cm.getColumn(cm.getColumnIndex("Password")).setCellRenderer(new PasswordCellRenderer());

        cm.getColumn(cm.getColumnIndex("Bank Pin")).setCellEditor(new PasswordCellEditor());
        cm.getColumn(cm.getColumnIndex("Bank Pin")).setCellRenderer(new PasswordCellRenderer());

        cm.getColumn(0).setResizable(false);
        cm.getColumn(1).setResizable(false);
        cm.getColumn(2).setResizable(false);
        cm.getColumn(3).setResizable(false);
        cm.getColumn(3).setCellEditor(new DefaultCellEditor(rewardsBox));

        getContentPane().add(center, BorderLayout.CENTER);
        getContentPane().add(scrollPane, BorderLayout.NORTH);
        getContentPane().add(bottom, BorderLayout.SOUTH);

        getContentPane().setPreferredSize(new Dimension(600, 305));

        pack();
        setLocationRelativeTo(getOwner());
    }

    public Account[] getAccounts() {
        final Account[] acc = new Account[model.getRowCount()];
        for (int i = 0; i < acc.length; i++) {
            acc[i] = new Account((String) model.getValueAt(i, 0)
                    , (String) model.getValueAt(i, 1)
                    , (String) model.getValueAt(i, 2)
                    , (String) model.getValueAt(i, 3));
        }
        return acc;
    }

    public void saveAccounts() {
        final String json = gson.toJson(getAccounts());
        try {
            final FileWriter writer = new FileWriter(accountFile);
            writer.append(json);
            writer.close();
            log.info("Saved Account");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error saving accounts!");
        }
    }

    public String[][] loadAccounts() {
        ArrayList<String[]> arrayList = new ArrayList<>();
        try {
            if (accountFile.exists()) {
                final Account[] accounts = gson.fromJson(NetUtils.readPage(accountFile.toURI().toURL().toString())[0], Account[].class);
                for (Account acc : accounts) {
                    arrayList.add(new String[]{acc.getEmail(), acc.getPassword(), acc.getPin() == -1 ? "" : acc.getPin() + "", acc.getReward().name()});
                }
                log.info("Successfully loaded " + accounts.length + " accounts!", Color.GREEN);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return arrayList.toArray(new String[arrayList.size()][4]);
    }

    private class PasswordCellRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = -8149913137634230574L;

        @Override
        protected void setValue(final Object value) {
            if (value == null) {
                setText("");
            } else {
                final String str = value.toString();
                final StringBuilder b = new StringBuilder();
                for (int i = 0; i < str.length(); ++i) {
                    b.append("\u25CF");
                }
                setText(b.toString());
            }
        }
    }

    private static class PasswordCellEditor extends DefaultCellEditor {
        private static final long serialVersionUID = -8042183192369284908L;

        public PasswordCellEditor() {
            super(new JPasswordField());
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
