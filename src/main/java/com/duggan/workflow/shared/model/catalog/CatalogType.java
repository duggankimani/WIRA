package com.duggan.workflow.shared.model.catalog;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum CatalogType implements IsSerializable{

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
