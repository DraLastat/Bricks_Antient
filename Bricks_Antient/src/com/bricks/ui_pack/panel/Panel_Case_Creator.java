package com.bricks.ui_pack.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.bricks.Global_Observer;
import com.bricks.Main_Entry;
import com.bricks.ui_pack.Bricks_Bean;
import com.bricks.ui_pack.Constants_UI;
import com.bricks.ui_pack.Element_Listener;
import com.bricks.ui_pack.Custom_Button;
import com.bricks.ui_pack.View_Listener;
import com.bricks.background_runner.Execution_Main;
import com.bricks.node_selection.Realtime_Screen_UI;
import com.bricks.tools.Utils_File;
import com.bricks.tools.Utils_Property;
import com.bricks.tools.Utils_SQL;
import com.bricks.tools.Time_Series_Chart;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

/**
 * Case create page
 */
public class Panel_Case_Creator extends JPanel implements Observer, Global_Observer {

	private static final long serialVersionUID = 1L;

	private Custom_Button buttonEleAdd;
	private Custom_Button buttonActAdd;
	private Custom_Button buttonVerAdd;
	private Custom_Button buttonRowDelete;
	private Custom_Button buttonEleRefresh;
	private Custom_Button buttonActRefresh;
	private Custom_Button buttonSave;
	private Custom_Button buttonDragAdd;
	private Custom_Button buttonScrshot;
	private Custom_Button buttonDocRead;
	private Custom_Button buttonJsonLoad;
	private Custom_Button buttonPlayList;
	private Custom_Button buttonRTChart;
	private Custom_Button buttonLogSave;
	private Custom_Button buttonTimer;
	private Custom_Button buttonClear;
	private Custom_Button buttonAddAll;
	private JTextArea logArea;
	private int ver_type;
	private ArrayList<Bricks_Bean> caseList = null;
	private final static String CURRENT_DIR = System.getProperty("user.dir");
	private StringBuilder xpath;
	private StringBuilder cus_name;
	private String act_name = "";
	private StringBuilder scrshot_pathname;
	private int action;
	private int scrshot_X;
	private int scrshot_Y;
	private IDevice device;
	private JSONArray tmpJson;

	/**
	 * Element Adding Panel
	 */
	private JComboBox<String> comboxAppName;
	private JComboBox<String> comboxViewName;
	private JComboBox<String> comboxEleName;
	private JComboBox<String> comboxActName;
	private JComboBox<String> comboxVerName;
	private JTable casetable;
	private DefaultTableModel model;
	private StringBuilder appName;
	private String appStartName = "";
	private String pkg = "";
	private String filepath;
	private Utils_SQL sql = null;
	private ResultSet xpathSet = null;
	private Object[] table_row = new Object[5];
	private int[] speAddList = { 2, 4, 7, 11, 12, 14 };

	private Popup_Window popWin;
	private EventList<String> actEventList = new BasicEventList<>();
	private EventList<String> eleEventList = new BasicEventList<>();

	/**
	 * Initialize
	 */
	public Panel_Case_Creator(Utils_SQL sql) {
		this.sql = sql;
		this.setBackground(Constants_UI.MAIN_BACK_COLOR);
		this.setLayout(new BorderLayout());
		addComponent();
		addListener();
		initialize();
	}

	private void initialize() {
		caseList = new ArrayList<>();
		xpath = new StringBuilder();
		cus_name = new StringBuilder();
		scrshot_pathname = new StringBuilder();
		appName = new StringBuilder();

		popWin = new Popup_Window(table_row, model, casetable, sql, caseList);
	}

	private JPanel CaseCre;

	private void addComponent() {
		this.add(getUpPanel(), BorderLayout.NORTH);
		this.add(getCenterPanel(), BorderLayout.CENTER);
		this.add(getDownPanel(), BorderLayout.SOUTH);
	}

	/**
	 * Title Panel
	 * 
	 * @return
	 */
	private JPanel getUpPanel() {
		JPanel panelUp = new JPanel();
		panelUp.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelUp.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 5));
		JLabel labelTitle = new JLabel(Utils_Property.getProperty("bricks.ui.casecre.title"));
		labelTitle.setFont(Constants_UI.FONT_TITLE);
		labelTitle.setForeground(Constants_UI.TOOL_BAR_BACK_COLOR);
		panelUp.add(labelTitle);
		return panelUp;
	}

	/**
	 * case list set to Jtable
	 * 
	 * @return
	 */
	private JPanel getCenterPanel() {
		JPanel panelCenter = new JPanel();
		panelCenter.setBackground(Constants_UI.TABLE_LINE_COLOR);
		JPanel TablePanel = new JPanel();
		TablePanel.setPreferredSize(new Dimension(810, 250));
		casetable = new JTable();
		Object[] columns = { "Id", "Type", "SPEC 1", "SPEC 2", "SPEC 3" };
		model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		casetable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		casetable.setModel(model);
		casetable.setBackground(Color.LIGHT_GRAY);
		casetable.setForeground(Color.black);
		Font font = new Font("", 1, 10);
		casetable.setFont(font);
		casetable.setRowHeight(30);
		casetable.setPreferredScrollableViewportSize(new Dimension(800, 200));
		TablePanel.add(new JScrollPane(casetable));
		panelCenter.add(TablePanel);
		panelCenter.updateUI();

		return panelCenter;
	}

	
	/**
	 * caselist editing & log print Panel
	 * 
	 * @return
	 */
	
	private JTextField DataFrom;

	private JPanel getDownPanel() {
		JPanel panelDown = new JPanel();
		panelDown.setBackground(Constants_UI.TOOL_BAR_BACK_COLOR);
		panelDown.setPreferredSize(new Dimension(810, 320));
		panelDown.setLayout(new GridLayout(1, 2));
		JPanel LeftPanel = new JPanel();
		JPanel RightPanel = new JPanel();

		// Database input panel
		JPanel DataGrid = new JPanel();
		JLabel DataSrc = new JLabel(Utils_Property.getProperty("bricks.ui.casecre.datasrc"));
		DataSrc.setFont(Constants_UI.FONT_NORMAL);
		DataGrid.setPreferredSize(new Dimension(420, 40));
		DataGrid.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 5));
		DataGrid.setBackground(Constants_UI.TABLE_LINE_COLOR);
		DataFrom = new JTextField();
		DataFrom.setPreferredSize(new Dimension(180, 24));
		buttonDocRead = new Custom_Button(Constants_UI.ICON_DOCREAD, 
										  Constants_UI.ICON_DOCREAD_ENABLE,
										  Constants_UI.ICON_DOCREAD_DISABLE, 
										  Utils_Property.getProperty("bricks.ui.casecre.btntip.docread"));
		buttonJsonLoad = new Custom_Button(Constants_UI.ICON_JSONLOAD, 
										   Constants_UI.ICON_JSONLOAD_ENABLE,
										   Constants_UI.ICON_JSONLOAD_DISABLE, 
										   Utils_Property.getProperty("bricks.ui.casecre.btntip.jsonload"));
		DataGrid.add(DataSrc);
		DataGrid.add(DataFrom);
		DataGrid.add(buttonDocRead);
		DataGrid.add(buttonJsonLoad);

		// Bricks adding panel
		JPanel ElePanel_APP = new JPanel();
		ElePanel_APP.setPreferredSize(new Dimension(420, 40));
		ElePanel_APP.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 5));
		ElePanel_APP.setBackground(Constants_UI.TABLE_LINE_COLOR);
		JPanel ElePanel_View = new JPanel();
		ElePanel_View.setPreferredSize(new Dimension(420, 40));
		ElePanel_View.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 5));
		ElePanel_View.setBackground(Constants_UI.TABLE_LINE_COLOR);
		JPanel ElePanel_Name = new JPanel();
		ElePanel_Name.setPreferredSize(new Dimension(420, 40));
		ElePanel_Name.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 5));
		ElePanel_Name.setBackground(Constants_UI.TABLE_LINE_COLOR);
		JLabel Ele_Null = new JLabel();
		Ele_Null.setPreferredSize(new Dimension(50, 35));
		JLabel Ele_Null2 = new JLabel();
		Ele_Null2.setPreferredSize(new Dimension(50, 35));
		JLabel ElePick = new JLabel(Utils_Property.getProperty("bricks.ui.casecre.elepick"));
		ElePick.setFont(Constants_UI.FONT_NORMAL);
		buttonScrshot = new Custom_Button(Constants_UI.ICON_SCRSHOT, 
										  Constants_UI.ICON_SCRSHOT_ENABLE,
										  Constants_UI.ICON_SCRSHOT_DISABLE, 
										  Utils_Property.getProperty("bricks.ui.casecre.btntip.screenshot"));
		buttonEleAdd = new Custom_Button(Constants_UI.ICON_ELE_ADD, 
										 Constants_UI.ICON_ELE_ADD_ENABLE,
										 Constants_UI.ICON_ELE_ADD_DISABLE, 
										 Utils_Property.getProperty("bricks.ui.casecre.btntip.addele"));
		buttonEleRefresh = new Custom_Button(Constants_UI.ICON_ROW_REFRESH, 
											 Constants_UI.ICON_ROW_REFRESH_ENABLE,
											 Constants_UI.ICON_ROW_REFRESH_DISABLE, 
											 Utils_Property.getProperty("bricks.ui.casecre.btntip.reele"));

		addEleCombox();

		ElePanel_APP.add(ElePick);
		ElePanel_APP.add(comboxAppName);
		ElePanel_View.add(Ele_Null);
		ElePanel_View.add(comboxViewName);
		ElePanel_Name.add(Ele_Null2);
		ElePanel_Name.add(comboxEleName);
		ElePanel_Name.add(buttonScrshot);
		ElePanel_Name.add(buttonEleAdd);
		ElePanel_Name.add(buttonEleRefresh);

		JPanel ActPanel = new JPanel();
		ActPanel.setPreferredSize(new Dimension(420, 40));
		ActPanel.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 5));
		ActPanel.setBackground(Constants_UI.TABLE_LINE_COLOR);
		JLabel ActPick = new JLabel(Utils_Property.getProperty("bricks.ui.casecre.actpick"));
		ActPick.setFont(Constants_UI.FONT_NORMAL);
		comboxActName = new JComboBox<String>();
		comboxActName.setEditable(false);
		comboxActName.setSelectedItem(null);
		comboxActName.setPreferredSize(Constants_UI.TEXT_COMBOX_SIZE_ITEM);
		comboxActName.addItem("Single-Click");
		comboxActName.addItem("Long-Press");
		comboxActName.addItem("Point Drag");
		comboxActName.addItem("Set Text");
		comboxActName.addItem("SeekBar Drag");
		comboxActName.addItem("Push To Device");
		comboxActName.addItem("Reboot Device");
		comboxActName.addItem("Scroll");
		comboxActName.addItem("Spinner select");
		comboxActName.addItem("Key HOME");
		comboxActName.addItem("Key BACK");
		comboxActName.addItem("Tap Point");
		comboxActName.addItem("Save text to tmp");
		comboxActName.addItem("Send adb cmd");
		comboxActName.addItem("Appium reinit");
		comboxActName.addItem("Open switch");
		comboxActName.addItem("Close switch");
		comboxActName.setSelectedItem(null);
		comboxActName.addItemListener(new ActListener());
		JLabel ActNull = new JLabel();
		ActNull.setPreferredSize(new Dimension(35, 40));
		buttonActAdd = new Custom_Button(Constants_UI.ICON_ELE_ADD, 
										 Constants_UI.ICON_ELE_ADD_ENABLE,
										 Constants_UI.ICON_ELE_ADD_DISABLE,
										 Utils_Property.getProperty("bricks.ui.casecre.btntip.addact"));
		buttonActRefresh = new Custom_Button(Constants_UI.ICON_ROW_REFRESH, 
											 Constants_UI.ICON_ROW_REFRESH_ENABLE,
											 Constants_UI.ICON_ROW_REFRESH_DISABLE,
											 Utils_Property.getProperty("bricks.ui.casecre.btntip.react"));
		ActPanel.add(ActPick);
		ActPanel.add(comboxActName);
		ActPanel.add(ActNull);
		ActPanel.add(buttonActAdd);
		ActPanel.add(buttonActRefresh);

		JPanel VerPanel = new JPanel();
		VerPanel.setPreferredSize(new Dimension(420, 40));
		VerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 5));
		VerPanel.setBackground(Constants_UI.TABLE_LINE_COLOR);
		JLabel VerPick = new JLabel(Utils_Property.getProperty("bricks.ui.casecre.verpick"));
		VerPick.setFont(Constants_UI.FONT_NORMAL);
		comboxVerName = new JComboBox<String>();
		comboxVerName.addItem(Utils_Property.getProperty("bricks.ui.casecre.textver"));
		comboxVerName.addItem(Utils_Property.getProperty("bricks.ui.casecre.extver"));
		comboxVerName.addItem(Utils_Property.getProperty("bricks.ui.casecre.cmpver"));
		comboxVerName.setEditable(false);
		comboxVerName.setSelectedItem(null);
		comboxVerName.setPreferredSize(Constants_UI.TEXT_COMBOX_SIZE_ITEM);

		comboxVerName.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String item = (String) e.getItem();
				if (Utils_Property.getProperty("bricks.ui.casecre.textver").equals(item)) {
					ver_type = 1;
				} else if (Utils_Property.getProperty("bricks.ui.casecre.imgver").equals(item)) {

				} else if (Utils_Property.getProperty("bricks.ui.casecre.extver").equals(item)) {
					ver_type = 2;
				} else if (item == null) {
					ver_type = 0;
				} else if (Utils_Property.getProperty("bricks.ui.casecre.cmpver").equals(item)) {
					ver_type = 3;
				}
			}
		});

		JLabel VerNull = new JLabel();
		VerNull.setPreferredSize(new Dimension(35, 40));
		buttonVerAdd = new Custom_Button(Constants_UI.ICON_ELE_ADD, 
										 Constants_UI.ICON_ELE_ADD_ENABLE,
										 Constants_UI.ICON_ELE_ADD_DISABLE, 
										 Utils_Property.getProperty("bricks.ui.casecre.btntip.addver"));
		VerPanel.add(VerPick);
		VerPanel.add(comboxVerName);
		VerPanel.add(VerNull);
		VerPanel.add(buttonVerAdd);

		JPanel RunPanel = new JPanel();
		RunPanel.setPreferredSize(new Dimension(420, 40));
		RunPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 14, 5));
		
		buttonAddAll = new Custom_Button(Constants_UI.ICON_ADDALL, 
										 Constants_UI.ICON_ADDALL_ENABLE,
										 Constants_UI.ICON_ADDALL_DISABLE, 
										 Utils_Property.getProperty("bricks.ui.casecre.btntip.addall"));
		buttonClear = new Custom_Button(Constants_UI.ICON_CLEAR, 
										Constants_UI.ICON_CLEAR_ENABLE,
										Constants_UI.ICON_CLEAR_DISABLE,
										Utils_Property.getProperty("bricks.ui.casecre.btntip.clear"));
		buttonTimer = new Custom_Button(Constants_UI.ICON_TIMER,
										Constants_UI.ICON_TIMER_ENABLE,
										Constants_UI.ICON_TIMER_DISABLE,
										Utils_Property.getProperty("bricks.ui.casecre.btntip.timer"));
		buttonRowDelete = new Custom_Button(Constants_UI.ICON_ROW_DELETE, 
											Constants_UI.ICON_ROW_DELETE_ENABLE,
											Constants_UI.ICON_ROW_DELETE_DISABLE, 
											Utils_Property.getProperty("bricks.ui.casecre.btntip.rowdelete"));
		buttonSave = new Custom_Button(Constants_UI.ICON_LIST_SAVE,
									   Constants_UI.ICON_LIST_SAVE_ENABLE,
									   Constants_UI.ICON_LIST_SAVE_DISABLE, 
									   Utils_Property.getProperty("bricks.ui.casecre.btntip.listsave"));
		buttonPlayList = new Custom_Button(Constants_UI.ICON_PLAY_LIST, 
										   Constants_UI.ICON_PLAY_LIST_ENABLE,
										   Constants_UI.ICON_PLAY_LIST_DISABLE, 
										   Utils_Property.getProperty("bricks.ui.casecre.btntip.playlist"));
		buttonRTChart = new Custom_Button(Constants_UI.ICON_RTCHART,
										  Constants_UI.ICON_RTCHART_ENABLE,
										  Constants_UI.ICON_RTCHART_DISABLE, 
										  Utils_Property.getProperty("bricks.ui.casecre.btntip.rtchart"));
		buttonLogSave = new Custom_Button(Constants_UI.ICON_LOGSAVE, 
										  Constants_UI.ICON_LOGSAVE_ENABLE,
										  Constants_UI.ICON_LOGSAVE_DISABLE, 
				   						  Utils_Property.getProperty("bricks.ui.casecre.btntip.logsave"));
		
		RunPanel.add(buttonTimer);
		RunPanel.add(buttonRowDelete);
		RunPanel.add(buttonAddAll);
		RunPanel.add(buttonClear);
		RunPanel.add(buttonPlayList);
		RunPanel.add(buttonRTChart);
		RunPanel.add(buttonSave);
		RunPanel.add(buttonLogSave);

		JPanel panelLog = new JPanel();
		panelLog.setBackground(Constants_UI.TOOL_BAR_BACK_COLOR);
		panelLog.setLayout(new BorderLayout());
		logArea = new JTextArea(17, 50);
		logArea.setBackground(Constants_UI.LOG_COLOR);
		logArea.setForeground(Constants_UI.MAIN_BACK_COLOR);
		logArea.setEditable(false);
		PrintStream printStream = new PrintStream(new CustomOutputStream(logArea));
		panelLog.add(new JScrollPane(logArea));
		LeftPanel.add(DataGrid);
		LeftPanel.add(ElePanel_APP);
		LeftPanel.add(ElePanel_View);
		LeftPanel.add(ElePanel_Name);
		LeftPanel.add(ActPanel);
		LeftPanel.add(VerPanel);
		LeftPanel.add(RunPanel);
		RightPanel.add(panelLog);
		panelDown.add(LeftPanel);
		panelDown.add(RightPanel);

		return panelDown;
	}

	// Bricks button listener
	public void addListener() {

		buttonDocRead.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser jfc = new JFileChooser(new File(CURRENT_DIR + "/json"));
					jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					jfc.showDialog(new JLabel(), "Choose");
					File selectedFile = jfc.getSelectedFile();
					if (selectedFile == null) {
						appStartName = "";
					} else {
						filepath = jfc.getSelectedFile().getPath();
						DataFrom.setText(filepath.substring(filepath.lastIndexOf("\\") + 1));
						tmpJson = Utils_File.loadJson(filepath);
						String filename = filepath.substring(filepath.lastIndexOf("\\") + 1);
						appStartName = filename.substring(0, filename.indexOf("_"));
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		buttonJsonLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (appStartName.equals(""))
						appStartName = filepath.substring(filepath.lastIndexOf("\\") + 1).substring(0,
								filepath.substring(filepath.lastIndexOf("\\") + 1).indexOf("_"));
					// model.getDataVector().clear();
					for (int i = 0; i < tmpJson.size(); i++) {
						String str = JSONObject.toJSONString(tmpJson.get(i));
						Bricks_Bean brick = JSON.parseObject(str, Bricks_Bean.class);

						if (brick.getProperty().equals("ele")) {
							table_row[1] = Utils_Property.getProperty("bricks.ui.casecre.ele");
							table_row[2] = filepath.substring(filepath.lastIndexOf("\\") + 1,
									filepath.lastIndexOf("_"));
							table_row[3] = brick.getEle_page();
							table_row[4] = brick.getCustom_name();
							model.addRow(table_row);
						} else if (brick.getProperty().equals("act")) {
							actionLoad(brick.getAction_name(), brick, true);
							table_row[1] = Utils_Property.getProperty("bricks.ui.casecre.act");
							table_row[2] = act_name;
							table_row[3] = "N/A";
							table_row[4] = "N/A";
							model.addRow(table_row);
						} else if (brick.getProperty().equals("val")) {
							switch (brick.getValidation_name()) {
							case 1:
								table_row[1] = Utils_Property.getProperty("bricks.ui.casecre.textver");
								table_row[2] = brick.getParams().get("expect_text");
								table_row[3] = "N/A";
								table_row[4] = "N/A";
								break;
							case 2:
								table_row[1] = Utils_Property.getProperty("bricks.ui.casecre.extver");
								table_row[2] = "N/A";
								table_row[3] = "N/A";
								table_row[4] = "N/A";
								break;
							case 3:
								table_row[1] = Utils_Property.getProperty("bricks.ui.casecre.cmpver");
								table_row[2] = "N/A";
								table_row[3] = "N/A";
								table_row[4] = "N/A";
							}
							model.addRow(table_row);
						} else if (brick.getProperty().equals("time")) {
							table_row[1] = Utils_Property.getProperty("bricks.ui.casecre.timer");
							table_row[2] = brick.getParams().get("time");
							table_row[3] = "N/A";
							table_row[4] = "N/A";
							model.addRow(table_row);
						}

						caseList.add(brick);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		buttonEleAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					table_row[1] = Utils_Property.getProperty("bricks.ui.casecre.ele");
					table_row[2] = comboxAppName.getSelectedItem();
					table_row[3] = comboxViewName.getSelectedItem();
					table_row[4] = comboxEleName.getSelectedItem();

					Bricks_Bean brick = new Bricks_Bean();
					brick.setEle_xpath(xpath.toString());
					brick.setCustom_name(cus_name.toString());
					brick.setProperty("ele");
					brick.setEle_page(comboxViewName.getSelectedItem().toString());
					if (appStartName.equals(""))
						appStartName = appName.toString();
					System.out.println(appStartName);

					int i = casetable.getSelectedRow();
					if (i >= 0) {
						model.insertRow(i + 1, table_row);
						caseList.add(i + 1, brick);
					} else {
						model.addRow(table_row);
						caseList.add(brick);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		buttonActAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Bricks_Bean brick = new Bricks_Bean();
				brick.setAction_name(action);
				brick.setProperty("act");

				try {
					actionLoad(action, brick, false);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		buttonVerAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					popWin.popSelect(ver_type, null, 0);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		buttonScrshot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					screenPointGet(null);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		buttonRowDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// i = the index of the selected row
					int[] rows = casetable.getSelectedRows();
					int count = 0;
					if (rows.length > 0) {
						for (int i = 0; i < rows.length; i++) {
							model.removeRow(rows[i] - count);
							caseList.remove(rows[i] - count);
							count++;
						}
					} else {
						System.out.println("Delete Error");
					}
					if (caseList.size() == 0)
						appStartName = "";
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		buttonTimer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					popWin.popSelect(4, null, 0);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		buttonAddAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (comboxEleName.getSelectedItem() != null)
					buttonEleAdd.doClick();
				if (comboxActName.getSelectedItem() != null)
					buttonActAdd.doClick();
			}
		});

		buttonClear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				while (model.getRowCount() > 0)
					model.removeRow(0);

				caseList.clear();
				appStartName = "";
			}
		});

		buttonEleRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int i = casetable.getSelectedRow();

					if (i >= 0) {
						model.setValueAt(Utils_Property.getProperty("bricks.ui.casecre.ele"), i, 1);
						model.setValueAt(comboxAppName.getSelectedItem(), i, 2);
						model.setValueAt(comboxViewName.getSelectedItem(), i, 3);
						model.setValueAt(comboxEleName.getSelectedItem(), i, 4);

						Bricks_Bean brick = new Bricks_Bean();
						brick.setEle_xpath(xpath.toString());
						brick.setCustom_name(cus_name.toString());
						brick.setProperty("ele");
						brick.setEle_page(comboxViewName.getSelectedItem().toString());

						caseList.set(i, brick);
					} else {
						System.out.println("Update Ele Error");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		buttonActRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int i = casetable.getSelectedRow();

					if (i >= 0) {
						model.setValueAt(act_name, i, 1);
					} else {
						System.out.println("Update Act Error");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		buttonPlayList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String str = JSON.toJSONString(caseList);
				System.out.println(str);
				JSONArray jsonFile = JSON.parseArray(str);

				switch (appStartName) {
				case "DJI GO4":
					pkg = "dji.go.v4";
					break;
				case "DJI GO3":
					pkg = "dji.pilot";
					break;
				case "RM500 Launcher":
					pkg = "com.dpad.launcher";
					break;
				case "RM500 Settings":
					pkg = "com.android.settings";
					break;
				case "MG 1A/P":
					pkg = "dji.prof.mg";
					break;
				case "MG 1S":
					pkg = "dji.prof.args.tiny";
					break;
				case "DJI GO4 Pad":
					pkg = "dji.pilot.pad";
					break;
				}

				try {
					Main_Entry.cachedThreadPool.submit(new Runnable() {
						public void run() {
							new Execution_Main(logArea, device, pkg, 1).runTestCase(jsonFile);
						}
					});
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		buttonSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					JFrame cus_save_frame = new JFrame("SAVE");
					JLabel cus_save_label = new JLabel(Utils_Property.getProperty("bricks.ui.casecre.cussave"));
					JTextField cus_save_text = new JTextField();
					JButton cus_save_btn = new JButton("SAVE");
					JPanel cus_type_pane = new JPanel();
					JPanel cus_btn_pane = new JPanel();
					cus_save_frame.setSize(250, 120);
					cus_save_frame.setVisible(true);
					cus_save_frame.setLayout(new BorderLayout());
					cus_save_frame.setLocation(Main_Entry.frame.getLocationOnScreen());
					cus_save_frame.setLocationRelativeTo(Main_Entry.frame);
					cus_save_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					cus_save_label.setFont(Constants_UI.FONT_NORMAL);
					cus_save_text.setPreferredSize(new Dimension(100, 30));

					cus_type_pane.add(cus_save_label);
					cus_type_pane.add(cus_save_text);
					cus_btn_pane.add(cus_save_btn);
					cus_save_frame.add(cus_type_pane, BorderLayout.NORTH);
					cus_save_frame.add(cus_btn_pane, BorderLayout.SOUTH);

					cus_save_btn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								SimpleDateFormat timeFormat = new SimpleDateFormat("hhmmss");
								// String time =
								// timeFormat.format(Calendar.getInstance().getTime());
								String cus_name = cus_save_text.getText();

								File json = new File("json/" + appStartName + "_" + cus_name + ".json");
								if (!json.getParentFile().exists())
									json.getParentFile().mkdirs();

								String str = JSON.toJSONString(caseList);
								PrintWriter pw = new PrintWriter(
										new OutputStreamWriter(new FileOutputStream(json), StandardCharsets.UTF_8));
								// PrintWriter pw = new PrintWriter(new
								// FileWriter(json));
								pw.print(str);
								pw.flush();
								pw.close();
								cus_save_frame.dispose();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					});

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		buttonLogSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					JFrame log_save_frame = new JFrame("SAVE");
					JLabel log_save_label = new JLabel(Utils_Property.getProperty("bricks.ui.casecre.logsave"));
					JTextField log_save_text = new JTextField();
					JButton log_save_btn = new JButton("SAVE");
					JPanel log_type_pane = new JPanel();
					JPanel log_btn_pane = new JPanel();
					log_save_frame.setSize(250, 120);
					log_save_frame.setVisible(true);
					log_save_frame.setLayout(new BorderLayout());
					log_save_frame.setLocation(Main_Entry.frame.getLocationOnScreen());
					log_save_frame.setLocationRelativeTo(Main_Entry.frame);
					log_save_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					log_save_label.setFont(Constants_UI.FONT_NORMAL);
					log_save_text.setPreferredSize(new Dimension(100, 30));

					log_type_pane.add(log_save_label);
					log_type_pane.add(log_save_text);
					log_btn_pane.add(log_save_btn);
					log_save_frame.add(log_type_pane, BorderLayout.NORTH);
					log_save_frame.add(log_btn_pane, BorderLayout.SOUTH);

					log_save_btn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								String log_name = log_save_text.getText();
								File logstream = new File("report/" + appStartName + "_" + log_name + ".txt");
								if (!logstream.getParentFile().exists())
									logstream.getParentFile().mkdirs();

								String str = logArea.getText();
								PrintWriter pw = new PrintWriter(new FileWriter(logstream));
								pw.print(str);
								pw.flush();
								pw.close();
								log_save_frame.dispose();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					});

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
	}

	// JTextArea output method
	class CustomOutputStream extends OutputStream {
		private JTextArea textArea;

		public CustomOutputStream(JTextArea textArea) {
			this.textArea = textArea;
		}

		@Override
		public void write(int b) throws IOException {
			// redirects data to the text area
			textArea.append(String.valueOf((char) b));
			// scrolls the text area to the end of data
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
	}

	class ActListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String action_name = (String) e.getItem();
				switch (action_name) {
				case "Single-Click":
					action = 0;
					break;
				case "Long-Press":
					action = 1;
					break;
				case "Set Text":
					action = 2;
					break;
				case "Point Drag":
					action = 4;
					break;
				case "Push To Device":
					action = 5;
					break;
				case "Reboot Device":
					action = 6;
					break;
				case "Scroll":
					action = 7;
					break;
				case "Key HOME":
					action = 8;
					break;
				case "Key BACK":
					action = 9;
					break;
				case "SeekBar Drag":
					action = 10;
					break;
				case "Spinner select":
					action = 11;
					break;
				case "Tap Point":
					action = 12;
					break;
				case "Save text to tmp":
					action = 13;
					break;
				case "Send adb cmd":
					action = 14;
					break;
				case "Appium reinit":
					action = 16;
					break;
				case "Open switch":
					action = 17;
					break;
				case "Close switch":
					action = 18;
					break;
				}
			}
		}
	}

	public void observe(Observable o) {
		o.addObserver(this);
	}

	@Override
	public void frameImageChange(BufferedImage image) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ADBChange(IDevice[] devices) {
		// TODO Auto-generated method stub
		this.device = devices[0];
	}

	private Point point_chosen = null;
	private ImageIcon Scrshot_image = null;

	public void screenPointGet(Bricks_Bean brick) {
		JFrame scrshot_frame = new JFrame();
		scrshot_frame.setSize(550, 650);
		scrshot_frame.setTitle("ScreenShot View");
		scrshot_frame.setLayout(new BorderLayout());
		scrshot_frame.setVisible(true);
		Scrshot_image = new ImageIcon(scrshot_pathname.toString());
		JLabel picLabel = new JLabel(Scrshot_image);
		JPanel posibtn_pane = new JPanel();
		buttonDragAdd = new Custom_Button(Constants_UI.ICON_ELE_ADD, Constants_UI.ICON_ELE_ADD_ENABLE,
				Constants_UI.ICON_ELE_ADD_DISABLE, Utils_Property.getProperty("bricks.ui.casecre.btntip.dragpoint"));
		JLabel pointx = new JLabel();
		JLabel pointy = new JLabel();
		posibtn_pane.add(pointx);
		posibtn_pane.add(pointy);
		posibtn_pane.add(buttonDragAdd);
		scrshot_frame.add(picLabel, BorderLayout.NORTH);
		scrshot_frame.add(posibtn_pane, BorderLayout.SOUTH);
		scrshot_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		scrshot_frame.setLocation(Main_Entry.frame.getLocationOnScreen());
		scrshot_frame.setLocationRelativeTo(Main_Entry.frame);
		point_chosen = null;

		if (brick != null) {
			picLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					CollectingOutputReceiver receiver = new CollectingOutputReceiver();
					String output = "";
					try {
						device.executeShellCommand("wm size", receiver, 0, TimeUnit.SECONDS);
						output = receiver.getOutput();
					} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException
							| IOException e2) {
						e2.printStackTrace();
					}
					int y = Integer.parseInt(output.split(":")[1].trim().split("\n")[0].split("x")[0]);
					int x = Integer.parseInt(output.split(":")[1].trim().split("\n")[0].split("x")[1]);

					double xRatio = ((double) e.getX()) / ((double) Scrshot_image.getIconWidth());
					double yRatio = ((double) e.getY()) / ((double) Scrshot_image.getIconHeight());
					scrshot_X = (int) (xRatio * x);
					scrshot_Y = (int) (yRatio * y);
					if (point_chosen == null) {
						point_chosen = new Point(scrshot_X, scrshot_Y);
					} else {
						point_chosen.x = scrshot_X;
						point_chosen.y = scrshot_Y;
					}
					pointx.setText("X:" + scrshot_X);
					pointy.setText("Y:" + scrshot_Y);

					buttonDragAdd.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {

							try {
								table_row[1] = Utils_Property.getProperty("bricks.ui.casecre.act");
								table_row[2] = Utils_Property.getProperty("bricks.ui.casecre.act.pd");
								table_row[3] = "X:" + scrshot_X;
								table_row[4] = "Y:" + scrshot_Y;

								Map point = new HashMap();
								point.put("DesPoint", point_chosen);
								brick.setParams(point);

								int i = casetable.getSelectedRow();
								if (i >= 0) {
									model.insertRow(i + 1, table_row);
									caseList.add(i + 1, brick);
								} else {
									model.addRow(table_row);
									caseList.add(brick);
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							scrshot_frame.dispose();
						}
					});
				}
			});
		}
	}

	private void addEleCombox() {
		comboxAppName = new JComboBox<String>();
		comboxAppName.addItem("DJI GO3");
		comboxAppName.addItem("DJI GO4");
		comboxAppName.addItem("DJI Pilot");
		comboxAppName.addItem("RM500 Launcher");
		comboxAppName.addItem("RM500 Settings");
		comboxAppName.addItem("MG 1A/P");
		comboxAppName.addItem("MG 1S");
		comboxAppName.addItem("DJI GO4 Pad");
		comboxAppName.setEditable(false);
		comboxAppName.setSelectedItem(null);
		comboxAppName.setPreferredSize(Constants_UI.TEXT_COMBOX_SIZE_ITEM);

		comboxViewName = new JComboBox<String>();
		comboxViewName.setEditable(true);
		comboxViewName.setPreferredSize(Constants_UI.TEXT_COMBOX_SIZE_ITEM);

		comboxEleName = new JComboBox<String>();
		comboxEleName.setEditable(true);
		comboxEleName.setPreferredSize(Constants_UI.TEXT_COMBOX_SIZE_ITEM);

		AutoCompleteSupport.install(comboxViewName, actEventList);
		AutoCompleteSupport.install(comboxEleName, eleEventList);
		JTextField textView = (JTextField) comboxViewName.getEditor().getEditorComponent();
		JTextField textEle = (JTextField) comboxEleName.getEditor().getEditorComponent();
		textView.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				actEventList.clear();
				comboxEleName.setSelectedItem(null);
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				if (comboxAppName.getSelectedItem() != null) {
					ResultSet rs = null;
					try {
						String app = new String(((String) comboxAppName.getSelectedItem()).getBytes(), "UTF-8");
						if (app.equals(""))
							return;
						appName.delete(0, appName.length()).append(app);
						rs = sql.queryElement("ACTIVITY", appName.toString());

						ArrayList<String> actList = new ArrayList<>();

						while (rs.next()) {
							actList.add(new String(rs.getBytes("ACTIVITY_NAME"), "UTF-8"));
						}
						actEventList.addAll(actList);
					} catch (UnsupportedEncodingException | SQLException e1) {
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
		});

		textEle.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				eleEventList.clear();
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				String viewName = textView.getText();
				ResultSet rs = sql.queryElement("ELEMENT", appName.toString(), viewName);

				ArrayList<String> eleList = new ArrayList<>();
				try {
					rs.next();
					String eleFirst = new String(rs.getBytes(1), "UTF-8");
					eleEventList.add(eleFirst);
					while ((rs.next())) {
						String eleName = rs.getString(1);
						eleList.add(eleName);
					}
					eleEventList.addAll(eleList);
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
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
		});

		comboxViewName.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				eleEventList.clear();
			}
		});

		comboxEleName.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String ele_cus = (String) e.getItem();
					xpathSet = sql.queryElement("ELEMENT", comboxAppName.getSelectedItem().toString(),
							comboxViewName.getSelectedItem().toString(), ele_cus);
				}

				try {
					while (xpathSet.next()) {
						xpath.delete(0, xpath.length()).append(xpathSet.getString("XPATH"));
						cus_name.delete(0, cus_name.length()).append(xpathSet.getString("CUSTOM_NAME"));
						scrshot_pathname.delete(0, scrshot_pathname.length()).append(xpathSet.getString("SCREEN_PATH"));
						String state = xpathSet.getString(5);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				} finally {

				}
			}
		});
	}

	private void actionLoad(int actionInput, Bricks_Bean brick, boolean caseLoad) {
		boolean contains = false;
		switch (actionInput) {
		case 0:
			act_name = "Click";
			break;
		case 1:
			act_name = "Long Press";
			break;
		case 2:
			act_name = "Set Text";
			if (!caseLoad)
				popWin.popSelect(5, brick, 1);
			break;
		case 4:
			act_name = "Point Drag";
			if (!caseLoad)
				screenPointGet(brick);
			break;
		case 5:
			act_name = "Push To Device";
			break;
		case 6:
			act_name = "Reboot Device";
			break;
		case 7:
			act_name = "Scroll";
			if (!caseLoad)
				popWin.popSelect(6, brick, 2);
			break;
		case 8:
			act_name = "Key HOME";
			break;
		case 9:
			act_name = "Key BACK";
			break;
		case 10:
			act_name = "DB";
			break;
		case 11:
			act_name = "Spinner select";
			if (!caseLoad)
				popWin.popSelect(5, brick, 2);
			break;
		case 12:
			act_name = "Tap Point";
			if (!caseLoad)
				screenPointGet(brick);
			break;
		case 13:
			act_name = "Save text to tmp";
			break;
		case 14:
			act_name = "Send adb cmd";
			if (!caseLoad)
				popWin.popSelect(5, brick, 3);
			break;
		case 16:
			act_name = "Appium reinit";
			break;
		case 17:
			act_name = "Open switch";
			break;
		case 18:
			act_name = "Close switch";
			break;
		}

		for (int i = 0; i < speAddList.length; i++) {
			if (speAddList[i] == actionInput)
				contains = true;
		}

		if (!contains && !caseLoad) {
			table_row[1] = Utils_Property.getProperty("bricks.ui.casecre.act");
			table_row[2] = act_name;
			table_row[3] = "N/A";
			table_row[4] = "N/A";

			int i = casetable.getSelectedRow();
			if (i >= 0) {
				model.insertRow(i + 1, table_row);
				caseList.add(i + 1, brick);
			} else {
				model.addRow(table_row);
				caseList.add(brick);
			}
		}
	}
}
