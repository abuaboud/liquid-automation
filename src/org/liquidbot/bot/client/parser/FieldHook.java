package org.liquidbot.bot.client.parser;

/**
 * Created by Hiasat on 7/29/2014.
 */
public class FieldHook {

    private final String fieldLine;

    /**
     * Prase Text line for Field Hook info
     *
     * @param fieldLine
     */
    public FieldHook(String fieldLine) {
        this.fieldLine = fieldLine;
    }

    /**
     * @return OwnerHookName#MethodName
     */
    public String getFieldKey() {
        return fieldLine.split(" ")[0];
    }

    /**
     * @return Field Integer
     */
    public String getType() {
        return fieldLine.split(" ")[2];
    }

    /**
     * @return fieldName
     */
    public String getFieldName() {
        return fieldLine.split(" ")[1].split("#")[1];
    }

    /**
     * @return Owner classname
     */
    public String getClassName() {
        return fieldLine.split(" ")[1].split("#")[0];
    }

    /**
     * @return Integer : multiplier
     */
    public int getMultiplier() {
        return Integer.parseInt(fieldLine.split(" ")[3]);
    }
}
