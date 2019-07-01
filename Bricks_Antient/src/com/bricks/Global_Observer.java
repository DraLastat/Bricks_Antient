package com.bricks;

import java.awt.image.BufferedImage;

import com.android.ddmlib.IDevice;


/**
 * @author DraLastat
 * @Description listener for refreshing screen
 */


public interface Global_Observer {
	
	public void frameImageChange(BufferedImage image);
	
	public void ADBChange(IDevice[] devices);
}
