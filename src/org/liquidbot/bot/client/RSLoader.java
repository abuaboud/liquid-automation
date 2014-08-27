package org.liquidbot.bot.client;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.Constants;
import org.liquidbot.bot.client.input.InternalKeyboard;
import org.liquidbot.bot.client.input.InternalMouse;
import org.liquidbot.bot.client.parser.HookReader;
import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.utils.FileDownloader;
import org.liquidbot.bot.utils.Utilities;
import org.liquidbot.component.Canvas;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Kenneth on 7/29/2014.
 */
public class RSLoader extends JPanel implements AppletStub {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8902296630509146560L;
	private boolean isAppletLoaded = false;
	private final Font font = new Font("Calibri", Font.PLAIN, 15);


	private RsJarLoader classLoader = null;
	private FileDownloader downloader;
	private Applet applet;
	private final Parameters params;

	public RSLoader(final Configuration configuration) {
		this.setLayout(new BorderLayout());
		params = new Parameters(Constants.WORLDS[Random.nextInt(0, Constants.WORLDS.length)] - 300);
		setPreferredSize(new Dimension(Constants.APPLET_WIDTH, Constants.APPLET_HEIGHT));

		if (HookReader.VERSION <= Constants.CLIENT_VERSION) {
			final Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {

					downloader = new FileDownloader(params.get("codebase") + params.get("initial_jar"),
							Utilities.getContentDirectory() + "game/os-gamepack.jar");
					downloader.run();

					final File jar = new File(Utilities.getContentDirectory() + "game/os-gamepack.jar");

					try {
						classLoader = new RsJarLoader(jar.toURI().toURL());
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
					applet.setPreferredSize(new Dimension(763, 504));
					applet.init();
					applet.start();
					isAppletLoaded = true;
					RSLoader.this.add(applet, BorderLayout.CENTER);
					RSLoader.this.revalidate();

					while (applet.getComponents().length == 0) {
						Utilities.sleep(1000, 1500);
					}

					Reflection.init();
					while (Game.getGameState() < 10) {
						Utilities.sleep(500, 1000);
					}
					Utilities.sleep(2000, 3000);
					final Canvas canvas = new Canvas((java.awt.Canvas) Reflection.value("Client#getCanvas()", null));
					configuration.setCanvas(canvas);
					canvas.set();

					configuration.setKeyboard(new InternalKeyboard());
					configuration.setMouse(new InternalMouse());

				}
			});
			thread.start();
		}
	}

	@Override
	public void paintComponent(Graphics graphics) {
		if (!isAppletLoaded) {
			final Graphics2D graphics2D = (Graphics2D) graphics;
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			graphics2D.setColor(Color.BLACK);
			graphics2D.fillRect(0, 0, getWidth(), getHeight());
			graphics2D.setFont(font);

			if (HookReader.VERSION > Constants.CLIENT_VERSION) {
				graphics.setColor(Color.GREEN.darker());
				graphics2D.drawString("There is Newer Version for Bot , Please download it from www.Liquidbot.org", 150, 280);
			} else {
				if (downloader != null || downloader.isFinished()) {
					final int width = downloader.getPercentage() * 300 / 100;

					graphics.setColor(Color.GREEN.darker());
					graphics2D.fillRect(225, 200, width, 45);
					graphics.setColor(Color.GREEN.darker());
					graphics2D.drawRect(225, 200, 300, 45);

					graphics2D.setColor(Color.WHITE);
					graphics2D.drawString("Downloading gamepack - " + downloader.getPercentage() + "%", 285, 230);
				}
				graphics.setColor(Color.GREEN.darker());
				graphics2D.drawString("LiquidBot is loading, please wait!", 285, 480);
			}
			repaint(600);
		}
	}


	public Class<?> loadClass(final String className) {
		if (classLoader == null) {
			System.out.println("Error Null Class Loader");
			return null;
		}
		if(!classLoader.classes().containsKey(className)){
			try {
				return classLoader.loadClass(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return classLoader.classes().get(className);
	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	public URL getDocumentBase() {
		return getCodeBase();
	}

	@Override
	public URL getCodeBase() {
		try {
			return new URL(params.get("codebase"));
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
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
	@Override
	public void appletResize(int width, int height) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
