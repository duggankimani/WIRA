package com.duggan.workflow.client.ui.component;

import com.google.gwt.dom.client.BRElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class Break extends ComplexPanel {

	public Break() {
		setElement(Document.get().createBRElement());
	}

	public void setId(String id) {
		// Set an attribute common to all tags
		getElement().setId(id);
	}

	public void setDir(String dir) {
		// Set an attribute specific to this tag
		((BRElement) getElement().cast()).setDir(dir);
	}

	public void add(Widget w) {
		// ComplexPanel requires the two-arg add() method
		super.add(w, getElement());
	}
}
