package com.bricks.ui_pack.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.android.ddmlib.IDevice;
import com.bricks.Main_Entry;
import com.bricks.ui_pack.Constants_UI;
import com.bricks.ui_pack.Custom_Button;
import com.bricks.background_runner.Execution_Main;
import com.bricks.tools.Utils_Property;

/**
 * 
 * @author DraLastat
 * @Description 
 */

public class Panel_Case_Result extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private static Custom_Button buttonCaseFind;
	private static Custom_Button buttonCaseDelte;
	private static Custom_Button buttonStart;
	private static Custom_Button buttonChart;
	private static Custom_Button buttonLogSave;
	private static int run_time = 1;
	private Object[] table_row = new Object[2];

	private static Logger logger = Logger.getLogger(Panel_Case_Result.class);
	
	private String filepath;
	private JTextArea logprint;
	private IDevice device;
	private JSONArray jsonFile;
	private String appName;
	private List<String> case_list;
	private String pkg = "";
	private String fileName = "";
	
	public Panel_Case_Result(IDevice device) {
		this.device = device;
		initialize();
		addComponent();
		addListener();
	}

	private void initialize() {
		this.setBackground(Constants_UI.MAIN_BACK_COLOR);
		this.setLayout(new BorderLayout());
	}

	private void addComponent() {
		this.add(getCenterPanel(), BorderLayout.CENTER);

	}

	private JTextField case_name;
	private JTable List_table;
	private DefaultTableModel model;
	private JTextField run_num;
	private JPanel getCenterPanel() {
		JPanel panelCenter = new JPanel();
		panelCenter.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelCenter.setLayout(new GridLayout(2, 1));
		
		JPanel panelGridCaseFind = new JPanel();
		panelGridCaseFind.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelGridCaseFind.setLayout(new GridLayout(2, 1));

        JPanel panelGrid1 = new JPanel();
        panelGrid1.setBackground(Constants_UI.MAIN_BACK_COLOR);
        panelGrid1.setLayout(new BorderLayout());
        JPanel panelGrid2 = new JPanel();
        panelGrid2.setBackground(Constants_UI.MAIN_BACK_COLOR);
//        panelGrid2.setLayout(new FlowLayout(FlowLayout.RIGHT, ConstantsUI.MAIN_H_GAP, 15));
        
		case_name = new JTextField();
		case_name.setEditable(false);
		case_name.setFont(Constants_UI.FONT_NORMAL);
		case_name.setPreferredSize(Constants_UI.TEXT_FIELD_SIZE_ITEM);
		case_list = new ArrayList();
		
		JPanel panelList = new JPanel();
		JPanel panelListBtn = new JPanel();
		panelList.setBackground(Color.WHITE);
		panelListBtn.setBackground(Color.WHITE);
		panelListBtn.setLayout(new GridLayout(2,1));
		List_table = new JTable();
		Object[] columns = {"JSON Name","Path"};
		model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        List_table.setModel(model);
        List_table.setBackground(Color.LIGHT_GRAY);
        List_table.setForeground(Color.black);
        Font font = new Font("",1,10);
        List_table.setFont(font);
        List_table.setRowHeight(30);
        List_table.setPreferredScrollableViewportSize(new Dimension(500, 100));
        
        JLabel multirun_label1 = new JLabel(Utils_Property.getProperty("bricks.ui.caserun.runtimes1"));
        JLabel multirun_label2 = new JLabel(Utils_Property.getProperty("bricks.ui.caserun.runtimes2"));
        JLabel label_null = new JLabel();
        run_num = new JTextField();
        
        multirun_label1.setFont(Constants_UI.FONT_NORMAL);
        multirun_label2.setFont(Constants_UI.FONT_NORMAL);
        run_num.setPreferredSize(new Dimension(25,20));
        run_num.setBackground(Color.WHITE);
        run_num.setText("1");
        label_null.setPreferredSize(new Dimension(400,20));
		
		buttonCaseFind = new Custom_Button(Constants_UI.ICON_DOCREAD, Constants_UI.ICON_DOCREAD_ENABLE,
                Constants_UI.ICON_DOCREAD_DISABLE, Utils_Property.getProperty("bricks.ui.casecre.btntip.docread"));
		buttonCaseDelte = new Custom_Button(Constants_UI.ICON_ROW_DELETE, Constants_UI.ICON_ROW_DELETE_ENABLE,
                Constants_UI.ICON_ROW_DELETE_DISABLE, Utils_Property.getProperty("bricks.ui.casecre.btntip.docdelte"));
        buttonStart = new Custom_Button(Constants_UI.ICON_PLAY_LIST, Constants_UI.ICON_PLAY_LIST_ENABLE,
				Constants_UI.ICON_PLAY_LIST_DISABLE, Utils_Property.getProperty("bricks.ui.casecre.btntip.playlist"));
        //buttonStart.setEnabled(false);
        buttonChart = new Custom_Button(Constants_UI.ICON_RTCHART, Constants_UI.ICON_RTCHART_ENABLE,
				Constants_UI.ICON_RTCHART_DISABLE, Utils_Property.getProperty("bricks.ui.casecre.btntip.rtchart"));
        //buttonStop.setEnabled(false);
        
        panelList.add(new JScrollPane(List_table));
        panelListBtn.add(buttonCaseFind);
        panelListBtn.add(buttonCaseDelte);
        panelGrid1.add(panelList, BorderLayout.WEST);
        panelGrid1.add(panelListBtn, BorderLayout.CENTER);
        panelGrid2.add(multirun_label1);
        panelGrid2.add(run_num);
        panelGrid2.add(multirun_label2);
        panelGrid2.add(label_null);
        panelGrid2.add(buttonChart);
        panelGrid2.add(buttonStart);

        panelGridCaseFind.add(panelGrid1);
        panelGridCaseFind.add(panelGrid2);
		
		JPanel panelGridLog = new JPanel();
		panelGridLog.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelGridLog.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 0));

		JLabel labelRunLogTitle = new JLabel(Utils_Property.getProperty("bricks.ui.caserun.logprint"));
		JLabel labellogprintNull = new JLabel();
		JLabel labellogprintNull2 = new JLabel();
		logprint = new JTextArea(12, 68);
		logprint.setBorder(null);
		logprint.setBorder(new EmptyBorder(0,0,0,0));
		logprint.setLineWrap(true);
        logprint.setWrapStyleWord(true);
        logprint.setForeground(Constants_UI.MAIN_BACK_COLOR);
        logprint.setBackground(Constants_UI.TABLE_BACK_COLOR);
		buttonLogSave = new Custom_Button(Constants_UI.ICON_LOGSAVE, Constants_UI.ICON_LOGSAVE_ENABLE,
				Constants_UI.ICON_LOGSAVE_DISABLE, Utils_Property.getProperty("bricks.ui.casecre.btntip.logsave"));
		labelRunLogTitle.setFont(Constants_UI.FONT_NORMAL);
		labelRunLogTitle.setPreferredSize(Constants_UI.LABLE_SIZE_ITEM);
		labellogprintNull.setPreferredSize(Constants_UI.LABLE_SIZE_CASE_NULL_ITEM);
		labellogprintNull2.setPreferredSize(new Dimension(480,40));
		panelGridLog.add(labelRunLogTitle);
		panelGridLog.add(labellogprintNull);
		panelGridLog.add(new JScrollPane(logprint));
		panelGridLog.add(labellogprintNull2);
		panelGridLog.add(buttonLogSave);


		panelCenter.add(panelGridCaseFind);
		panelCenter.add(panelGridLog);
		return panelCenter;
	}
	
	public void addListener() {
		
		buttonCaseFind.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser jfc = new JFileChooser(new File(System.getProperty("user.dir") + "/json"));  
                    jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
                    jfc.setMultiSelectionEnabled(true);
                    jfc.showDialog(new JLabel(), "选择"); 
                    File[] files = jfc.getSelectedFiles();
                    filepath = files[0].getPath();
                    
                    fileName = filepath.substring(filepath.lastIndexOf("\\")+1);
                    appName = fileName.substring(0, fileName.indexOf("_"));
                    
                    for (int i=0; i<files.length; i++) {
	                    table_row[0] = files[i].getPath().substring(filepath.lastIndexOf("\\")+1);
	                    table_row[1] = files[i].getPath();
	                    model.addRow(table_row);
	                    case_list.add(files[i].getPath());
                    }
                } catch (Exception e1) {
                    logger.error("open table_field file fail:" + e1.toString());
                    e1.printStackTrace();
                }
            }
        });
		
		buttonCaseDelte.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
            	try {
                    // i = the index of the selected row
                    int i = List_table.getSelectedRow();
                    if(i >= 0){
                        model.removeRow(i);
                        case_list.remove(i);
                        System.out.println(case_list);
                    }
                    else{
                        System.out.println("Delete Error");
                    }
                } catch (Exception e1) {
                	e1.printStackTrace();
                }

            }
	  	});
		
		buttonStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Main_Entry.cachedThreadPool.execute((new Runnable() {
					
					@Override
					public void run() {
						
						switch (appName) {
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
		            			pkg = "com.android.settings.Settings";
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
						
						logprint.setText("");
						new Execution_Main(case_list, logprint, device, pkg, Integer.parseInt(run_num.getText())).runTestCase();;
					}
				}));
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
                	log_save_frame.setSize(250,120);
                	log_save_frame.setVisible(true);
                	log_save_frame.setLayout(new BorderLayout());
                	log_save_frame.setLocation(Main_Entry.frame.getLocationOnScreen());  
                	log_save_frame.setLocationRelativeTo(Main_Entry.frame);
                	log_save_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            		log_save_label.setFont(Constants_UI.FONT_NORMAL);
            		log_save_text.setPreferredSize(new Dimension(100,30));
            		
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
                            	File logstream = new File("report/" + log_name + ".txt");
                            	if (!logstream.getParentFile().exists())
                            		logstream.getParentFile().mkdirs();
                            	
                            	String str = logprint.getText();
                            	PrintWriter pw = new PrintWriter(new FileWriter(logstream));
                                pw.print(str);
                                pw.flush();
                                pw.close();
                                log_save_frame.dispose();
                        	}catch (Exception e1) {
                        		e1.printStackTrace();
                        	}
                    	}
                    });

                } catch (Exception e1) {
                	e1.printStackTrace();
                }

            }
        });
		
//		buttonChart.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				JFrame appSelectFrame = new JFrame();
//				appSelectFrame.setSize(270, 130);
//				appSelectFrame.setTitle("App Select");
//				appSelectFrame.setLayout(new BorderLayout());
//				appSelectFrame.setLocation(MainEntry.frame.getLocationOnScreen());
//				appSelectFrame.setLocationRelativeTo(MainEntry.frame);
//				
//				JPanel appSelectPanel = new JPanel();
//				appSelectPanel.setPreferredSize(new Dimension(270, 60));
//				JLabel appName = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.appname"));
//				appName.setFont(ConstantsUI.FONT_NORMAL);
//				JComboBox<String> comboxAppName = new JComboBox<String>();
//				comboxAppName.setPreferredSize(new Dimension(150, 40));
//				comboxAppName.addItem("DJI GO3");
//				comboxAppName.addItem("DJI GO4");
//				comboxAppName.addItem("DJI Pilot");
//				comboxAppName.addItem("RM500 Launcher");
//				comboxAppName.addItem("RM500 Settings");
//				comboxAppName.addItem("MG 1A/P");
//				comboxAppName.addItem("MG 1S");
//				comboxAppName.addItem("DJI GO4 Pad");
//				comboxAppName.addItemListener(new ItemListener() {
//					
//					@Override
//					public void itemStateChanged(ItemEvent e) {
//						// TODO Auto-generated method stub
//						if (e.getStateChange() == ItemEvent.SELECTED) {
//							switch ((String)e.getItem()) {
//								case "DJI GO4":
//			            			pkg = "dji.go.v4";
//			            			break;
//			            		case "DJI GO3":
//			            			pkg = "dji.pilot";
//			            			break;
//			            		case "RM500 Launcher":
//			            			pkg = "com.dpad.launcher";
//			            			break;
//			            		case "RM500 Settings":
//			            			pkg = "com.android.settings.Settings";
//			            			break;
//			            		case "MG 1A/P":
//			            			pkg = "dji.prof.mg";
//			            			break;
//			            		case "MG 1S":
//			            			pkg = "dji.prof.args.tiny";
//			            			break;
//			            		case "DJI GO4 Pad":
//			            			pkg = "dji.pilot.pad";
//			            			break;
//							}
//						}
//					}
//				});
//				
//				MyIconButton buttonWatchStart = new MyIconButton(ConstantsUI.ICON_PLAY, ConstantsUI.ICON_PLAY_ENABLE,
//						ConstantsUI.ICON_PLAY_DISABLE, PropertyUtil.getProperty("bricks.ui.runresult.btnwatch"));
//				buttonWatchStart.addActionListener(new ActionListener() {
//					
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
//				
//				appSelectPanel.add(appName);
//				appSelectPanel.add(comboxAppName);
//				appSelectFrame.add(appSelectPanel, BorderLayout.NORTH);
//				appSelectFrame.add(buttonWatchStart, BorderLayout.SOUTH);
//				appSelectFrame.setVisible(true);
//			}
//		});
	}
}
