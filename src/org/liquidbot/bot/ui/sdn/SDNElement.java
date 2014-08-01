package org.liquidbot.bot.ui.sdn;

import org.liquidbot.bot.script.SkillCategory;
import org.liquidbot.bot.utils.Utilities;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Created by Kenneth on 7/31/2014.
 */
public class SDNElement extends JPanel {

    private String clazz;
    private String name;
    private String desc;
    private String author;
    private SkillCategory skillCategory;
    private int scriptId;
    private int ownerUserId;
    private int privlage;
    private int cancelled;
    private double price;
    private int billing;
    private String made_date;
    private String last_update;
    private boolean collection;

    private JButton button;
    private JLabel imageLabel;

    private JLabel nameLabel;
    private JLabel descriptionLabel;

    private JPanel bottom;
    private JPanel center;

    public SDNElement(String data) {
        String[] info = data.split("~");
        scriptId = Integer.parseInt(info[0]);
        ownerUserId = Integer.parseInt(info[1]);
        name = info[2];
        desc = info[3];
        skillCategory = SkillCategory.getCategory(Integer.parseInt(info[4]));
        privlage = Integer.parseInt(info[5]);
        author = info[6];
        price = Double.parseDouble(info[7]);
        billing = Integer.parseInt(info[8]);
        made_date = info[9];
        last_update = info[10];
        collection = Integer.parseInt(info[11]) == 1;
        clazz = info[12];
        cancelled =  Integer.parseInt(info[13]);

        setLayout(new BorderLayout());

        button = new JButton(collection ? "Remove" : "Add");
        bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
        bottom.add(Box.createHorizontalGlue());
        bottom.add(button);

        center = new JPanel();
        center.setLayout(new BorderLayout());

        imageLabel = new JLabel(skillCategory.getIcon());
        nameLabel = new JLabel(name);
        descriptionLabel = new JLabel("<html><center>" + desc + "</center></html>");
        center.add(descriptionLabel, BorderLayout.CENTER);
        center.add(nameLabel, BorderLayout.NORTH);
        center.add(imageLabel, BorderLayout.WEST);

        setBorder(new EtchedBorder());
        setPreferredSize(new Dimension(250, 150));
        add(bottom, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);
    }


    public int getCancelled() {
        return cancelled;
    }

    public String getClazz() {
        return clazz;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getAuthor() {
        return author;
    }

    public SkillCategory getSkillCategory() {
        return skillCategory;
    }

    public int getScriptId() {
        return scriptId;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public int getPrivlage() {
        return privlage;
    }

    public double getPrice() {
        return price;
    }

    public int getBilling() {
        return billing;
    }

    public String getMade_date() {
        return made_date;
    }

    public String getLast_update() {
        return last_update;
    }

    public boolean isCollection() {
        return collection;
    }
}
