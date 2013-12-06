package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class ColumnProperty extends FieldWidget {

	private static ColumnPropertyUiBinder uiBinder = GWT
			.create(ColumnPropertyUiBinder.class);

	interface ColumnPropertyUiBinder extends UiBinder<Widget, ColumnProperty> {
	}
	
	final Widget widget;
	@UiField SelectBasic slctField;
	@UiField TextField txtField;
	
	public ColumnProperty() {
		super();
		widget = uiBinder.createAndBindUi(this);
		add(widget);
		
	}
	
	public ColumnProperty(Property property) {
		this();
		txtField.txtComponent.setClass("input-large"); //Smaller TextField
		txtField.lblEl.setInnerText(property.getCaption());
		
		slctField.lblEl.setInnerText(property.getCaption());
		
		List<KeyValuePair> pairList = new ArrayList<KeyValuePair>();
		
		pairList.add(new KeyValuePair("Test","Checkbox"));
		pairList.add(new KeyValuePair("Test2","TextBox"));
		pairList.add(new KeyValuePair("Test3","Label Field"));
		
		slctField.lstItems.setItems(pairList);
	}

	@Override	
	public FieldWidget cloneWidget() {
		return new ColumnProperty();
	}

	@Override
	protected DataType getType() {
		return DataType.COLUMNPROPERTY;
	}

}
