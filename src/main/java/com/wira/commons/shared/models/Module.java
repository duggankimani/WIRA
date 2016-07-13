package com.wira.commons.shared.models;

public enum Module {

	ACCESSMGT("Access Management"),
	PROCESSES("Processes"),
	UNASSIGNED("Unassigned Tasks"),
	DASHBOARDS("Dashboards"),
	REPORTS("Reports"),
	CASEREGISTRY("Case Registry"),
	FILEVIEWER("File Viewer"),
	SETTINGS("Settings"),
	MAILLOG("Mail log"),
	DATATABLES("Data Tables"),
	DATASOURCES("Data sources");
	
	private String displayName;

	private Module(String displayName) {
		this.displayName = displayName;
	
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
