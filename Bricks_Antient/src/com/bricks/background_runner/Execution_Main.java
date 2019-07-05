package com.bricks.background_runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.logging.LogEntry;

import com.alibaba.fastjson.JSONArray;
import com.android.ddmlib.IDevice;
import com.bricks.Main_Entry;
import com.bricks.tools.Performance_Watcher;
import com.bricks.background_runner.execution.Appium_Init;
import com.bricks.background_runner.execution.Testcase_Runner;
import com.bricks.mini_decode.Mini_Cap_Util;
import com.bricks.tools.Utils_File;
import com.bricks.tools.Systeminfo_Get;

import io.appium.java_client.android.AndroidDriver;

/**
 * 
 * @author DraLastat
 * @Description 
 */

public class Execution_Main {
	private static final Logger LOG = Logger.getLogger("ExecutionMain.class");
	
	private String pkg = "";
	private String launchActivity = null;
	private String startAct = "";
	private PrintWriter log_file_writer;
	private boolean isRunning = false;
	private AndroidDriver driver;
	private List<String> caseList;
	private JTextArea logText;
	private IDevice device;
	private int num;
	private Testcase_Runner testCase;
	
//	private static final ExecutionMain instance = new ExecutionMain();
    
    public Execution_Main(List<String> caseList, JTextArea logText, IDevice device, String pkg, int num){
    	this.caseList = caseList;
    	this.logText = logText;
    	this.device = device;
    	this.pkg = pkg;
    	this.num = num;
    	init();
    }
    
    public Execution_Main(JTextArea logText, IDevice device, String pkg, int num){
    	this.logText = logText;
    	this.device = device;
    	this.pkg = pkg;
    	this.num = num;
    	init();
    }
    
//    private static class HolderClass {
//		private final static ExecutionMain instance = new ExecutionMain();
//	}
    
//    public static ExecutionMain getInstance(List<String> caseList1, JTextArea logText1, IDevice device1, String pkg1, int num1){
//    	caseList = caseList1;
//    	logText = logText1;
//    	device = device1;
//    	pkg = pkg1;
//    	num = num1;
//        return HolderClass.instance;
//    }
    
    private void init() {
    	isRunning = true;
    	
    	switch (pkg) {
			case "com.dji.industry.pilot":
				launchActivity = "com.dji.industry.pilot.SplashActivity";
				break;
			case "dji.pilot":
				launchActivity = "dji.pilot.main.activity.DJILauncherActivity";
				break;
			case "dji.go.v4":
				launchActivity = "dji.pilot.main.activity.DJILauncherActivity";
				break;
			case "com.dpad.launcher":
				launchActivity = "com.dpad.launcher.Launcher";
				break;
			case "dji.prof.mg": case "dji.prof.args.tiny":
				launchActivity = "dji.prof.mg.main.SplashActivity";
				break;
			case "dji.pilot.pad":
				launchActivity = "dji.pilot.main.activity.DJILauncherActivity";
				break;
			case "com.android.settings":
				launchActivity = "com.android.settings.Settings";
				break;
		}
    	
    	driver = Appium_Init.getInstance(device, pkg, launchActivity).getDriver();
    	Systeminfo_Get sysInfoGet = new Systeminfo_Get(device, pkg);
		
    	testCase = new Testcase_Runner(0, driver, logText, device, pkg);
    	
		Performance_Watcher watcher = new Performance_Watcher(device, pkg, sysInfoGet, testCase);
		Main_Entry.cachedThreadPool.submit(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				while (isRunning) {
					watcher.startWatch();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
    }
    
	public void runTestCase() {
		try {
			Mini_Cap_Util.getInstance(device).stopScreenListener();
			for (int i=0; i<num; i++) {
				for (int j=0; j<caseList.size(); j++) {
					String casePath = caseList.get(j);
					JSONArray jsonFile = Utils_File.loadJson(casePath);
					testCase.run(jsonFile, casePath.substring(casePath.lastIndexOf("\\")+1));
				}
			}
		} catch (NullPointerException e1) {
			LOG.error(e1);
		} catch (NoSuchSessionException e2) {
			LOG.error(e2);
		} catch (Exception e) {
			LOG.error(e);
		}finally {
			Mini_Cap_Util.getInstance(device).startScreenListener();
			try {
				isRunning = false;
//				saveLog(caseName);
				log_file_writer.close();
//				driver.quit();
			} catch (Exception e1) {
				LOG.error(e1);
			}
		}
	}
	
	public void runTestCase(JSONArray jsonFile) {
		try {
			Mini_Cap_Util.getInstance(device).stopScreenListener();
			testCase.run(jsonFile, "tmpTest");
		} catch (NullPointerException e1) {
			LOG.error(e1);
		} catch (NoSuchSessionException e2) {
			LOG.error(e2);
		} catch (Exception e) {
			LOG.error(e);
		}finally {
			Mini_Cap_Util.getInstance(device).startScreenListener();
			try {
				isRunning = false;
//				saveLog(caseName);
				log_file_writer.close();
//				driver.quit();
			} catch (Exception e1) {
				LOG.error(e1);
			}
		}
	}
	
	private void saveLog(String caseName) throws FileNotFoundException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	    Date date = Calendar.getInstance().getTime();
	    String reportDate = df.format(date);
	    
	    String logPath = System.getProperty("user.dir") + File.separator + "log" + File.separator + "AppiumLog_";
	    LOG.info(driver.getSessionId() + ": Saving device log...");
	    List<LogEntry> logEntries = driver.manage().logs().get("logcat").filter(Level.CONFIG);
	    File logFile = null;
    	logFile = new File(logPath + reportDate + "_" + caseName + ".log");
	    
	    if (!logFile.exists())
	    	logFile.getParentFile().mkdirs();
	    
		log_file_writer = new PrintWriter(logFile);
		for (int i=0; i<logEntries.size(); i++)
			log_file_writer.println(logEntries.get(i));
	    log_file_writer.flush();
	    LOG.info(driver.getSessionId() + ": Saving device log - Done.");
	}
	
	public String getPkg() {
		return pkg;
	}

	public void setPkg(String pkg) {
		this.pkg = pkg;
	}

	public String getStartAct() {
		return startAct;
	}

	public void setStartAct(String startAct) {
		this.startAct = startAct;
	}

}
