package org.liquidbot.bot.ui.login;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.client.security.encryption.AES;
import org.liquidbot.bot.ui.login.misc.User;
import org.liquidbot.bot.utils.Logger;
import org.liquidbot.bot.utils.NetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;

/**
 * Created by Kenneth on 8/3/2014.
 */

public class IPBLogin extends JFrame implements WindowListener {

    private final Configuration config = Configuration.getInstance();
    private final Logger log = new Logger(IPBLogin.class);

    private JTextField textField1;
    private JPasswordField passwordField1;
    private JLabel label1;
    private JButton button1;
    private JCheckBox checkBox1;
    private JLabel label2;

    private String username;
    private String password;

    private String loginString;

    private boolean attemptLogin() {
        final String raw = NetUtils.readPage("http://liquidbot.org/client/login.php?username=" + this.username + "&password=" + this.password)[0];
        if(raw.contains("FAILED") || raw.isEmpty()) {
            return false;
        } else if(raw.contains("LOGGED IN")) {
            this.loginString = raw;
            return true;
        }
        return false;
    }

    public IPBLogin() {
        setResizable(false);
        addWindowListener(this);
        textField1 = new JTextField();
        textField1.setText("Username");
        textField1.setForeground(Color.LIGHT_GRAY);
        textField1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                textField1.setForeground(Color.BLACK);
                textField1.setText("");
            }
        });
        passwordField1 = new JPasswordField();
        passwordField1.setText("Password");
        passwordField1.setForeground(Color.LIGHT_GRAY);
        passwordField1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                passwordField1.setForeground(Color.BLACK);
                passwordField1.setText("");
            }
        });
        label1 = new JLabel();
        button1 = new JButton();
        button1.requestFocusInWindow();
        checkBox1 = new JCheckBox();
        label2 = new JLabel();

        setTitle("User Login");
        Container contentPane = getContentPane();

        label1.setText("Please enter your forum login details!");
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setFont(new Font("Calibri", Font.PLAIN, 14));

        button1.setText("Login");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IPBLogin.this.username = textField1.getText().replaceAll(" ", "%20");
                IPBLogin.this.password = new String(passwordField1.getPassword());
                if(!attemptLogin()) {
                    label1.setText("Invalid details, please try again!");
                } else {
                    config.setUser(new User(loginString));
                    config.setEncryption(new AES());
                    dispose();
                }
            }
        });

        checkBox1.setText("Remember me?");
        checkBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.error("Saving of accounts is not yet implemented, sorry for the inconvenience");
            }
        });

        label2.setText("Create an account");
        label2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    Desktop.getDesktop().browse(new URI("http://liquidbot.org/forum/index.php?app=core&module=global&section=register"));
                } catch (Exception x) {
                    x.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                label2.setForeground(Color.GREEN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                label2.setForeground(Color.BLACK);
            }
        });
        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(checkBox1)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                                                .addComponent(label2, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(passwordField1, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                                        .addComponent(textField1, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                                        .addComponent(label1, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE))
                                .addContainerGap())
                        .addComponent(button1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addComponent(label1, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                                .addComponent(textField1, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(passwordField1, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(checkBox1)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE))
                                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                                .addComponent(label2)
                                                .addGap(9, 9, 9)))
                                .addComponent(button1, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
        );
        pack();
        setLocationRelativeTo(getOwner());
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        log.error("Login closed without details! Closing client.");
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}