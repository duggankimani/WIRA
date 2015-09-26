package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.upload.attachment.ShowAttachmentEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

	private String href;
	
	public LinkField() {
		super();
		addProperty(new Property(HREF, "HREF", DataType.STRING));
		widget = uiBinder.createAndBindUi(this);
		aLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new ShowAttachmentEvent(href, aLink.getText()));
			}
		});
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
		href = getPropertyValue(HREF);
	}
	
	@Override
	protected void setCaption(String caption) {
		//System.err.println(">>>>>>>>>>>> Link field Create -- "+caption);
		aLink.setText(caption);		
		aLink.setTitle(caption);
	}
	
	@Override
	protected void setHelp(String help) {
		super.setTitle(help);
	}

	@Override
	public Widget getInputComponent() {
		return aLink;
	}

	@Override
	public Element getViewElement() {
		return null;
	}
	
	@Override
	public void setComponentValid(boolean isValid) {
		
	}

}
