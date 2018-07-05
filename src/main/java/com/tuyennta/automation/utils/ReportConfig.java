package com.tuyennta.automation.utils;

import freemarker.template.Configuration;

public class ReportConfig {

	private static ReportConfig INSTANCE;
	
	private ReportConfig() {
		
	}
	
	public static ReportConfig getInstance () {
		if (INSTANCE == null) {
			INSTANCE = new ReportConfig();
		}
		return INSTANCE;
	}
	
	public String getReportFolder() {
		return reportFolder;
	}

	public void setReportFolder(String reportFolder) {
		this.reportFolder = reportFolder;
	}

	public Configuration getFreemarkerConfig() {
		return freemarkerConfig;
	}

	public void setFreemarkerConfig(Configuration freemarkerConfig) {
		this.freemarkerConfig = freemarkerConfig;
	}

	public TestReport getCurrentReportInstance() {
		return currentReportInstance;
	}

	public void setCurrentReportInstance(TestReport currentReportInstance) {
		this.currentReportInstance = currentReportInstance;
	}

	private String reportFolder;
	private Configuration freemarkerConfig;
	private TestReport currentReportInstance;
}
