package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.client.ui.admin.formbuilder.FormBuilderPresenter;
import com.duggan.workflow.client.ui.admin.formbuilder.propertypanel.PropertyPanelPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class DateField extends Field {

	private static DateFieldUiBinder uiBinder = GWT
			.create(DateFieldUiBinder.class);

	interface DateFieldUiBinder extends UiBinder<Widget, DateField> {
	}

	@UiField AbsolutePanel abscontainer;
	@UiField FocusPanel fcsContainer;
	
	@Inject
	PropertyPanelPresenter propertypanel;
	
	@Inject
	FormBuilderPresenter  builderpanel;
	
	private final Widget widget;
	
	public DateField() {
		super();
		widget = uiBinder.createAndBindUi(this);	
		add(widget);
		activateClick();
	}
	
	@Override
	public Field cloneWidget() {
		return new DateField();
	}
	
	public void activateClick(){
		fcsContainer.addClickHandler(new ClickHandler(
				) {
			
			@Override
			public void onClick(ClickEvent event) {
				builderpanel.addToPopupSlot(propertypanel, false);
			}
		});
	}
	
	
}
