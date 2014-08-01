package org.liquidbot.bot.ui.sdn;

import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;
import org.liquidbot.bot.script.SkillCategory;
import org.liquidbot.bot.ui.swing.VerticalFlowLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Kenneth on 7/31/2014.
 */
public class SDNFrame extends JFrame {

    private JTextField searchField;
    private ArrayList<SDNElement> elements = new ArrayList<>();
    private JList<SkillCategory> categorys;

    private JPanel topPanel;
    private JPanel scriptPanel;
    private JScrollPane scrollPane;

    public SDNFrame() {
        super("LiquidBot SDN");
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
                for(SDNElement element : elements) {
                    if(element.getName().toLowerCase().contains(searchField.getText().toLowerCase())) {
                        scriptPanel.add(element);
                    }
                    if(element.getAuthor().toLowerCase().contains(searchField.getText().toLowerCase())) {
                        scriptPanel.add(element);
                    }
                    if(element.getDesc().toLowerCase().contains(searchField.getText().toLowerCase())) {
                        scriptPanel.add(element);
                    }
                    if(element.getSkillCategory().getName().toLowerCase().contains(searchField.getText().toLowerCase())) {
                        scriptPanel.add(element);
                    }
                }
                SwingUtilities.updateComponentTreeUI(scriptPanel);
            }
        });

        categorys = new JList<>(SkillCategory.values());
        categorys.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                scriptPanel.removeAll();

                final SkillCategory cat = categorys.getSelectedValue();
                for(SDNElement element : elements) {
                    if(element.getSkillCategory() == cat) {
                        scriptPanel.add(element);
                    }
                }

                SwingUtilities.updateComponentTreeUI(getContentPane());
            }
        });

        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(searchField);

        scriptPanel = new JPanel();
        final VerticalFlowLayout layout = new VerticalFlowLayout();
        layout.setHGap(4); layout.setVGap(4);
        scriptPanel.setLayout(layout);
        scriptPanel.setPreferredSize(new Dimension(500, 300));
        scrollPane = new JScrollPane(scriptPanel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(categorys, BorderLayout.WEST);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().setPreferredSize(new Dimension(680, 490));

        for(String str : scripts) {
            elements.add(new SDNElement(str));
        }
        for(SDNElement element : elements) {
            scriptPanel.add(element);
        }
        scriptPanel.setBorder(new EtchedBorder());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    public String[] scripts = {
            "7~63~Walker~Get from A to B easily~0~1~Lemons~0~0~2014-07-29 16:00:38~2014-07-31 06:13:40~0~v2of9ppqdc.jar~0",
            "6~63~Chickens~Kills chickens in 3 different locations~2~1~Lemons~0~0~2014-07-29 05:20:41~2014-07-29 15:59:55~0~2tncmsur03.jar~0",
            "5~63~Walker_TestGrounds~Walker~0~1~Lemons~0~0~2014-07-29 05:11:04~2014-07-29 05:11:04~0~u71wrsemr.jar~1",
            "4~12~Darkcore_Mankiller~Kills men in edgeville~16~1~Isolate~0~0~2014-07-25 09:44:06~2014-07-25 09:44:06~0~xtyppbrjy4.jar~0",
            "3~5~DarkCoreCombat~Fights anything~2~1~Calle~0~0~2014-07-23 16:26:09~2014-07-23 16:26:09~0~l3on3cvv75.jar~0",
            "2~2~LiquidFisher~Advanced Fishing Script~8~1~Magorium~0~0~2014-07-23 05:12:36~2014-07-29 11:13:16~1~i05a85bal5.jar~0",
            "1~2~LiquidChopper~Chop Oak Tree at West Varrock~23~1~Magorium~0~0~2014-07-23 05:12:30~2014-07-24 03:30:14~0~ey4e08u5cf.jar~0"
    };

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new SyntheticaAluOxideLookAndFeel());
        } catch (UnsupportedLookAndFeelException | ParseException e) {
            e.printStackTrace();
        }

        final SDNFrame frame = new SDNFrame();
        frame.setVisible(true);
    }

}
