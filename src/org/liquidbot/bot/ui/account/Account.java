package org.liquidbot.bot.ui.account;

import com.google.gson.Gson;
import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.Constants;
import org.liquidbot.bot.utils.NetUtils;

import java.io.File;
import java.net.MalformedURLException;

/**
 * Created by Kenneth on 8/5/2014.
 */
public class Account {

    private String email, password, pin;
    private Reward reward;

    public Account(String email, String password, String pin, Reward reward) {
        this.email = Configuration.getInstance().getEncryption().encrypt(email);
        this.password = Configuration.getInstance().getEncryption().encrypt(password);
        this.pin = pin;
        this.reward = reward;
    }

    public String getEmail() {
        return Configuration.getInstance().getEncryption().decrypt(email);
    }

    public String getPassword() {
        return Configuration.getInstance().getEncryption().decrypt(password);
    }

    public int getPin() {
        if (pin.length() == 0)
            return -1;
        return Integer.parseInt(pin);
    }

    public Reward getReward() {
        return reward;
    }

    @Override
    public String toString() {
        return getEmail();
    }

    public enum Reward {
        ATTACK(0), DEFENSE(1), STRENGTH(2), CONSTITUTION(3), RANGE(4), PRAYER(5), MAGIC(6),
        COOKING(7), WOODCUTTING(8), FLETCHING(9), FISHING(10), FIREMAKING(11), CRAFTING(12),
        SMITHING(13), MINING(14), HERBLORE(15), AGILITY(16), THIEVING(17), SLAYER(18),
        FARMING(19), RUNECRAFTING(20), HUNTER(21), CONSTRUCTION(22);

        private int id;

        private Reward(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Reward get(String name) {
            for(Reward reward : values()) {
                if(reward.name().equalsIgnoreCase(name)) {
                    return reward;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }

}
