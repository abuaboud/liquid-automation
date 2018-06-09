package org.liquidbot.bot.ui.sdn;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.SkillCategory;
import org.liquidbot.bot.script.loader.ScriptInfo;
import org.liquidbot.bot.script.loader.ScriptLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created on 7/31/2014.
 */
public class SDNFrame extends JFrame {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3477461781165675L;
	private JTextField searchField;
	private ArrayList<SDNElement> elements = new ArrayList<>();

	private JComboBox<String> categories;

	private JPanel topPanel;
	private JPanel scriptPanel;
	private JScrollPane scrollPane;

	public SDNFrame() {
		super("LiquidBot SDN");
		setResizable(false);

		SkillCategory[] skillCategories = SkillCategory.values();
		final String[] categoriesList = new String[skillCategories.length + 1];
		categoriesList[0] = "All";
		for (int x = 1; x < categoriesList.length; x++) {
			categoriesList[x] = skillCategories[x - 1].getName();
		}

		searchField = new JTextField(20);
		searchField.setForeground(Color.LIGHT_GRAY);
		searchField.setText("Search");
		searchField.setMaximumSize(new Dimension(100, 30));
		searchField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				super.focusGained(e);
				searchField.setForeground(Color.BLACK);
				searchField.setText("");
				categories.setSelectedItem(categoriesList[0]);
			}

			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);
				searchField.setForeground(Color.LIGHT_GRAY);
				searchField.setText("Search");
			}
		});
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				super.keyTyped(e);
				scriptPanel.removeAll();
				int index = 0;
				for (int realIndex = 0; realIndex < elements.size(); realIndex++) {
					SDNElement element = elements.get(realIndex);
					if (element.getScriptInfo() != null) {
						if (element.getScriptInfo().getName().toLowerCase().contains(searchField.getText().toLowerCase())) {
							scriptPanel.add(element);
							reBounds(index, realIndex);
							index++;
						} else if (element.getScriptInfo().getAuthor().toLowerCase().contains(searchField.getText().toLowerCase())) {
							scriptPanel.add(element);
							reBounds(index, realIndex);
							index++;
						} else if (element.getScriptInfo().getDesc().toLowerCase().contains(searchField.getText().toLowerCase())) {
							scriptPanel.add(element);
							reBounds(index, realIndex);
							index++;
						} else if (element.getScriptInfo().getSkillCategory().getName().toLowerCase().contains(searchField.getText().toLowerCase())) {
							scriptPanel.add(element);
							reBounds(index, realIndex);
							index++;
						}
					}
				}
				scriptPanel.setPreferredSize(new Dimension(765, (150 * (index / 3))));
				SwingUtilities.updateComponentTreeUI(scriptPanel);
			}
		});

		categories = new JComboBox<>(categoriesList);
		categories.setSelectedItem(categoriesList[0]);
		categories.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent evt) {
				scriptPanel.removeAll();
				final String selected = (String) categories.getSelectedItem();
				int index = 0;
				for (int realIndex = 0; realIndex < elements.size(); realIndex++) {
					SDNElement element = elements.get(realIndex);
					if (selected.equalsIgnoreCase("All") || element.getScriptInfo().getSkillCategory().getName().equalsIgnoreCase(selected)) {
						scriptPanel.add(element);
						reBounds(index, realIndex);
						index++;
					}
				}
				scriptPanel.setPreferredSize(new Dimension(765, (150 * (index / 3))));
				SwingUtilities.updateComponentTreeUI(getContentPane());
			}
		});

		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(categories);
		topPanel.add(Box.createHorizontalGlue());
		topPanel.add(searchField);

		scriptPanel = new JPanel();
		scriptPanel.setLayout(null);

		scrollPane = new JScrollPane(scriptPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);


		scriptPanel.setPreferredSize(new Dimension(765, (150 * (elements.size() / 3))));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(770, 508);
	}


	private void reBounds(int index, int realIndex) {
		final int width = 250;
		final int height = 150;
		final int spacing = 3;
		final int scriptPerRow = 3;
		SDNElement element = elements.get(realIndex);
		int col = index / scriptPerRow;
		int row = index - (col * scriptPerRow);
		int x = row * width + spacing;
		int y = col * height + spacing;
		element.setBounds(x, y, width, height);
	}

	public void loadScripts() {
		scriptPanel.removeAll();
		elements.clear();
		if (Configuration.getInstance().getUser() != null) {
			for (ScriptInfo scriptInfo : ScriptLoader.getRepositoryScripts()) {
				elements.add(new SDNElement(scriptInfo));
			}
		}
		for (int index = 0; index < elements.size(); index++) {
			reBounds(index, index);
			scriptPanel.add(elements.get(index));
		}
		scriptPanel.setPreferredSize(new Dimension(765, (150 * (elements.size() / 3))));
	}

}