package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class FieldWidget extends AbsolutePanel implements HasDragHandle{

	private FocusPanel shim = new FocusPanel();
	
	protected Map<String, Property> props = new LinkedHashMap<String, Property>();
	
	public FieldWidget() {
		shim.addStyleName("demo-PaletteWidget-shim");
		addProperty(new Property("NAME", "Name", DataType.STRING));
		addProperty(new Property("CAPTION", "Label Text", DataType.STRING));
		addProperty(new Property("HELP", "Help", DataType.STRING));
		addProperty(new Property("MANDATORY", "Mandatory", DataType.BOOLEAN));
		addProperty(new Property("READONLY", "Read Only", DataType.BOOLEAN));
	}

	protected void addProperty(Property property) {
		assert props !=null;
		assert property!=null;
		assert property.getName()!=null;
		
		props.put(property.getName(), property);
	}

	public abstract FieldWidget cloneWidget();

	public void activatePopup(){
		shim.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				OnOptionSelected optionSelected = new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						//determine what to do/show
						if(name.equals("Save")){
							saveProperties();
						}
					}

				};

				//AppManager.showPopUp("Property Editor", new PropertyEditor(properties), optionSelected, "Save", "Cancel");
				AppManager.showPropertyPanel(getProperties());
			}
		});
	}
	

	private void saveProperties() {
		
	}
	
	public Field getField(){
		Field field = new Field();
		
		field.setCaption(getValue("CAPTION"));
//		field.setFormId(formId);
//		field.setId(id);
		field.setName(getValue("NAME"));
		field.setProperties(getProperties());
		field.setType(DataType.STRING);
		field.setValue(null);
		
		return field;
	}
	
	private String getValue(String key) {
		
		Property property = props.get(key);
		
		if(property==null)
			return null;
		
		Value value = property.getValue();
		if(value==null)
			return null;
		
		return value.getValue()==null? null : value.getValue().toString();
	}

	@Override
	public Widget getDragHandle() {
		return shim;
	}

	/**
	 * Let shim size match our size.
	 * 
	 * @param width
	 *            the desired pixel width
	 * @param height
	 *            the desired pixel height
	 */
	@Override
	public void setPixelSize(int width, int height) {
		super.setPixelSize(width, height);
		shim.setPixelSize(width, height);
	}

	/**
	 * Let shim size match our size.
	 * 
	 * @param width
	 *            the desired CSS width
	 * @param height
	 *            the desired CSS height
	 */
	@Override
	public void setSize(String width, String height) {
		super.setSize(width, height);
		shim.setSize(width, height);
	}

	/**
	 * Adjust the shim size and attach once our widget dimensions are known.
	 */
	@Override
	protected void onLoad() {
		super.onLoad();
		shim.setPixelSize(getOffsetWidth(), getOffsetHeight());
		getElement().getStyle().setPosition(Position.RELATIVE);
	
		add(shim, 0, 0);
	}

	/**
	 * Remove the shim to allow the widget to size itself when reattached.
	 */
	@Override
	protected void onUnload() {
		super.onUnload();
		shim.removeFromParent();
	}
	
	public List<Property> getProperties(){
		List<Property> values = new ArrayList<Property>();
		values.addAll(props.values());
		return values;
	}
}
