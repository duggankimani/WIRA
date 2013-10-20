package com.duggan.workflow.client.ui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Popup extends Composite {

	private static PopupUiBinder uiBinder = GWT.create(PopupUiBinder.class);

	interface PopupUiBinder extends UiBinder<Widget, Popup> {
	}

	Widget widget;
	@UiField PopupPanel popupPanel; 
	@UiField VerticalPanel container;
	
	Widget parent;
	
	private Popup() {
		widget = uiBinder.createAndBindUi(this);
	}
	
	public Popup(Widget targetObject){
		this();
		this.parent = targetObject;
		
		initWidget(parent);
	}
	
	public void show(){
		if(parent!=null){
			popupPanel.showRelativeTo(parent);
		}else{
			popupPanel.show();
		}
	}
	
	public void add(Widget w){
		container.add(w);
	}

}
