package com.bricks.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;

import com.bricks.ui_pack.Constants_UI;
import com.bricks.node_selection.tree.Basic_Tree_Node;

/**
 * 
 * @author DraLastat
 * @Description 
 */
public class Utils_Constants {

	/**
	 * 
	 */
	public final static String PATH_CONFIG = Constants_UI.CURRENT_DIR + File.separator + "config" + File.separator
			+ "config.xml";
	
	/**
	 * Minicap decode
	 */
	private static final String ROOT = System.getProperty("user.dir");
	
	public static File getMinicap() {
		return new File(ROOT, "minicap");
	}

	public static File getMinicapBin() {
		return new File(ROOT, "minicap/bin");
	}

	public static File getMinicapSo() {
		return new File(ROOT, "minicap/shared");
	}

	/**
	 * node selection
	 */
	public static Document document;
	public static List<Basic_Tree_Node>  checkList = new ArrayList<Basic_Tree_Node>();;

	/**
	 * 
	 */
	public final static String PATH_PROPERTY = Constants_UI.CURRENT_DIR + File.separator + "config" + File.separator
			+ "zh-cn.properties";
	public final static Config_Manager CONFIGER = Config_Manager.getConfigManager();
	// xpath
	public final static String XPATH_LAST_SYNC_TIME = "//dataSync/status/lastSyncTime";
	public final static String XPATH_LAST_KEEP_TIME = "//dataSync/status/lastKeepTime";
	public final static String XPATH_SUCCESS_TIME = "//dataSync/status/successTime";
	public final static String XPATH_FAIL_TIME = "//dataSync/status/failTime";

	public final static String XPATH_TYPE_FROM = "//dataSync/database/from/type";
	public final static String XPATH_HOST_FROM = "//dataSync/database/from/host";
	public final static String XPATH_NAME_FROM = "//dataSync/database/from/name";
	public final static String XPATH_USER_FROM = "//dataSync/database/from/user";
	public final static String XPATH_PASSWORD_FROM = "//dataSync/database/from/password";

	public final static String XPATH_TYPE_TO = "//dataSync/database/to/type";
	public final static String XPATH_HOST_TO = "//dataSync/database/to/host";
	public final static String XPATH_NAME_TO = "//dataSync/database/to/name";
	public final static String XPATH_USER_TO = "//dataSync/database/to/user";
	public final static String XPATH_PASSWORD_TO = "//dataSync/database/to/password";

	public final static String XPATH_SCHEDULE = "//dataSync/schedule/radio";
	public final static String XPATH_SCHEDULE_FIX_TIME = "//dataSync/schedule/fixtime";

	public final static String XPATH_AUTO_BAK = "//dataSync/setting/autoBak";
	public final static String XPATH_DEBUG_MODE = "//dataSync/setting/debugMode";
	public final static String XPATH_STRICT_MODE = "//dataSync/setting/strictMode";
	public final static String XPATH_MYSQL_PATH = "//dataSync/setting/mysqlPath";
	public final static String XPATH_PRODUCT_NAME = "//dataSync/setting/productname";

	public final static String XPATH_POSITION_CODE = "//dataSync/increase/POSITION_CODE";

	public final static String PATH_LOG = Constants_UI.CURRENT_DIR + File.separator + "log" + File.separator + "log.log";
}
