package com.tuyennta.automation.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.tuyennta.automation.dto.TestScriptResultDTO;
import com.tuyennta.automation.dto.TestStepResultDTO;
import com.tuyennta.automation.settings.Setting;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * Utility class support for writing test report
 * 
 * @author Tuyen Nguyen
 *
 */
public class TestReport {

	private File tsuiteReportFile = null;
	protected File tscriptReportFile = null;
	protected File tscriptEvidenceFolder = null;
	protected int evidenceIndex;
	protected String currentTscript = null;
	private Template tsuiteTemplate = null;
	protected HashMap<String, Object> testStepResult = null;
	private List<TestScriptResultDTO> listTestScriptResult = null;
	protected Template tscriptTemplate = null;
	
	public TestReport() {
		//save current instance
		ReportConfig.getInstance().setCurrentReportInstance(this);
	}
	
	public void initTestReport() {
		Date exec_time = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		ReportConfig.getInstance().setReportFolder(Setting.TestReportFolder + File.separator + dateFormat.format(exec_time));
		// create folder based-on current runtime
		File reportFolder = new File(ReportConfig.getInstance().getReportFolder());
		reportFolder.mkdirs();
		
		//init freemarker
		ReportConfig.getInstance().setFreemarkerConfig(new Configuration(Configuration.VERSION_2_3_27));
        ReportConfig.getInstance().getFreemarkerConfig().setClassForTemplateLoading(this.getClass(), "/");
		ReportConfig.getInstance().getFreemarkerConfig().setDefaultEncoding("UTF-8");
		ReportConfig.getInstance().getFreemarkerConfig().setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		ReportConfig.getInstance().getFreemarkerConfig().setLogTemplateExceptions(false);
		ReportConfig.getInstance().getFreemarkerConfig().setWrapUncheckedExceptions(true);
        
	}

	public void initTestSuiteReport() {
		tsuiteReportFile = new File(ReportConfig.getInstance().getReportFolder() + File.separator + "TestSuiteReport.html");
		try {
			tsuiteReportFile.createNewFile();

			//freemarker
			listTestScriptResult = new ArrayList<>();
			tsuiteTemplate = ReportConfig.getInstance().getFreemarkerConfig().getTemplate("TestSuiteTemplate.ftlh");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void initTestScriptReport(String reportName) {
		// create report file
		currentTscript = reportName.substring(0, reportName.indexOf(".x"));
		File tscriptReport = new File(ReportConfig.getInstance().getReportFolder() + File.separator + currentTscript);
		tscriptReport.mkdirs();
		tscriptReportFile = new File(tscriptReport.getAbsolutePath() + File.separator + currentTscript + "_Report.html");
		try {
			//create testscript report file
			tscriptReportFile.createNewFile();
			
			//create test evidence folder
			initTestScriptEvidence();
			
			//freemarker
			testStepResult = new HashMap<>();			
			tscriptTemplate = ReportConfig.getInstance().getFreemarkerConfig().getTemplate("TestScriptTemplate.ftlh");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	protected void initTestScriptEvidence () {
		tscriptEvidenceFolder = new File(tscriptReportFile.getParent() + File.separator + "Evidences");
		tscriptEvidenceFolder.mkdir();
		evidenceIndex = 1;
	}

	public void writeTestScriptReport(List<TestStepResultDTO> listStepResult) {
		testStepResult.put("listTstep", listStepResult);
		testStepResult.put("testScriptName", this.tscriptReportFile.getName().substring(0, this.tscriptReportFile.getName().indexOf("_")));
		Writer out;
		try {
			out = new OutputStreamWriter(new FileOutputStream(tscriptReportFile));
	        tscriptTemplate.process(testStepResult, out);
		} catch (TemplateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeTestSuiteReport() {
		Writer out;
		try {
			out = new OutputStreamWriter(new FileOutputStream(tsuiteReportFile));
			HashMap<String, Object> root = new HashMap<>();
			root.put("listTScript", listTestScriptResult);
	        tsuiteTemplate.process(root, out);
		} catch (TemplateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateTestSuiteReport(String testScriptName, boolean result) {
		TestScriptResultDTO tscriptRs = new TestScriptResultDTO();
		tscriptRs.setTestScriptName(testScriptName);
		tscriptRs.setResult(result);
		listTestScriptResult.add(tscriptRs);
	}

	public void addEvidence (File image) {
		try {
			FileUtils.copyFile(image, new File(tscriptEvidenceFolder + File.separator + currentTscript + "_" + (evidenceIndex++) + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
