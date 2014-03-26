package com.duggan.workflow.client.ui.component;

import com.google.gwt.user.client.ui.IntegerBox;

public class IntegerField extends IntegerBox {

	public void setPlaceholder(String placeHolderValue){
		getElement().setAttribute("placeHolder", placeHolderValue);
	}
	
	public void setClass(String className){
		setStyleName(className);
	}
	
	public void setType(String type){
		getElement().setAttribute("type", type);
	}

}
