package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;

public class HTMLTextField extends FieldWidget {

	Element lblEl;
	com.duggan.workflow.client.ui.component.TextField txtComponent;

	private final Widget widget;

	private Element textInput;

	/**
	 * Wrapped Text Field input - These are text fields generated from raw html
	 * forms. see {@link HTMLForm}
	 * 
	 * @param textInput
	 * @param designMode
	 */
	public HTMLTextField(Element textInput, boolean designMode) {
		super();
		this.textInput = textInput;
		this.designMode = designMode;
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, refId));
		addProperty(new Property(PLACEHOLDER, "Place Holder", DataType.STRING,
				refId));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
		addProperty(new Property(ALIGNMENT, "Alignment", DataType.SELECTBASIC,
				new KeyValuePair("left", "Left"), new KeyValuePair("center",
						"Center"), new KeyValuePair("right", "Right")));
		addProperty(new Property(CUSTOMTRIGGER, "Trigger Class",
				DataType.STRING));

		// Wrap
		txtComponent = com.duggan.workflow.client.ui.component.TextField
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

		setProperty(HELP, textInput.getTitle());
		setProperty(MANDATORY,
				new BooleanValue(textInput.hasAttribute("required")));
		setProperty(READONLY,
				new BooleanValue(textInput.hasAttribute("readonly")));
		
		// field Properties update
		field.setProperties(getProperties());
		
		// Events
		txtComponent.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				execTrigger();
			}
		});

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
		return new HTMLTextField(textInput,designMode);
	}

	@Override
	protected void setCaption(String caption) {
	}

	@Override
	protected void setPlaceHolder(String placeHolder) {
		txtComponent.setPlaceholder(placeHolder);
	}

	@Override
	protected void setHelp(String help) {
		txtComponent.setTitle(help);
	}

	@Override
	protected DataType getType() {
		return DataType.STRING;
	}

	@Override
	public Value getFieldValue() {
		String value = txtComponent.getValue();

		if (value == null || value.isEmpty())
			return null;

		// System.err.println("TextField.FieldValue "+field.getName()+"= "+value);
		return new StringValue(field.getLastValueId(), field.getName(), value);
	}

	@Override
	public void setValue(Object value) {
		super.setValue(value);

		if (value != null) {
			if (!(value instanceof String)) {
				value = value.toString();
			}

			txtComponent.setValue((String) value);
		} else {
			txtComponent.setValue(null);
		}
	}

	@Override
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly || isComponentReadOnly();
	}

	@Override
	public Widget createComponent(boolean small) {

		if (!readOnly)
			if (small) {
				txtComponent.setClass("input-medium");
			}
		return null;
	}

	@Override
	protected void setAlignment(String alignment) {
		txtComponent.getElement().getStyle()
				.setTextAlign(TextAlign.valueOf(alignment.toUpperCase()));

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
//		txtComponent.setv
	}

}
