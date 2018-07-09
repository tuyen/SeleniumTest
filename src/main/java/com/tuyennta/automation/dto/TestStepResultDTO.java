package com.tuyennta.automation.dto;

import com.tuyennta.automation.utils.Utils;

public class TestStepResultDTO extends TestStepDTO {

	private String result;
	private String cause = "";
	public String getResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result == true ? "Passed" : "Failed";
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause += Utils.formatErrorMessage(cause);
	}
	
	public TestStepResultDTO(TestStepDTO testStep) {
		this.action = testStep.getAction();
		this.object = testStep.getObject();
		this.rowIndex = testStep.getRowIndex();
		this.parameters = testStep.getParameters();
		this.comment = testStep.getComment();
		this.result = "Passed";
	}
}
