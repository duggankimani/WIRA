package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class TextArea extends FieldWidget {

	private static TextAreaUiBinder uiBinder = GWT
			.create(TextAreaUiBinder.class);
	
	private final Widget widget;

	interface TextAreaUiBinder extends UiBinder<Widget, TextArea> {
	}

	@UiField Element lblEl;
	@UiField com.duggan.workflow.client.ui.component.TextArea txtComponent;
	@UiField DivElement commentContainer;
	
	public TextArea() {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, id));
		addProperty(new Property(PLACEHOLDER, "Place Holder", DataType.STRING, id));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
		widget= uiBinder.createAndBindUi(this);
		txtComponent.getElement().setAttribute("id", "textarea");
		add(widget);
	}
	
	public TextArea(final Property property) {
		this();
		lblEl.setInnerHTML(property.getCaption());
		txtComponent.setName(property.getName());
		
		Value textValue = property.getValue();
		String text = textValue==null? "": textValue.getValue().toString();
		
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
				 * Sysnched with the form field, so that the changes are observed immediately
				 * 
				 * All other Properties need not be synched this way 
				 */
				if(name.equals(CAPTION) || name.equals(PLACEHOLDER) || name.equals(HELP)){				
					firePropertyChanged(property, value);
				}
				//AppContext.getEventBus().fireEvent(new );
				//AppContext.getEventBus().fireEvent(event);
				
			}
		});
		//initPropertyWidget();
	}


	@Override
	public FieldWidget cloneWidget() {
		return new TextArea();
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
	
	public DivElement getCommentContainer() {
		return commentContainer;
	}
	
	@Override
	protected DataType getType() {
		return DataType.STRINGLONG;
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
		if(value!=null)
			txtComponent.setValue((String)value);
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		txtComponent.setReadOnly(readOnly);

	}
	
	@Override
	public Widget getComponent() {
		return txtComponent;
	}
}
