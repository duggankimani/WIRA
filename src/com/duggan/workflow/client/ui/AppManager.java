package com.duggan.workflow.client.ui;

import com.duggan.workflow.client.ui.popup.GenericPopupPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.ViewImpl;

public class AppManager {

	@Inject MainPagePresenter mainPagePresenter;
	@Inject GenericPopupPresenter popupPresenter;
	//@Inject CreateDocPresenter presenter;
	
	@Inject
	public AppManager(EventBus eventBus){	
	}
	
	public void showPopUp(String header, Widget widget,final OnOptionSelected onOptionSelected, String ... buttons){
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
	
	public void showPopUp(String header, PresenterWidget<ViewImpl> presenter, final OnOptionSelected onOptionSelected,
			String ... buttons){
		showPopUp(header, presenter.getWidget(), onOptionSelected, buttons);
	}
}
