package org.liquidbot.bot.ui;

import org.liquidbot.bot.Constants;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created on 7/31/2014.
 */
public class BotConsole extends JPanel {

	private final Font font = new Font("Calibri", Font.PLAIN, 13);
	private final Color background = new Color(43, 43, 43);
	private final Color foreground = new Color(168, 182, 197);
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss z");

	private boolean displaying = false;

	private final JScrollPane scrollPane;
	private final JTextPane textPane;
	private final StyledDocument doc;
	private final Style style;

	public BotConsole() {
		textPane = new JTextPane();
		textPane.setEditable(false);
		scrollPane = new JScrollPane(textPane);
		scrollPane.setOpaque(false);
		doc = textPane.getStyledDocument();
		style = textPane.addStyle("Style", null);

		textPane.setForeground(foreground);
		textPane.setBackground(background);
		textPane.setFont(font);

		setPreferredSize(new Dimension(Constants.APPLET_WIDTH, 125));
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}

	public void append(String str, Color color) {
		try {
			String[] count = textPane.getText().split("\n");
			if (count.length > 300)
				textPane.setText("");

			StyleConstants.setForeground(style, foreground);
			doc.insertString(doc.getLength(), "[" + DATE_FORMAT.format(Calendar.getInstance().getTime()) + "] ", style);
			StyleConstants.setForeground(style, color != null ? color : foreground);
			doc.insertString(doc.getLength(), str + "\n", style);
			textPane.setCaretPosition(doc.getLength());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void append(String str) {
		append(str, foreground);
	}

	public boolean isDisplaying() {
		return displaying;
	}

	public void display(boolean visible) {
		displaying = visible;
	}


}
