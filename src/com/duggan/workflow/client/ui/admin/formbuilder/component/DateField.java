package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class DateField extends FieldWidget {

	private static DateFieldUiBinder uiBinder = GWT
			.create(DateFieldUiBinder.class);

	interface DateFieldUiBinder extends UiBinder<Widget, DateField> {
	}
	
	private final Widget widget;
	
	public DateField() {
		super();
		addProperty(new Property("DATEFORMAT", "Date Format", DataType.STRING));
		widget = uiBinder.createAndBindUi(this);	
		add(widget);
	}
	
	/**
	 * This is an edit property field - This is a field
	 * used to edit a single property
	 * 
	 * @param property
	 */
	public DateField(Property property) {
		this();
		
		String caption = property.getCaption();
		String name = property.getName();
		Value val = property.getValue();
		
	}

	@Override
	public FieldWidget cloneWidget() {
		return new DateField();
	}	
	
}
