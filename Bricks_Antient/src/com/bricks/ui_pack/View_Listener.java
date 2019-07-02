package com.bricks.ui_pack;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import com.bricks.tools.Utils_SQL;

/**
 * 
 * @author DraLastat
 * @Description 
 */

public class View_Listener implements ItemListener {

	private Utils_SQL sql;
	private JComboBox<String> comboxEleName;
	private Element_Listener elisten;
	private StringBuilder appName;
	
	public View_Listener(Utils_SQL sql, JComboBox<String> comboxEleName, Element_Listener elisten, StringBuilder appName) {
		// TODO Auto-generated constructor stub
		this.sql = sql;
		this.comboxEleName = comboxEleName;
		this.elisten = elisten;
		this.appName = appName;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if (e.getStateChange() == ItemEvent.SELECTED) {
			String viewName = (String) e.getItem();
			ResultSet rs = sql.queryElement("ELEMENT", appName.toString(), viewName);
			
			comboxEleName.removeAllItems();
			comboxEleName.removeItemListener(elisten);
			try {
				rs.next();
				String eleFirst = new String(rs.getBytes(1), "UTF-8");
				comboxEleName.addItem(eleFirst);
				while ((rs.next())) {
					String eleName = rs.getString(1);
					comboxEleName.addItem(eleName);
				}
				comboxEleName.setSelectedItem(null);
				comboxEleName.addItemListener(elisten);
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				if (rs != null) {
					try {
						rs.close();
						rs = null;
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

}
