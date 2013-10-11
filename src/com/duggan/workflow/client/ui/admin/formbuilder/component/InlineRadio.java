package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class InlineRadio extends Field {

	private static InlineRadioUiBinder uiBinder = GWT
			.create(InlineRadioUiBinder.class);

	interface InlineRadioUiBinder extends UiBinder<Widget, InlineRadio> {
	}
	
	private final Widget widget;
	
	public InlineRadio() {
		super();
		widget= uiBinder.createAndBindUi(this);
		
		add(widget);
	}

	@Override
	public Field cloneWidget() {
		return new InlineRadio();
	}

}
