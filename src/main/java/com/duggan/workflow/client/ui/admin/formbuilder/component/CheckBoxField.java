package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class CheckBoxField extends FieldWidget {

	private static InlineCheckBoxUiBinder uiBinder = GWT
			.create(InlineCheckBoxUiBinder.class);
	

	interface InlineCheckBoxUiBinder extends UiBinder<Widget,CheckBoxField> {
	}
	
	@UiField AbsolutePanel container;
	
	@UiField Element lblEl;
	
	@UiField CheckBox component;
	@UiField SpanElement spnMandatory;
	
	private final Widget widget;

	public CheckBoxField() {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, id));
		widget = uiBinder.createAndBindUi(this);
		add(widget);
		UIObject.setVisible(spnMandatory, false);
	}
	
	/**
	 * This is an edit property field - This is a field
	 * used to edit a single property
	 * 
	 * @param property
	 */
	public CheckBoxField(final Property property) {
		this();
		designMode=true;
		
		lblEl.setInnerHTML(property.getCaption());
		
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
		return new CheckBoxField();
	}
	
	@Override
	protected void setCaption(String caption) {
		lblEl.setInnerText(caption);
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
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly || isComponentReadOnly();
		component.setEnabled(!this.readOnly);
	}
	
	@Override
	public Widget getComponent(boolean small) {
		return component;
	}
	
	@Override
	public Value getFieldValue() {
		BooleanValue value = new BooleanValue(null, getPropertyValue(NAME), component.getValue());
		return value;
	}
	
	@Override
	public void setValue(Object value) {
		if(value!=null && value instanceof Boolean){
			component.setValue((Boolean)value);
		}
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		if(field.getDocId()!=null)
			component.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
						ENV.setContext(field, event.getValue());
				}
			});
	}

}
