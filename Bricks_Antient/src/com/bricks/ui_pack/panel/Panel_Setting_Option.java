package com.bricks.ui_pack.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.bricks.ui_pack.Constants_UI;
import com.bricks.ui_pack.Custom_Button;
import com.bricks.tools.Utils_Property;

/**
 * 
 * @author DraLastat
 * @Description 
 */

public class Panel_Setting_Option extends JPanel {

    private static final long serialVersionUID = 1L;

    private static Custom_Button buttonSave;

    private static JTextField textField;

    private static Logger logger = Logger.getLogger(Panel_Setting_Option.class);

    public Panel_Setting_Option() {
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
        panelCenter.setLayout(new GridLayout(1, 1));

        JPanel panelGridOption = new JPanel();
        panelGridOption.setBackground(Constants_UI.MAIN_BACK_COLOR);
        panelGridOption.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 0));

        JPanel panelItem1 = new JPanel(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 0));
        JPanel panelItem2 = new JPanel(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 0));
        JPanel panelItem3 = new JPanel(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 0));
        JPanel panelItem4 = new JPanel(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 0));
        JPanel panelItem5 = new JPanel(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 0));
        JPanel panelItem6 = new JPanel(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 0));
        JPanel panelItem7 = new JPanel(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 0));

        panelItem1.setBackground(Constants_UI.MAIN_BACK_COLOR);
        panelItem2.setBackground(Constants_UI.MAIN_BACK_COLOR);
        panelItem3.setBackground(Constants_UI.MAIN_BACK_COLOR);
        panelItem4.setBackground(Constants_UI.MAIN_BACK_COLOR);
        panelItem5.setBackground(Constants_UI.MAIN_BACK_COLOR);
        panelItem6.setBackground(Constants_UI.MAIN_BACK_COLOR);
        panelItem7.setBackground(Constants_UI.MAIN_BACK_COLOR);

        panelItem1.setPreferredSize(Constants_UI.PANEL_ITEM_SIZE);
        panelItem2.setPreferredSize(Constants_UI.PANEL_ITEM_SIZE);
        panelItem3.setPreferredSize(Constants_UI.PANEL_ITEM_SIZE);
        panelItem4.setPreferredSize(Constants_UI.PANEL_ITEM_SIZE);
        panelItem5.setPreferredSize(Constants_UI.PANEL_ITEM_SIZE);
        panelItem6.setPreferredSize(Constants_UI.PANEL_ITEM_SIZE);
        panelItem7.setPreferredSize(Constants_UI.PANEL_ITEM_SIZE);

        JLabel label = new JLabel(Utils_Property.getProperty("bricks.ui.setting.JSONPath"));
        textField = new JTextField();
        label.setBackground(Constants_UI.MAIN_BACK_COLOR);
        label.setFont(Constants_UI.FONT_RADIO);
        textField.setFont(Constants_UI.FONT_RADIO);
        Dimension dm = new Dimension(334, 26);
        textField.setPreferredSize(dm);
        panelItem5.add(label);
        panelItem5.add(textField);

        panelGridOption.add(panelItem1);
        panelGridOption.add(panelItem2);
        panelGridOption.add(panelItem3);
        panelGridOption.add(panelItem4);
        panelGridOption.add(panelItem5);
        panelGridOption.add(panelItem6);
        panelGridOption.add(panelItem7);

        panelCenter.add(panelGridOption);
        return panelCenter;
    }

    private JPanel getDownPanel() {
        JPanel panelDown = new JPanel();
        panelDown.setBackground(Constants_UI.MAIN_BACK_COLOR);
        panelDown.setLayout(new FlowLayout(FlowLayout.RIGHT, Constants_UI.MAIN_H_GAP, 15));

        buttonSave = new Custom_Button(Constants_UI.ICON_SAVE, Constants_UI.ICON_SAVE_ENABLE,
                Constants_UI.ICON_SAVE_DISABLE, "");
        panelDown.add(buttonSave);

        return panelDown;
    }

}
