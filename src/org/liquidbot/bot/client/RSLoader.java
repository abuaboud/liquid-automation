package org.liquidbot.bot.client;

import org.liquidbot.bot.Constants;
import org.liquidbot.bot.utils.FileDownloader;
import org.liquidbot.bot.utils.NetUtils;
import org.liquidbot.bot.utils.Utilities;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class RSLoader extends JPanel implements AppletStub {

    private boolean isAppletLoaded = false;
    private final Font font = new Font("Calibri", Font.PLAIN, 15);
    private final Color color = new Color(99, 223, 245);
    private FileDownloader downloader;
    private Applet applet;
    private final Parameters params;

    public RSLoader() {
        this.setLayout(new BorderLayout());
        params = new Parameters(1);
        setPreferredSize(new Dimension(Constants.APPLET_WIDTH, Constants.APPLET_HEIGHT));

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                downloader = new FileDownloader(params.get("codebase") + params.get("initial_jar"),
                        Utilities.getContentDirectory() + "game/os-gamepack.jar");
                downloader.run();

                final File jar = new File(Utilities.getContentDirectory() + "game/os-gamepack.jar");

                URLClassLoader classLoader = null;
                try {
                    classLoader = new URLClassLoader(new URL[]{jar.toURI().toURL()});
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                final String mainClass = params.get("initial_class").replaceAll(".class", "");
                try {
                    applet = (Applet) classLoader.loadClass(mainClass).newInstance();
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException a) {
                    a.printStackTrace();
                }

                applet.setStub(RSLoader.this);
                applet.init();
                applet.start();
                isAppletLoaded = true;
                RSLoader.this.add(applet, BorderLayout.CENTER);
                RSLoader.this.revalidate();
            }
        });
        thread.start();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        if(!isAppletLoaded) {
            final Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(0, 0, getWidth(), getHeight());
            graphics2D.setFont(font);

            if(downloader != null && !downloader.isFinished()) {
                final int width = downloader.getPercentage() * 300 / 100;

                graphics2D.setColor(Color.GRAY);
                graphics2D.fillRect(225, 200, width, 45);
                graphics2D.setColor(Color.WHITE);
                graphics2D.drawRect(225, 200, 300, 45);

                graphics2D.setColor(Color.CYAN);
                graphics2D.drawString("Downloading gamepack - " + downloader.getPercentage() + "%", 285, 230);
            }
            graphics2D.drawString("LiquidBot is loading, please wait!", 285, 480);
            repaint(600);
        }
    }

    public Applet getApplet() {
        return applet;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public URL getDocumentBase() {
        try {
            final URL documentBase = new URL(params.get("codebase"));
            return documentBase;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public URL getCodeBase() {
        try {
            final URL documentBase = new URL(params.get("codebase"));
            return documentBase;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getParameter(String name) {
        return params.get(name);
    }

    @Override
    public AppletContext getAppletContext() {
        return null;
    }

    @Override
    public void appletResize(int width, int height) {
    }
}
