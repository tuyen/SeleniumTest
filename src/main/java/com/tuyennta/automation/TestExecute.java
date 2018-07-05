package com.tuyennta.automation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.tuyennta.automation.dto.TestScriptDTO;
import com.tuyennta.automation.dto.TestStepDTO;
import com.tuyennta.automation.dto.TestStepResultDTO;
import com.tuyennta.automation.selenium.Selenium;
import com.tuyennta.automation.settings.Setting;
import com.tuyennta.automation.utils.Const;
import com.tuyennta.automation.utils.ReportConfig;
import com.tuyennta.automation.utils.SubTestReport;
import com.tuyennta.automation.utils.TestReport;
import com.tuyennta.automation.utils.Utils;

public class TestExecute {
	private static Selenium selenium = null;
	private static int loopSize = -1;
	private static int loopIndex = -1;
	private static List<TestStepDTO> loopSteps = null;
	private static List<TestStepResultDTO> currentListResult = null;
	private static TestReport currentReport = null;
	/**
	 * Execute test script step by step and write report
	 * 
	 * @param testScript
	 */
	public static boolean executeTestScript(TestScriptDTO testScript) {
		// init test script report
		currentReport = new TestReport();
		currentReport.initTestScriptReport(testScript.getTestScriptName());

		// execute test steps and get the result

		TestStepResultDTO result = null;
		currentListResult = new ArrayList<>();
		for (TestStepDTO step : testScript.getListTestStep()) {
			if (loopSteps != null) {
				loopSteps.add(step);
			}
			result = executeTestStep(step);
			currentListResult.add(result);
			if (result.getResult().equalsIgnoreCase("Failed")) {
				// stop
				break;
			}
		}

		// free selenium instance
		selenium.closeBrowser();
		selenium = null;

		// write report
		currentReport.writeTestScriptReport(currentListResult);
		return result.getResult().equalsIgnoreCase("Passed");
	}

	private static boolean executeSubTestScript(TestScriptDTO testScript) {
		// init test script report
		SubTestReport report = new SubTestReport(currentReport);
		report.initTestScriptReport(testScript.getTestScriptName());

		// execute test steps and get the result
		List<TestStepResultDTO> listResult = new ArrayList<>();
		TestStepResultDTO result = null;
		for (TestStepDTO step : testScript.getListTestStep()) {
			result = executeTestStep(step);
			listResult.add(result);
			if (result.getResult().equalsIgnoreCase("Failed")) {
				// write report and stop
				report.writeTestScriptReport(listResult);
				return false;
			}
		}

		// write report
		report.writeTestScriptReport(listResult);
		//set current report instance back to the currentReport
		ReportConfig.getInstance().setCurrentReportInstance(currentReport);
		return true;
	}

	private static boolean loop() {
		TestStepResultDTO result = null;
		for (TestStepDTO step : loopSteps) {
			result = executeTestStep(step);
			currentListResult.add(result);
			if (result.getResult().equalsIgnoreCase("Failed")) {
				// stop
				ReportConfig.getInstance().getCurrentReportInstance().writeTestScriptReport(currentListResult);
				return false;
			}
		}
		ReportConfig.getInstance().getCurrentReportInstance().writeTestScriptReport(currentListResult);
		return true;
	}

	/**
	 * Execute a test step
	 * 
	 * @param step
	 * @return
	 */
	private static TestStepResultDTO executeTestStep(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);

		// get parameters'value from test data or variable
		try {
			step.setParameters(Utils.getParameterValue(step.getParameters()));
			result.setParameters(step.getParameters());
		} catch (Exception e) {
			result.setCause(e.toString());
			result.setResult(false);
			return result;
		}

		// action...
		switch (step.getAction()) {
		case Const.ACTION_OPEN_BROWSER:
			selenium = new Selenium();
			result.setResult(selenium.openBrowser(step));
			break;
		case Const.ACTION_OPEN_NEW_TAB:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result.setResult(selenium.openNewTab(step));
			}
			break;
		case Const.ACTION_SWITCH_TO_TAB:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.switchToTab(step);
			}
			break;
		case Const.ACTION_CLOSE_TAB:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.closeTab(step);
			}
			break;
		case Const.ACTION_CLOSE_BROWSER:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				selenium.closeBrowser();
				selenium = null;
			}
			break;
		case Const.ACTION_IMPORT_DATA:
			try {
				Setting.LoadTestData(step.getParameters());
			} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
				result.setCause(e.toString());
				result.setResult(false);
			}
			break;
		case Const.ACTION_CHECK_VISIBLE:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.checkVisible(step);
			}
			break;
		case Const.ACTION_CHECK_ENABLE:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.checkEnable(step);
			}
			break;
		case Const.ACTION_COMPARE_TEXT:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.compareText(step);
			}
			break;
		case Const.ACTION_COMPARE_VALUE:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.compareValue(step);
			}
			break;
		case Const.ACTION_CHECK:
			break;
		case Const.ACTION_DOWNLOAD:
			break;
		case Const.ACTION_UPLOAD_FILE:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.uploadFile(step);
			}
			break;
		case Const.ACTION_UPLOAD_FILES:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.uploadFiles(step);
			}
			break;
		case Const.ACTION_KEY_PRESS:
			result = selenium.keypress(step);
			break;
		// CASE start loop
		case Const.ACTION_START_LOOP:
			loopSize = Integer.parseInt(step.getParameters());
			loopIndex = 1;
			loopSteps = new ArrayList<>();
			result.setResult(true);
			break;
		case Const.ACTION_END_LOOP:
			loopSteps.remove(loopSteps.size() - 1);
			while (loopIndex < loopSize) {
				if (!loop()) {
					result.setResult(false);
					break;
				}
				loopIndex++;
			}
			loopSteps = null;
			loopIndex = -1;
			loopSize = -1;
			break;
		case Const.ACTION_CLICK:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.click(step);
			}
			break;
		case Const.ACTION_MOUSE_HOVER:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.mouseHover(step);
			}
			break;
		case Const.ACTION_SELECT_BY_VALUE:
		case Const.ACTION_SELECT_BY_TEXT:
		case Const.ACTION_SELECT_BY_INDEX:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.select(step);
			}
			break;
		case Const.ACTION_SET_TEXT:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.setText(step);
			}
			break;
		// CASE call sub test script
		case Const.ACTION_CALL_SUB_TEST_SCRIPT:
			TestScriptDTO tscriptDTO = null;
			try {
				// read test script from excel file
				tscriptDTO = Setting.loadTestScript(step.getParameters());

				// run test script
				boolean r = TestExecute.executeSubTestScript(tscriptDTO);
				result.setResult(r);
				if (!r) {
					result.setCause("Please see the sub test script report for detail");
				}
			} catch (EncryptedDocumentException | InvalidFormatException | IOException e1) {
				result.setCause(e1.toString());
				result.setResult(false);
			}
			break;
		case Const.ACTION_CHECK_SELECTED:
			break;
		// CASE set clipboard
		case Const.ACTION_SET_CLIPBOARD:
			Utils.setClipboard(step.getParameters());
			break;
		case Const.ACTION_CONFIRM_DIALOG:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.confirmDialog(step);
			}
			break;
		case Const.ACTION_CANCEL_DIALOG:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.cancelDialog(step);
			}
			break;
		case Const.ACTION_CAPTURE_SCREEN:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.captureScreen(step);
			}
			break;
		case Const.ACTION_STORE_TEXT:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.storeText(step);
			}
			break;
		case Const.ACTION_STORE_VALUE:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.storeValue(step);
			}
			break;
		case Const.ACTION_WAIT:
			try {
				Thread.sleep(Long.parseLong(step.getParameters()));
			} catch (NumberFormatException | InterruptedException e) {
				result.setResult(false);
				result.setCause(e.toString());
			}
			break;
		case Const.ACTION_WAIT_FOR_ELEMENT_VISIBLE:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.waitForElementVisible(step);
			}
			break;
		case Const.ACTION_WAIT_FOR_ELEMENT_CLICKABLE:
			if (selenium == null) {
				result.setCause("Browser is not opened, please open browser as first step!");
				result.setResult(false);
			} else {
				result = selenium.waitForElementClickable(step);
			}
			break;
		default:
			result.setCause("Action is not implemented, please contact to administrator to get supported");
			result.setResult(false);
			break;
		}
		return result;
	}
}
