package com.duggan.workflow.client.ui.component;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class TextArea extends com.google.gwt.user.client.ui.TextArea {

	public TextArea() {
		super();
	}

	public TextArea(Element element) {
		super(element);
	}

	public void setRows(String rows) {
		rows = rows.trim();
		if (rows.matches("[0-9]+")) {
			Integer noOfRows = new Integer(rows);
			setHeight((noOfRows * 26) + "px");
		}
	}

	public void setClass(String className) {
		setStyleName(className);
	}

	public void setPlaceholder(String placeHolderValue) {
		getElement().setAttribute("placeHolder", placeHolderValue);
	}

	public void setType(String type) {
		getElement().setAttribute("type", type);
	}

	public static TextArea wrap(Element element, boolean isHTMLFormElement) {
		// Assert that the element is attached.
		assert Document.get().getBody().isOrHasChild(element);

		TextArea textArea = new TextArea(element);

		// Mark it attached and remember it for cleanup.
		textArea.onAttach();
		if(!isHTMLFormElement){
			RootPanel.detachOnWindowClose(textArea);
		}
		//

		return textArea;
	}

}
