package com.duggan.workflow.client.ui.admin.formbuilder.propertypanel;

import java.util.List;

import com.duggan.workflow.client.ui.events.SavePropertiesEvent;
import com.duggan.workflow.shared.model.form.FormModel;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class PropertyPanelPresenter extends
		PresenterWidget<PropertyPanelPresenter.MyView> {


	public interface MyView extends PopupView {
		void showProperties(List<Property> properties, FormModel model);
		PopupPanel getPopUpContainer();
		FocusPanel getPopoverFocus();
		HTMLPanel getiArrow() ;
		HasClickHandlers getSaveButton();
		void showBody(boolean status, Widget w);
	}
	
	FormModel parentModel; //

	
	@Inject
	public PropertyPanelPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {				
				fireEvent(new SavePropertiesEvent(parentModel));
				getView().hide();
			}
		});
	}

	public void setProperties(FormModel parentModel, List<Property> properties) {
		this.parentModel = parentModel;
		getView().showProperties(properties, parentModel);
	}
	
}
