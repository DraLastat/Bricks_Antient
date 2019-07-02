package com.bricks.node_selection;

import java.util.Map;
import java.util.Observable;

/**
 * @author DraLastat
 * @Description listener for updating node information
 */

public class Variable_Observer extends Observable {
	private Map<String, String> node_info;
	
	public void setInfo(Map<String, String> node_info) {
		synchronized (this) {
			this.node_info = node_info;
		}
		setChanged();
		notifyObservers();
	}

	public synchronized Map<String, String> getInfo() {
		return node_info;
	}
}

