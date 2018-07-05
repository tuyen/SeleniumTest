package com.tuyennta.automation.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.tuyennta.automation.dto.TestScriptDTO;
import com.tuyennta.automation.dto.TestStepDTO;
import com.tuyennta.automation.dto.TestSuiteDTO;
import com.tuyennta.automation.utils.Const;
import com.tuyennta.automation.utils.ExcelHelper;

/**
 * 
 * @author Tuyen Nguyen
 *
 */
public class Setting {

	public static HashMap<String, String> WebDriverLocation = new HashMap<String, String>();
	public static String TestSuiteFile = "";
	public static String TestScriptFolder = "";
	public static String TestDataFolder = "";
	public static String TestReportFolder = "";
	public static HashMap<String, String> TestData = null;
	public static HashMap<String, String> TestVariable = new HashMap<String, String>();
	
	public static void LoadDefaultSettings() {
		File propsFile = new File("SeleniumTest.properties");
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(propsFile));
			WebDriverLocation.put(Const.GC_WEB_DRIVER_NAME, props.getProperty(Const.GC_WEB_DRIVER_NAME));
			WebDriverLocation.put(Const.FF_WEB_DRIVER_NAME, props.getProperty(Const.FF_WEB_DRIVER_NAME));
			WebDriverLocation.put(Const.IE_WEB_DRIVER_NAME, props.getProperty(Const.IE_WEB_DRIVER_NAME));
			TestSuiteFile = props.getProperty(Const.TEST_SUITE_FILE);
			TestScriptFolder = props.getProperty(Const.TEST_SCRIPT_FOLDER);
			TestDataFolder = props.getProperty(Const.TEST_DATA_FOLDER);
			TestReportFolder = props.getProperty(Const.TEST_REPORT_FOLDER);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Read test suite from excel file
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static TestSuiteDTO loadTestSuite () throws EncryptedDocumentException, InvalidFormatException, IOException{
		Workbook tSuiteWB = ExcelHelper.openWorkbook(TestSuiteFile);
		Sheet sheet = tSuiteWB.getSheetAt(0);
		TestSuiteDTO tSuitedto = new TestSuiteDTO();
		TestScriptDTO tScriptdto = null;
		List<TestScriptDTO> listTScript = new ArrayList<TestScriptDTO>();
		for (Row row : sheet) {
			if (row.getCell(0) != null && row.getCell(0).getStringCellValue().equalsIgnoreCase("r")) {
				tScriptdto = new TestScriptDTO();
				tScriptdto.setTestScriptName(ExcelHelper.getCellValueAsString(tSuiteWB, row.getCell(1)));
				listTScript.add(tScriptdto);
			}
		}
		tSuitedto.setTestScript(listTScript);
		return tSuitedto;
	}
	
	public static TestScriptDTO loadTestScript (String scriptName) throws EncryptedDocumentException, InvalidFormatException, IOException {
		TestScriptDTO tscriptDTO = new TestScriptDTO();
		tscriptDTO.setTestScriptName(scriptName);
		Workbook tscriptWB = ExcelHelper.openWorkbook(TestScriptFolder + File.separator + scriptName);
		Sheet sheet = tscriptWB.getSheetAt(0);
		TestStepDTO tstepDTO = null;
		List<TestStepDTO> listTestStep = new ArrayList<TestStepDTO>();
		int rowIndex = 1;
		for (Row row : sheet) {
			if (row.getCell(0) != null && row.getCell(0).getStringCellValue().equalsIgnoreCase("r")) {
				tstepDTO = new TestStepDTO();
				tstepDTO.setRowIndex(rowIndex);
				tstepDTO.setAction(ExcelHelper.getCellValueAsString(tscriptWB, row.getCell(1)).toLowerCase());
				tstepDTO.setObject(ExcelHelper.getCellValueAsString(tscriptWB, row.getCell(2)));
				tstepDTO.setParameters(ExcelHelper.getCellValueAsString(tscriptWB, row.getCell(3)));
				tstepDTO.setComment(ExcelHelper.getCellValueAsString(tscriptWB, row.getCell(4)));
				listTestStep.add(tstepDTO);
			}
			rowIndex++;
		}
		tscriptDTO.setListTestStep(listTestStep);
		return tscriptDTO;
	}
	
	public static void LoadTestData (String testDataName) throws EncryptedDocumentException, InvalidFormatException, IOException {
		TestData = new HashMap<String, String>();
		Workbook tscriptWB = ExcelHelper.openWorkbook(TestDataFolder + File.separator + testDataName);
		Sheet sheet = tscriptWB.getSheetAt(0);
		for (Row row : sheet) {
			TestData.put(ExcelHelper.getCellValueAsString(tscriptWB, row.getCell(0)), ExcelHelper.getCellValueAsString(tscriptWB, row.getCell(1)));			
		}
	}
}
