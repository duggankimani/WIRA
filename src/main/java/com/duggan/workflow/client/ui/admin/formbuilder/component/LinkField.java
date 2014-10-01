package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class LinkField extends FieldWidget{

	private static LinkFieldUiBinder uiBinder = GWT
			.create(LinkFieldUiBinder.class);

	private final Widget widget;
	
	interface LinkFieldUiBinder extends UiBinder<Widget, LinkField> {
	}

	@UiField ActionLink aLink;
	
	public LinkField() {
		super();
		addProperty(new Property(HREF, "HREF", DataType.STRING));
		widget = uiBinder.createAndBindUi(this);
		aLink.setTarget("_blank");
		add(widget);
	}
	
	@Override
	public FieldWidget cloneWidget() {
		return new LinkField();
	}
	
	@Override
	protected DataType getType() {
		
		return DataType.LINK;
	}
	
	@Override
	public void setField(Field field) {
		super.setField(field);
		String href = getPropertyValue(HREF);
		System.err.println("HREF = "+href);
		aLink.setHref(href);
	}
	
	@Override
	protected void setCaption(String caption) {
		aLink.setText(caption);		
	}
	
	@Override
	protected void setHelp(String help) {
		super.setTitle(help);
	}

}
