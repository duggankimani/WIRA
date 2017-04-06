package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;

import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class CheckBoxGroup extends FieldWidget implements IsSelectionField {

	private static CheckBoxGroupUiBinder uiBinder = GWT
			.create(CheckBoxGroupUiBinder.class);

	interface CheckBoxGroupUiBinder extends UiBinder<Widget, CheckBoxGroup> {
	}

	private final Widget widget;

	@UiField
	Element lblEl;
	@UiField
	HTMLPanel vPanel;
	@UiField
	HTMLPanel panelGroup;
	
	Value fieldValue = null;

	public CheckBoxGroup() {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, refId));
		addProperty(new Property(SELECTIONTYPE, "Reference", DataType.STRING));
		widget = uiBinder.createAndBindUi(this);
		add(widget);
	}
	
	public CheckBoxGroup(ArrayList<Element> checkboxes) {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, refId));
		addProperty(new Property(SELECTIONTYPE, "Reference", DataType.STRING));
		widget = uiBinder.createAndBindUi(this);
		add(widget);
	}

	@Override
	public FieldWidget cloneWidget() {
		return new CheckBoxGroup();
	}

	@Override
	protected void setCaption(String caption) {
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
		return DataType.CHECKBOXGROUP;
	}

	@Override
	public void setSelectionValues(ArrayList<KeyValuePair> values) {
		vPanel.clear();
		if (values == null) {
			return;
		}

		for (KeyValuePair pair : values) {
			vPanel.add((CheckBox) getCheckBox(pair));
		}
		field.setSelectionValues(values);
	}

	private CheckBox getCheckBox(KeyValuePair pair) {
		CheckBox button = new CheckBox(getPropertyValue(SELECTIONTYPE));

		String name = pair.getName();
		if (name == null) {
			name = "checkboxbtns";
		}

		button.addStyleName("checkbox");
		button.getElement().setId(name);
		button.setDirectionEstimator(true);
		button.setText(pair.getValue());
		button.setName(pair.getKey());
		button.setFormValue(pair.getKey());
		resetStyle(button);
		button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				CheckBox src = (CheckBox) event.getSource();
				valueChange(src, event.getValue());
			}
		});
		return button;
	}

	private void resetStyle(CheckBox checkbox) {
		//Span
		Element span = checkbox.getElement();
		span.getStyle().setDisplay(Display.INLINE_BLOCK);

		//Input
		Element input = span.getElementsByTagName("input").getItem(0);
		input.getStyle().setMarginLeft(0, Unit.PX);
		//Label
		Element label = span.getElementsByTagName("label").getItem(0);
		label.getStyle().setDisplay(Display.INLINE_BLOCK);
		label.getStyle().setPaddingLeft(5, Unit.PX);
	}

	protected void valueChange(CheckBox source, boolean value) {
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
		int count = vPanel.getWidgetCount();
		ArrayList<KeyValuePair> ArrayList = new ArrayList<KeyValuePair>();

		for (int i = 0; i < count; i++) {
			CheckBox txtBox = (CheckBox) vPanel.getWidget(i);
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
		super.setValue(value);
		int count = vPanel.getWidgetCount();

		if (value != null) {
			String[] values = value.toString().split(";");
			
			//Window.alert(">> Size="+values.length);
			for (int idx=0; idx<values.length; idx++) {
				String dbKey = values[idx];
				
				if (dbKey == null || dbKey.trim().isEmpty()) {
					continue;
				}
				
				for (int i = 0; i < count; i++) {
					CheckBox txtBox = (CheckBox) vPanel.getWidget(i);
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
		return vPanel;
	}

	@Override
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly || isComponentReadOnly();

		int count = vPanel.getWidgetCount();
		for (int i = 0; i < count; i++) {
			CheckBox btn = (CheckBox) vPanel.getWidget(i);
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
