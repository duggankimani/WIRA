package com.duggan.workflow.client.ui;

import java.util.List;

import com.duggan.workflow.client.ui.admin.formbuilder.propertypanel.PropertyPanelPresenter;
import com.duggan.workflow.client.ui.popup.GenericPopupPresenter;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.ViewImpl;

public class AppManager {

	@Inject static MainPagePresenter mainPagePresenter;
	@Inject static GenericPopupPresenter popupPresenter;
	@Inject static PropertyPanelPresenter propertyPanel;
	
	public static void showPopUp(String header, Widget widget,final OnOptionSelected onOptionSelected, String ... buttons){
		popupPresenter.setHeader(header);
		popupPresenter.setInSlot(GenericPopupPresenter.BODY_SLOT, null);
		popupPresenter.setInSlot(GenericPopupPresenter.BUTTON_SLOT, null);
		
		popupPresenter.getView().setInSlot(GenericPopupPresenter.BODY_SLOT, widget);		
		for(final String text: buttons){
			Anchor aLnk = new Anchor();
			aLnk.setHTML(text+"&nbsp;<i class=\"icon-double-angle-right\"></i>");
			aLnk.setStyleName("btn btn-primary pull-left");
			
			aLnk.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					onOptionSelected.onSelect(text);
					popupPresenter.getView().hide();
				}
			});
			popupPresenter.getView().addToSlot(GenericPopupPresenter.BUTTON_SLOT, aLnk);
		}
		
		mainPagePresenter.addToPopupSlot(popupPresenter, true);		
		
	}
	
	public static void showPopUp(String header, PresenterWidget<ViewImpl> presenter, final OnOptionSelected onOptionSelected,
			String ... buttons){
		showPopUp(header, presenter.getWidget(), onOptionSelected, buttons);
	}
	
	public static void showPropertyPanel(List<Property> properties){
		assert propertyPanel!=null;
		
		propertyPanel.setProperties(properties);
		mainPagePresenter.addToPopupSlot(propertyPanel, true);	
	}
}
