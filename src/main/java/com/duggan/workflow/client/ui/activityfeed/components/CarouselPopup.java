package com.duggan.workflow.client.ui.activityfeed.components;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.component.BulletPanel;
import com.duggan.workflow.client.ui.component.OLPanel;
import com.duggan.workflow.client.ui.events.CloseCarouselEvent;
import com.duggan.workflow.client.ui.events.CloseCarouselEvent.CloseCarouselHandler;
import com.duggan.workflow.client.ui.images.ImageResources;
import com.duggan.workflow.client.util.AppContext;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class CarouselPopup extends Composite implements CloseCarouselHandler {

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

	@UiField FocusPanel parentPanel;
	
	private static CarouselPopupUiBinder uiBinder = GWT
			.create(CarouselPopupUiBinder.class);

	interface CarouselPopupUiBinder extends UiBinder<Widget, CarouselPopup> {
	}
	

	HandlerRegistration reg=null;
	public CarouselPopup() {
		initWidget(uiBinder.createAndBindUi(this));
		focusContainer.getElement().setAttribute("id", "CarouselFocus");
		
		parentPanel.addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if(timer!=null){
					timer.cancel();
				}
			}
		});
		
		parentPanel.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				//Window.alert("outside");
				AppManager.hidePopup();
			}
		});
				
		aClose.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				AppManager.hidePopup();
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
				"Click on the 'Add Document' Button. Choose the Type of document from the ArrayList and Click.");
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
				"Use the Process HashMap to view the progress of your requests.");

		CarouselItem carousel2 = new CarouselItem(false,
				ImageResources.IMAGES.tasks(),
				"Each task has the name of the person working on the task.");

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
		
		CarouselItem carousel2 = new CarouselItem(false,
				ImageResources.IMAGES.advancedTaskSearch(),
				"Filter your requests and tasks");

		CarouselItem carousel3 = new CarouselItem(false,
				ImageResources.IMAGES.taskSearch(),
				"Use Advanced search to further filter your requests and tasks");


		innerCarousel.add(carousel1);
		innerCarousel.add(carousel2);
		innerCarousel.add(carousel3);
		showIndicators(3);
	}

	public void showReview() {
		clear();
		spnHeader.setInnerHTML("Reviewing Tasks");
		CarouselItem carousel1 = new CarouselItem(true,
				ImageResources.IMAGES.document_action(),
				"Easily make your decision using a ArrayList of Actions displayed on top of each Task");

		CarouselItem carousel2 = new CarouselItem(false,
				ImageResources.IMAGES.activity(),
				"Comment and seek clarifications for your tasks");

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

	//give the user 400ms to have focused on the carousel
	Timer timer = null;
	
	@Override
	public void onCloseCarousel(CloseCarouselEvent event) {
		timer = new Timer() {
			
			@Override
			public void run() {
				AppManager.hidePopup();
			}
		};
		timer.schedule(600);
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		Type<? extends EventHandler> type = CloseCarouselEvent.TYPE;
		reg= AppContext.getEventBus().addHandler((GwtEvent.Type<EventHandler>)type,this);

	}
	
	@Override
	protected void onUnload() {
		super.onUnload();
		reg.removeHandler();
	}
}
