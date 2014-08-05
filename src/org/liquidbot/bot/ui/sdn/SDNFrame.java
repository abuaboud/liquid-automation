package org.liquidbot.bot.ui.sdn;

import org.liquidbot.bot.script.SkillCategory;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by Kenneth on 7/31/2014.
 */
public class SDNFrame extends JFrame {


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
                    if (element.getName().toLowerCase().contains(searchField.getText().toLowerCase())) {
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


        for (String str : scripts) {
            elements.add(new SDNElement(str));
        }

        for (int index = 0; index < elements.size(); index++) {
            reBounds(index, index);
            scriptPanel.add(elements.get(index));
        }

        scriptPanel.setPreferredSize(new Dimension(765, (150 * (elements.size() / 3))));
        scriptPanel.setBorder(new EtchedBorder());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getOwner());
        setSize(770, 508);
    }

    public String[] scripts = {
            "7~63~Walker~Get from A to B easily~0~1~Lemons~0~0~2014-07-29 16:00:38~2014-07-31 06:13:40~0~v2of9ppqdc.jar~0",
            "6~63~Chickens~Kills chickens in 3 different locations~2~1~Lemons~0~0~2014-07-29 05:20:41~2014-07-29 15:59:55~0~2tncmsur03.jar~0",
            "5~63~Walker_TestGrounds~Walker~0~1~Lemons~0~0~2014-07-29 05:11:04~2014-07-29 05:11:04~0~u71wrsemr.jar~1",
            "4~12~Darkcore_Mankiller~Kills men in edgeville~16~1~Isolate~0~0~2014-07-25 09:44:06~2014-07-25 09:44:06~0~xtyppbrjy4.jar~0",
            "3~5~DarkCoreCombat~Fights anything~2~1~Calle~0~0~2014-07-23 16:26:09~2014-07-23 16:26:09~0~l3on3cvv75.jar~0",
            "2~2~LiquidFisher~Advanced Fishing Script~8~1~Magorium~0~0~2014-07-23 05:12:36~2014-07-29 11:13:16~1~i05a85bal5.jar~0",
            "1~2~LiquidChopper~Chop Oak Tree at West Varrock~23~1~Magorium~0~0~2014-07-23 05:12:30~2014-07-24 03:30:14~0~ey4e08u5cf.jar~0",
            "7~63~Walker~Get from A to B easily~0~1~Lemons~0~0~2014-07-29 16:00:38~2014-07-31 06:13:40~0~v2of9ppqdc.jar~0",
            "6~63~Chickens~Kills chickens in 3 different locations~2~1~Lemons~0~0~2014-07-29 05:20:41~2014-07-29 15:59:55~0~2tncmsur03.jar~0",
            "5~63~Walker_TestGrounds~Walker~0~1~Lemons~0~0~2014-07-29 05:11:04~2014-07-29 05:11:04~0~u71wrsemr.jar~1",
            "4~12~Darkcore_Mankiller~Kills men in edgeville~16~1~Isolate~0~0~2014-07-25 09:44:06~2014-07-25 09:44:06~0~xtyppbrjy4.jar~0",
            "3~5~DarkCoreCombat~Fights anything~2~1~Calle~0~0~2014-07-23 16:26:09~2014-07-23 16:26:09~0~l3on3cvv75.jar~0",
            "2~2~LiquidFisher~Advanced Fishing Script~8~1~Magorium~0~0~2014-07-23 05:12:36~2014-07-29 11:13:16~1~i05a85bal5.jar~0",
            "1~2~LiquidChopper~Chop Oak Tree at West Varrock~23~1~Magorium~0~0~2014-07-23 05:12:30~2014-07-24 03:30:14~0~ey4e08u5cf.jar~0",
            "7~63~Walker~Get from A to B easily~0~1~Lemons~0~0~2014-07-29 16:00:38~2014-07-31 06:13:40~0~v2of9ppqdc.jar~0",
            "6~63~Chickens~Kills chickens in 3 different locations~2~1~Lemons~0~0~2014-07-29 05:20:41~2014-07-29 15:59:55~0~2tncmsur03.jar~0",
            "5~63~Walker_TestGrounds~Walker~0~1~Lemons~0~0~2014-07-29 05:11:04~2014-07-29 05:11:04~0~u71wrsemr.jar~1",
            "4~12~Darkcore_Mankiller~Kills men in edgeville~16~1~Isolate~0~0~2014-07-25 09:44:06~2014-07-25 09:44:06~0~xtyppbrjy4.jar~0",
            "3~5~DarkCoreCombat~Fights anything~2~1~Calle~0~0~2014-07-23 16:26:09~2014-07-23 16:26:09~0~l3on3cvv75.jar~0",
            "2~2~LiquidFisher~Advanced Fishing Script~8~1~Magorium~0~0~2014-07-23 05:12:36~2014-07-29 11:13:16~1~i05a85bal5.jar~0",
            "1~2~LiquidChopper~Chop Oak Tree at West Varrock~23~1~Magorium~0~0~2014-07-23 05:12:30~2014-07-24 03:30:14~0~ey4e08u5cf.jar~0"
    };

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

}
