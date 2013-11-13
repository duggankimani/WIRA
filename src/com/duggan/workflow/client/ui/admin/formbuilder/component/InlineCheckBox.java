package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

public class InlineCheckBox extends FieldWidget {

	private static InlineCheckBoxUiBinder uiBinder = GWT
			.create(InlineCheckBoxUiBinder.class);
	

	interface InlineCheckBoxUiBinder extends UiBinder<Widget,InlineCheckBox> {
	}
	
	@UiField AbsolutePanel container;
	
	@UiField Element lblEl;
	
	@UiField CheckBox component;
	
	private final Widget widget;

	public InlineCheckBox() {
		super();
		widget = uiBinder.createAndBindUi(this);
		add(widget);
	}
	
	/**
	 * This is an edit property field - This is a field
	 * used to edit a single property
	 * 
	 * @param property
	 */
	public InlineCheckBox(final Property property) {
		this();
		showShim=true;
		
		setCaption(property.getCaption());
		
		Value value = property.getValue();
		if(value!=null){
			Object val = value.getValue();
			if(val==null){
				val = new BooleanValue(false);
			}
		}else{
			value = new BooleanValue(false);
		}
		property.setValue(value);
		
		component.setValue((Boolean)value.getValue());
		
		component.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				Value value = property.getValue();
				value.setValue(event.getValue());
			}
		});
		//prop
	}

	@Override
	public FieldWidget cloneWidget() {
		return new InlineCheckBox();
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
		component.setTitle(help);
	}

	@Override
	protected DataType getType() {
		return DataType.CHECKBOX;
	}
	
	@Override
	public void setField(Field field) {
		super.setField(field);
		
		Value value = field.getValue();
		
	}
	
	@Override
	protected void setReadOnly(boolean readOnly) {
		component.setEnabled(false);
	}
}
