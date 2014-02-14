package com.duggan.workflow.shared.responses;

import java.lang.String;

public class ExportFormResponse extends BaseResponse {

	private String xml;

	public ExportFormResponse() {
		// For serialization only
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}
}
