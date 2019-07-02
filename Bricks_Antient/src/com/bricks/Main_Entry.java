package com.bricks;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.bricks.ui_pack.Constants_UI;
import com.bricks.ui_pack.panel.Panel_Case_Creator;
import com.bricks.ui_pack.panel.Panel_Case_Runner;
import com.bricks.ui_pack.panel.Panel_Case_Runner_Data;
import com.bricks.ui_pack.panel.Panel_Element_Creator;
import com.bricks.ui_pack.panel.Panel_Setting_Main;
import com.bricks.ui_pack.panel.Panel_Status;
import com.bricks.ui_pack.panel.Panel_Sidebar;
import com.bricks.background_runner.execution.Appium_Init;
import com.bricks.node_selection.Variable_Observer;
import com.bricks.tools.Device_Connector;
import com.bricks.tools.Utils_Excel;
import com.bricks.tools.Utils_SQL;

/**
 *
 * @author DraLastat
 */

public class Main_Entry implements Global_Observer {
	private static final Logger LOG = Logger.getLogger(Main_Entry.class);

	public static JFrame frame;
	private Panel_Sidebar toolbar;
	private static JPanel mainPanel;
	public static JPanel mainPanelCenter;
	public static Panel_Status statusPanel;
	public static Panel_Element_Creator elecrePanel;
	public static Panel_Case_Creator casecrePanel;
	public static Panel_Case_Runner caserunPanel;
	public static Panel_Setting_Main settingPanel;
	public static JDialog dialog;
	private Connection connection;

	private Utils_SQL sql;
	private Device_Connector adb;
	public static ExecutorService cachedThreadPool = Executors.newFixedThreadPool(100);
	private Variable_Observer obs = new Variable_Observer();
	public static Utils_Excel exlUtil = null;

	/**
	 * 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main_Entry window = new Main_Entry();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main_Entry() {
		initialize();
	}

	private void initialize() {
		PropertyConfigurator
				.configure(Constants_UI.CURRENT_DIR + File.separator + "config" + File.separator + "log4j.properties");
		LOG.info("==================BricksInitStart====================");

		// init adb
		adb = new Device_Connector();
		adb.registerObserver(Main_Entry.this);

		// init UImanager
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			LOG.error(e);
		}

		// init sqlite
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:ElementInfo.db");
			connection.setAutoCommit(true);

			sql = new Utils_SQL(connection);
			sql.creatTable();
		} catch (Exception e) {
			LOG.error(e);
			System.exit(0);
		}

		// init appium
		Thread appium = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Runtime.getRuntime().exec("cmd /c start appium");
				} catch (IOException e) {
					LOG.error(e);
				}
			}
		});
		cachedThreadPool.submit(appium);

		// init UI
		frame = new JFrame();
		frame.setBounds(Constants_UI.MAIN_WINDOW_X, Constants_UI.MAIN_WINDOW_Y, Constants_UI.MAIN_WINDOW_WIDTH,
				Constants_UI.MAIN_WINDOW_HEIGHT);
		frame.setTitle(Constants_UI.APP_NAME);
		frame.setIconImage(Constants_UI.IMAGE_ICON);
		frame.setBackground(Constants_UI.MAIN_BACK_COLOR);

		frame.setResizable(false);

		mainPanel = new JPanel(true);
		mainPanel.setBackground(Color.white);
		mainPanel.setLayout(new BorderLayout());

		toolbar = new Panel_Sidebar();
		statusPanel = new Panel_Status();
		elecrePanel = new Panel_Element_Creator(obs, sql);
		adb.registerObserver(elecrePanel);
		casecrePanel = new Panel_Case_Creator(sql);
		adb.registerObserver(casecrePanel);
		caserunPanel = new Panel_Case_Runner();
		adb.registerObserver(caserunPanel);
		settingPanel = new Panel_Setting_Main();

		mainPanel.add(toolbar, BorderLayout.WEST);

		mainPanelCenter = new JPanel(true);
		mainPanelCenter.setLayout(new BorderLayout());
		mainPanelCenter.add(statusPanel, BorderLayout.CENTER);

		mainPanel.add(mainPanelCenter, BorderLayout.CENTER);

		frame.add(mainPanel);

		obs.addObserver(elecrePanel);
		adb.init();
		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					// Runtime.getRuntime().exec("cmd start " +
					// System.getProperty("user.dir") + "/close.bat");
					Runtime.getRuntime().exec("taskkill /F /IM node.exe");

					connection.close();
				} catch (Exception e1) {
					LOG.error(e1);
				}
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				LOG.info("==================BricksEnd==================");

			}

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowActivated(WindowEvent e) {

			}
		});
	}

	@Override
	public void frameImageChange(BufferedImage image) {
	}

	@Override
	public void ADBChange(IDevice[] devices) {
	}
}
