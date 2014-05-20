package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.DataType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class ButtonGroup extends FieldWidget {

	private static MultipleButtonUiBinder uiBinder = GWT
			.create(MultipleButtonUiBinder.class);

	interface MultipleButtonUiBinder extends UiBinder<Widget, ButtonGroup> {
	}
	private final Widget widget;
	
	@UiField Element lblEl;
	
	public ButtonGroup() {
		super();
		widget= uiBinder.createAndBindUi(this);
		
		add(widget);
	}

	@Override
	public FieldWidget cloneWidget() {
		return new ButtonGroup();
	}
	
	@Override
	protected void setCaption(String caption) {
		lblEl.setInnerHTML(caption);
	}
	
	@Override
	protected void setPlaceHolder(String placeHolder) {
		//txtComponent.setPlaceholder(placeHolder);
	}
	
	@Override
	protected void setHelp(String help) {
		//txtComponent.setTitle(help);
	}

	@Override
	protected DataType getType() {
		
		return DataType.MULTIBUTTON;
	}

}
