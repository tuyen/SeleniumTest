package com.tuyennta.automation.utils;

/**
 * Constants
 * 
 * @author Tuyen Nguyen
 *
 */
public class Const {
	
	/**
	 * System configurations
	 */
	public static final String IE_WEB_DRIVER_NAME = "IEWebDriver";
	public static final String FF_WEB_DRIVER_NAME = "FFWebDriver";
	public static final String GC_WEB_DRIVER_NAME = "GCWebDriver";
	public static final String GC_WEB_DRIVER_CONF_STRING = "webdriver.chrome.driver";
	public static final String FF_WEB_DRIVER_CONF_STRING = "webdriver.gecko.driver";
	public static final String IE_WEB_DRIVER_CONF_STRING = "webdriver.ie.driver";
	public static final String TEST_SUITE_FILE = "TestSuiteFile";
	public static final String TEST_SCRIPT_FOLDER = "TestScriptFolder";
	public static final String TEST_DATA_FOLDER = "TestDataFolder";
	public static final String TEST_REPORT_FOLDER = "TestReportFolder";
	public static final String GC = "GC";
	public static final String FF = "FF";
	public static final String IE = "IE";
	public static final String AUTOIT_FILE_RUNNER = "Driver/AutoIT/AutoIt3.exe";
	public static final String AUTOIT_FILE_COMMAND = "Driver/AutoIT/Command.au";
	
	/**
	 * Action Keywords
	 */
	public static final String ACTION_OPEN_BROWSER = "openbrowser";
	public static final String ACTION_OPEN_NEW_TAB = "opennewtab";
	public static final String ACTION_CLOSE_TAB = "closetab";
	public static final String ACTION_CLOSE_BROWSER = "closebrowser";
	public static final String ACTION_CHECK_VISIBLE = "checkvisible";
	public static final String ACTION_CHECK_ENABLE = "checkenable";
	public static final String ACTION_COMPARE_TEXT = "comparetext";
	public static final String ACTION_COMPARE_LINK_TEXT = "comparelinktext";
	public static final String ACTION_COMPARE_VALUE = "comparevalue";
	public static final String ACTION_CHECK = "check";
	public static final String ACTION_SWITCH_TO_TAB = "switchtotab";
	public static final String ACTION_DOWNLOAD = "download";
	public static final String ACTION_UPLOAD_FILE = "uploadfile";
	public static final String ACTION_UPLOAD_FILES = "uploadfiles";
	public static final String ACTION_IMPORT_DATA = "importdata";
	public static final String ACTION_KEY_PRESS = "keypress";
	public static final String ACTION_START_LOOP = "startloop";
	public static final String ACTION_END_LOOP = "endloop";
	public static final String ACTION_CLICK = "click";
	public static final String ACTION_CALL_SUB_TEST_SCRIPT = "callsubtestscript";
	public static final String ACTION_WAIT_FOR_ELEMENT_VISIBLE = "waitforelementvisible";
	public static final String ACTION_WAIT_FOR_ELEMENT_CLICKABLE = "waitforelementclickable";
	public static final String ACTION_MOUSE_HOVER = "mousehover";
	public static final String ACTION_SELECT_BY_VALUE = "selectbyvalue";
	public static final String ACTION_SELECT_BY_TEXT = "selectbytext";
	public static final String ACTION_SELECT_BY_INDEX = "selectbyindex";
	public static final String ACTION_SET_TEXT = "settext";
	public static final String ACTION_CHECK_SELECTED = "checkselected";
	public static final String ACTION_SET_CLIPBOARD = "setclipboard";
	public static final String ACTION_CLEAR_CLIPBOARD = "clearclipboard";
	public static final String ACTION_CONFIRM_DIALOG = "confirmdialog";
	public static final String ACTION_CANCEL_DIALOG = "canceldialog";
	public static final String ACTION_CLEAR_CACHE = "clearcache";
	public static final String ACTION_CAPTURE_SCREEN = "capturescreen";
	public static final String ACTION_CAPTURE_ELEMENT = "captureelement";
	public static final String ACTION_STORE_TEXT = "storetext";
	public static final String ACTION_STORE_VALUE = "storevalue";
	public static final String ACTION_WAIT = "wait";
	public static final String ACTION_WAIT_LOAD = "waitload";
	public static final String ACTION_ASSIGN_VALUE = "assignvalue";
}
