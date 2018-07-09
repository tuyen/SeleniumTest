package com.tuyennta.automation.dto;

import java.util.List;

public class TestSuiteDTO {

	private List<TestScriptDTO> testScript;

	public List<TestScriptDTO> getTestScript() {
		return testScript;
	}

	public void setTestScript(List<TestScriptDTO> testScript) {
		this.testScript = testScript;
	}
}
