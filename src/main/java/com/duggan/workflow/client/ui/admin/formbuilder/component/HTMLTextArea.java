package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class HTMLTextArea extends FieldWidget {

	private final Widget widget;
	
	@UiField
	Element lblEl;
	@UiField
	com.duggan.workflow.client.ui.component.TextArea txtComponent;

	private Element textInput;
	
	public HTMLTextArea(Element textInput, boolean designMode) {
		super();
		this.textInput = textInput;
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, refId));
		addProperty(new Property(PLACEHOLDER, "Place Holder", DataType.STRING,
				refId));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
		addProperty(new Property(LABELPOSITION, "Label Position",
				DataType.SELECTBASIC, new KeyValuePair("top", "Top"),
				new KeyValuePair("left", "Left")));

		// Wrap
		txtComponent = com.duggan.workflow.client.ui.component.TextArea
				.wrap(textInput,true);
		widget = txtComponent;
		assert textInput.getId() != null;

		// Set
		setProperty(NAME, textInput.getId());
		field.setName(textInput.getId());
		lblEl = findLabelFor(textInput);
		if (lblEl != null) {
			field.setCaption(lblEl.getInnerHTML());
			setProperty(CAPTION, lblEl.getInnerHTML());
		}

		if (designMode) {
			txtComponent.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					showProperties(0);
				}
			});
		}
	}

	@Override
	public FieldWidget cloneWidget() {
		return new HTMLTextArea(textInput, true);
	}

	@Override
	protected void setCaption(String caption) {
//		lblEl.setInnerHTML(caption);
	}

	@Override
	protected void setPlaceHolder(String placeHolder) {
		txtComponent.setPlaceholder(placeHolder);
	}

	@Override
	protected void setHelp(String help) {
		txtComponent.setTitle(help);
	}

	public HTMLPanel getContainer() {
		return null;
	}

	@Override
	protected DataType getType() {
		return DataType.STRINGLONG;
	}

	@Override
	public Value getFieldValue() {

		String value = txtComponent.getValue();

		if (value == null || value.isEmpty())
			return null;

		return new StringValue(field.getLastValueId(), field.getName(), value);
	}

	@Override
	public void setValue(Object value) {
		super.setValue(value);
		if (value != null) {
			txtComponent.setValue((String) value);
		}
	}

	@Override
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly || isComponentReadOnly();
		if (!isReadOnly) {
			txtComponent.getElement().getStyle()
					.setBorderStyle(BorderStyle.SOLID);
		} else {
			txtComponent.getElement().getStyle()
					.setBorderStyle(BorderStyle.NONE);
		}
	}

	@Override
	public Widget createComponent(boolean small) {
		return null;
	}

	protected void setLabelPosition(boolean top) {
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (field.getDocId() != null)
			txtComponent
					.addValueChangeHandler(new ValueChangeHandler<String>() {
						@Override
						public void onValueChange(ValueChangeEvent<String> event) {
							ENV.setContext(field, event.getValue());
						}
					});
	}

	@Override
	public Widget getInputComponent() {
		return txtComponent;
	}

	@Override
	public Element getViewElement() {
		return null;
	}

	@Override
	public void setComponentValid(boolean isValid) {
	}
}
