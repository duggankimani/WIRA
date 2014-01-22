package com.duggan.workflow.client.ui.delegate.userDisplay;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UserDisplay extends Composite {

	private static UserDisplayUiBinder uiBinder = GWT
			.create(UserDisplayUiBinder.class);

	interface UserDisplayUiBinder extends UiBinder<Widget, UserDisplay> {
	}
	
	@UiField SpanElement spnNames;
	
	public UserDisplay() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setSpnNames(String text) {
		spnNames.setInnerText(text);
	}
	
}
