package com.bricks.ui_pack.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.android.ddmlib.IDevice;
import com.bricks.ui_pack.Custom_Button;
import com.bricks.Global_Observer;
import com.bricks.Main_Entry;
import com.bricks.ui_pack.Constants_UI;
import com.bricks.tools.Utils_Property;
/**
 * 
 * @author DraLastat
 */
public class Panel_Sidebar extends JPanel implements Global_Observer {

	private static final long serialVersionUID = 1L;

	private static Custom_Button buttonStatus;
	private static Custom_Button buttonElecre;
	private static Custom_Button buttonCasecre;
	private static Custom_Button buttonCaserun;
	private static Custom_Button buttonSetting;

	public Panel_Sidebar() {
		initialize();
		addButton();
		addListener();
	}

	private void initialize() {
		Dimension preferredSize = new Dimension(48, Constants_UI.MAIN_WINDOW_HEIGHT);
		this.setPreferredSize(preferredSize);
		this.setMaximumSize(preferredSize);
		this.setMinimumSize(preferredSize);
		this.setBackground(Constants_UI.TOOL_BAR_BACK_COLOR);
		this.setLayout(new GridLayout(2, 1));
	}

	private void addButton() {

		JPanel panelUp = new JPanel();
		panelUp.setBackground(Constants_UI.TOOL_BAR_BACK_COLOR);
		panelUp.setLayout(new FlowLayout(-2, -2, -4));
		JPanel panelDown = new JPanel();
		panelDown.setBackground(Constants_UI.TOOL_BAR_BACK_COLOR);
		panelDown.setLayout(new BorderLayout(0, 0));

		buttonStatus = new Custom_Button(Constants_UI.ICON_STATUS_ENABLE, Constants_UI.ICON_STATUS_ENABLE,
				Constants_UI.ICON_STATUS, Utils_Property.getProperty("bricks.ui.status.title"));
		buttonElecre = new Custom_Button(Constants_UI.ICON_DATABASE, Constants_UI.ICON_DATABASE_ENABLE,
				Constants_UI.ICON_DATABASE, Utils_Property.getProperty("bricks.ui.elecre.title"));
		buttonCasecre = new Custom_Button(Constants_UI.ICON_SCHEDULE, Constants_UI.ICON_SCHEDULE_ENABLE,
				Constants_UI.ICON_SCHEDULE, Utils_Property.getProperty("bricks.ui.casecre.title"));
		buttonCaserun = new Custom_Button(Constants_UI.ICON_BACKUP, Constants_UI.ICON_BACKUP_ENABLE,
				Constants_UI.ICON_BACKUP, Utils_Property.getProperty("bricks.ui.caserun.title"));
		buttonSetting = new Custom_Button(Constants_UI.ICON_SETTING, Constants_UI.ICON_SETTING_ENABLE,
				Constants_UI.ICON_SETTING, Utils_Property.getProperty("bricks.ui.setting.title"));

		panelUp.add(buttonStatus);
		panelUp.add(buttonElecre);
		panelUp.add(buttonCasecre);
		panelUp.add(buttonCaserun);

		panelDown.add(buttonSetting, BorderLayout.SOUTH);
		this.add(panelUp);
		this.add(panelDown);

	}

	private void addListener() {
		buttonStatus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonStatus.setIcon(Constants_UI.ICON_STATUS_ENABLE);
				buttonElecre.setIcon(Constants_UI.ICON_DATABASE);
				buttonCasecre.setIcon(Constants_UI.ICON_SCHEDULE);
				buttonCaserun.setIcon(Constants_UI.ICON_BACKUP);
				buttonSetting.setIcon(Constants_UI.ICON_SETTING);

				Main_Entry.mainPanelCenter.removeAll();
				//StatusPanel.setContent();
				Main_Entry.mainPanelCenter.add(Main_Entry.statusPanel, BorderLayout.CENTER);
				Main_Entry.elecrePanel.getSwitch_btn().setSelected(false);

				Main_Entry.mainPanelCenter.updateUI();
			}
		});

		buttonElecre.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonStatus.setIcon(Constants_UI.ICON_STATUS);
				buttonElecre.setIcon(Constants_UI.ICON_DATABASE_ENABLE);
				buttonCasecre.setIcon(Constants_UI.ICON_SCHEDULE);
				buttonCaserun.setIcon(Constants_UI.ICON_BACKUP);
				buttonSetting.setIcon(Constants_UI.ICON_SETTING);

				Main_Entry.mainPanelCenter.removeAll();
				Main_Entry.mainPanelCenter.add(Main_Entry.elecrePanel, BorderLayout.CENTER);

				Main_Entry.mainPanelCenter.updateUI();
			}
		});

		buttonCasecre.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonStatus.setIcon(Constants_UI.ICON_STATUS);
				buttonElecre.setIcon(Constants_UI.ICON_DATABASE);
				buttonCasecre.setIcon(Constants_UI.ICON_SCHEDULE_ENABLE);
				buttonCaserun.setIcon(Constants_UI.ICON_BACKUP);
				buttonSetting.setIcon(Constants_UI.ICON_SETTING);

				Main_Entry.mainPanelCenter.removeAll();
				Main_Entry.mainPanelCenter.add(Main_Entry.casecrePanel, BorderLayout.CENTER);
				Main_Entry.elecrePanel.getSwitch_btn().setSelected(false);

				Main_Entry.mainPanelCenter.updateUI();

			}
		});

		buttonCaserun.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonStatus.setIcon(Constants_UI.ICON_STATUS);
				buttonElecre.setIcon(Constants_UI.ICON_DATABASE);
				buttonCasecre.setIcon(Constants_UI.ICON_SCHEDULE);
				buttonCaserun.setIcon(Constants_UI.ICON_BACKUP_ENABLE);
				buttonSetting.setIcon(Constants_UI.ICON_SETTING);

				Main_Entry.mainPanelCenter.removeAll();
				Main_Entry.mainPanelCenter.add(Main_Entry.caserunPanel, BorderLayout.CENTER);
				Main_Entry.elecrePanel.getSwitch_btn().setSelected(false);

				Main_Entry.mainPanelCenter.updateUI();

			}
		});

		buttonSetting.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonStatus.setIcon(Constants_UI.ICON_STATUS);
				buttonElecre.setIcon(Constants_UI.ICON_DATABASE);
				buttonCasecre.setIcon(Constants_UI.ICON_SCHEDULE);
				buttonCaserun.setIcon(Constants_UI.ICON_BACKUP);
				buttonSetting.setIcon(Constants_UI.ICON_SETTING_ENABLE);

				Main_Entry.mainPanelCenter.removeAll();
				Main_Entry.mainPanelCenter.add(Main_Entry.settingPanel, BorderLayout.CENTER);
				Main_Entry.elecrePanel.getSwitch_btn().setSelected(false);

				Main_Entry.mainPanelCenter.updateUI();

			}
		});
	}

	@Override
	public void frameImageChange(BufferedImage image) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ADBChange(IDevice[] devices) {
		// TODO Auto-generated method stub
		
	}
}
