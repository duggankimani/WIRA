package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class IFrameField extends FieldWidget{

	private static IFrameFieldUiBinder uiBinder = GWT
			.create(IFrameFieldUiBinder.class);

	interface IFrameFieldUiBinder extends UiBinder<Widget, IFrameField> {
	}
	
	@UiField HTMLPanel panelIframe;
	private final Widget widget;
	
	@UiField LabelElement lblTitle;

	public IFrameField() {
		super();
		widget = uiBinder.createAndBindUi(this);
		addProperty(new Property(IFRAME, "IFRAME", DataType.STRINGLONG));
		add(widget);
	}

	@Override
	public FieldWidget cloneWidget() {
		
		return new IFrameField();
	}

	@Override
	protected DataType getType() {
		return DataType.IFRAME;
	}
	
	@Override
	protected void setCaption(String caption) {
		super.setCaption(caption);
		lblTitle.setInnerText(caption);
	}
	
	@Override
	public void setField(Field field) {
		super.setField(field);
		
		String iframe = getPropertyValue(IFRAME);
		panelIframe.clear();
		if(iframe!=null){
			panelIframe.add(new HTML(iframe.toString()));
		}
	}

	@Override
	public Widget getInputComponent() {
		return this;
	}

	@Override
	public Element getViewElement() {
		return null;
	}


}
