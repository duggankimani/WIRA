package com.duggan.workflow.client.ui.activityfeed.components;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.component.BulletPanel;
import com.duggan.workflow.client.ui.component.OLPanel;
import com.duggan.workflow.client.ui.images.ImageResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class CarouselPopup extends Composite {

	@UiField
	FocusPanel panelContainer;
	@UiField
	HTMLPanel innerCarousel;
	@UiField
	OLPanel olCarousels;
	
	@UiField
	SpanElement spnHeader;

	private static CarouselPopupUiBinder uiBinder = GWT
			.create(CarouselPopupUiBinder.class);

	interface CarouselPopupUiBinder extends UiBinder<Widget, CarouselPopup> {
	}
	
	
	public void showIndicators(int count){
		olCarousels.clear();
		for(int i=1; i<=count; i++){
			BulletPanel liElement = new BulletPanel();
			System.err.println(i);
			if(i == 1){
				liElement.getElement().setClassName("active");
			}
			liElement.getElement().setAttribute("data-slide-to", Integer.toString(i));
			liElement.setStyleName("hand");
			liElement.getElement().setAttribute("data-target","#quote-carousel");
			olCarousels.add(liElement);
		}
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
	
	private void clear() {
		innerCarousel.clear();
	}
	
	public void showCreate() {
		clear();
		spnHeader.setInnerHTML("Creating New Request");
		CarouselItem carousel1 = new CarouselItem(true,
				ImageResources.IMAGES.leaveapp(),
				"Fill In the Form, Submit it For Approval");
		CarouselItem carousel2 = new CarouselItem(false,
				ImageResources.IMAGES.adddoc(),
				"Click on the Add Document Button. Choose the Type of document");

		
		innerCarousel.add(carousel1);
		innerCarousel.add(carousel2);
		showIndicators(2);
	}

	public void showFollowUp() {
		clear();
		spnHeader.setInnerHTML("Following Up Request");
		CarouselItem carousel1 = new CarouselItem(
				true,
				ImageResources.IMAGES.business_process(),
				"On the right side of your document, exist a business process layout which indicates how far your document has reached in whole process.");
		
		CarouselItem carousel2 = new CarouselItem(false,
				ImageResources.IMAGES.show_users(),
				"Put the mouse pointer on top of each node to see the users involved");

		innerCarousel.add(carousel1);
		innerCarousel.add(carousel2);
		showIndicators(2);
	}
	public void showTask() {
		clear();
		spnHeader.setInnerHTML("Managing Tasks");
		CarouselItem carousel1 = new CarouselItem(
				true,
				ImageResources.IMAGES.tasks(),
				"Get a listing of all your tasks sorted based on the date.");
		
		innerCarousel.add(carousel1);
		showIndicators(1);
	}
	
	public void showReview(){
		clear();
		spnHeader.setInnerHTML("Reviewing Tasks");
		CarouselItem carousel1 = new CarouselItem(
				true,
				ImageResources.IMAGES.document_action(),
				"Get a listing of all your tasks sorted based on the date.");
		
		CarouselItem carousel2 = new CarouselItem(
				false,
				ImageResources.IMAGES.activity(),
				"Comment and seek clarifications from the bottom panel at each document");
		
		
		innerCarousel.add(carousel1);
		innerCarousel.add(carousel2);
		showIndicators(2);
	}

	public FocusPanel getPanelContainer() {
		return panelContainer;
	}
}
