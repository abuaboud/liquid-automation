package org.liquidbot.bot.ui.account;

import org.liquidbot.bot.Configuration;

/**
 * Created by Kenneth on 8/5/2014.
 */
public class Account {

    private String email, password, pin, reward;

    public Account(String email, String password, String pin, String reward) {
        this.email = email;
        this.password = password;
        this.pin = pin;
        this.reward = reward;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPin() {
        return Integer.parseInt(pin);
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Reward getReward() {
        return Reward.get(reward);
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        return getEmail() + "\t | " + getPassword() + "\t | " + pin + "\t | " + reward;
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
            for(Reward rew : Reward.values()) {
                if(rew.name().toLowerCase().equals(name.toLowerCase()))
                    return rew;
            }
            return null;
        }

        @Override
        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }
}
