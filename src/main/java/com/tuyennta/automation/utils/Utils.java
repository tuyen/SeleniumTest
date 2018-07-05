package com.tuyennta.automation.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import com.tuyennta.automation.settings.Setting;

/**
 * Some utility functions
 * 
 * @author Tuyen Nguyen
 *
 */
public class Utils {

	/**
	 * Get real value of parameter
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static String getParameterValue(String param) throws Exception {
		String value = null;
		if (param.startsWith("#")) {
			value = Setting.TestData.get(param.substring(1));
		} else if (param.startsWith("$")) {
			value = Setting.TestVariable.get(param.substring(1));
		} else {
			value = param;
		}
		if (value == null) {
			if (param.startsWith("#")) {
				throw new Exception(
						"Test data is not imported, please import test data in advance by using ImportData action");
			} else if (param.startsWith("$")) {
				throw new Exception(
						"Test variable is not defined in system, please assign value to a variable in advance");
			}
		}
		return value;
	}

	/**
	 * This function will format the input message (Exception message) for a user
	 * friendly output
	 * 
	 * @param msg
	 * @return
	 */
	public static String formatErrorMessage(String msg) {
		if (msg.contains("Exception:")) {
			msg = msg.substring(msg.indexOf("Exception:") + 10);
		}

		if (msg.contains("(Session info:")) {
			return msg.substring(0, msg.indexOf("(Session info:"));
		}
		return msg;
	}

	/**
	 * Set clipboard content
	 * @param content
	 */
	public static void setClipboard(String content) {
		StringSelection selection = new StringSelection(content);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
	}
	
	public static void waitForMe(long miliseconds) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
