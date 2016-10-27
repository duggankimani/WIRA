package com.duggan.workflow.client.ui.component;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimpleRadioButton;

public class SimpleRadio extends SimpleRadioButton {

	public SimpleRadio() {
		super("");
	}

	public SimpleRadio(Element e) {
		super(e);
	}

	public static SimpleRadio wrap(Element element, boolean isHTMLFormElement) {
		// Assert that the element is attached.
		assert Document.get().getBody().isOrHasChild(element);

		SimpleRadio radioButton = new SimpleRadio(element);

		// Mark it attached and remember it for cleanup.
		radioButton.onAttach();
		
		if(!isHTMLFormElement){
			RootPanel.detachOnWindowClose(radioButton);
		}

		return radioButton;
	}

	public void setReadOnly(boolean readOnly) {
		super.setEnabled(!readOnly);
	}

}
