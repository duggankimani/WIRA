package com.duggan.workflow.client.ui.component;

public class TextArea extends com.google.gwt.user.client.ui.TextArea {

	public void setRows(String rows){
		rows = rows.trim();
		if(rows.matches("[0-9]+")){
			Integer noOfRows = new Integer(rows);
			setHeight((noOfRows*26)+"px");
		}
	}
	
	public void setClass(String className){
		setStyleName(className);
	}
}
