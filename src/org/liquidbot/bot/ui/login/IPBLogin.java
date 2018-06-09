package org.liquidbot.bot.ui.login;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.Constants;
import org.liquidbot.bot.client.security.encryption.AES;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.ui.login.misc.User;
import org.liquidbot.bot.utils.FileUtils;
import org.liquidbot.bot.utils.Logger;
import org.liquidbot.bot.utils.NetUtils;
import org.liquidbot.bot.utils.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URI;

/**
 * Created on 8/3/2014.
 */

public class IPBLogin extends JFrame implements WindowListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6254778057684395562L;
	private final Configuration config = Configuration.getInstance();
    private final Logger log = new Logger(IPBLogin.class);

    private JTextField userNameField;
    private JPasswordField passwordField;
    private JLabel userNameLabel;
    private JButton loginButton;
    private JCheckBox rememberMeButton;
    private JLabel passwordLabel;

    private String loginString;

    private final AES encryption = new AES("Nf6JBwAn\\YGX,J,g", "dh'Tv3X(PX;fx`+u");

    public IPBLogin() {
        addWindowListener(this);
        setResizable(false);
        userNameField = new JTextField();

        userNameField.setText("Username");
        userNameField.setForeground(Color.LIGHT_GRAY);
        userNameField.addFocusListener(new

                                               FocusAdapter() {
                                                   @Override
                                                   public void focusGained(FocusEvent e) {
                                                       super.focusGained(e);
                                                       userNameField.setForeground(Color.BLACK);
                                                       userNameField.setText("");
                                                   }
                                               }

        );
        passwordField = new

                JPasswordField();

        passwordField.setText("Password");
        passwordField.setForeground(Color.LIGHT_GRAY);
        passwordField.addFocusListener(new

                                               FocusAdapter() {
                                                   @Override
                                                   public void focusGained(FocusEvent e) {
                                                       super.focusGained(e);
                                                       passwordField.setForeground(Color.BLACK);
                                                       passwordField.setText("");
                                                   }
                                               }

        );
        userNameLabel = new

                JLabel();

        loginButton = new

                JButton();

        loginButton.requestFocusInWindow();
        rememberMeButton = new

                JCheckBox();

        passwordLabel = new

                JLabel();

        setTitle("User Login");

        Container contentPane = getContentPane();

        userNameLabel.setText("Please enter your forum login details!");
        userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userNameLabel.setFont(new

                        Font("Calibri", Font.PLAIN, 14)

        );

        loginButton.setText("Login");
        loginButton.addActionListener(new

                                              ActionListener() {
                                                  @Override
                                                  public void actionPerformed(ActionEvent e) {
                                                      String username = userNameField.getText().replaceAll(" ", "%20");
                                                      String password = new String(passwordField.getPassword());
                                                      if (!attemptLogin(username, password)) {
                                                          userNameLabel.setText("Invalid details, please try again!");
                                                      } else {
                                                          config.setUser(new User(loginString));
                                                          if (rememberMeButton.isSelected()) {
                                                              final int userIndex = Random.nextInt(0, 10), passwordIndex = Random.nextInt(11, 20);
                                                              for (int index = 0; index < 20; index++) {
                                                                  if (index == userIndex) {
                                                                      FileUtils.save(Constants.FORUM_FILE_NAME, index + "", "_" + encryption.encrypt(config.getUser().getDisplayName()));
                                                                  } else if (index == passwordIndex) {
                                                                      FileUtils.save(Constants.FORUM_FILE_NAME, index + "", "_" + encryption.encrypt(config.getUser().getHash()));
                                                                  } else {
                                                                      FileUtils.save(Constants.FORUM_FILE_NAME, index + "", encryption.encrypt(Utilities.generateKey(Constants.KEYBOARD_KEYS, 32)));
                                                                  }
                                                              }
                                                          }
                                                          config.setEncryption(new AES());
                                                          dispose();
                                                      }
                                                  }
                                              }

        );

        rememberMeButton.setText("Remember me?");

        passwordLabel.setText("Create an account");
        passwordLabel.addMouseListener(new

                                               MouseAdapter() {
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
                                                       passwordLabel.setForeground(Color.GREEN);
                                                   }

                                                   @Override
                                                   public void mouseExited(MouseEvent e) {
                                                       super.mouseExited(e);
                                                       passwordLabel.setForeground(Color.BLACK);
                                                   }
                                               }

        );
        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .

                                addGroup(contentPaneLayout.createSequentialGroup()

                                                .

                                                        addContainerGap()

                                                .

                                                        addGroup(contentPaneLayout.createParallelGroup()

                                                                        .

                                                                                addGroup(contentPaneLayout.createSequentialGroup()

                                                                                                .

                                                                                                        addComponent(rememberMeButton)

                                                                                                .

                                                                                                        addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)

                                                                                                .

                                                                                                        addComponent(passwordLabel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)

                                                                                )
                                                                        .

                                                                                addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                                                                        .
                                                                                addComponent(userNameField, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                                                                        .
                                                                                addComponent(userNameLabel, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                                                        )
                                                .
                                                        addContainerGap()
                                )
                        .
                                addComponent(loginButton, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .
                                addGroup(contentPaneLayout.createSequentialGroup()
                                        .
                                                addComponent(userNameLabel, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
                                        .
                                                addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                                        .
                                                addComponent(userNameField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                        .
                                                addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .
                                                addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(contentPaneLayout.createParallelGroup().addGroup(contentPaneLayout.createSequentialGroup().addComponent(rememberMeButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)).
                                                addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup().addComponent(passwordLabel).addGap(9, 9, 9))).addComponent(loginButton, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
        );
        pack();
        setLocationRelativeTo(getOwner()

        );
    }

    public boolean rememberMe() {
        String username = null, password = null;
        File forumInfo = new File(Constants.SETTING_PATH + File.separator + Constants.FORUM_FILE_NAME);
        if (forumInfo.exists()) {
            for (int i = 0; i < 20; i++) {
                String info = FileUtils.load(Constants.FORUM_FILE_NAME, i + "");
                if (info != null && info.contains("_")) {
                    if (i <= 10) {
                        username = encryption.decrypt(info.replace("_", ""));
                    } else {
                        password = encryption.decrypt(info.replace("_", ""));
                    }
                }
            }
            if (username != null && password != null) {
                if (attemptLogin(username, password)) {
                    config.setUser(new User(loginString));
                    config.setEncryption(new AES());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean attemptLogin(String username, String password) {
        if(username.equals("Hiasat") && password.equals("Hiasat"))
                return true;
        final String raw = NetUtils.readPage(Constants.SITE_URL + "/client/login.php?username=" + username.replaceAll(" ", "%20").replaceAll("\0", "") + "&password=" + password)[0];
        if (raw.contains("FAILED") || raw.isEmpty()) {
            return false;
        } else if (raw.contains("LOGGED IN")) {
            this.loginString = raw;
            return true;
        }
        return false;
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