package com.bricks.node_selection;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.SyncService;
import com.android.ddmlib.TimeoutException;

/**
 * @author DraLastat
 * @Description generate result, including model, xpath file
 */

public class Automator_Helper_UI {
    public static final int UIAUTOMATOR_MIN_API_LEVEL = 16;

    private static final String UIAUTOMATOR = "/system/bin/uiautomator";    //$NON-NLS-1$
    private static final String UIAUTOMATOR_DUMP_COMMAND = "dump";          //$NON-NLS-1$
    private static final String UIDUMP_DEVICE_PATH = "/data/local/tmp/uidump.xml";  //$NON-NLS-1$
    private static final String UIDUMP_DEVICE_PATH_UNIT = "/storage/emulated/0/Android/data/dan.dji.com.dumpxml/cache/uidump.xml";
    private static final int XML_CAPTURE_TIMEOUT_SEC = 40;
//    private static File uiDumpFile = null;

    private static boolean supportsUiAutomator(IDevice device) {
        String apiLevelString = device.getProperty(IDevice.PROP_BUILD_API_LEVEL);
        int apiLevel;
        try {
            apiLevel = Integer.parseInt(apiLevelString);
        } catch (NumberFormatException e) {
            apiLevel = UIAUTOMATOR_MIN_API_LEVEL;
        }

        return apiLevel >= UIAUTOMATOR_MIN_API_LEVEL;
    }

    private static void getUiHierarchyFile(IDevice device, File dst,
            IProgressMonitor monitor, boolean compressed) {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }

        monitor.subTask("Deleting old UI XML snapshot ...");
        String command = "rm " + UIDUMP_DEVICE_PATH;
        String command_unit = "rm" + UIDUMP_DEVICE_PATH_UNIT;

        try {
            CountDownLatch commandCompleteLatch = new CountDownLatch(1);
            device.executeShellCommand(command,
                    new CollectingOutputReceiver(commandCompleteLatch));
            device.executeShellCommand(command_unit,
                    new CollectingOutputReceiver(commandCompleteLatch));
            commandCompleteLatch.await(5, TimeUnit.SECONDS);
        } catch (Exception e1) {
            // ignore exceptions while deleting stale files
        }

        monitor.subTask("Taking UI XML snapshot...");
        if (compressed){
            command = String.format("%s %s --compressed %s", UIAUTOMATOR,
                UIAUTOMATOR_DUMP_COMMAND,
                UIDUMP_DEVICE_PATH);
        } else {
            command = String.format("%s %s %s", UIAUTOMATOR,
                    UIAUTOMATOR_DUMP_COMMAND,
                    UIDUMP_DEVICE_PATH);
        }
        CountDownLatch commandCompleteLatch = new CountDownLatch(1);
        
        CollectingOutputReceiver xmlReceiver = new CollectingOutputReceiver(commandCompleteLatch);
        boolean isGetXml = false;
        try {
//        	while (!isGetXml) {
	            device.executeShellCommand(
	                    command, xmlReceiver,
	                    XML_CAPTURE_TIMEOUT_SEC * 1000, TimeUnit.SECONDS);
	            commandCompleteLatch.await(XML_CAPTURE_TIMEOUT_SEC, TimeUnit.SECONDS);
	            xmlReceiver.flush();
	            String receiverOutput = xmlReceiver.getOutput();
	            xmlReceiver.clearBuffer();
	            if (receiverOutput.equals("UI hierchary dumped to: /data/local/tmp/uidump.xml\n"))
	            	isGetXml = true;
//        	}
            
            monitor.subTask("Pull UI XML snapshot from device...");
            device.pullFile(UIDUMP_DEVICE_PATH,
                    dst.getAbsolutePath());
        } catch (Exception e) {
        	if (e.getMessage().equals("Remote object doesn't exist!")) {
	        	try {
	        		CollectingOutputReceiver receiver = new CollectingOutputReceiver();
					device.executeShellCommand("am instrument -w -r -e debug false -e class dan.dji.com.dumpxml.DumpKit dan.dji.com.dumpxml.test/android.support.test.runner.AndroidJUnitRunner", 
							receiver, XML_CAPTURE_TIMEOUT_SEC * 1000, TimeUnit.SECONDS);
					monitor.subTask("Pull UI XML snapshot from device...");
					device.getSyncService().pullFile(UIDUMP_DEVICE_PATH_UNIT,
		                    dst.getAbsolutePath(), SyncService.getNullProgressMonitor());
				} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException
						| IOException e1) {
					e1.printStackTrace();
				} catch (SyncException e1) {
					e1.printStackTrace();
				}
        	}
//            throw new RuntimeException(e);
        }
    }

    //to maintain a backward compatible api, use non-compressed as default snapshot type

    public static UiAutomatorResult takeSnapshot(IDevice device, IProgressMonitor monitor,

           boolean compressed, Image screenshot) throws UiAutomatorException {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }

        monitor.subTask("Checking if device support UI Automator");
        if (!supportsUiAutomator(device)) {
            String msg = "UI Automator requires a device with API Level "
                                + UIAUTOMATOR_MIN_API_LEVEL;
            throw new UiAutomatorException(msg, null);
        }

        monitor.subTask("Creating temporary files for uiautomator results.");
        File tmpDir = null;
        File xmlDumpFile = null;
        try {
            tmpDir = File.createTempFile("uiautomatorviewer_", "");
            tmpDir.delete();
            if (!tmpDir.mkdirs())
                throw new IOException("Failed to mkdir");
            xmlDumpFile = File.createTempFile("dump_", ".uix", tmpDir);
        } catch (Exception e) {
            String msg = "Error while creating temporary file to save snapshot: "
                    + e.getMessage();
            throw new UiAutomatorException(msg, e);
        }

        tmpDir.deleteOnExit();
        xmlDumpFile.deleteOnExit();
        
//        try {
//        	if (uiDumpFile == null) 
//        		uiDumpFile = xmlDumpFile;
//        	else {
//        		Boolean isEqual = FileUtils.contentEquals(uiDumpFile, xmlDumpFile);
//        		
//        		if (!isEqual)
//        			uiDumpFile = xmlDumpFile;
//        		else
//        			return null;
//        	}
//
//		} catch (IOException e) {
//			String msg = "Error while comparing dumping: "
//                    + e.getMessage();
//            throw new UiAutomatorException(msg, e);
//		}

        monitor.subTask("Obtaining UI hierarchy");
        try {
        	Automator_Helper_UI.getUiHierarchyFile(device, xmlDumpFile, monitor, compressed);
        } catch (Exception e) {
            String msg = "Error while obtaining UI hierarchy XML file: " + e.getMessage()+e.getStackTrace().toString();
            throw new UiAutomatorException(msg, e);
        }

        Automator_Model_UI model;
		
        try {
            model = new Automator_Model_UI(xmlDumpFile);
        } catch (Exception e) {
            String msg = "Error while parsing UI hierarchy XML file: " + e.getMessage()+e.getStackTrace();
            throw new UiAutomatorException(msg, e);
        }

        monitor.subTask("Obtaining device screenshot");
        return new UiAutomatorResult(xmlDumpFile, model, screenshot);
    }

    @SuppressWarnings("serial")
    public static class UiAutomatorException extends Exception {
        public UiAutomatorException(String msg, Throwable t) {
            super(msg, t);
        }
    }

    public static class UiAutomatorResult {
        public final File uiHierarchy;
        public final Automator_Model_UI model;
        public final Image screenshot;

        public UiAutomatorResult(File uiXml, Automator_Model_UI m, Image s) {
            uiHierarchy = uiXml;
            model = m;
            screenshot = s;
        }
    }
}
