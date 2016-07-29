package com.duggan.workflow.client.ui.component;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class TextField extends TextBox{
	
	public TextField() {
	}
	
	public TextField(Element element) {
		super(element);
	}


	public void setPlaceholder(String placeHolderValue){
		getElement().setAttribute("placeHolder", placeHolderValue);
	}
	
	public void setClass(String className){
		setStyleName(className);
	}
	
	public void setType(String type){
		getElement().setAttribute("type", type);
	}
	
	public void setDisabled(Boolean isDisabled){
		if(isDisabled){
			getElement().setAttribute("disabled", "disabled");
		}
	}
	
	public static TextField wrap(Element element) {
	    // Assert that the element is attached.
	    assert Document.get().getBody().isOrHasChild(element);

	    TextField textBox = new TextField(element);

	    // Mark it attached and remember it for cleanup.
	    textBox.onAttach();
	   // RootPanel.detachOnWindowClose(textBox);

	    return textBox;
	}
	
}
