package com.bricks.ui_pack.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import com.bricks.ui_pack.Constants_UI;
import com.bricks.tools.Utils_Property;

/**
 * 
 * @author DraLastat
 * @Description 
 */

public class Panel_Status extends JPanel{
	
	private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(Panel_Status.class);
  
    public Panel_Status() {
        super(true);
        initialize();
        addComponent();
    }

    private void initialize() {
        this.setBackground(Constants_UI.MAIN_BACK_COLOR);
        this.setLayout(new BorderLayout());
    }

    private void addComponent() {
        this.add(getUpPanel(), BorderLayout.NORTH);
        this.add(getDownPanel(), BorderLayout.SOUTH);

    }
    
    private JPanel getUpPanel() {
        JPanel panelUp = new JPanel();
        panelUp.setBackground(Constants_UI.MAIN_BACK_COLOR);
        panelUp.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 5));

        JLabel labelTitle = new JLabel(Utils_Property.getProperty("bricks.ui.status.name"));
        labelTitle.setFont(Constants_UI.FONT_TITLE);
        labelTitle.setForeground(Constants_UI.TOOL_BAR_BACK_COLOR);
        panelUp.add(labelTitle);

        return panelUp;
    }

	private JPanel getDownPanel() {
		JPanel panelDown = new JPanel();
		panelDown.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelDown.setLayout(new FlowLayout(FlowLayout.RIGHT, Constants_UI.MAIN_H_GAP, 15));

		JLabel labelInfo = new JLabel(Utils_Property.getProperty("bricks.ui.app.info"));
		labelInfo.setFont(Constants_UI.FONT_NORMAL);
		labelInfo.setForeground(Color.gray);

		panelDown.add(labelInfo);

		return panelDown;
	}


}
