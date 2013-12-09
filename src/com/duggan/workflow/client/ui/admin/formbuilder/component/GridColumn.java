package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

public class GridColumn extends Composite {

	Label label;
	FieldWidget fieldWidget;
	
	public GridColumn(Field field){
		
		HTMLPanel panel = new HTMLPanel("");
		initWidget(panel);
		
		label = new Label();
		panel.add(label);
		
		addStyleName("thead th td");		
		setText(field.getCaption());				
		this.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				//System.err.println("Shit clicked");
				int arrowPosition = getAbsoluteTop() - 30;
				fieldWidget.showProperties(arrowPosition);
			}
		});
		
		this.fieldWidget = FieldWidget.getWidget(field.getType(), field, true);
		fieldWidget.setHeight("1px");
		fieldWidget.setWidth("1px");
		panel.add(fieldWidget);
		
	}
	
	private void addClickHandler(ClickHandler clickHandler) {
		label.addClickHandler(clickHandler);
	}

	private void setText(String caption) {
		label.setText(caption);
	}

	public Field getField(){
		return fieldWidget.getField();
	}

	public void delete() {
		fieldWidget.delete();
	}
	
}
