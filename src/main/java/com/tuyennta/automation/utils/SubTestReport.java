package com.tuyennta.automation.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SubTestReport extends TestReport {
	
	private String parentTestScriptName = null;
	
	public SubTestReport(TestReport parent) {
		parentTestScriptName = parent.currentTscript;
	}
	
	@Override
	public void initTestScriptReport(String reportName) {
		// create report file
		currentTscript = reportName.substring(0, reportName.indexOf(".x"));
		String reportFolder = parentTestScriptName + File.separator + currentTscript;
		
		File tscriptReport = new File(ReportConfig.getInstance().getReportFolder() + File.separator + reportFolder);
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
}
