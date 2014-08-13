package org.liquidbot.bot.ui;

import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.wrappers.Widget;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;
import org.liquidbot.component.debug.WidgetDebugger;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Hiasat on 8/13/14.
 */
public class WidgetViewer extends JFrame implements TreeSelectionListener,ActionListener {

	private JScrollPane scrollPane;
	private JList<String> list;
	private JTree tree;
	private JButton refresh;

	public WidgetViewer() {
		setTitle("Widget Explorer");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setLayout(null);

		tree = new JTree();
		tree.addTreeSelectionListener(this);
		tree.setModel(widgetTree(null));
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		refresh = new JButton();
		refresh.setText("Refresh");
		refresh.addActionListener(this);
		refresh.setBounds(325, 5, 110,25);
		add(refresh);

		scrollPane = new JScrollPane(tree);
		scrollPane.setBounds(5, 42, 210, 505);

		list = new JList<>();
		list.setBounds(215, 45, 225, 500);

		add(list);
		add(scrollPane);

		pack();
		setSize(450, 575);
		setLocationRelativeTo(getOwner());
		setVisible(true);
	}

	private DefaultTreeModel widgetTree(String search) {
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("Widgets");
		for (Widget widget : Widgets.get()) {
			WidgetChild[] widgetChildren = widget.getChildren();
			if (widget.isValid() && widgetChildren != null && widgetChildren.length > 0) {
				DefaultMutableTreeNode childrenNode = new DefaultMutableTreeNode("Widget-" + widget.getIndex());
				for (WidgetChild widgetChild : widgetChildren) {
					if (widgetChild.isVisible()) {
						if (search == null || search.length() == 0 || (widgetChild.getText() != null || widgetChild.getText().toLowerCase().contains(search.toLowerCase()))) {
							DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode("WidgetChild-" + widgetChild.getIndex());
							childrenNode.add(defaultMutableTreeNode);
							WidgetChild[] childChildren = widgetChild.getChildren();
							if (childChildren != null && childChildren.length > 0) {
								for (WidgetChild child : childChildren) {
									if (child.isVisible()) {
										defaultMutableTreeNode.add(new DefaultMutableTreeNode("Child-" + child.getIndex()));
									}
								}
							}
						}
					}
				}
				treeNode.add(childrenNode);
			}
		}
		return new DefaultTreeModel(treeNode);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if (tree.getSelectionModel() == null || tree.getSelectionModel().getSelectionPath() == null)
			return;
		Object[] o = tree.getSelectionModel().getSelectionPath().getPath();
		int w[] = new int[o.length - 1];

		if (o.length > 2) {
			for (int i = 0; i < o.length; i++) {
				if (i != 0) {
					w[i - 1] = Integer.parseInt(o[i].toString().split("-")[1]);
				}
			}
			WidgetChild widget = w.length == 2 ? Widgets.get(w[0], w[1]) : Widgets.get(w[0], w[1]).getChild(w[2]);
		    DefaultListModel<String> listModel = new DefaultListModel<>();

			listModel.addElement("Parent: " + w[0]);
			listModel.addElement("Child: " + (widget.getId() & 0xFFFF));
			listModel.addElement("Child ID: " + widget.getId());
			listModel.addElement("Parent ID: " + widget.getParentId());

			listModel.addElement("Relative X: " + widget.getRelativeX());
			listModel.addElement("Relative Y: " + widget.getRelativeY());
			listModel.addElement("Scroll X: " + widget.getScrollX());
			listModel.addElement("Scroll Y: " + widget.getScrollY());
			listModel.addElement("Absolute X: " + widget.getLocation().getX());
			listModel.addElement("Absolute Y: " + widget.getLocation().getY());
			listModel.addElement("isVisible: " + widget.isVisible());
			listModel.addElement("Width: " + widget.getWidth());
			listModel.addElement("Height: " + widget.getHeight());
			listModel.addElement("Model ID: " + widget.getModelId());
			listModel.addElement("Item Id: " + widget.getItemId());
			listModel.addElement("Item Stack: " + widget.getItemStack());
			listModel.addElement("SlotContentIds: " + toInt(widget));
			listModel.addElement("Texture Id: " + widget.getTextureId());
			listModel.addElement("Actions: " + getActions(widget));
			listModel.addElement("BorderThickness: " + widget.getBorderThickness());
			listModel.addElement("Text: " + widget.getText());
			listModel.addElement("Text Color: " + widget.getTextColor());
			listModel.addElement("Name: " + widget.getName());

			WidgetDebugger.x = widget.getX();
			WidgetDebugger.y = widget.getY();
			WidgetDebugger.height = widget.getHeight();
			WidgetDebugger.width = widget.getWidth();

			list.setModel(listModel);
		}
	}

	private String toInt(WidgetChild widget) {
		if (widget == null || widget.getSlotContentIds() == null)
			return "";
		int[] slots = widget.getSlotContentIds();
		String s = "";
		for (int i : slots) {
			s = s + (s.length() == 0 ? "" : ",") + i;
		}
		return s;
	}

	private String getActions(WidgetChild widget) {
		if (widget == null || widget.getActions() == null)
			return "";
		String[] slots = widget.getActions();
		String s = "";
		for (String i : slots) {
			s = s + (s.length() == 0 ? "" : ",") + i;
		}
		return s;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == refresh){
			tree.setModel(widgetTree(null));
		}
	}
}
