package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.client.ui.admin.formbuilder.HasProperties;
import com.duggan.workflow.client.ui.events.DeleteFieldEvent;
import com.duggan.workflow.client.ui.events.PropertyChangedEvent;
import com.duggan.workflow.client.ui.events.DeleteFieldEvent.DeleteFieldHandler;
import com.duggan.workflow.client.ui.events.PropertyChangedEvent.PropertyChangedHandler;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.LongValue;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class GridColumn extends Composite implements PropertyChangedHandler, DeleteFieldHandler {

	Label label;
	FieldWidget fieldWidget;
	Anchor anchor;

	public GridColumn(Field field) {
		this(field, true);
	}

	public GridColumn(Field field, boolean designMode) {
		AppContext.getEventBus().addHandler(PropertyChangedEvent.TYPE, this);
		AppContext.getEventBus().addHandler(DeleteFieldEvent.getType(), this);
		
		HTMLPanel panel = new HTMLPanel("");
		initWidget(panel);

		label = new Label();
		panel.add(label);

		String caption = field.getCaption();
		if(caption==null){
			field.getProperty(HasProperties.CAPTION);
			caption = field.getProperty(HasProperties.NAME);
			if(caption==null){
				caption = "Label";
			}
			field.setCaption(caption);
		}
		
		// addStyleName("thead th td");
		label.getElement().setInnerHTML(field.getCaption());

		if (designMode) {
			this.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					int arrowPosition = getAbsoluteTop() - 30;
					fieldWidget.showProperties(arrowPosition);
				}
			});
		}

		this.fieldWidget = FieldWidget.getWidget(field.getType(), field,
				designMode);
		
		String propertyVal = field.getProperty(HasProperties.COLWIDTH);
		Property prop = new Property(HasProperties.COLWIDTH, "Width", DataType.STRING);
		if(propertyVal==null){
			this.fieldWidget.addProperty(prop);
		}else{
			StringValue val = new StringValue(null, HasProperties.COLWIDTH, propertyVal);
			prop.setValue(val);
			this.fieldWidget.addProperty(prop);
		}
		
		this.fieldWidget.addStyleName("hide");
		fieldWidget.setHeight("1px");
		fieldWidget.setWidth("1px");// attached but not visible : So it listens
									// to events
		if (fieldWidget instanceof SelectBasic
				|| fieldWidget instanceof DateField) {
			// Window.alert(fieldWidget.getField()+"");
			fieldWidget.getElement().getStyle().setDisplay(Display.NONE);
		}
		panel.add(fieldWidget);

	}

	private void addClickHandler(ClickHandler clickHandler) {
		label.addClickHandler(clickHandler);
	}

	// private void setText(String caption) {
	// label.setText(caption);
	// }

	public Field getField() {
		return fieldWidget.getField();
	}

	public void delete() {
		fieldWidget.delete();
	}
	
	/**
	 * FieldWidget Already takes care of backend delete. 
	 * For grids, we must remove the column from the grid
	 */
	@Override
	public void onDeleteField(DeleteFieldEvent event) {
		Field fld = event.getField();
		Field field = fieldWidget.getField();
		if(fld.getRefId().equals(field.getRefId())){
			removeColumn();
		}
	}

	public void removeColumn() {
		
	}

	public Label getDragComponent() {
		return label;
	} 

	public Widget getInputComponent() {
		// create a copy of
		return fieldWidget.createComponent(true);
	}

	@Override
	public void onPropertyChanged(PropertyChangedEvent event) {
		if (!event.isForField()) {
			return;
		}

		assert event.getComponentId() != null;
		if (!fieldWidget.getField().getRefId().equals(event.getComponentId())) {
			return;
		}

		String property = event.getPropertyName();
		Object value = event.getPropertyValue();

		if (property.equals(HasProperties.CAPTION))
			label.setText(value == null ? null : value.toString());

	}

}
