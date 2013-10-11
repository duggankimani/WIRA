package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class DateField extends Field {

	private static DateFieldUiBinder uiBinder = GWT
			.create(DateFieldUiBinder.class);

	interface DateFieldUiBinder extends UiBinder<Widget, DateField> {
	}
	
	private final Widget widget;
	
	public DateField() {
		super();
		//properties.add(new Property(name, caption, type));
		
		widget = uiBinder.createAndBindUi(this);	
		add(widget);
	}
	
	public DateField(Property property) {
		this();
		//setProperty(property);
	}

	@Override
	public Field cloneWidget() {
		return new DateField();
	}	
	
}
