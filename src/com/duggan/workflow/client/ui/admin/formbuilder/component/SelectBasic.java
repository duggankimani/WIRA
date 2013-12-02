package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.List;

import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class SelectBasic extends FieldWidget implements IsSelectionField{

	private static SelectBasicUiBinder uiBinder = GWT
			.create(SelectBasicUiBinder.class);
	
	private final Widget widget;

	@UiField Element lblEl;
	
	@UiField DropDownList<KeyValuePair> lstItems;
	
	interface SelectBasicUiBinder extends UiBinder<Widget, SelectBasic> {
	}

	public SelectBasic() {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, id));
		addProperty(new Property(SELECTIONTYPE, "Reference", DataType.STRING));
		widget= uiBinder.createAndBindUi(this);
		add(widget);
	}

	@Override
	public FieldWidget cloneWidget() {
		return new SelectBasic();
	}
	
	@Override
	protected void setCaption(String caption) {
		lblEl.setInnerHTML(caption);
	}
	
	@Override
	protected void setPlaceHolder(String placeHolder) {
		//txtComponent.setPlaceholder(placeHolder);
	}
	
	@Override
	protected void setHelp(String help) {
		lstItems.setTitle(help);
	}
	
	@Override
	public Value getFieldValue() {
		KeyValuePair kvp = lstItems.getValue();
		
		String value = null;
		
		if(kvp!=null){
			value = kvp.getKey();
		}
		
		if(value!=null){
			return new StringValue(value);
		}
		
		return null;
	}
	
	@Override
	protected DataType getType() {
		return DataType.SELECTBASIC;
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		//lstItems.setReadOnly(readOnly);
	}

	@Override
	public void setSelectionValues(List<KeyValuePair> values) {
		
		if(values!=null){
			lstItems.setItems(values);
		}
		
		field.setSelectionValues(values);
	}

	@Override
	public List<KeyValuePair> getValues() {
		return lstItems.values();
	}
}
