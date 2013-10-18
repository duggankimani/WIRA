package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class SelectBasic extends FieldWidget {

	private static SelectBasicUiBinder uiBinder = GWT
			.create(SelectBasicUiBinder.class);
	
	private final Widget widget;

	@UiField Element lblEl;
	
	interface SelectBasicUiBinder extends UiBinder<Widget, SelectBasic> {
	}

	public SelectBasic() {
		super();
		addProperty(new Property("SELECTIONTYPE", "Reference", DataType.STRING));
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
		//txtComponent.setTitle(help);
	}
	
	@Override
	protected DataType getType() {
		return DataType.SELECTBASIC;
	}

}
