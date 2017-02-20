package com.duggan.workflow.client.ui.component;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class SimpleButton extends Button{

	public SimpleButton(Element e) {
		super(e);
	}
	
	public static Button wrap(com.google.gwt.dom.client.Element element, boolean isHTMLWrapped) {
	    // Assert that the element is attached.
	    assert Document.get().getBody().isOrHasChild(element);

	    SimpleButton button = new SimpleButton(element);

	    // Mark it attached and remember it for cleanup.
	    button.onAttach();
	    if(!isHTMLWrapped){
	    	RootPanel.detachOnWindowClose(button);
	    }
	    return button;
	  }

}
