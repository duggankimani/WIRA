package com.duggan.workflow.client.ui.activityfeed;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.activityfeed.components.CarouselPopup;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ActivitiesView extends ViewImpl implements
		ActivitiesPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ActivitiesView> {
	}

	@UiField ComplexPanel panelActivity;
	@UiField Anchor aCreate;
	@UiField Anchor aFollowUp;
	@UiField Anchor aReceive;
	@UiField Anchor aReview;
	@UiField Anchor aClose;
	@UiField DivElement divTutorial;
	@UiField LIElement liCreate;
	@UiField LIElement liFollowUp;
	@UiField LIElement liReceive;
	@UiField LIElement liReview;
	protected boolean hasFocused=true;
	
	@Inject
	public ActivitiesView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		final CarouselPopup popUp1 = new CarouselPopup();
		final int[] position = new int[2];
		position[0]=40;
		
		aCreate.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				position[1]=liCreate.getAbsoluteRight();
				popUp1.showCreate();
				AppManager.showCarouselPanel(popUp1,position,false);
				System.out.println("Li Create:"+ position[1]);
			}
		});
		
		aFollowUp.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				position[1]= liFollowUp.getAbsoluteRight();
				AppManager.showCarouselPanel(popUp1,position,false);
				popUp1.showFollowUp();
				System.out.println("Li FollowUp:"+ position[1]);
			}
		});
		
		aReceive.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				position[1]=liReceive.getAbsoluteRight()-825;
				AppManager.showCarouselPanel(popUp1,position,true);
				popUp1.showTask();
				System.out.println("Li Receive:"+ position[1]);
			}
		});
		
		aReview.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				position[1]=liReview.getAbsoluteRight()-825;
				AppManager.showCarouselPanel(popUp1,position,true);
				popUp1.showReview();
				System.out.println("Li Review:"+ position[1]);
			}
		});
		
		popUp1.getPanelContainer().addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				hasFocused=true;
			}
		});
		
		aCreate.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if(!hasFocused){
					AppManager.hideCarousel();
				}
			}
		});
		aClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				divTutorial.addClassName("hidden");
			}
		});
		//TODO: Remove this afterwards
		divTutorial.addClassName("hidden");
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public HasWidgets getPanelActivity(){
		return panelActivity;
	}
}
