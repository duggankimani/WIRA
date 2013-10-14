package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class MultipleButton extends FieldWidget {

	private static MultipleButtonUiBinder uiBinder = GWT
			.create(MultipleButtonUiBinder.class);

	interface MultipleButtonUiBinder extends UiBinder<Widget, MultipleButton> {
	}
	private final Widget widget;
	
	@UiField Element lblEl;
	
	public MultipleButton() {
		super();
		widget= uiBinder.createAndBindUi(this);
		
		add(widget);
	}

	@Override
	public FieldWidget cloneWidget() {
		return new MultipleButton();
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

}
