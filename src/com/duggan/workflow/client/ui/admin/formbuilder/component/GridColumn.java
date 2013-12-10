package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;

public class GridColumn extends Label {

	private Field field;
	FieldWidget fieldWidget;
	Anchor anchor;
	
	public GridColumn(Field field){
		this.field = field;
		addStyleName("thead tr th td ");
		
		setText(field.getCaption());
		
		this.fieldWidget = FieldWidget.getWidget(DataType.STRING, field, true);
				
		this.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				//System.err.println("Shit clicked");
				int arrowPosition = getAbsoluteTop() - 30;
				fieldWidget.showProperties(arrowPosition);
			}
		});
		
	}
	
	public Field getField(){
		return field;
	}
	
}
