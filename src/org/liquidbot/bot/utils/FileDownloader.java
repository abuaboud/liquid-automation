package org.liquidbot.bot.utils;

import java.io.*;
import java.net.URLConnection;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class FileDownloader implements Runnable {

    private final String source, destination;
    private int percentage = 0;
    private int length, written;

    public FileDownloader(String source, String destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public void run() {
        OutputStream output;
        InputStream input;
        URLConnection connection;

        try {
            connection = NetUtils.createURLConnection(source);
            length = connection.getContentLength();
            final File dest = new File(destination);

            if (dest.exists()) {
                final URLConnection savedFileConnection = dest.toURI().toURL().openConnection();
                if (savedFileConnection.getContentLength() == length) {
                    return;
                }
            } else {
                final File parent = dest.getParentFile();
                if (!parent.exists()) parent.mkdirs();
            }

            output = new FileOutputStream(dest);
            input = connection.getInputStream();

            final byte[] data = new byte[1024];
            int read;

            while ((read = input.read(data)) != -1) {
                output.write(data, 0, read);
                written += read;
                percentage = (int) (((double) written / (double) length) * 100D);
            }

            output.flush();
            output.close();
            input.close();

        } catch (IOException a) {
            System.out.println("Error downloading file!");
            a.printStackTrace();
        }
    }

    public boolean isFinished() {
        return written == 0 || length == written;
    }

    public int getPercentage() {
        return percentage;
    }
}
