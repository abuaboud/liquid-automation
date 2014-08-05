package org.liquidbot.bot.ui.login;

import net.miginfocom.swing.MigLayout;
import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.client.security.encryption.AES;
import org.liquidbot.bot.ui.login.misc.User;
import org.liquidbot.bot.utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Kenneth on 8/3/2014.
 */

public class IPBLogin extends JFrame {

    private final JButton okButton, cancelButton;
    private final JTextField userField;
    private final JPasswordField passField;
    private final JPanel centerPanel, bottomPanel;

    private final Configuration config = Configuration.getInstance();
    private final Logger log = new Logger(IPBLogin.class);

    public IPBLogin() {
        super("Enter your LiquidBot forum details!");
        getContentPane().setLayout(new MigLayout());

        okButton = new JButton("Login");
        okButton.setMnemonic('l');
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("Attempting to login as " + userField.getText());
                setVisible(false);
                config.setUser(new User(userField.getText(), new String(passField.getPassword())));

            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.setMnemonic('c');
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.error("Skipping login, running under guest. Note: You will not have SDN access!");
                setVisible(false);
            }
        });

        userField = new JTextField(15);
        passField = new JPasswordField(15);
        passField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    log.info("Attempting to login as " + userField.getText());
                    setVisible(false);
                    config.setUser(new User(userField.getText(), new String(passField.getPassword())));
                }
            }
        });

        centerPanel = new JPanel();
        centerPanel.setLayout(new MigLayout("nogrid, flowy"));
        centerPanel.add(new JLabel("Username"));
        centerPanel.add(userField, "h 30");
        centerPanel.add(new JLabel("Password"));
        centerPanel.add(passField, "h 30");

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new MigLayout("nogrid, flowx"));

        bottomPanel.add(okButton, "gap 5");
        bottomPanel.add(cancelButton);

        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
    }
}
