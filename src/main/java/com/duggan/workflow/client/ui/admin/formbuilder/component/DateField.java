package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.Date;

import com.duggan.workflow.client.ui.component.DateInput;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.DateValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class DateField extends FieldWidget {

	private static DateFieldUiBinder uiBinder = GWT
			.create(DateFieldUiBinder.class);

	interface DateFieldUiBinder extends UiBinder<Widget, DateField> {
	}

	private final Widget widget;

	@UiField
	Element lblEl;
	@UiField
	DateInput dateBox;
	@UiField
	HTMLPanel panelControls;
	@UiField
	HTMLPanel panelGroup;
	@UiField
	InlineLabel lblComponent;
	@UiField
	SpanElement spnMandatory;
	@UiField
	SpanElement spnMsg;
	@UiField
	Element spnIcon;

	public DateField() {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, refId));
		addProperty(new Property("DATEFORMAT", "Date Format", DataType.STRING));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
		addProperty(new Property(CUSTOMTRIGGER, "Trigger Class",
				DataType.STRING));
		widget = uiBinder.createAndBindUi(this);
		add(widget);
		UIObject.setVisible(spnMandatory, false);
		lblComponent.addStyleName("hide");

		dateBox.getDateInput().addValueChangeHandler(
				new ValueChangeHandler<Date>() {
					@Override
					public void onValueChange(ValueChangeEvent<Date> event) {
						execTrigger();
					}
				});

	}

	/**
	 * This is an edit property field - This is a field used to edit a single
	 * property
	 * 
	 * @param property
	 */
	public DateField(Property property) {
		this();

		String caption = property.getCaption();
		String name = property.getName();
		Value val = property.getValue();
		designMode = false;

	}

	@Override
	public FieldWidget cloneWidget() {
		return new DateField();
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
		dateBox.setTitle(help);
	}

	@Override
	protected DataType getType() {
		return DataType.DATE;
	}

	@Override
	public Value getFieldValue() {
		Date dt = dateBox.getDate();

		if (dt == null) {
			return null;
		}

		return new DateValue(field.getLastValueId(), field.getName(), dt);
	}

	@Override
	public void setValue(Object value) {
		super.setValue(value);
		if (value != null && value instanceof Date) {
			dateBox.setValue((Date) value);

			if (lblComponent != null)
				lblComponent.setText(DateUtils.DATEFORMAT_SYS.format((Date) value));
		} else if (value != null && value instanceof String) {

			lblComponent.setText(value.toString());
			try {
				dateBox.setValue(DateUtils.DATEFORMAT_SYS.parse(value.toString()));
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly || isComponentReadOnly();

//		UIObject.setVisible(dateBox.getElement(), !this.readOnly);
//		UIObject.setVisible(lblComponent.getElement(), this.readOnly);
//
//		UIObject.setVisible(spnMandatory, (!this.readOnly && isMandatory()));
		dateBox.setReadOnly(this.readOnly);
	}

	@Override
	public Widget createComponent(boolean small) {

		if (!readOnly)
			if (small) {
				dateBox.setStyle("input-small");
			}

		return panelControls;
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		if (field.getDocId() != null)	
			dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
				@Override
				public void onValueChange(ValueChangeEvent<Date> event) {
					ENV.setContext(field, event.getValue());
				}
			});
	}

	public Value from(String key, String val) {
		try {
			return new DateValue(null, key, DateUtils.DATEFORMAT.parse(val));
		} catch (Exception e) {
		}

		return super.from(key, val);
	}

	@Override
	public Widget getInputComponent() {
		return dateBox.getInputComponent();
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
	
	@Override
	public void gridFormat(boolean isGridField) {
		super.gridFormat(isGridField);
		lblEl.addClassName("hide");
		lblEl.removeClassName("control-label");
		panelGroup.removeStyleName("control-group");
		dateBox.removeStyleName("input-xlarge");
		panelControls.removeStyleName("controls");
	}
}
