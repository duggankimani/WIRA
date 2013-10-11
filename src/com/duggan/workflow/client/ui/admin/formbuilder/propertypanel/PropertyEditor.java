package com.duggan.workflow.client.ui.admin.formbuilder.propertypanel;

import java.util.List;

import com.duggan.workflow.client.ui.admin.formbuilder.component.DateField;
import com.duggan.workflow.client.ui.admin.formbuilder.component.InlineCheckBox;
import com.duggan.workflow.client.ui.admin.formbuilder.component.TextField;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

public class PropertyEditor extends Composite {

	private static PropertyEditorUiBinder uiBinder = GWT
			.create(PropertyEditorUiBinder.class);

	interface PropertyEditorUiBinder extends UiBinder<Widget, PropertyEditor> {
	}
	
	@UiField HTMLPanel pContainer;
	
	public PropertyEditor(){
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public PropertyEditor(List<Property> properties) {
		this();
		initComponents(properties);
	}

	private void initComponents(List<Property> properties) {

		for(Property property: properties){
			switch (property.getType()) {
			case BOOLEAN:
				add(new InlineCheckBox());
				break;
				
			case DATE:
				add(new DateField(property));
				break;

			case DOUBLE:
				add(new TextField());
				break;

			case INTEGER:
				add(new TextField());
				break;

			case STRING:
				add(new TextField());
				break;
			}
		}
	}

	private void add(Widget field) {
		pContainer.add(field);
	}
	
	

}
