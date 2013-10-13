package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class SelectMultiple extends FieldWidget {

	private static SelectMultipleUiBinder uiBinder = GWT
			.create(SelectMultipleUiBinder.class);

	interface SelectMultipleUiBinder extends UiBinder<Widget, SelectMultiple> {
	}
	private final Widget widget;
	
	public SelectMultiple() {
		super();
		widget=uiBinder.createAndBindUi(this);
		add(widget);
	}

	@Override
	public FieldWidget cloneWidget() {
		return new SelectMultiple();
	}

}
