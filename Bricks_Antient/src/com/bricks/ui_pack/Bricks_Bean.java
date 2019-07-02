package com.bricks.ui_pack;

import java.util.Map;

/**
 * 
 * @author DraLastat
 * @Description 
 */

public class Bricks_Bean {
	private String ele_xpath;
	private String custom_name;
	private String ele_page;
	private int action_name;
	private String property;
	private Map<String, Object> params;
	private int validation_name;
	
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	public int getValidation_name() {
		return validation_name;
	}
	public void setValidation_name(int validation_name) {
		this.validation_name = validation_name;
	}
	
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public int getAction_name() {
		return action_name;
	}
	public String getCustom_name() {
		return custom_name;
	}
	public String getEle_xpath() {
		return ele_xpath;
	}
	public void setAction_name(int action_name) {
		this.action_name = action_name;
	}
	public void setCustom_name(String custom_name) {
		this.custom_name = custom_name;
	}
	public void setEle_xpath(String ele_xpath) {
		this.ele_xpath = ele_xpath;
	}
	public String getEle_page() {
		return ele_page;
	}
	public void setEle_page(String ele_page) {
		this.ele_page = ele_page;
	}
}
