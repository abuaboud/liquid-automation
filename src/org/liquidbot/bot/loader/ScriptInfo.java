package org.liquidbot.bot.loader;

import org.liquidbot.bot.script.SkillCategory;

/*
 * Created by Hiasat on 8/2/14
 */
public class ScriptInfo {

    public String clazz;
    public String name;
    public String desc;
    public String author;
    public SkillCategory skillCategory;
    public int scriptId;
    public int ownerUserId;
    public int privilege;
    public int cancelled;
    public double price;
    public int billing;
    public String made_date;
    public String last_update;
    public boolean collection;

    public ScriptInfo(String data) {
        String[] info = data.split("~");
        scriptId = Integer.parseInt(info[0]);
        ownerUserId = Integer.parseInt(info[1]);
        name = info[2];
        desc = info[3];
        skillCategory = SkillCategory.getCategory(Integer.parseInt(info[4]));
        privilege = Integer.parseInt(info[5]);
        author = info[6];
        price = Double.parseDouble(info[7]);
        billing = Integer.parseInt(info[8]);
        made_date = info[9];
        last_update = info[10];
        collection = Integer.parseInt(info[11]) == 1;
        clazz = info[12];
        cancelled = Integer.parseInt(info[13]);
    }

    public ScriptInfo(String clazz, String name, String desc, String author, SkillCategory skillCategory) {
        this.clazz = clazz;
        this.name = name;
        this.desc = desc;
        this.author = author;
        this.skillCategory = skillCategory;
        this.scriptId = -1;
        this.ownerUserId = -1;
        this.cancelled = 0;
        this.privilege = 1;
        this.price = 0;
        this.billing = 0;
        this.made_date = "";
        this.last_update = "";
        this.collection = true;
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

    public int getPrivilege() {
        return privilege;
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
