package com.duggan.workflow.client.ui.activityfeed.components;

import com.duggan.workflow.client.ui.AppManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

public class CarouselPopup extends Composite {
	
	@UiField FocusPanel panelContainer;
	private static CarouselPopupUiBinder uiBinder = GWT
			.create(CarouselPopupUiBinder.class);

	interface CarouselPopupUiBinder extends UiBinder<Widget, CarouselPopup> {
	}

	public CarouselPopup() {
		initWidget(uiBinder.createAndBindUi(this));
		
		panelContainer.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				AppManager.hideCarousel();
			}
		});
	}
	
	
	public FocusPanel getPanelContainer() {
		return panelContainer;
	}
}
