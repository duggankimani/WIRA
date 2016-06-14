package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.form.KeyValuePair;

public interface IsSelectionField {

	public void setSelectionValues(ArrayList<KeyValuePair> values);
	
	public ArrayList<KeyValuePair> getValues();
}
