package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
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

public class TextArea extends FieldWidget {

	private static TextAreaUiBinder uiBinder = GWT
			.create(TextAreaUiBinder.class);

	private final Widget widget;

	interface TextAreaUiBinder extends UiBinder<Widget, TextArea> {
	}

	@UiField
	Element lblEl;
	@UiField
	com.duggan.workflow.client.ui.component.TextArea txtComponent;
	@UiField
	HTMLPanel panelControls;
	@UiField
	HTMLPanel panelGroup;
	@UiField
	InlineLabel lblComponent;
	@UiField
	SpanElement spnMandatory;
	@UiField
	DivElement divControls;
	@UiField
	SpanElement spnMsg;
	@UiField
	Element spnIcon;

	public TextArea() {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, id));
		addProperty(new Property(PLACEHOLDER, "Place Holder", DataType.STRING,
				id));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
		addProperty(new Property(LABELPOSITION, "Label Position",
				DataType.SELECTBASIC, new KeyValuePair("top", "Top"),
				new KeyValuePair("left", "Left")));

		widget = uiBinder.createAndBindUi(this);

		txtComponent.getElement().setAttribute("id", "textarea");
		add(widget);
		UIObject.setVisible(spnMandatory, false);
	}

	public TextArea(Element textInput, boolean designMode) {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, id));
		addProperty(new Property(PLACEHOLDER, "Place Holder", DataType.STRING,
				id));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
		addProperty(new Property(LABELPOSITION, "Label Position",
				DataType.SELECTBASIC, new KeyValuePair("top", "Top"),
				new KeyValuePair("left", "Left")));

		// Wrap
		txtComponent = com.duggan.workflow.client.ui.component.TextArea
				.wrap(textInput);
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

	public TextArea(final Property property) {
		this();
		lblEl.setInnerHTML(property.getCaption());
		txtComponent.setName(property.getName());

		Value textValue = property.getValue();
		String text = textValue == null ? "" : textValue.getValue().toString();

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
				 * Holder, help to be Sysnched with the form field, so that the
				 * changes are observed immediately
				 * 
				 * All other Properties need not be synched this way
				 */
				if (name.equals(CAPTION) || name.equals(PLACEHOLDER)
						|| name.equals(HELP) || name.equals(STATICCONTENT)) {
					firePropertyChanged(property, value);
				}
				// AppContext.getEventBus().fireEvent(new );
				// AppContext.getEventBus().fireEvent(event);

			}
		});
		// initPropertyWidget();
	}

	@Override
	public FieldWidget cloneWidget() {
		return new TextArea();
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

	public HTMLPanel getContainer() {
		return panelControls;
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
			if (lblComponent != null)
				lblComponent.setText((String) value);

		}
	}

	@Override
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly || isComponentReadOnly();
		UIObject.setVisible(txtComponent.getElement(), !this.readOnly);
		UIObject.setVisible(lblComponent.getElement(), this.readOnly);
		UIObject.setVisible(spnMandatory, (!this.readOnly && isMandatory()));

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
		return panelControls;
	}

	protected void setLabelPosition(boolean top) {
		if (top) {
			// if(getFieldValue()==null)
			panelControls.removeStyleName("comment-container");

			lblEl.removeClassName("control-label");
			divControls.removeClassName("controls");
			divControls.getStyle().setMarginBottom(12, Unit.PX);
		} else {
			// if(txtComponent.getValue()==null ||
			// txtComponent.getValue().isEmpty())
			// panelControls.addStyleName("comment-container");

			lblEl.addClassName("control-label");
			divControls.addClassName("controls");
		}
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
		return lblComponent.getElement();
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
