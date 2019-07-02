package com.bricks.background_runner.base;

import org.openqa.selenium.WebElement;

import com.bricks.background_runner.execution.Appium_Init;

import io.appium.java_client.android.AndroidDriver;

/**
 * 
 * @author DraLastat
 * @Description 
 */

public class Cus_Validation {
	
	private AndroidDriver driver;	
	private int[] validationList = new int[50];
	
	public Cus_Validation(AndroidDriver driver) {
		this.driver = driver;
	}
	
	public void setDriver(AndroidDriver driver) {
		this.driver = driver;
	}

	//1.text val
	public boolean getText(String eleName, String expectText) {
		WebElement ele = new Cus_Element(Appium_Init.WAIT_TIME, driver).explicitlyWait(eleName);
		String text = ele.getAttribute("text");
		
		if (text.equals(expectText))
			return true;
		
		return false;
	}
	
	//2.ele val
	public boolean getExactEle(String eleName) {
		WebElement ele = new Cus_Element(Appium_Init.WAIT_TIME, driver).explicitlyWait(eleName);
		
		if (ele != null)
			return true;
		return false;
	}
	
	public int getEleShowNum(String eleName) {
		WebElement ele = new Cus_Element(Appium_Init.WAIT_TIME, driver).explicitlyWait(eleName);
		int num = Integer.parseInt(ele.getAttribute("text"));
		
		return num;
	}
	
	//4.tmp val
	public boolean checkTmp(String eleName, StringBuilder tmpStore) {
		WebElement ele = new Cus_Element(Appium_Init.WAIT_TIME, driver).explicitlyWait(eleName);
		String text = ele.getAttribute("text");
		if (text.equals(tmpStore.toString()))
			return true;
		
		return false;
	}
}
