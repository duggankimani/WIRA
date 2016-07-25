package com.duggan.workflow.client.ui.component;

import com.duggan.workflow.client.event.CheckboxSelectionEvent;
import com.duggan.workflow.client.event.CheckboxSelectionEvent.CheckboxSelectionHandler;
import com.duggan.workflow.client.util.AppContext;
import com.google.gwt.user.client.ui.CheckBox;

public class Checkbox extends CheckBox implements CheckboxSelectionHandler {

	private Object model;

	public Checkbox() {
	}
	
	public Checkbox(Object model) {
		this.model = model;
	}

	public Object getModel() {
		return model;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		AppContext.getEventBus().addHandler(CheckboxSelectionEvent.getType(),
				this);
	}

	@Override
	public void onCheckboxSelection(CheckboxSelectionEvent event) {
		if (event.getModel() != null) {
			setValue(event.getModel().equals(model) && event.getValue());
		}
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
	}

}
