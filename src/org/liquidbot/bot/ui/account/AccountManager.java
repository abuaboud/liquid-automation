package org.liquidbot.bot.ui.account;

import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;
import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Properties;

/**
 * Created by Kenneth on 8/5/2014.
 */
public class AccountManager extends JFrame {

    private JLabel label1;
    private JTextField textField1;
    private JLabel label2;
    private JPasswordField passwordField1;
    private JLabel label3;
    private JSpinner spinner1;
    private JLabel label4;
    private JComboBox<Skills> comboBox1;
    private JButton button1;

    private final Configuration config = Configuration.getInstance();

    public AccountManager() {
        label1 = new JLabel();
        textField1 = new JTextField();
        label2 = new JLabel();
        passwordField1 = new JPasswordField();
        label3 = new JLabel();
        spinner1 = new JSpinner();
        label4 = new JLabel();
        comboBox1 = new JComboBox<>(Skills.values());
        button1 = new JButton();

        //======== this ========
        setTitle("Account Manager");
        Container contentPane = getContentPane();

        //---- label1 ----
        label1.setText("Username");

        //---- label2 ----
        label2.setText("Password");

        //---- label3 ----
        label3.setText("Bank Pin");

        //---- label4 ----
        label4.setText("Experience");

        //---- button1 ----
        button1.setText("Save Account");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final Properties props = FileUtils.loadProperties("accounts");
                try {
                    props.put("account#" + (props.keySet().size() + 1), config.getEncryption().encrypt(textField1.getText()) + ":" + config.getEncryption().encrypt(new String(passwordField1.getPassword()))
                            + ":" + spinner1.getValue() + ":" + comboBox1.getSelectedItem());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                FileUtils.saveProperties(props, "accounts");
            }
        });

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(textField1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                                        .addComponent(passwordField1, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGroup(contentPaneLayout.createParallelGroup()
                                                        .addComponent(label1)
                                                        .addComponent(label2))
                                                .addGap(0, 211, Short.MAX_VALUE))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGroup(contentPaneLayout.createParallelGroup()
                                                        .addComponent(label3)
                                                        .addComponent(spinner1, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(contentPaneLayout.createParallelGroup()
                                                        .addComponent(label4)
                                                        .addComponent(comboBox1, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE))))
                                .addContainerGap())
                        .addComponent(button1, GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(label1)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(passwordField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label3)
                                        .addComponent(label4))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(spinner1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                                .addComponent(button1))
        );
        pack();
        setLocationRelativeTo(getOwner());
    }

    public enum Skills {
        ATTACK(0), DEFENSE(1), STRENGTH(2), CONSTITUTION(3), RANGE(4), PRAYER(5), MAGIC(6),
        COOKING(7), WOODCUTTING(8), FLETCHING(9), FISHING(10), FIREMAKING(11), CRAFTING(12),
        SMITHING(13), MINING(14), HERBLORE(15), AGILITY(16), THIEVING(17), SLAYER(18),
        FARMING(19), RUNECRAFTING(20), HUNTER(21), CONSTRUCTION(22);

        private int id;
        private Skills(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
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
