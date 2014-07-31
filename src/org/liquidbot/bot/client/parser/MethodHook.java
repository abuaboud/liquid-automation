package org.liquidbot.bot.client.parser;

/**
 * Created by Hiasat on 7/29/2014.
 */
public class MethodHook {

    private final String methodLine;

    /**
     * Prase Text line for Hook info
     *
     * @param methodLine
     */
    public MethodHook(String methodLine){
       this.methodLine = methodLine;
    }

    /**
     *
     * @return OwnerHookName#MethodName
     */
    public String getMethodKey(){
        return methodLine.split(" ")[0];
    }

    /**
     *
     * @return Field Integer
     */
    public String getType(){
        return methodLine.split(" ")[2];
    }

    /**
     *
     * @return methodName
     */
    public String getMethodName() {
        return methodLine.split(" ")[1].split("#")[1];
    }

    /**
     *
     * @return Owner classname
     */
    public String getClassName(){
        return methodLine.split(" ")[1].split("#")[0];
    }

    /**
     *
     * @return Integer : correct Param to be sent for Method
     */
    public int getCorrectParam(){
        return Integer.parseInt(methodLine.split(" ")[3]);
    }
}
