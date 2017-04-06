package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;
import java.util.HashMap;

import com.duggan.workflow.client.ui.component.SimpleRadio;
import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;

public class HTMLRadioGroup extends FieldWidget implements IsSelectionField {
	
	Element lblEl;
	//HTMLPanel vPanel;
	Value fieldValue = null;

	private boolean isWrappedField;// HTMLForm Child Field

	HashMap<String, SimpleRadio> wrappedFields = new HashMap<String, SimpleRadio>();

	private Element parentElement;
	
	public HTMLRadioGroup(Element parentElement, boolean designMode) {
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
			showPropertiesOnClick(parentElement.getId());
		}

		NodeList<Element> radios = parentElement.getElementsByTagName("input");
		for (int i = 0; i < radios.getLength(); i++) {
			Element radio = radios.getItem(i);
			if (radio.getAttribute("type") == null
					|| !radio.getAttribute("type").equals("radio")) {
				GWT.log("Ignore Invalid radio input found in parent '"
						+ parentElement.getId() + "' -> " + radio.toString());
				continue;
			}

			SimpleRadio button = SimpleRadio.wrap(radio, true);
			wrappedFields.put(radio.getAttribute("value"), button);
			button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					SimpleRadio src = (SimpleRadio) event.getSource();
					valueChange(src, event.getValue());
				}
			});
		}

		// parentElement
	}
	
	@Override
	public void setField(Field field) {
		super.setField(field);
	}

	@Override
	public FieldWidget cloneWidget() {
		return new HTMLRadioGroup(parentElement, designMode);
	}

	@Override
	protected void setCaption(String caption) {
//		if (lblEl != null)
//			lblEl.setInnerHTML(caption);
	}

	@Override
	protected void setPlaceHolder(String placeHolder) {
		// txtComponent.setPlaceholder(placeHolder);
	}

	@Override
	protected void setHelp(String help) {
		// txtComponent.setTitle(help);
	}

	@Override
	protected DataType getType() {
		return DataType.BOOLEAN;
	}

	@Override
	public void setSelectionValues(ArrayList<KeyValuePair> values) {
	}

	protected void valueChange(SimpleRadio source, boolean value) {
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
	public void setValue(Object value) {
		if (isWrappedField) {
			if(value!=null){
				SimpleRadio radio = wrappedFields.get(value.toString());
				radio.setValue(true);
				fieldValue = new StringValue(null, field.getName(), value.toString());
			}
			
			return;
		}
	}

	@Override
	public Value getFieldValue() {

		return fieldValue;
	}
	
	@Override
	public ArrayList<KeyValuePair> getValues() {
		return new ArrayList<KeyValuePair>();
	}


	@Override
	public Widget createComponent(boolean small) {
		return null;
	}

	@Override
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly || isComponentReadOnly();
		for (SimpleRadio radio:wrappedFields.values()) {
			radio.setReadOnly(this.readOnly);
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
