package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class FormWidget extends FieldWidget {

	private static FormWidgetUiBinder uiBinder = GWT
			.create(FormWidgetUiBinder.class);

	interface FormWidgetUiBinder extends UiBinder<Widget, FormWidget> {
	}

	@UiField Element lblEl;
	@UiField com.duggan.workflow.client.ui.component.TextField txtComponent;
	@UiField InlineLabel lblReadOnly;
	@UiField HTMLPanel panelControls;
	@UiField SpanElement spnMandatory;
	
	public FormWidget() {
		super();		
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
//		Property property = new Property(FORM, "Form", DataType.SELECTBASIC);
//		property.set
//		addProperty())
	}

	@Override
	public FieldWidget cloneWidget() {
		return new FormWidget();
	}

	@Override
	protected DataType getType() {
		return DataType.FORM;
	}

	@Override
	public Widget getInputComponent() {
		
		return txtComponent;
	}

	@Override
	public Element getViewElement() {
		return null;
	}

}
