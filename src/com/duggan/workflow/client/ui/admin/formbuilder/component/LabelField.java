package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.DataType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class LabelField extends FieldWidget {

	private static LabelFieldUiBinder uiBinder = GWT
			.create(LabelFieldUiBinder.class);

	private final Widget widget;
	
	interface LabelFieldUiBinder extends UiBinder<Widget, LabelField> {
	}


	@UiField Element lblEl;
	@UiField InlineLabel lblComponent;
	
	public LabelField() {
		super();
		
		widget = uiBinder.createAndBindUi(this);
		add(widget);
		
		lblComponent.getElement().setAttribute("type","text");
	}

	@Override
	public FieldWidget cloneWidget() {
		return new LabelField();
	}

	@Override
	protected DataType getType() {
		return DataType.LABEL;
	}
	
	@Override
	protected void setCaption(String caption) {
		lblEl.setInnerText(caption);
	}
	
	@Override
	public void setValue(Object value) {
		if(value!=null && value instanceof String){
			lblComponent.setText(value.toString());
		}
	}
	
	@Override
	public void setTitle(String title) {
		if(title!=null)
			lblEl.setTitle(title);
	}
	
	@Override
	public Widget getComponent() {
		return lblComponent;
	}
}
