package com.bricks.ui_pack.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.bricks.Main_Entry;
import com.bricks.ui_pack.Constants_UI;
import com.bricks.tools.Utils_Property;

/**
 * 
 * @author DraLastat
 * @Description 
 */

public class Panel_Setting_Main extends JPanel {

	private static final long serialVersionUID = 1L;

	public static JPanel settingPanelMain;
	private static JPanel panelOption;
	private static JPanel panelAbout;
	private static JPanel settingPanelOption;
	private static JPanel settingPanelAbout;

	public Panel_Setting_Main() {
		initialize();
		addComponent();
		addListener();
	}

	private void initialize() {
		this.setBackground(Constants_UI.MAIN_BACK_COLOR);
		this.setLayout(new BorderLayout());
		settingPanelOption = new Panel_Setting_Option();
		settingPanelAbout = new Panel_Setting_About();
	}

	private void addComponent() {

		this.add(getUpPanel(), BorderLayout.NORTH);
		this.add(getCenterPanel(), BorderLayout.CENTER);

	}

	private JPanel getUpPanel() {
		JPanel panelUp = new JPanel();
		panelUp.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelUp.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 5));

		JLabel labelTitle = new JLabel(Utils_Property.getProperty("bricks.ui.setting.title"));
		labelTitle.setFont(Constants_UI.FONT_TITLE);
		labelTitle.setForeground(Constants_UI.TOOL_BAR_BACK_COLOR);
		panelUp.add(labelTitle);

		return panelUp;
	}

	private JPanel getCenterPanel() {
		JPanel panelCenter = new JPanel();
		panelCenter.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelCenter.setLayout(new BorderLayout());

		JPanel panelList = new JPanel();
		Dimension preferredSize = new Dimension(245, Constants_UI.MAIN_WINDOW_HEIGHT);
		panelList.setPreferredSize(preferredSize);
		panelList.setBackground(new Color(62, 62, 62));
		panelList.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		panelOption = new JPanel();
		panelOption.setBackground(new Color(120, 165, 202));
		panelOption.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 13));
		Dimension preferredSizeListItem = new Dimension(245, 48);
		panelOption.setPreferredSize(preferredSizeListItem);
		panelAbout = new JPanel();
		panelAbout.setBackground(Constants_UI.TOOL_BAR_BACK_COLOR);
		panelAbout.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 13));
		panelAbout.setPreferredSize(preferredSizeListItem);

		JLabel labelOption = new JLabel(Utils_Property.getProperty("bricks.ui.setting.option"));
		JLabel labelAbout = new JLabel(Utils_Property.getProperty("bricks.ui.setting.about"));
		Font fontListItem = new Font(Utils_Property.getProperty("ds.ui.font.family"), 0, 15);
		labelOption.setFont(fontListItem);
		labelAbout.setFont(fontListItem);
		labelOption.setForeground(Color.white);
		labelAbout.setForeground(Color.white);
		panelOption.add(labelOption);
		panelAbout.add(labelAbout);

		panelList.add(panelOption);
		panelList.add(panelAbout);

		settingPanelMain = new JPanel();
		settingPanelMain.setBackground(Constants_UI.MAIN_BACK_COLOR);
		settingPanelMain.setLayout(new BorderLayout());
		settingPanelMain.add(settingPanelOption);

		panelCenter.add(panelList, BorderLayout.WEST);
		panelCenter.add(settingPanelMain, BorderLayout.CENTER);

		return panelCenter;
	}

	private void addListener() {
		panelOption.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				panelOption.setBackground(new Color(120, 165, 202));
				panelAbout.setBackground(Constants_UI.TOOL_BAR_BACK_COLOR);

				Panel_Setting_Main.settingPanelMain.removeAll();
				//SettingPanelOption.setCurrentOption();
				Panel_Setting_Main.settingPanelMain.add(settingPanelOption);
				Main_Entry.settingPanel.updateUI();

			}
		});

		panelAbout.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				panelAbout.setBackground(new Color(120, 165, 202));
				panelOption.setBackground(Constants_UI.TOOL_BAR_BACK_COLOR);

				Panel_Setting_Main.settingPanelMain.removeAll();
				Panel_Setting_Main.settingPanelMain.add(settingPanelAbout);
				Main_Entry.settingPanel.updateUI();

			}
		});

	}
}
