package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class SingleButton extends Field {

	private static SingleButtonUiBinder uiBinder = GWT
			.create(SingleButtonUiBinder.class);

	interface SingleButtonUiBinder extends UiBinder<Widget, SingleButton> {
	}
	
	private final Widget widget;
	public SingleButton() {
		super();
		widget=uiBinder.createAndBindUi(this);
		add(widget);
	}
	@Override
	public Field cloneWidget() {
		return new SingleButton();
	}

}
