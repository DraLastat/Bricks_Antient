package com.bricks;


/**
 * @author DraLastat
 * @Description listener for subject
 */

public interface Subject_Listener {
	
	public void registerObserver(Global_Observer o);

	public void removeObserver(Global_Observer o);

	public void notifyObservers(Object obj);
}
