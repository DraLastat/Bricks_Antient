package com.bricks.ui_pack.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.bricks.ui_pack.Constants_UI;
import com.bricks.ui_pack.Custom_Button;
import com.bricks.tools.Utils_Property;

/**
 * 
 * @author DraLastat
 * @Description 
 */

public class Panel_Setting_About extends JPanel {

	private static final long serialVersionUID = 1L;

	public Panel_Setting_About() {
		initialize();
		addComponent();
	}

	private void initialize() {
		this.setBackground(Constants_UI.MAIN_BACK_COLOR);
		this.setLayout(new BorderLayout());
	}

	private void addComponent() {

		this.add(getCenterPanel(), BorderLayout.CENTER);
		this.add(getDownPanel(), BorderLayout.SOUTH);

	}

	private JPanel getCenterPanel() {
		JPanel panelCenter = new JPanel();
		panelCenter.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelCenter.setLayout(new GridLayout(3, 1));

		JPanel panelGridIcon = new JPanel();
		panelGridIcon.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelGridIcon.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 0));

		Custom_Button icon = new Custom_Button(Constants_UI.ICON_DATA_SYNC, Constants_UI.ICON_DATA_SYNC,
				Constants_UI.ICON_DATA_SYNC, "");
		JLabel labelName = new JLabel(Constants_UI.APP_NAME);
		JLabel labelVersion = new JLabel(Constants_UI.APP_VERSION);

		labelName.setFont(Constants_UI.FONT_NORMAL);
		labelVersion.setFont(Constants_UI.FONT_NORMAL);

		Dimension size = new Dimension(200, 30);
		labelName.setPreferredSize(size);
		labelVersion.setPreferredSize(size);

		panelGridIcon.add(icon);
		panelGridIcon.add(labelName);
		panelGridIcon.add(labelVersion);

		JPanel panelGridHelp = new JPanel();
		panelGridHelp.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelGridHelp.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 0));

		JLabel labelAdvice = new JLabel(Utils_Property.getProperty("ds.ui.app.advice"));
		JLabel labelHelp = new JLabel(Utils_Property.getProperty("ds.ui.app.help"));

		labelAdvice.setFont(Constants_UI.FONT_NORMAL);
		labelHelp.setFont(Constants_UI.FONT_NORMAL);

		labelAdvice.setPreferredSize(Constants_UI.LABLE_SIZE);
		labelHelp.setPreferredSize(Constants_UI.LABLE_SIZE);

		panelGridHelp.add(labelAdvice);
		panelGridHelp.add(labelHelp);

		panelCenter.add(panelGridIcon);
		// panelCenter.add(panelGridHelp);
		return panelCenter;
	}

	private JPanel getDownPanel() {
		JPanel panelDown = new JPanel();
		panelDown.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelDown.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 15));

		JLabel labelInfo = new JLabel(Utils_Property.getProperty("ds.ui.app.info"));
		labelInfo.setFont(Constants_UI.FONT_NORMAL);
		labelInfo.setForeground(Color.gray);

		panelDown.add(labelInfo);

		return panelDown;
	}

}
