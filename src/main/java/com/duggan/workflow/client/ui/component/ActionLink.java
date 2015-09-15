package com.duggan.workflow.client.ui.component;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;

public class ActionLink extends Anchor {

	public ActionLink() {
		
		addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//setStyleName("hidden");
			}	
		});
	}
	
	public ActionLink(String text) {
		super();
		setText(text);
	}

	public void setDataToggle(String data){
		getElement().setAttribute("data-toggle", data);
	}
	
	public void setRole(String role){
		getElement().setAttribute("role", role);
	}
	
	public void setDataOriginalTitle(String data){
		getElement().setAttribute("data-original-title", data);
	}
	
	public void setDataPlacement(String data){
		getElement().setAttribute("data-placement", data);
	}
	
	@Override
	public void onBrowserEvent(Event event) {
	    switch (DOM.eventGetType(event)) {
	        case Event.ONDBLCLICK:
	        case Event.ONFOCUS:
	        case Event.ONCLICK:
	            if (!isEnabled()) {
	                return;
	            }
	            break;
	    }
	    super.onBrowserEvent(event);
	}
}
