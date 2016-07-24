package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.client.ui.admin.formbuilder.HasProperties;
import com.duggan.workflow.client.ui.events.PropertyChangedEvent;
import com.duggan.workflow.client.ui.events.PropertyChangedEvent.PropertyChangedHandler;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class GridColumn extends Composite implements
PropertyChangedHandler{

	Label label;
	FieldWidget fieldWidget;
	Anchor anchor;
	
	public GridColumn(Field field){
		AppContext.getEventBus().addHandler(PropertyChangedEvent.TYPE, this);
		HTMLPanel panel = new HTMLPanel("");
		initWidget(panel);
		
		label = new Label();
		panel.add(label);
		
		addStyleName("thead th td");		
		label.getElement().setInnerHTML(field.getCaption());				
		this.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				int arrowPosition = getAbsoluteTop() - 30;
				fieldWidget.showProperties(arrowPosition);
			}
		});
		
		this.fieldWidget = FieldWidget.getWidget(field.getType(), field, true);
		fieldWidget.setHeight("1px");
		fieldWidget.setWidth("1px");//attached but not visible : So it listens to events
		if(fieldWidget instanceof SelectBasic){
			//Window.alert(fieldWidget.getField()+"");
			fieldWidget.getElement().getStyle().setDisplay(Display.NONE);
		}
		panel.add(fieldWidget);
		
	}
	
	private void addClickHandler(ClickHandler clickHandler) {
		label.addClickHandler(clickHandler);
	}

//	private void setText(String caption) {
//		label.setText(caption);
//	}

	public Field getField(){
		return fieldWidget.getField();
	}

	public void delete() {
		fieldWidget.delete();
	}
	
	public Label getDragComponent(){
		return label;
	}

	public Widget getInputComponent(){
		//create a copy of 
		return fieldWidget.createComponent(true);
	}
	
	@Override
	public void onPropertyChanged(PropertyChangedEvent event) {
		if (!event.isForField()) {
			return;
		}

		assert event.getComponentId()!=null;
		if (!fieldWidget.getField().getId().equals(event.getComponentId())) {
			return;
		}

		String property = event.getPropertyName();
		Object value = event.getPropertyValue();

		if (property.equals(HasProperties.CAPTION))
			label.setText(value == null ? null : value.toString());

	}
	
}
