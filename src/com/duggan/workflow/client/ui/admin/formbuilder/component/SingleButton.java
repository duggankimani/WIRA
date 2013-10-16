package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.DataType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class SingleButton extends FieldWidget {

	private static SingleButtonUiBinder uiBinder = GWT
			.create(SingleButtonUiBinder.class);

	interface SingleButtonUiBinder extends UiBinder<Widget, SingleButton> {
	}
	
	private final Widget widget;
	
	@UiField Element lblEl;
	
	public SingleButton() {
		super();
		widget=uiBinder.createAndBindUi(this);
		add(widget);
	}
	
	@Override
	public FieldWidget cloneWidget() {
		return new SingleButton();
	}
	
	@Override
	protected DataType getType() {
		return DataType.BUTTON;
	}

}
