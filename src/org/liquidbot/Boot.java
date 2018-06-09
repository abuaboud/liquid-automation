package org.liquidbot;

import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;
import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.client.parser.HookReader;
import org.liquidbot.bot.client.security.LSecurityManager;
import org.liquidbot.bot.script.api.interfaces.Condition;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.ui.BotConsole;
import org.liquidbot.bot.ui.BotFrame;
import org.liquidbot.bot.ui.login.IPBLogin;
import org.liquidbot.bot.utils.Logger;
import org.liquidbot.bot.utils.Utilities;

import javax.swing.*;
import java.text.ParseException;

/**
 * Created on 7/29/2014.
 */
public class Boot {

	private final Logger log = new Logger(Boot.class);
	private final Configuration config = Configuration.getInstance();

	public Boot() {
		System.setSecurityManager(new LSecurityManager());
		config.setConsole(new BotConsole());
		log.info("Parsing hooks..");
		HookReader.init();


		final IPBLogin login = new IPBLogin();
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (login.rememberMe()) {
					login.dispose();
				} else {
					login.setVisible(true);
				}
			}
		}).start();

		Time.sleep(new Condition() {
			@Override
			public boolean active() {
				return !login.isVisible();
			}
		}, 2000); // to help people with slower computers.

		while (login.isVisible()) {
			Utilities.sleep(200, 300);
		}

		log.info("Launching client..");
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				config.setBotFrame(new BotFrame());
				config.getBotFrame().setVisible(true);
			}
		});
	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(new SyntheticaAluOxideLookAndFeel());
		} catch (UnsupportedLookAndFeelException | ParseException e) {
			e.printStackTrace();
		}

		JPopupMenu.setDefaultLightWeightPopupEnabled(true);

		new Boot();

	}

}