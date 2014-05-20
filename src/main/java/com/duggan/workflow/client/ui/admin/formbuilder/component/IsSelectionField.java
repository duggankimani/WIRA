package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.List;

import com.duggan.workflow.shared.model.form.KeyValuePair;

public interface IsSelectionField {

	public void setSelectionValues(List<KeyValuePair> values);
	
	public List<KeyValuePair> getValues();
}
