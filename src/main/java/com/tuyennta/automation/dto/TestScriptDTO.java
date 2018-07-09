package com.tuyennta.automation.dto;

import java.util.List;

public class TestScriptDTO {

	protected String testScriptName;
	private List<TestStepDTO> listTestStep;
	public String getTestScriptName() {
		return testScriptName;
	}
	public void setTestScriptName(String testScriptName) {
		this.testScriptName = testScriptName;
	}
	public List<TestStepDTO> getListTestStep() {
		return listTestStep;
	}
	public void setListTestStep(List<TestStepDTO> listTestStep) {
		this.listTestStep = listTestStep;
	}
	
}
