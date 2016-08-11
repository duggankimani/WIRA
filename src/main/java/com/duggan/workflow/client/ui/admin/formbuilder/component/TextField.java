package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.client.ui.admin.formbuilder.FormBuilderPresenter;
import com.duggan.workflow.client.ui.admin.formbuilder.propertypanel.PropertyPanelPresenter;
import com.duggan.workflow.client.ui.events.PropertyChangedEvent;
import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class TextField extends FieldWidget {

	private static TextFieldUiBinder uiBinder = GWT
			.create(TextFieldUiBinder.class);

	interface TextFieldUiBinder extends UiBinder<Widget, TextField> {
	}

	@UiField
	Element lblEl;
	@UiField
	com.duggan.workflow.client.ui.component.TextField txtComponent;
	@UiField
	InlineLabel lblReadOnly;
	@UiField
	HTMLPanel panelControls;
	@UiField
	HTMLPanel panelGroup;
	@UiField
	SpanElement spnMandatory;
	@UiField
	SpanElement spnMsg;
	@UiField
	Element spnIcon;

	private final Widget widget;
	private Property property;

	/**
	 * Default FormBuilder Text Field
	 */
	public TextField() {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, refId));
		addProperty(new Property(PLACEHOLDER, "Place Holder", DataType.STRING,
				refId));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
		addProperty(new Property(ALIGNMENT, "Alignment", DataType.SELECTBASIC,
				new KeyValuePair("left", "Left"), new KeyValuePair("center",
						"Center"), new KeyValuePair("right", "Right")));
		addProperty(new Property(CUSTOMTRIGGER, "Trigger Class",
				DataType.STRING));

		widget = uiBinder.createAndBindUi(this);
		add(widget);
		UIObject.setVisible(spnMandatory, false);

		txtComponent.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				execTrigger();
			}
		});
	}

	/**
	 * Wrapped Text Field input - These are text fields generated from raw html
	 * forms. see {@link HTMLForm}
	 * 
	 * @param textInput
	 * @param designMode
	 */
	public TextField(Element textInput, boolean designMode) {
		super();
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
	
	/**
	 * Text Fields generated for {@link FormBuilderPresenter} Field Properties
	 * {@link PropertyPanelPresenter}
	 * 
	 * @param property
	 */
	public TextField(final Property property) {
		this();
		this.property = property;
		lblEl.setInnerHTML(property.getCaption());
		txtComponent.setName(property.getName());

		Value textValue = property.getValue();
		String text = textValue == null ? ""
				: textValue.getValue() == null ? "" : textValue.getValue()
						.toString();

		txtComponent.setText(text);
		txtComponent.setClass("input-large"); // Smaller TextField
		designMode = true;

		final String name = property.getName();

		txtComponent.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String value = event.getValue();
				Value previousValue = property.getValue();
				if (previousValue == null) {
					previousValue = new StringValue();
				} else {
					if (event.getValue().equals(previousValue.getValue())) {
						return;
					}
				}

				previousValue.setValue(value);
				property.setValue(previousValue);

				/**
				 * This allows visual properties including Caption, Place
				 * Holder, help to be Synchronised with the form field, so that
				 * the changes are observed immediately
				 * 
				 * All other Properties need not be synched this way
				 */
				if (name.equals(CAPTION) || name.equals(PLACEHOLDER)
						|| name.equals(HELP)) {
					firePropertyChanged(property, value);
				}

			}
		});
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
		if (property != null)
			if (property.getName().equals(NAME)) {
				addRegisteredHandler(PropertyChangedEvent.TYPE,
						new PropertyChangedEvent.PropertyChangedHandler() {
							@Override
							public void onPropertyChanged(
									PropertyChangedEvent event) {
								if (event.getPropertyName().equals(CAPTION)) {
									String propertyValue = event
											.getPropertyValue() == null ? null
											: event.getPropertyValue()
													.toString();

									if (propertyValue == null
											|| propertyValue.isEmpty()) {
										return;
									}
									propertyValue = propertyValue.replaceAll(
											"\\s", "_");
									Converter<String, String> converter = CaseFormat.UPPER_UNDERSCORE
											.converterTo(CaseFormat.LOWER_CAMEL);
									propertyValue = converter
											.convert(propertyValue);
									txtComponent.setValue(propertyValue);
									Value value = property.getValue();
									if (value == null) {
										value = new StringValue(null, NAME,
												propertyValue);
									} else {
										value.setValue(propertyValue);
									}

									property.setValue(value);

								}
							}
						});
			}
	}

	@Override
	public FieldWidget cloneWidget() {
		return new TextField();
	}

	@Override
	protected void setCaption(String caption) {
		lblEl.setInnerHTML(caption);
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
			if (lblReadOnly != null) {
				// Raw HTML Fields may be null
				lblReadOnly.setText((String) value);
			}

		} else {
			txtComponent.setValue(null);
			if (lblReadOnly != null) {
				// Raw HTML Fields may be null
				lblReadOnly.setText(null);
			}
		}
	}

	@Override
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly || isComponentReadOnly();

		UIObject.setVisible(txtComponent.getElement(), !this.readOnly);
		UIObject.setVisible(lblReadOnly.getElement(), this.readOnly);

		UIObject.setVisible(spnMandatory, (!this.readOnly && isMandatory()));
	}

	@Override
	public Widget createComponent(boolean small) {

		if (!readOnly)
			if (small) {
				txtComponent.setClass("input-medium");
			}
		return panelControls;
	}

	@Override
	protected void setAlignment(String alignment) {
		txtComponent.getElement().getStyle()
				.setTextAlign(TextAlign.valueOf(alignment.toUpperCase()));
		panelControls.getElement().getStyle()
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
		return lblReadOnly.getElement();
	}

	@Override
	public void setComponentValid(boolean isValid) {
		spnMsg.removeClassName("hide");
		spnIcon.removeClassName("icon-ok-circle");
		spnIcon.removeClassName("icon-remove-circle");
		if (isValid) {
			panelGroup.addStyleName("success");
			spnIcon.addClassName("icon-ok-circle");
			panelGroup.removeStyleName("error");
		} else {
			panelGroup.removeStyleName("success");
			panelGroup.addStyleName("error");
			spnIcon.addClassName("icon-remove-circle");
		}
	}

}
