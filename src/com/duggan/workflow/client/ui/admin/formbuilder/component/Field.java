package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class Field extends AbsolutePanel implements HasDragHandle{

	private FocusPanel shim = new FocusPanel();

	protected List<Property> properties = new ArrayList<Property>();
	
	
	public Field() {
		shim.addStyleName("demo-PaletteWidget-shim");
		properties.add(new Property("NAME", "Name", DataType.STRING));
		properties.add(new Property("CAPTION", "Label Text", DataType.STRING));
		properties.add(new Property("HELP", "Help", DataType.STRING));
		properties.add(new Property("MANDATORY", "Mandatory", DataType.BOOLEAN));
		properties.add(new Property("READONLY", "Read Only", DataType.BOOLEAN));
	}

	public abstract Field cloneWidget();

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
				
				/*Position of the pop-over*/
				int top=7;
				int left=80;
				int arrowPosition =shim.getAbsoluteTop()-30;
				AppManager.showPropertyPanel(properties,top,left,arrowPosition);
			}
		});
	}
	

	private void saveProperties() {
		
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
		return properties;
	}
}
