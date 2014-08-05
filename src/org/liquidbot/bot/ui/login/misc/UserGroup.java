package org.liquidbot.bot.ui.login.misc;

/**
 * Created by Kenneth on 8/3/2014.
 */
public enum UserGroup {

    ADMINISTRATOR(4), GLOBAL_MODERATOR(6), GOLD_FARMER(12), GRAPHICS_ARTIST(14), ID_VERIFIED(15), MODERATOR(19),
    MEMBER(3), MEMBER_OF_THE_MONTH(16), PREMIUM_SCRIPT_WRITER(18), RESTRICTED(5), SCHOLAR(11), SCRIPT_WRITER(10),
    SPONSOR(8), VALIDATING(1), VETERANS(17), VIP(7);

    private int id;

    private UserGroup(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static UserGroup get(final int id) {
        for (UserGroup group : values()) {
            if (group.getId() == id) {
                return group;
            }
        }
        return null;
    }

}