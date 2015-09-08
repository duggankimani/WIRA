package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class SelectMultiple extends FieldWidget {

	private static SelectMultipleUiBinder uiBinder = GWT
			.create(SelectMultipleUiBinder.class);

	interface SelectMultipleUiBinder extends UiBinder<Widget, SelectMultiple> {
	}
	
	private final Widget widget;
	
	@UiField Element lblEl;
	
	public SelectMultiple() {
		super();
		addProperty(new Property(SELECTIONTYPE, "Reference", DataType.STRING));
		widget=uiBinder.createAndBindUi(this);
		add(widget);
	}

	@Override
	public FieldWidget cloneWidget() {
		return new SelectMultiple();
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
		
		return DataType.SELECTMULTIPLE;
	}

	@Override
	public Widget getInputComponent() {
		return this;
	}

	@Override
	public Element getViewElement() {
		return null;
	}
}
