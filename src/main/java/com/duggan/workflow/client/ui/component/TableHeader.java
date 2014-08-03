package com.duggan.workflow.client.ui.component;

public class TableHeader{
	String titleName;
	String thStyleName;
	Double width;
	
	public TableHeader(String titleName, Double width) {
		this(titleName,width,null);
	}
	
	public TableHeader(String titleName, Double width, String styleName) {
		this.titleName = titleName;
		this.width = width;
		this.thStyleName = styleName;
	}
	
	public String getTitleName() {
		return titleName;
	}
	
	public String getStyleName() {
		return thStyleName;
	}

	public Double getWidth() {
		return width;
	}
}