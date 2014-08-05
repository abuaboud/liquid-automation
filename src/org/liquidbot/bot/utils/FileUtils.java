package org.liquidbot.bot.utils;

import java.io.*;
import java.util.Properties;

/**
 * Created by Kenneth on 8/5/2014.
 */
public class FileUtils {

    public static void saveProperties(Properties properties, String name) {
        try {
            final OutputStream outputStream = new FileOutputStream(Utilities.getContentDirectory() + name + ".properties");
            properties.store(outputStream, null);
            outputStream.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Properties loadProperties(String name) {
        final Properties props = new Properties();
        try {
            final File file = new File(Utilities.getContentDirectory() + name + ".properties");
            if(!file.exists()) file.mkdirs();
            props.load(new FileInputStream(file));
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        return props;
    }

}
