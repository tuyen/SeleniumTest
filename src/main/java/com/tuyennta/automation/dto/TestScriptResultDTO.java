package com.tuyennta.automation.dto;

public class TestScriptResultDTO extends TestScriptDTO {

	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result == true ? "Passed" : "Failed";
	}
}
