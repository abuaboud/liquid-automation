package org.liquidbot.bot.ui.login.misc;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.ui.login.IPBLogin;
import org.liquidbot.bot.utils.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created on 8/3/2014.
 */
public class User {

    private int userId;
    private String displayName, hash;
    private List<UserGroup> secondaryGroups;
    private UserGroup primaryGroup;

    private final Logger log = new Logger(IPBLogin.class);
    private final Configuration config = Configuration.getInstance();

    public User(String loginString) {
        this.secondaryGroups = new ArrayList<>();
        if(loginString == null){
            this.primaryGroup = UserGroup.ADMINISTRATOR;
            this.secondaryGroups.add(UserGroup.SCHOLAR);
            this.userId = 1;
            this.displayName = "Hiasat";
            this.hash = "qTYGLQueDQuJsEDkjaqbrrkG9SVDZ38y";
            log.info("Login successful. Welcome " + getDisplayName() + "!", Color.GREEN);
            return;
        }
        try {
            final String[] data = loginString.split("<br>");
            this.hash = data[1];
            this.userId = Integer.parseInt(data[2]);
            this.primaryGroup = UserGroup.get(Integer.parseInt(data[3]));

            final String secondaryGroupString = data[4];
            if (secondaryGroupString.contains(",")) {
                final String[] split = secondaryGroupString.split(",");
                for (String str : split) {
                    final UserGroup group = str.isEmpty() ? null : UserGroup.get(Integer.parseInt(str));
                    if (group != null) {
                        secondaryGroups.add(group);
                    }
                }
            }
            this.displayName = data[5];
        } catch (Exception ex) {
            log.error("Error logging into your account, please check your details!");
            config.setUser(null);
            ex.printStackTrace();
            return;
        }
        log.info("Login successful. Welcome " + getDisplayName() + "!", Color.GREEN);
    }

    public int getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getHash() {
        return hash;
    }

    public List<UserGroup> getSecondaryGroups() {
        return secondaryGroups;
    }

    public UserGroup getPrimaryGroup() {
        return primaryGroup;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", displayName='" + displayName + '\'' +
                ", hash='" + hash + '\'' +
                ", secondaryGroups=" + secondaryGroups +
                ", primaryGroup=" + primaryGroup +
                '}';
    }
}
