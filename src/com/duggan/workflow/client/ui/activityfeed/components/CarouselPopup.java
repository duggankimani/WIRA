package com.duggan.workflow.client.ui.activityfeed.components;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.component.BulletPanel;
import com.duggan.workflow.client.ui.component.OLPanel;
import com.duggan.workflow.client.ui.images.ImageResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class CarouselPopup extends Composite {

	@UiField
	FocusPanel focusContainer;
	@UiField
	HTMLPanel innerCarousel;

	@UiField
	HTMLPanel innerContainer;
	
	@UiField
	HTMLPanel panelControls;

	@UiField
	OLPanel olCarousels;
	
	@UiField
	Anchor aClose;

	@UiField
	SpanElement spnHeader;

	// Additional width for focusPanel
	int additionalWidth;

	private static CarouselPopupUiBinder uiBinder = GWT
			.create(CarouselPopupUiBinder.class);

	interface CarouselPopupUiBinder extends UiBinder<Widget, CarouselPopup> {
	}
	

	public CarouselPopup() {
		initWidget(uiBinder.createAndBindUi(this));
		focusContainer.getElement().setAttribute("id", "CarouselFocus");

		focusContainer.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				//Window.alert("outside");
				AppManager.hideCarousel();
			}
		});
		
		aClose.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				AppManager.hideCarousel();
			}
		});

	}
	
	public void showIndicators(int count) {
		olCarousels.clear();
		
		if(count<=1){
			panelControls.addStyleName("hide");
		}else{
			panelControls.removeStyleName("hide");
		}
		
		for (int i = 1; i <= count; i++) {
			BulletPanel liElement = new BulletPanel();
			if (i == 1) {
				liElement.getElement().addClassName("active");
			}
			liElement.getElement().setAttribute("data-slide-to",
					Integer.toString(i));
			liElement.addStyleName("hand");
			liElement.getElement().setAttribute("data-target",
					"#quote-carousel");
			olCarousels.add(liElement);
		}
	}


	private void clear() {
		innerCarousel.clear();
	}

	public void showCreate(int liWidth) {
		clear();
		spnHeader.setInnerHTML("Creating New Request");
		CarouselItem carousel1 = new CarouselItem(true,
				ImageResources.IMAGES.adddoc(),
				"Click on the Add Document Button. Choose the Type of document from the List and Click.");
		CarouselItem carousel2 = new CarouselItem(false,
				ImageResources.IMAGES.leaveapp(),
				"Fill In the Form, Submit it For Approval");

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
				"Check the right side of a request in progress to see how far it has gone through the process.");

		CarouselItem carousel2 = new CarouselItem(false,
				ImageResources.IMAGES.show_users(),
				"Put the mouse pointer on top of each node to see the users involved in each process.");

		innerCarousel.add(carousel1);
		innerCarousel.add(carousel2);
		showIndicators(2);
	}

	public void showTask() {
		clear();
		spnHeader.setInnerHTML("Managing Tasks");
		CarouselItem carousel1 = new CarouselItem(true,
				ImageResources.IMAGES.tasks(),
				"The middle section displays a listing of all your tasks");

		innerCarousel.add(carousel1);
		showIndicators(1);
	}

	public void showReview() {
		clear();
		spnHeader.setInnerHTML("Reviewing Tasks");
		CarouselItem carousel1 = new CarouselItem(true,
				ImageResources.IMAGES.document_action(),
				"Decide what to do with from a list of Actions displayed on top of each Task");

		CarouselItem carousel2 = new CarouselItem(false,
				ImageResources.IMAGES.activity(),
				"Comment and seek clarifications from the bottom panel at each document/Task");

		innerCarousel.add(carousel1);
		innerCarousel.add(carousel2);
		showIndicators(2);
	}

	/*
	 * Sets the focus panel to display more to the Left
	 */
	public void setFocus(boolean isLeft) {
		/*if (isLeft) {
			focusContainer.getElement().getStyle().setLeft(-281, Unit.PX);
		} else {
			focusContainer.getElement().getStyle().setLeft(0, Unit.PX);
		}*/
	}
}
