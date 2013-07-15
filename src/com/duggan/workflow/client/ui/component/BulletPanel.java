package com.duggan.workflow.client.ui.component;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class BulletPanel extends ComplexPanel {

	HTMLPanel p;
	
	public BulletPanel() {
		setElement(Document.get().createLIElement());
	}

	public void setId(String id) {
		// Set an attribute common to all tags
		getElement().setId(id);
	}

	public void setDir(String dir) {
		// Set an attribute specific to this tag
		((LIElement) getElement().cast()).setDir(dir);
	}

	public void add(Widget w) {
		// ComplexPanel requires the two-arg add() method
		super.add(w, getElement());
	}
}
