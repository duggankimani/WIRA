package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;

import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;

public class HTMLSelectBasic extends FieldWidget implements IsSelectionField {

	private final Widget widget;

	Element lblEl;
	DropDownList<KeyValuePair> lstItems;
	private Element select;
	
	public HTMLSelectBasic(Element select, boolean designMode) {
		super();
		this.designMode = designMode;
		this.select = select;
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, refId));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
		addProperty(new Property(CUSTOMTRIGGER, "Trigger Class",
				DataType.STRING));
		addProperty(new Property(SQLDS, "Data Source", DataType.SELECTBASIC));
		addProperty(new Property(SQLSELECT, "Sql", DataType.STRINGLONG));

		// Wrap
		lstItems = com.duggan.workflow.client.ui.component.DropDownList
				.wrap(select,true);
		
		//setSelectionValues(lstItems.getValues());
		
		widget = lstItems;
		assert select.getId() != null;

		// Set
		setProperty(NAME, select.getId());
		field.setName(select.getId());
		lblEl = findLabelFor(select);
		if (lblEl != null) {
			field.setCaption(lblEl.getInnerHTML());
			setProperty(CAPTION, lblEl.getInnerHTML());
		}

		//DB Persistence - necessary? unless dev wants to override properties
		setProperty(HELP, select.getTitle());
		setProperty(MANDATORY,
				new BooleanValue(select.hasAttribute("required")));
		setProperty(READONLY, new BooleanValue(select.hasAttribute("readonly")));

		// field Properties update
		field.setProperties(getProperties());

		if (designMode) {
			lstItems.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					showProperties(0);
				}
			});
		}else{
			// Events
			lstItems.addValueChangeHandler(new ValueChangeHandler<KeyValuePair>() {
				@Override
				public void onValueChange(ValueChangeEvent<KeyValuePair> event) {
					execTrigger();
				}
			});
		}

	}

	@Override
	public FieldWidget cloneWidget() {
		return new HTMLSelectBasic(select,designMode);
	}

	@Override
	protected void setCaption(String caption) {
//		lblEl.setInnerHTML(caption);
	}

	@Override
	protected void setPlaceHolder(String placeHolder) {
		// txtComponent.setPlaceholder(placeHolder);
	}

	@Override
	protected void setHelp(String help) {
		lstItems.setTitle(help);
	}

	@Override
	public Value getFieldValue() {
		KeyValuePair kvp = lstItems.getValue();

		String value = null;

		if (kvp != null) {
			value = kvp.getKey();
		}

		if (value != null) {
			return new StringValue(field.getLastValueId(), field.getName(),
					value);
		}else{
			return new StringValue(field.getLastValueId(), field.getName(),
					null);
		}

//		return null;
	}

	@Override
	protected DataType getType() {
		return DataType.SELECTBASIC;
	}
	
	@Override
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly || isComponentReadOnly();
		lstItems.setReadOnly(this.readOnly);
	}

	@Override
	public void setSelectionValues(ArrayList<KeyValuePair> values) {
		if (designMode
				&& (getPropertyValue(SQLDS) != null || getPropertyValue(SQLSELECT) != null)) {
			// design mode
			values = new ArrayList<KeyValuePair>();

			// design mode; and loaded from a different system/subsystem
			// the user doesnt see these loaded values i.e the drop down cant
			// show the values
			// loaded in design mode

			// we need to disable this capability so that in case the user
			// changes from db loading
			// to manual listing, the web browser does not sending data loaded
			// from another subsystem
			// as new data provided manually
		}
		
		if (values != null && !values.isEmpty()) {
			lstItems.setItems(values);

			// design mode values set here before save is called
			// iff these we manually entered/ not referenced from another ds
			field.setSelectionValues(values);
		}


	}

	@Override
	public ArrayList<KeyValuePair> getValues() {
		return lstItems.values();
	}

	@Override
	public void setValue(Object value) {
		super.setValue(value);
		if (value == null) {
			return;
		}
		
		String key = (String) value;
		lstItems.setValueByKey(key);
	}

	@Override
	public Widget createComponent(boolean small) {
		return null;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (field.getDocRefId() != null)
			lstItems.addValueChangeHandler(new ValueChangeHandler<KeyValuePair>() {
				@Override
				public void onValueChange(ValueChangeEvent<KeyValuePair> event) {
					ENV.setContext(field, event.getValue() == null ? null
							: event.getValue().getKey());
				}
			});
	}

	@Override
	public Widget getInputComponent() {
		return lstItems.getComponent();
	}

	@Override
	public Element getViewElement() {
		return null;
	}

	@Override
	public void setComponentValid(boolean isValid) {
	}

}
