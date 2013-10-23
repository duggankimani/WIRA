package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.Date;

import com.duggan.workflow.client.ui.component.DateInput;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.DateValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class DateField extends FieldWidget {

	private static DateFieldUiBinder uiBinder = GWT
			.create(DateFieldUiBinder.class);

	interface DateFieldUiBinder extends UiBinder<Widget, DateField> {
	}
	
	private final Widget widget;
	
	@UiField Element lblEl;
	@UiField DateInput dateBox;
	
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
		showShim=false;
		
	}

	@Override
	public FieldWidget cloneWidget() {
		return new DateField();
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
		dateBox.setTitle(help);
	}
	
	@Override
	protected DataType getType() {
		return DataType.DATE;
	}
	
	@Override
	public Value getFieldValue() {
		Date dt = dateBox.getDate();
		
		if(dt==null){
			return null;
		}
		
		return new DateValue(field.getId(), field.getName(), dt);
	}
}
