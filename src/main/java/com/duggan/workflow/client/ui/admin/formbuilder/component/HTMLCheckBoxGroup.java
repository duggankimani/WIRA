package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;
import java.util.HashMap;

import com.duggan.workflow.client.ui.component.SimpleCheckbox;
import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.Widget;

public class HTMLCheckBoxGroup extends FieldWidget implements IsSelectionField {

	Value fieldValue;
	private Element parentElement;
	private boolean isWrappedField=true;
	HashMap<String, SimpleCheckBox> wrappedFields = new HashMap<String, SimpleCheckBox>();

	public HTMLCheckBoxGroup(Element parentElement, boolean designMode) {
		super();
		this.parentElement = parentElement;
		this.isWrappedField = true;
		this.designMode = designMode;
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX,
				refId));
		addProperty(new Property(SELECTIONTYPE, "Reference", DataType.STRING));
		field.setProperties(getProperties());

		setProperty(NAME, parentElement.getId());
		field.setName(parentElement.getId());

		if (designMode) {
			//showPropertiesOnClick(parentElement.getId());
		}

		NodeList<Element> checkboxs = parentElement.getElementsByTagName("input");
		for (int i = 0; i < checkboxs.getLength(); i++) {
			Element checkbox = checkboxs.getItem(i);
			if (checkbox.getAttribute("type") == null
					|| !checkbox.getAttribute("type").equals("checkbox")) {
				GWT.log("Ignore Invalid checkbox input found in parent '"
						+ parentElement.getId() + "' -> " + checkbox.toString());
				continue;
			}

			GWT.log("Checkbox input found in parent '"
					+ parentElement.getId() + "' -> " + checkbox.getAttribute("value"));
			SimpleCheckBox button = SimpleCheckbox.wrap(checkbox,true);
			wrappedFields.put(checkbox.getAttribute("value"), button);
			button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					SimpleCheckBox src = (SimpleCheckBox) event.getSource();
					valueChange(src, event.getValue());
				}
			});
		}
	}

	@Override
	public FieldWidget cloneWidget() {
		return new HTMLCheckBoxGroup(parentElement, designMode);
	}

	@Override
	protected void setHelp(String help) {
		parentElement.setTitle(help);
	}

	@Override
	protected DataType getType() {
		return DataType.CHECKBOXGROUP;
	}
	
	@Override
	public void setSelectionValues(ArrayList<KeyValuePair> values) {
		
	}

	protected void valueChange(SimpleCheckBox source, boolean value) {
		if (value) {
			fieldValue = new StringValue(field.getLastValueId(),
					source.getName(), source.getFormValue());
			ENV.setContext(field, source.getFormValue());
		} else {
			fieldValue = null;
			ENV.setContext(field, null);
		}
	}

	@Override
	public ArrayList<KeyValuePair> getValues() {
		ArrayList<KeyValuePair> ArrayList = new ArrayList<KeyValuePair>();

		for (SimpleCheckBox txtBox: wrappedFields.values()) {
			
			String name = txtBox.getFormValue();
			String val = txtBox.getFormValue();

			if (txtBox.getValue() != null && txtBox.getValue()) {
				KeyValuePair pair = new KeyValuePair();
				pair.setKey(name);
				pair.setValue(val);
				ArrayList.add(pair);
			}
		}

		return ArrayList;
	}

	@Override
	public void setValue(Object value) {
		if (value != null) {
			String[] values = value.toString().split(";");
			
			for (int idx=0; idx<values.length; idx++) {
				String dbKey = values[idx];
				
				if (dbKey == null || dbKey.trim().isEmpty()) {
					continue;
				}
				
				for (SimpleCheckBox txtBox: wrappedFields.values()) {
					String key = txtBox.getFormValue();
					if (dbKey.equals(key)) {
						txtBox.setValue(true);
						valueChange(txtBox, true);
						break;
					}
				}
			}
		}
	}

	@Override
	public Value getFieldValue() {
		StringBuffer buffer = new StringBuffer();
		for (KeyValuePair pair : getValues()) {
			buffer.append(pair.getKey() + ";");
		}

		fieldValue = new StringValue(field.getLastValueId(), field.getName(),
				buffer.toString());
		return fieldValue;
	}

	@Override
	public Widget createComponent(boolean small) {
		return null;
	}

	@Override
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly || isComponentReadOnly();

		for (SimpleCheckBox btn: wrappedFields.values()) {
			btn.setEnabled(!this.readOnly);
			// btn.set
		}
	}

	@Override
	public Widget getInputComponent() {
		return this;
	}

	@Override
	public Element getViewElement() {
		return null;
	}

	@Override
	public void setComponentValid(boolean isValid) {

	}
}
