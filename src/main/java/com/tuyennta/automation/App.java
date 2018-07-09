package com.tuyennta.automation;

import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.tuyennta.automation.dto.TestScriptDTO;
import com.tuyennta.automation.dto.TestSuiteDTO;
import com.tuyennta.automation.settings.Setting;
import com.tuyennta.automation.utils.TestReport;

/**
 * Main application
 *
 */
public class App {
	public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException {
		String testResultDetail = "";
		int countFail = 0;
		
		// load default settings
		Setting.LoadDefaultSettings();

		// read test suite
		TestSuiteDTO testSuite = Setting.loadTestSuite();

		// initialize report
		TestReport report = new TestReport();
		report.initTestReport();
		report.initTestSuiteReport();

		// read & run test script
		boolean testScriptResult = false;
		boolean wholeTestResult = true;
		for (TestScriptDTO tscriptDTO : testSuite.getTestScript()) {
			// read test script from excel file
			tscriptDTO = Setting.loadTestScript(tscriptDTO.getTestScriptName());

			// run test script
			testScriptResult = TestExecute.executeTestScript(tscriptDTO);

			// update test suite report
			report.updateTestSuiteReport(tscriptDTO.getTestScriptName(), testScriptResult);

			//update result detail message
			testResultDetail += "- "+tscriptDTO.getTestScriptName() + ": " + (testScriptResult == true ? "Passed" : "Failed") + "\r\n";
			
			// if one test script fail then this whole test fail
			if (!testScriptResult) {
				wholeTestResult = false;
				countFail ++;
			}
		}

		// write test suite report
		report.writeTestSuiteReport();

		// exit status of whole tests
		System.out.println("Test finished with " + countFail + " script(s) fail in total " + testSuite.getTestScript().size() + " script(s)");
		System.out.println(testResultDetail);
		if (wholeTestResult) {
			System.exit(0);
		} else {
			System.exit(1);
		}
	}
}
