package com.duggan.workflow.shared.model.catalog;

public enum CatalogType {

	DATATABLE("Data Table"),
	REPORTTABLE("Report Table"),
	REPORTVIEW("Report View");
	
	private String displayName;

	public String getDisplayName() {
		return displayName;
	}

	private CatalogType(String displayName){
		this.displayName = displayName;
	}
	
}
