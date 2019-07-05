package com.bricks.background_runner.execution;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;
import com.bricks.background_runner.base.Cus_Action;
import com.bricks.background_runner.base.Cus_Element;
import com.bricks.background_runner.base.Cus_Validation;
import com.bricks.tools.Utils_Excel;
import com.bricks.tools.Utils_Timer;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.events.api.general.AppiumWebDriverEventListener;

/**
 * 
 * @author DraLastat
 * @Description 
 */

public class Testcase_Runner implements AppiumWebDriverEventListener{
	private static final Logger LOG = Logger.getLogger("RunTestCase.class");
	
	private WebElement ele_sub;
	private String ele_customName;
	private int runMode;
	private AndroidDriver driver;
	private Cus_Action action;
	private Cus_Validation validation;
	private String pkg;
	private JTextArea logText;
	private IDevice device;
	private Utils_Excel exlUtils;
	private XSSFCellStyle style;
	private String caseFailRecordPath;
	private String caseName;
	
	private XSSFSheet resultSheet;
	private XSSFRow resultRow;
	private int resultRowNum;
	private Map<String, Object[]> resultInfo = null;
	
	public Testcase_Runner(int runMode, AndroidDriver driver, JTextArea logText, IDevice device, String pkg) {
//		this.path = path;
		this.runMode = runMode;
		this.driver = driver;
		this.logText = logText;
		this.device = device;
		this.pkg = pkg;
		init();
	}
	
	private void init() {
		action = new Cus_Action(driver, device);
		validation = new Cus_Validation(driver);
		exlUtils = Utils_Excel.getInstance();
		
		style = exlUtils.getCellAlignCenter(exlUtils.getWorkbookResult());
		resultSheet = exlUtils.getResultSheet("CaseResult");
		resultRowNum = resultSheet.getLastRowNum();
		resultInfo = new TreeMap<String, Object[]>();
		if (resultRowNum == 0) {
			Object[] arrayOb = new Object[5];
			arrayOb[0] = "time";
			arrayOb[1] = "case_name";
			arrayOb[2] = "state";
			arrayOb[3] = "info";
			arrayOb[4] = "Screenshot Path";
			
			resultInfo.put("1", arrayOb);
			int rowId = 0;
			int cellId = 0;
			
			resultRow = resultSheet.createRow(rowId++);
			for (Object obj : arrayOb) {
				Cell cell = resultRow.createCell(cellId ++);
				cell.setCellValue((String) obj);
			}
			exlUtils.updateResultWorkbook();
		}
	}
	
	public void run(JSONArray jsonFile, String caseName) {
		this.caseName = caseName;
		int actionCount = 1;
		StringBuilder tmpStore1 = new StringBuilder();
		StringBuilder tmpStore2 = new StringBuilder();
		
		Object[] resultList = new Object[5];
		resultList[0] = Utils_Timer.formatTimeStamp(System.currentTimeMillis());
		resultList[1] = caseName;
		resultList[2] = "Pass";
		resultList[3] = "";
		resultList[4] = "";
		
		//running start
		if (this.runMode == 0) {
			for (int i=0; i<jsonFile.size(); i++) {
				JSONObject obj = jsonFile.getJSONObject(i);
				try {
					if (obj.getString("property").equals("ele")) {
							ele_sub = new Cus_Element(Appium_Init.WAIT_TIME, driver).explicitlyWait(obj.getString("ele_xpath"));
							this.ele_customName = obj.getString("custom_name");
					} else if (obj.getString("property").equals("act")) {
						Thread.sleep(300);
						actionCount ++;
//					    getScreenshot(screenshotRunPath, actionCount);
						this.actionSwitch(obj, tmpStore1, tmpStore2);
						
//						performanceGet(actionCount, cpuTotalList, cpuProcessList, memList, fpsList, powerList);
					} else if (obj.getString("property").equals("val")) {
						this.validationSwitch(obj, tmpStore1);
						Thread.sleep(1000);
					} else if (obj.getString("property").equals("time")) {
						int time = ((Integer)obj.getJSONObject("params").get("time")) * 1000;
						Thread.sleep(time);
					}
				} catch (Exception e) {
					caseFailRecordPath = System.getProperty("user.dir") + File.separator + "screenshot/CaseFailScreenshot" + File.separator + caseName.substring(0, caseName.length()-5) + "-" + actionCount;
					try {
						CollectingOutputReceiver receiver = new CollectingOutputReceiver();
						String logCmd = "logcat -v time -d > /sdcard/log-"
								+ Utils_Timer.formatTimeForFile(System.currentTimeMillis()) + ".log";
						device.executeShellCommand(logCmd, receiver);
					} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException
							| IOException e3) {
						e3.printStackTrace();
					}

					File caseFailRecord = new File(caseFailRecordPath);
					if (!caseFailRecord.getParentFile().exists())
						caseFailRecord.getParentFile().mkdirs();
					
					String a[] = caseName.split(".json");
					
					try {
						getScreenshot(caseFailRecordPath + File.separator + a[0], actionCount);
					} catch (TimeoutException | AdbCommandRejectedException | IOException
							| ShellCommandUnresponsiveException | SyncException e2) {
						e2.printStackTrace();
					}
					resultList[2] = "Fail";
					resultList[3] = e.getMessage();
					resultList[4] = caseFailRecordPath + File.separator + a[0] + "_" + actionCount + ".png";
					resultList[4] = caseFailRecordPath;
					action.keyBACK();
					try {
						Thread.sleep(300);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					action.keyBACK();
					if (e instanceof NoSuchElementException)
						logText.append("No such element: " + this.ele_customName + "\n");
					else {
						logText.append("Case Failed" + "\n");
					}
					LOG.error(e);
					break;
				}
			}
			resultInfo.put(String.valueOf(resultRowNum++), resultList);
			resultRow= resultSheet.createRow(resultRowNum);
			int cellid = 0;
			for (Object obj : resultList) {
				Cell cell = resultRow.createCell(cellid++);
				if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Integer)
					cell.setCellValue((Integer) obj);
				else if (obj instanceof Float) 
					cell.setCellValue((Float) obj);
				cell.setCellStyle(style);
			}
			exlUtils.updateResultWorkbook();
		}
	}
	
	public void actionSwitch(JSONObject action_info, StringBuilder tmpStore1, StringBuilder tmpStore2) throws Exception {
		int action_name = action_info.getIntValue("action_name");
		switch (action_name) {
			case 0:
				action.click(ele_sub);
				logText.append(this.ele_customName + " is clicked" + "\n");
				break;
			case 1:
				action.longPress(ele_sub);
				logText.append(this.ele_customName + " is long pressed" + "\n");
				break;
			case 2:
				JSONObject params_text = action_info.getJSONObject("params");
				action.setText(ele_sub, params_text.getString("inputText"));
				logText.append(this.ele_customName + " set text successed" + "\n");
				break;
			case 4:
				JSONObject params_drag = action_info.getJSONObject("params");
				action.pointDrag(ele_sub, new Point(params_drag.getJSONObject("DesPoint").getIntValue("x"), params_drag.getJSONObject("DesPoint").getIntValue("y")));
				Thread.sleep(500);
				logText.append(this.ele_customName + " is draged to the position" + "\n");
				break;
			case 5:
				action.pushFileToDevice();
				logText.append("Pushing files..." + "\n");
				break;
			case 6:
				action.reboot();
				logText.append("Rebooting..." + "\n");
				break;
			case 7:
				JSONObject params_swipe = action_info.getJSONObject("params");
				if (params_swipe.getString("elePath") == null) {
					String scrollPosition = params_swipe.getString("scrollPos");
					double position = 0.0;
					switch (scrollPosition) {
						case "From quarter":
							position = 0.25;
							break;
						case "From half":
							position = 0.5;
							break;
						case "From Bottom":
							position = 1;
							break;
					}
					action.swipe(position, params_swipe.getString("containerPath"), params_swipe.getIntValue("heading"));
				} else {
					action.swipe(params_swipe.getString("elePath"), params_swipe.getString("containerPath"), params_swipe.getIntValue("heading"));
				}
				logText.append("Page is swiped" + "\n");
				break;
			case 8:
				action.keyHOME();
				logText.append("Press HOME" + "\n");
				break;
			case 9:
				action.keyBACK();
				logText.append("Press BACK" + "\n");
				break;
			case 10:
				action.dragBar(ele_sub);
				logText.append(this.ele_customName + " is dragged" + "\n");
				break;
			case 11:
				JSONObject params_spinner = action_info.getJSONObject("params");
				action.spinnerSelect(ele_sub, params_spinner.getString("choose"));
				logText.append("Spinner choose " + params_spinner.getString("choose") + "\n");
				break;
			case 12:
				JSONObject params_tap = action_info.getJSONObject("params");
				action.tapPoint(params_tap.getJSONObject("DesPoint").getIntValue("x"), params_tap.getJSONObject("DesPoint").getIntValue("y"));
				logText.append("Point is tapped" + "\n");
				break;
			case 13:
				action.saveToTmp(ele_sub, tmpStore1);
				logText.append(this.ele_customName + " value saved to tmp" + "\n");
				break;
			case 14:
				JSONObject params_adb = action_info.getJSONObject("params");
				action.sendAdb(params_adb.getString("cmd"));
				logText.append("Adb cmd is send" + "\n");
				break;
			case 15:
				JSONObject params_eles = action_info.getJSONObject("params");
				action.getChildViewNum(params_eles.getString("parentName"), params_eles.getString("childClass"), tmpStore2);
				logText.append("ChildView num is " + tmpStore2.toString() + " and save to tmp2 \n");
				break;
			case 16:
				driver = action.reInit(pkg);
				action.setDriver(driver);
				validation.setDriver(driver);
				logText.append("Appium reinit" + "\n");
				break;
			case 17:
				action.openswitch(ele_sub);
				logText.append(this.ele_customName + " switch has opened" + "\n");
				break;
			case 18:
				action.closeswitch(ele_sub);
				logText.append(this.ele_customName + " switch has closed" + "\n");
				break;
		}
		
	}
	
	public void validationSwitch(JSONObject validation_info, StringBuilder tmpStore) throws Exception {
		int validation_name = validation_info.getIntValue("validation_name");
		JSONObject params = validation_info.getJSONObject("params");
		
		switch (validation_name) {
			case 1:
				String ele_name_text = (String) params.get("ele_path");
				String except_text = (String) params.get("expect_text");
				if (validation.getText(ele_name_text, except_text))
					logText.append("Text validation success" + "\n");
				else
					logText.append("Text validation fail" + "\n");
				break;
			case 2:
				String ele_name_elval = (String) params.get("ele_path");
				if (validation.getExactEle(ele_name_elval))
					logText.append("Element validation success" + "\n");
				else
					logText.append("Element validation fail" + "\n");
				break;
			case 3:
				String ele_name_tmpVal = (String) params.get("ele_path");
				if (validation.checkTmp(ele_name_tmpVal, tmpStore))
					logText.append("Text remains" + "\n");
				else
					logText.append("Text differs" + "\n");
				break;
		}
	}

	public String getCaseName() {
		return caseName;
	}
	
	private void getScreenshot(String screenshotRunPath, int actionCount) throws TimeoutException, AdbCommandRejectedException, IOException, ShellCommandUnresponsiveException, SyncException {
		//adb screenshot
		String screenpath="/sdcard/" + actionCount+ ".png";
		device.executeShellCommand("screencap -p " + screenpath, new CollectingOutputReceiver());
		device.pullFile(screenpath, screenshotRunPath + ".png");
		device.executeShellCommand("rm " + screenpath, new CollectingOutputReceiver());
	}
	
	@Override
	public void afterChangeValueOf(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeChangeValueOf(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterAlertAccept(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterAlertDismiss(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterChangeValueOf(WebElement arg0, WebDriver arg1, CharSequence[] arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterClickOn(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateBack(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateForward(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateRefresh(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateTo(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterScript(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeAlertAccept(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeAlertDismiss(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeChangeValueOf(WebElement arg0, WebDriver arg1, CharSequence[] arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeClickOn(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateBack(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateForward(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateRefresh(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateTo(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeScript(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onException(Throwable arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
}
