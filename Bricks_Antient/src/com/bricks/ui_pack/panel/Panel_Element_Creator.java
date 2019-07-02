package com.bricks.ui_pack.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.android.ddmlib.IDevice;
import com.bricks.Global_Observer;
import com.bricks.ui_pack.Constants_UI;
import com.bricks.ui_pack.Custom_Button;
import com.bricks.node_selection.Realtime_Screen_UI;
import com.bricks.node_selection.Variable_Observer;
import com.bricks.tools.Utils_Property;
import com.bricks.tools.Utils_SQL;
import com.bricks.tools.Button_Switcher;
import com.hp.hpl.sparta.xpath.NodeTest;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

/**
 * 
 * @author DraLastat
 * @Description 
 */

public class Panel_Element_Creator extends JPanel implements Observer, Global_Observer {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger("ElecrePanel.class");
	private static JLabel ClickStatus;
	private static JLabel CheckStatus;
	private static JLabel ScrollStatus;
	private static JLabel Focustatus;
	private static JLabel LongClickStatus;
	private static Custom_Button buttonSave;
	private static Custom_Button buttonRef;
	private JPanel panelView;
	private JPanel panelEreCenter;
	
	
	private Realtime_Screen_UI realTimeScreen;
	private IDevice device;
	private Variable_Observer obs;
	
	private Utils_SQL sql;
	
	public Panel_Element_Creator(Variable_Observer obs, Utils_SQL sql) {
		this.obs = obs;
		this.sql = sql;
		initialize();
	}

	private void initialize() {
		this.setBackground(Constants_UI.MAIN_BACK_COLOR);
		this.setLayout(new BorderLayout());
		addComponent();
		addListener();
	}

	private JPanel LiveviewPanel;
	private void addComponent() {
		LiveviewPanel = getCenterPanel();
		this.add(getUpPanel(), BorderLayout.NORTH);
		this.add(LiveviewPanel, BorderLayout.CENTER);
		this.add(getRightPanel(), BorderLayout.EAST);
	}
	public Map<String, String> getNode_info;

	private JPanel getUpPanel() {
		JPanel panelUp = new JPanel();
		panelUp.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelUp.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 5));

		JLabel labelTitle = new JLabel(Utils_Property.getProperty("bricks.ui.elecre.title"));
		labelTitle.setFont(Constants_UI.FONT_TITLE);
		labelTitle.setForeground(Constants_UI.TOOL_BAR_BACK_COLOR);
		panelUp.add(labelTitle);
		
		return panelUp;
	}

	private Button_Switcher switch_btn;
	public Button_Switcher getSwitch_btn() {
		return switch_btn;
	}

	private JPanel getCenterPanel() {
		panelEreCenter = new JPanel();
		panelEreCenter.setBackground(Constants_UI.MAIN_BACK_COLOR);
		JPanel panelSuspend = new JPanel();
		panelSuspend.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelView = new JPanel();
		panelView.setPreferredSize(new Dimension(530,530));
		panelView.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelView.setLayout(new BorderLayout());
		panelView.updateUI();
		
        switch_btn = new Button_Switcher();
        
        switch_btn.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (switch_btn.isSelected()) {
                	realTimeScreen.startStaticMode();
                } else {
                	realTimeScreen.stopStaticMode();
                }
            }
        });
        panelSuspend.add(switch_btn);
		panelEreCenter.add(panelView);
		panelEreCenter.add(panelSuspend);

		return panelEreCenter;
	}

	private JTextField textFieldEleItem_1;
	private JTextField textFieldEleItem_4;
	private JComboBox<String> textFieldEleItem_5;
	private JComboBox<String> textFieldEleItem_6;
	private JPanel panelRight;
	private JTextField textFieldActivity;
	private JTextField textFieldElement;
	
	/*
	 * TODO // @dan Viewbox 
	 */
	private JPanel getRightPanel() {
		
		panelRight = new JPanel();
		Dimension preferredSize = new Dimension(280, 10);
		panelRight.setPreferredSize(preferredSize);
		panelRight.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelRight.setLayout(new BorderLayout());
		
		JPanel panelinfo = new JPanel();
		panelinfo.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelinfo.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 5));
		panelinfo.setPreferredSize(new Dimension(50, 150));
		
		JLabel ele_xpath = new JLabel(Utils_Property.getProperty("bricks.ui.elecre.item1"));
		JLabel checkable = new JLabel(Utils_Property.getProperty("bricks.ui.elecre.item2"));
		textFieldEleItem_1 = new JTextField();
		JTextField textFieldEleItem_2 = new JTextField();
		textFieldEleItem_1.setEditable(false);
		JLabel clickchk = new JLabel(Utils_Property.getProperty("bricks.ui.elecre.clickchk"));
		JLabel scrollchk = new JLabel(Utils_Property.getProperty("bricks.ui.elecre.scrollchk"));
		JLabel checkchk = new JLabel(Utils_Property.getProperty("bricks.ui.elecre.checkchk"));
		JLabel focuschk = new JLabel(Utils_Property.getProperty("bricks.ui.elecre.focuschk"));
		JLabel long_clickchk = new JLabel(Utils_Property.getProperty("bricks.ui.elecre.longclickchk"));
		JLabel label_null = new JLabel();
		ClickStatus = new JLabel();
		ScrollStatus = new JLabel();
		CheckStatus = new JLabel();
		Focustatus = new JLabel();
		LongClickStatus = new JLabel();
		
		
		ClickStatus.setIcon(Constants_UI.ICON_ELE_CHK);
		ScrollStatus.setIcon(Constants_UI.ICON_ELE_CHK);
		CheckStatus.setIcon(Constants_UI.ICON_ELE_CHK);
		Focustatus.setIcon(Constants_UI.ICON_ELE_CHK);
		LongClickStatus.setIcon(Constants_UI.ICON_ELE_CHK);
		
		ele_xpath.setFont(Constants_UI.SEC_TITLE);
		checkable.setFont(Constants_UI.SEC_TITLE);
		textFieldEleItem_1.setFont(Constants_UI.FONT_NORMAL);
		textFieldEleItem_2.setFont(Constants_UI.FONT_NORMAL);
		clickchk.setFont(Constants_UI.FONT_NORMAL);
		scrollchk.setFont(Constants_UI.FONT_NORMAL);
		checkchk.setFont(Constants_UI.FONT_NORMAL);
		focuschk.setFont(Constants_UI.FONT_NORMAL);
		long_clickchk.setFont(Constants_UI.FONT_NORMAL);
		
		ele_xpath.setPreferredSize(Constants_UI.LABLE_SIZE_ELE_ITEM);				
		checkable.setPreferredSize(Constants_UI.LABLE_SIZE_ELE_ITEM);
		label_null.setPreferredSize(Constants_UI.LABLE_NULL_ITEM);
		textFieldEleItem_1.setPreferredSize(Constants_UI.TEXT_FIELD_SIZE_ITEM);
		textFieldEleItem_2.setPreferredSize(Constants_UI.TEXT_FIELD_SIZE_ITEM);
		
		panelinfo.add(ele_xpath);
		panelinfo.add(textFieldEleItem_1);
		panelinfo.add(checkable);
		panelinfo.add(label_null);
		panelinfo.add(clickchk);
		panelinfo.add(ClickStatus);
		panelinfo.add(scrollchk);
		panelinfo.add(ScrollStatus);
		panelinfo.add(checkchk);
		panelinfo.add(CheckStatus);
		panelinfo.add(focuschk);
		panelinfo.add(Focustatus);
		panelinfo.add(long_clickchk);
		panelinfo.add(LongClickStatus);

		JPanel panelGridSetting = new JPanel();
		panelGridSetting.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelGridSetting.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 5));
		panelGridSetting.setPreferredSize(new Dimension(50, 160));

		JLabel app_name = new JLabel(Utils_Property.getProperty("bricks.ui.elecre.item4"));
		JLabel view_name = new JLabel(Utils_Property.getProperty("bricks.ui.elecre.item5"));
		JLabel ele_name = new JLabel(Utils_Property.getProperty("bricks.ui.elecre.item6"));
		textFieldEleItem_4 = new JTextField();
		textFieldEleItem_5 = new JComboBox<>();
		textFieldEleItem_6 = new JComboBox<>();

		app_name.setFont(Constants_UI.SEC_TITLE);
		view_name.setFont(Constants_UI.SEC_TITLE);
		ele_name.setFont(Constants_UI.SEC_TITLE);
		textFieldEleItem_4.setFont(Constants_UI.FONT_NORMAL);
		textFieldEleItem_5.setFont(Constants_UI.FONT_NORMAL);
		textFieldEleItem_6.setFont(Constants_UI.FONT_NORMAL);

		app_name.setPreferredSize(Constants_UI.LABLE_SIZE_ELE_ITEM);
		view_name.setPreferredSize(Constants_UI.LABLE_SIZE_ELE_ITEM);				
		ele_name.setPreferredSize(Constants_UI.LABLE_SIZE_ELE_ITEM);
		textFieldEleItem_4.setPreferredSize(Constants_UI.TEXT_FIELD_SIZE_ITEM);
		textFieldEleItem_5.setPreferredSize(Constants_UI.TEXT_FIELD_SIZE_ITEM);
		textFieldEleItem_6.setPreferredSize(Constants_UI.TEXT_FIELD_SIZE_ITEM);
		
		textFieldEleItem_5.setEditable(true);
		textFieldEleItem_6.setEditable(true);
		
		panelGridSetting.add(app_name);
		panelGridSetting.add(textFieldEleItem_4);
		panelGridSetting.add(view_name);
		panelGridSetting.add(textFieldEleItem_5);
		panelGridSetting.add(ele_name);
		panelGridSetting.add(textFieldEleItem_6);
		
		textFieldActivity = (JTextField) textFieldEleItem_5.getEditor().getEditorComponent();
		textFieldElement = (JTextField) textFieldEleItem_6.getEditor().getEditorComponent();

		JLabel labelEleItemNull_1 = new JLabel();
		labelEleItemNull_1.setPreferredSize(Constants_UI.LABLE_SIZE_NULL_ITEM);
        buttonSave = new Custom_Button(Constants_UI.ICON_SAVE, Constants_UI.ICON_SAVE_ENABLE,
                Constants_UI.ICON_SAVE_DISABLE, "");
        buttonRef = new Custom_Button(Constants_UI.ICON_ROW_REFRESH, Constants_UI.ICON_ROW_REFRESH_ENABLE,
        		Constants_UI.ICON_ROW_REFRESH_DISABLE, "");
        
        panelGridSetting.add(labelEleItemNull_1);
        panelGridSetting.add(buttonSave);
        panelGridSetting.add(buttonRef);
        
		panelRight.add(panelinfo, BorderLayout.NORTH);
		panelRight.add(panelGridSetting, BorderLayout.CENTER);
		return panelRight;
	}


	public void observe(Observable o) {
	    o.addObserver(this);
	}

	private Map<String, String> node_info;
	private String node_text = "";
	@Override
	public void update(Observable o, Object arg) {
		node_info = ((Variable_Observer) o).getInfo();
		textFieldEleItem_1.setText(node_info.get("xpath"));
		node_text = node_info.get("text");
		
		if(node_info.get("package").equals("dji.pilot"))
			textFieldEleItem_4.setText("DJI GO3");
		else if (node_info.get("package").equals("dji.go.v4"))
			textFieldEleItem_4.setText("DJI GO4");
		else if (node_info.get("package").equals("com.dji.industry.pilot"))
			textFieldEleItem_4.setText("DJI Pilot");
		else if (node_info.get("package").equals("com.dpad.launcher"))
			textFieldEleItem_4.setText("RM500 Launcher");
		else if (node_info.get("package").equals("com.android.settings"))
			textFieldEleItem_4.setText("RM500 Settings");
		else if (node_info.get("package").equals("dji.prof.mg"))
			textFieldEleItem_4.setText("MG 1A/P");
		else if (node_info.get("package").equals("dji.prof.args.tiny"))
			textFieldEleItem_4.setText("MG 1S");
		else if (node_info.get("package").equals("dji.pilot.pad"))
			textFieldEleItem_4.setText("DJI GO4 Pad");
		
		if(node_info != null && node_info.containsKey("clickable")){
			if(node_info.get("clickable").equals("1")){
				ClickStatus.setIcon(Constants_UI.ICON_ELE_CHK_TRUE);
			}else{
				ClickStatus.setIcon(Constants_UI.ICON_ELE_CHK_FALSE);
			}
		}
		if(node_info != null && node_info.containsKey("scrollable")){
			if(node_info.get("scrollable").equals("1")){
				ScrollStatus.setIcon(Constants_UI.ICON_ELE_CHK_TRUE);
			}else{
				ScrollStatus.setIcon(Constants_UI.ICON_ELE_CHK_FALSE);
			}
		}
		if(node_info != null && node_info.containsKey("checkable")){
			if(node_info.get("checkable").equals("1")){
				CheckStatus.setIcon(Constants_UI.ICON_ELE_CHK_TRUE);
			}else{
				CheckStatus.setIcon(Constants_UI.ICON_ELE_CHK_FALSE);
			}
		}
		if(node_info != null && node_info.containsKey("focusable")){
			if(node_info.get("focusable").equals("1")){
				Focustatus.setIcon(Constants_UI.ICON_ELE_CHK_TRUE);
			}else{
				Focustatus.setIcon(Constants_UI.ICON_ELE_CHK_FALSE);
			}
		}
		if(node_info != null && node_info.containsKey("long-clickable")){
			if(node_info.get("long-clickable").equals("1")){
				LongClickStatus.setIcon(Constants_UI.ICON_ELE_CHK_TRUE);
			}else{
				LongClickStatus.setIcon(Constants_UI.ICON_ELE_CHK_FALSE);
			}
		}
	}

	private String app_name_text;
	private String custom_name_text;
	private String xpath_text;
	private String state_text;
	private String activity_name_text;
	private String screen_text;
	private String textview_text;
	private Map<String, String> app_name = new HashMap<String, String>();
	private Map<String, String> custom_name = new HashMap<String, String>();
	private Map<String, String> xpath = new HashMap<String, String>();
	private Map<String, String> state = new HashMap<String, String>();
	private Map<String, String> activity_name = new HashMap<String, String>();
	private Map<String, String> screen = new HashMap<String, String>();
	private Map<String, String> view_text = new HashMap<String, String>();
	
	private ArrayList<Map<String, String>> sqllist = new ArrayList<Map<String, String>>();
	private EventList<String> actEventList = new BasicEventList<>();
	private EventList<String> eleEventList = new BasicEventList<>();
	
	public void addListener() {
		AutoCompleteSupport.install(textFieldEleItem_5, actEventList);
		AutoCompleteSupport.install(textFieldEleItem_6, eleEventList);
		
		buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(textFieldActivity.getText().equals(null) || textFieldElement.getText().equals(null)){
            		JOptionPane.showMessageDialog(buttonSave,"Failed");
            	}else{
            		buttonSave.setEnabled(true);
                    try {
                    	app_name_text = textFieldEleItem_4.getText();
                    	activity_name_text = new String(textFieldActivity.getText().getBytes(), "UTF-8");
                    	custom_name_text = new String(textFieldElement.getText().getBytes(), "UTF-8");
                    	state_text = node_info.get("clickable") + node_info.get("scrollable") + node_info.get("checkable") + node_info.get("focusable") + node_info.get("long-clickable");
                    	xpath_text = node_info.get("xpath");
                    	screen_text = node_info.get("screenPath");
                    	textview_text = new String(node_info.get("text").getBytes(), "UTF-8");
                    	app_name.put("APP_NAME", app_name_text);
                    	custom_name.put("CUSTOM_NAME", custom_name_text);
                    	activity_name.put("ACTIVITY_NAME", activity_name_text);
                    	xpath.put("XPATH", xpath_text);
                    	state.put("STATE", state_text);
                    	screen.put("SCREEN_PATH", screen_text);
                    	view_text.put("VIEW_TEXT", textview_text);
                    	sqllist.add(app_name);
                    	sqllist.add(custom_name);
                    	sqllist.add(activity_name);
                    	sqllist.add(xpath);
                    	sqllist.add(state);
                    	sqllist.add(screen);
                    	sqllist.add(view_text);
                    	sql.insertEle("ELEMENT", sqllist);
                    	JOptionPane.showMessageDialog(buttonSave,"Save Complete");
                    	
                    	textFieldElement.setText("");
                    } catch (SQLException e1) {
                    	if (e1.getMessage().equals("[SQLITE_CONSTRAINT_UNIQUE]  A UNIQUE constraint failed (UNIQUE constraint failed: ELEMENT.CUSTOM_NAME)"))
                    		JOptionPane.showMessageDialog(buttonSave, "Failed! The element already exists");
                    } catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
            		
            	}

//            	MainEntry.cachedThreadPool.shutdownNow();
            }
        });
		
		buttonRef.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(textFieldActivity.getText().equals(null) || textFieldElement.getText().equals(null))
            		JOptionPane.showMessageDialog(buttonSave,"Failed");
				else {
					try {
						app_name_text = textFieldEleItem_4.getText();
	                	activity_name_text = new String(textFieldActivity.getText().getBytes(), "UTF-8");
	                	custom_name_text = new String(textFieldElement.getText().getBytes(), "UTF-8");
	                	state_text = node_info.get("clickable") + node_info.get("scrollable") + node_info.get("checkable") + node_info.get("focusable") + node_info.get("long-clickable");
	                	xpath_text = node_info.get("xpath");
	                	screen_text = node_info.get("screenPath");
	                	textview_text = new String(node_info.get("text").getBytes(), "UTF-8");
	                	app_name.put("APP_NAME", app_name_text);
                    	custom_name.put("CUSTOM_NAME", custom_name_text);
                    	activity_name.put("ACTIVITY_NAME", activity_name_text);
                    	xpath.put("XPATH", xpath_text);
                    	state.put("STATE", state_text);
                    	screen.put("SCREEN_PATH", screen_text);
                    	view_text.put("VIEW_TEXT", textview_text);
                    	
                    	sqllist.add(app_name);
                    	sqllist.add(custom_name);
                    	sqllist.add(activity_name);
                    	sqllist.add(xpath);
                    	sqllist.add(state);
                    	sqllist.add(screen);
                    	sqllist.add(view_text);
                    	
                    	int success = sql.updateElement("ELEMENT", sqllist);
                    	if (success > 0)
                    		JOptionPane.showMessageDialog(buttonSave,"Update Complete");
                    	else
                    		JOptionPane.showMessageDialog(buttonSave,"Update failed");
                    	
					} catch (SQLException e1) {
                    	if (e1.getMessage().equals("[SQLITE_CONSTRAINT_UNIQUE]  A UNIQUE constraint failed (UNIQUE constraint failed: ELEMENT.CUSTOM_NAME)"))
                    		JOptionPane.showMessageDialog(buttonSave, "Failed! The element already exists");
                    } catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		textFieldActivity.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				actEventList.clear();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				ResultSet rs = sql.queryElement("ACTIVITY", textFieldEleItem_4.getText());
				ArrayList<String> actList = new ArrayList<>();
				try {
					while (rs.next()) {
						actList.add(new String(rs.getBytes("ACTIVITY_NAME"), "UTF-8"));
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e2) {
					e2.printStackTrace();
				}
				actEventList.addAll(actList);
			}
		});
		
		textFieldEleItem_5.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (e.getStateChange() == ItemEvent.SELECTED)
					textFieldElement.setText("");
			}
		});
		
		textFieldElement.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				eleEventList.clear();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				ResultSet rs = sql.queryElement("ELEMENT", textFieldEleItem_4.getText(), textFieldActivity.getText());
				ArrayList<String> eleList = new ArrayList<>();
				try {
					while (rs.next()) {
						eleList.add(new String(rs.getBytes("CUSTOM_NAME"), "UTF-8"));
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e2) {
					e2.printStackTrace();
				}
				eleEventList.addAll(eleList);
			}
		});
	}

	@Override
	public void frameImageChange(BufferedImage image) {
		
	}

	@Override
	public void ADBChange(IDevice[] devices) {
		if (devices[0] != null) {
			device = devices[0];
			realTimeScreen = new Realtime_Screen_UI(device, obs, this);
			realTimeScreen.addMouseListener(realTimeScreen);
			realTimeScreen.addMouseMotionListener(realTimeScreen);
			panelView.add(realTimeScreen, BorderLayout.CENTER);
			panelView.updateUI();
		} else {
			realTimeScreen.stopGetXml();
			realTimeScreen.removeMouseListener(realTimeScreen);
			realTimeScreen.removeMouseMotionListener(realTimeScreen);
			panelView.remove(realTimeScreen);
			panelView.updateUI();
			realTimeScreen = null;
		} 
	}
}
