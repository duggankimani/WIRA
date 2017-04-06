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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

public class RadioGroup extends FieldWidget implements IsSelectionField {

	private static InlineRadioUiBinder uiBinder = GWT
			.create(InlineRadioUiBinder.class);

	interface InlineRadioUiBinder extends UiBinder<Widget, RadioGroup> {
	}

	private Widget widget;

	@UiField
	Element lblEl;
	@UiField
	HTMLPanel panelGroup;
	@UiField
	HTMLPanel vPanel;
	Value fieldValue = null;

	private boolean isWrappedField;// HTMLForm Child Field

	HashMap<String, SimpleRadio> wrappedFields = new HashMap<String, SimpleRadio>();
	
	public RadioGroup() {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX,
				refId));
		addProperty(new Property(SELECTIONTYPE, "Reference", DataType.STRING));
		widget = uiBinder.createAndBindUi(this);
		add(widget);
	}

	@Deprecated
	public RadioGroup(Element parentElement, boolean designMode) {
		super();
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
		return new RadioGroup();
	}

	@Override
	protected void setCaption(String caption) {
		if (lblEl != null)
			lblEl.setInnerHTML(caption);
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
		if (this.isWrappedField) {
			return;
		}

		vPanel.clear();
		if (values == null) {
			return;
		}

		for (KeyValuePair pair : values) {
			vPanel.add((RadioButton) getRadio(pair));
		}
		field.setSelectionValues(values);
	}

	private RadioButton getRadio(KeyValuePair pair) {
		RadioButton button = new RadioButton(getPropertyValue(SELECTIONTYPE));

		String name = pair.getName();
		if (name == null) {
			name = "radiobtns";
		}

		button.addStyleName("radiobtns");
		button.getElement().setId(name);
		button.setDirectionEstimator(true);
		button.setText(pair.getValue());
		button.setFormValue(pair.getKey());
		button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				RadioButton src = (RadioButton) event.getSource();
				valueChange(src, event.getValue());
			}
		});
		return button;
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

	protected void valueChange(RadioButton source, boolean value) {
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
		if (isWrappedField) {
			return new ArrayList<KeyValuePair>();
		}

		int count = vPanel.getWidgetCount();
		ArrayList<KeyValuePair> ArrayList = new ArrayList<KeyValuePair>();

		for (int i = 0; i < count; i++) {
			RadioButton txtBox = (RadioButton) vPanel.getWidget(i);
			String name = txtBox.getName();
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
		if (isWrappedField) {
			if(value!=null){
				SimpleRadio radio = wrappedFields.get(value.toString());
				radio.setValue(true);
				fieldValue = new StringValue(null, field.getName(), value.toString());
			}
			
			return;
		}

		String val = value == null ? null : value.toString();
		super.setValue(value);
		int count = vPanel.getWidgetCount();
		for (int i = 0; i < count; i++) {
			RadioButton txtBox = (RadioButton) vPanel.getWidget(i);
			String key = txtBox.getFormValue();

			if (val != null)
				if (val.equals(key)) {
					txtBox.setValue(true);
					valueChange(txtBox, true);
					return;
				}
		}
	}

	@Override
	public Value getFieldValue() {

		return fieldValue;
	}

	@Override
	public Widget createComponent(boolean small) {
		return vPanel;
	}

	@Override
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly || isComponentReadOnly();

		int count = vPanel.getWidgetCount();
		for (int i = 0; i < count; i++) {
			RadioButton btn = (RadioButton) vPanel.getWidget(i);
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
	
	@Override
	public void gridFormat(boolean isGridField) {
		super.gridFormat(isGridField);
		lblEl.addClassName("hide");
		lblEl.removeClassName("control-label");
		panelGroup.removeStyleName("control-group");
		vPanel.removeStyleName("controls");
	}
}
