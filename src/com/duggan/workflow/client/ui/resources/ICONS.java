package com.duggan.workflow.client.ui.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ICONS extends ClientBundle{

	public static final ICONS INSTANCE = GWT.create(ICONS.class);

	ImageResource flaggrey();
	
    ImageResource flagyellow();
    
    ImageResource flagred();
    
    ImageResource flagwhite();
    
    ImageResource attachment();
    
}
