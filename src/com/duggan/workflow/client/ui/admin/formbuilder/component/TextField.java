package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class TextField extends FieldWidget {

	private static TextFieldUiBinder uiBinder = GWT
			.create(TextFieldUiBinder.class);

	interface TextFieldUiBinder extends UiBinder<Widget, TextField> {
	}

	@UiField Element lblEl;
	@UiField com.duggan.workflow.client.ui.component.TextField txtComponent;
	@UiField InlineLabel lblReadOnly;
	@UiField HTMLPanel panelControls;
	@UiField SpanElement spnMandatory;
	
	private final Widget widget;
	
	public TextField() {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, id));
		addProperty(new Property(PLACEHOLDER, "Place Holder", DataType.STRING, id));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
		addProperty(new Property(ALIGNMENT, "Alignment", DataType.SELECTBASIC, 
				new KeyValuePair("left", "Left"),
				new KeyValuePair("center", "Center"),
				new KeyValuePair("right", "Right")));

		widget = uiBinder.createAndBindUi(this);
		add(widget);
		UIObject.setVisible(spnMandatory, false);
	}
	
	public TextField(final Property property) {
		this();
		lblEl.setInnerHTML(property.getCaption());
		txtComponent.setName(property.getName());
		
		Value textValue = property.getValue();
		String text = textValue==null? "": 
			textValue.getValue()==null? "": textValue.getValue().toString();
		
		txtComponent.setText(text);
		txtComponent.setClass("input-large"); //Smaller TextField
		showShim=true;
		
		final String name = property.getName();
		
		txtComponent.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String value  = event.getValue();
				Value previousValue = property.getValue();
				if(previousValue==null){
					previousValue = new StringValue();
				}else{
					if(event.getValue().equals(previousValue.getValue())){
						return;
					}
				}
				
				
				previousValue.setValue(value);
				property.setValue(previousValue);
				
				/**
				 * This allows visual properties including Caption, Place Holder, help to be 
				 * Synchronised with the form field, so that the changes are observed immediately
				 * 
				 * All other Properties need not be synched this way 
				 */
				if(name.equals(CAPTION) || name.equals(PLACEHOLDER) || name.equals(HELP)){				
					firePropertyChanged(property, value);
				}
				
			}
		});
		
		//name.equals()
	}

	@Override
	public FieldWidget cloneWidget() {
		return new TextField();
	}
	
	@Override
	protected void setCaption(String caption) {
		lblEl.setInnerHTML(caption);
	}
	
	@Override
	protected void setPlaceHolder(String placeHolder) {
		txtComponent.setPlaceholder(placeHolder);
	}
	
	@Override
	protected void setHelp(String help) {
		txtComponent.setTitle(help);
	}
	
	@Override
	protected DataType getType() {
		return DataType.STRING;
	}
	
	@Override
	public Value getFieldValue() {
		String value = txtComponent.getValue();
		
		if(value==null || value.isEmpty())
			return null;
		
		
		return new StringValue(value);
	}
	
	@Override
	public void setValue(Object value) {
		if(value!=null){
			txtComponent.setValue((String)value);
			lblReadOnly.setText((String)value);
		}
	}
	
	@Override
	public void setReadOnly(boolean isReadOnly) {
		Boolean isComponentReadOnly = getValue(READONLY)==null? false : (Boolean)getValue(READONLY);
		if(isComponentReadOnly==null){
			isComponentReadOnly=false;
		}
		
		this.readOnly = isReadOnly && !isComponentReadOnly;
		
		UIObject.setVisible(txtComponent.getElement(),!isReadOnly);
		UIObject.setVisible(lblReadOnly.getElement(), isReadOnly);
		
		UIObject.setVisible(spnMandatory, (!isReadOnly && isMandatory()));
	}

	@Override
	public Widget getComponent(boolean small) {
				
		if(!readOnly)
		if(small){
			txtComponent.setClass("input-medium");
		}
		return panelControls;
	}
	
	@Override
	protected void setAlignment(String alignment) {		
		txtComponent.getElement().getStyle().setTextAlign(TextAlign.valueOf(alignment.toUpperCase()));		
		panelControls.getElement().getStyle().setTextAlign(TextAlign.valueOf(alignment.toUpperCase()));
		
	}
	
}
