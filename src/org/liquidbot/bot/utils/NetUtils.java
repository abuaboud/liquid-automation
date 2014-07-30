package org.liquidbot.bot.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class NetUtils {

    /**
     *  Static Method read source code of specific url
     *
     * @param url
     * @return source page of the link
     */
    public static String[] readPage(final String url) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            final URLConnection con = createURLConnection(url);
            final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
            in.close();
        } catch (Exception e) {
            System.out.println("Error reading page!");
            e.printStackTrace();
        }
        return lines.toArray(new String[lines.size()]);
    }

    /**
     * Static method for creating a URLConnection
     *
     * @param url the complete web URL for the file
     * @return The
     */
    public static URLConnection createURLConnection(String url) {
        try {
            final URL address = new URL(url);
            final URLConnection connection = address.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Content-Type", "image/png");
            return connection;
        } catch (IOException ex) {
            System.out.println("Error creating connection!");
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Handy static method for downloading and saving files.
     *
     * @param url      the complete web URL for the file
     * @param location the complete destination including extension for the file
     * @return true if the file exists in the location, false if an exception is thrown or the file does not exist
     */
    public static boolean downloadFile(String url, String location) {
        try {

            final URLConnection connection = createURLConnection(url);

            final int contentLength = connection.getContentLength();
            final File destination = new File(location);

            if (destination.exists()) {
                final URLConnection savedFileConnection = destination.toURI().toURL().openConnection();
                if (savedFileConnection.getContentLength() == contentLength) {
                    return true;
                }
            } else {
                final File parent = destination.getParentFile();
                if (parent != null && !parent.exists()) parent.mkdirs();
            }

            final ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());

            final FileOutputStream fos = new FileOutputStream(destination);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();

        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }

        System.out.println(url + "->" + location);
        return new File(location).exists();
    }


}
