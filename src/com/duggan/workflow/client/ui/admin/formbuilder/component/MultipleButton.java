package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class MultipleButton extends FieldWidget {

	private static MultipleButtonUiBinder uiBinder = GWT
			.create(MultipleButtonUiBinder.class);

	interface MultipleButtonUiBinder extends UiBinder<Widget, MultipleButton> {
	}
	private final Widget widget;
	
	public MultipleButton() {
		super();
		widget= uiBinder.createAndBindUi(this);
		
		add(widget);
	}

	@Override
	public FieldWidget cloneWidget() {
		return new MultipleButton();
	}

}
