package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class InlineCheckBox extends FieldWidget {

	private static InlineCheckBoxUiBinder uiBinder = GWT
			.create(InlineCheckBoxUiBinder.class);
	

	interface InlineCheckBoxUiBinder extends UiBinder<Widget,InlineCheckBox> {
	}
	
	@UiField AbsolutePanel container;
	
	private final Widget widget;

	public InlineCheckBox() {
		super();
		widget = uiBinder.createAndBindUi(this);
		container.getElement().getStyle().setPosition(Position.RELATIVE);
		add(widget);
	}

	@Override
	public FieldWidget cloneWidget() {
		return new InlineCheckBox();
	}

}
