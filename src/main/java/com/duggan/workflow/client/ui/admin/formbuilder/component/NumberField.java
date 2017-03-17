package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.client.ui.component.DoubleField;
import com.duggan.workflow.client.ui.events.OperandChangedEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.DoubleValue;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class NumberField extends FieldWidget{
	
	private static NumberFieldUiBinder uiBinder = GWT
			.create(NumberFieldUiBinder.class);

	interface NumberFieldUiBinder extends UiBinder<Widget, NumberField> {
	}

	@UiField Element lblEl;
	@UiField DoubleField txtComponent;
	@UiField InlineLabel lblReadOnly;
	@UiField HTMLPanel panelControls;
	@UiField HTMLPanel panelGroup;
	@UiField SpanElement spnMandatory;
	@UiField SpanElement spnMsg;
	@UiField Element spnIcon;
	
	private final Widget widget;
	private boolean isWrappedField;
	
	
	public NumberField(){
		super();
		addProperty(new Property(NUMBERFORMAT, "Format", DataType.STRING));
		setProperty(NUMBERFORMAT, "#,##0.00;(#,##0.00)");
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, refId));
		addProperty(new Property(PLACEHOLDER, "Place Holder", DataType.STRING, refId));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
		addProperty(new Property(CUSTOMTRIGGER, "Trigger Class", DataType.STRING));
		addProperty(new Property(FORMULA, "Formula", DataType.STRING));
		addProperty(new Property(ALIGNMENT, "Alignment", DataType.SELECTBASIC, 
				new KeyValuePair("left", "Left"),
				new KeyValuePair("center", "Center"),
				new KeyValuePair("right", "Right")));

		widget = uiBinder.createAndBindUi(this);
		add(widget);
		UIObject.setVisible(spnMandatory, false);
		UIObject.setVisible(lblReadOnly.getElement(), false);
		
		txtComponent.addValueChangeHandler(new ValueChangeHandler<Double>() {
			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				execTrigger();
			}
		});
	}
	
	@Deprecated
	public NumberField(Element numberInput, boolean designMode){
		super();
		isWrappedField=true;
		this.designMode = designMode;
		addProperty(new Property(NUMBERFORMAT, "Format", DataType.STRING));
		setProperty(NUMBERFORMAT, "#,##0.00;(#,##0.00)");
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, refId));
		addProperty(new Property(PLACEHOLDER, "Place Holder", DataType.STRING, refId));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
		addProperty(new Property(CUSTOMTRIGGER, "Trigger Class", DataType.STRING));
		addProperty(new Property(FORMULA, "Formula", DataType.STRING));
		addProperty(new Property(ALIGNMENT, "Alignment", DataType.SELECTBASIC, 
				new KeyValuePair("left", "Left"),
				new KeyValuePair("center", "Center"),
				new KeyValuePair("right", "Right")));

		
		// Wrap
		txtComponent = com.duggan.workflow.client.ui.component.DoubleField
				.wrap(numberInput,true);
		widget = txtComponent;
		assert numberInput.getId() != null;

		// Set
		setProperty(NAME, numberInput.getId());
		field.setName(numberInput.getId());
		lblEl = findLabelFor(numberInput);
		if (lblEl != null) {
			field.setCaption(lblEl.getInnerHTML());
			setProperty(CAPTION, lblEl.getInnerHTML());
		}

		setProperty(HELP, numberInput.getTitle());
		setProperty(MANDATORY,
				new BooleanValue(numberInput.hasAttribute("required")));
		setProperty(READONLY,
				new BooleanValue(numberInput.hasAttribute("readonly")));
		

		// field Properties update
		field.setProperties(getProperties());

		txtComponent.addValueChangeHandler(new ValueChangeHandler<Double>() {
			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				execTrigger();
			}
		});
		
		if (designMode) {
			txtComponent.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					showProperties(0);
				}
			});
		}
	}
		
	public NumberField(final Property property){
		this();
		lblEl.setInnerHTML(property.getCaption());
		txtComponent.setName(property.getName());
		//txtComponent.set
				
		Value textValue = property.getValue();
		String text = textValue==null? "": 
			textValue.getValue()==null? "": textValue.getValue().toString();
		
		txtComponent.setText(text);
		txtComponent.setClass("input-large"); //Smaller TextField
		designMode=true;
		
		final String name = property.getName();
		
		txtComponent.addValueChangeHandler(new ValueChangeHandler<Double>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				Double value  = event.getValue();
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
	public void setField(Field field) {
		if(field.getProperty(NUMBERFORMAT)!=null){
			txtComponent.setFormat(field.getProperty(NUMBERFORMAT));
		}
		
		super.setField(field);
		if(isWrappedField){
			registerHandlers();
		}
	}
	
	protected void registerValueChangeHandler(){
		txtComponent.addValueChangeHandler(new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				Double value = event.getValue();
				
				//ENV.setContext(field.getName(),field.getQualifiedName(), value);
				ENV.setContext(field, value);
				
				//fire based on actual name-- other fields are aware of actuals
				AppContext.fireEvent(new OperandChangedEvent(field.getDocSpecificName(), value, field.getLineRefId()));
			}
			
		});
		
	}
		
	@Override
	public void addRegisteredHandler(Type<? extends EventHandler> type,
			EventHandler handler) {
		// TODO Auto-generated method stub
		super.addRegisteredHandler(type, handler);
	}
	
	@Override
	public FieldWidget cloneWidget() {
		return new NumberField();
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
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly || isComponentReadOnly();
		txtComponent.setReadOnly(this.readOnly);
//		UIObject.setVisible(txtComponent.getElement(),!this.readOnly);
//		UIObject.setVisible(lblReadOnly.getElement(), this.readOnly);
//		
//		UIObject.setVisible(spnMandatory, (!this.readOnly && isMandatory()));
	}

	@Override
	public Widget createComponent(boolean isGridItem) {

		if(isGridItem){
			panelControls.removeStyleName("controls");
			txtComponent.removeStyleName("input-xlarge");
		}
		return panelControls;
	}
	
	@Override
	protected void setAlignment(String alignment) {		
		txtComponent.getElement().getStyle().setTextAlign(TextAlign.valueOf(alignment.toUpperCase()));		
		panelControls.getElement().getStyle().setTextAlign(TextAlign.valueOf(alignment.toUpperCase()));
		
	}

	
	@Override
	protected DataType getType() {
		return DataType.DOUBLE;
	}
	
	@Override
	public Value getFieldValue() {
		Double value = txtComponent.getValue();
		
		if(value==null)
			return null;
		
		
		return new DoubleValue(field.getLastValueId(), field.getName(),value);
	}
	
	@Override
	public void setValue(Object value) {
		if(value!=null){
			if(value instanceof Value){
				value= ((Value) value).getValue();
			}
			
			if(!(value instanceof Double)){
				try{
					value = new Double(value.toString());
				}catch(Exception e){return;}
			}
			
			ENV.setContext(field, (Double)value);
			txtComponent.setValue((Double)value);
			
			NumberFormat fmt = NumberFormat.getDecimalFormat();
		    String formatted = fmt.format((Double)value);
		    if(lblReadOnly!=null)
			lblReadOnly.setText(formatted);
		}else{
			super.setValue(0.0);
		}

	}
	
	public Value from(String key,String val) {
		try{
			return new DoubleValue(null,key,new Double(val.trim()));
		}catch(Exception e){
		}
		
		return super.from(key, val);
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		if(field.getDocId()!=null)
			txtComponent.addValueChangeHandler(new ValueChangeHandler<Double>() {
				@Override
				public void onValueChange(ValueChangeEvent<Double> event) {
						ENV.setContext(field, event.getValue());
				}
			});
	}

	@Override
	public Widget getInputComponent() {
		return txtComponent;
	}

	@Override
	public Element getViewElement() {
		return lblReadOnly.getElement();
	}
	
	@Override
	public Object parseValue(String value) {
		if(value==null){
			return null;
		}else if(value.isEmpty()){
			return null;
		}else{
			return Double.parseDouble(value);
		}
	}
	
	@Override
	public void setComponentValid(boolean isValid) {
		spnMsg.removeClassName("hide");
		spnIcon.removeClassName("icon-ok-circle");
		spnIcon.removeClassName("icon-remove-circle");
		if(isValid){
			panelGroup.addStyleName("success");
			spnIcon.addClassName("icon-ok-circle");
			panelGroup.removeStyleName("error");
		}else{
			panelGroup.removeStyleName("success");
			panelGroup.addStyleName("error");
			spnIcon.addClassName("icon-remove-circle");
		}
	}
	
	@Override
	public void gridFormat(boolean isGridField) {
		super.gridFormat(isGridField);
		lblEl.addClassName("hide");
		lblEl.removeClassName("control-label");
		panelGroup.removeStyleName("control-group");
		txtComponent.removeStyleName("input-xlarge");
		panelControls.removeStyleName("controls");
	}
}
