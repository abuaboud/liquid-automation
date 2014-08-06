package org.liquidbot.bot.utils;

import org.liquidbot.bot.Constants;

import java.io.*;
import java.util.Properties;

/**
 * Created by Kenneth on 8/5/2014.
 */
public class FileUtils {

    public static void save(String path, String fileName, String key, String value) {
        Properties prop = new Properties();
        try {
            File f = new File(path + File.separator + fileName);
            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                prop.load(fis);
            } else {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            prop.setProperty(key, value);
            prop.store(new FileWriter(f), "Info");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(String fileName, String key, String info) {
        save(Constants.SETTING_PATH, fileName, key, info);
    }

    public static String load(String path, String fileName, String key) {
        try {
            File f = new File(path + File.separator + fileName);

            Properties propIn = new Properties();

            if (!f.exists())
                return null;

            FileInputStream fis = new FileInputStream(f);
            propIn.load(fis);
            return propIn.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String load(String fileName, String key) {
        return load(Constants.SETTING_PATH, fileName, key);
    }

    public static void saveProperties(Properties properties, String name) {
        try {
            final OutputStream outputStream = new FileOutputStream(Utilities.getContentDirectory() + name + ".properties");
            properties.store(outputStream, null);
            outputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Properties loadProperties(String name) {
        final Properties props = new Properties();
        try {
            final File file = new File(Utilities.getContentDirectory() + name + ".properties");
            if (!file.exists()) file.mkdirs();
            props.load(new FileInputStream(file));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return props;
    }

}
