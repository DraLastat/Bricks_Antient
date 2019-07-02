package com.bricks.ui_pack;

import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * 
 * @author DraLastat
 * @Description 
 */

public class Custom_Button extends JButton {

	private static final long serialVersionUID = 1L;
	private ImageIcon iconEnable, iconDisable;
	private String tip;

	/**
	 * 
	 * 
	 * @param iconNormal
	 *           
	 * @param iconEnable
	 *           
	 * @param iconDisable
	 *            
	 * @param tip
	 *            
	 */
	public Custom_Button(ImageIcon iconNormal, ImageIcon iconEnable, ImageIcon iconDisable, String tip) {
		super(iconNormal);

		this.iconEnable = iconEnable;
		this.iconDisable = iconDisable;
		this.tip = tip;

		initialize();
		setUp();
	}

	/**
	 * 
	 */
	private void initialize() {
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
		this.setFocusable(true);
		this.setMargin(new Insets(0, 0, 0, 0));
	}

	/**
	 * 
	 */
	private void setUp() {
		this.setRolloverIcon(iconEnable);
		//this.setSelectedIcon(iconEnable);
		this.setPressedIcon(iconEnable);
		this.setDisabledIcon(iconDisable);

		if (!tip.equals("")) {
			this.setToolTipText(tip);
		}

	}
	public void isTrue(){
		this.setIcon(iconEnable);
	}
	public void isFalse(){
		this.setIcon(iconDisable);
	}
}
