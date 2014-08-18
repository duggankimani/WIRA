package com.duggan.workflow.client.ui.activityfeed;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.activityfeed.components.CarouselPopup;
import com.duggan.workflow.client.ui.admin.formbuilder.component.HR;
import com.duggan.workflow.client.ui.events.CloseCarouselEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.client.util.Definitions;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HRElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ActivitiesView extends ViewImpl implements
		ActivitiesPresenter.MyView {

	private final Widget widget;

	private Timer timer;
	final int timerSeconds = 600;

	public interface Binder extends UiBinder<Widget, ActivitiesView> {
	}

	@UiField
	ComplexPanel panelActivity;
	@UiField
	Anchor aCreate;
	@UiField
	Anchor aFollowUp;
	@UiField
	Anchor aReceive;
	@UiField
	Anchor aReview;
	@UiField
	Anchor aClose;
	@UiField
	DivElement divTutorial;
	@UiField
	LIElement liCreate;
	@UiField
	LIElement liFollowUp;
	@UiField
	LIElement liReceive;
	@UiField
	LIElement liReview;

	@UiField
	DivElement imgReceive;
	@UiField
	DivElement imgReview;

	@UiField FocusPanel parentPanel;
	protected boolean hasElapsed = false;

	@Inject
	public ActivitiesView(final Binder binder) {
		widget = binder.createAndBindUi(this);

	}
	

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasWidgets getPanelActivity() {
		return panelActivity;
	}


	@Override
	public void bind() {
		final CarouselPopup popUp1 = new CarouselPopup();
		final int[] position = new int[2];
		position[0] = 40;

		aCreate.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				// popUp1.showFocusArea();
				timer = new Timer() {
					@Override
					public void run() {
						position[1] = liCreate.getAbsoluteRight();
						popUp1.showCreate(liCreate.getClientWidth());
						AppManager.showCarouselPanel(popUp1, position, false);
						hasElapsed = true;
						popUp1.setFocus(true);
						// System.out.println("Li Create:"+ position[1]);
					}
				};
				timer.schedule(timerSeconds);
			}
		});

		aFollowUp.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				// popUp1.showFocusArea();
				timer = new Timer() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						position[1] = liFollowUp.getAbsoluteRight();
						AppManager.showCarouselPanel(popUp1, position, false);
						popUp1.showFollowUp();
						hasElapsed = true;
						popUp1.setFocus(true);
						// System.out.println("Li FollowUp:"+ position[1]);
					}
				};
				timer.schedule(timerSeconds);
			}
		});

		aReceive.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				timer = new Timer() {
					@Override
					public void run() {
						int browserWidth = Window.getClientWidth();
						int popovermaxwidth = (int) (0.4 * browserWidth);
						position[1] = imgReceive.getAbsoluteLeft()
									  - popovermaxwidth;
						popUp1.getElement().getStyle().setWidth(popovermaxwidth-15, Unit.PX);

						AppManager.showCarouselPanel(popUp1, position, true);
						popUp1.showTask();
						hasElapsed = true;
						popUp1.setFocus(false);
						// System.out.println("Li Receive:"+ position[1]);
					}
				};
				timer.schedule(timerSeconds);
			}
		});

		aReview.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {

				timer = new Timer() {
					@Override
					public void run() {
						int browserWidth = Window.getClientWidth();
						int popovermaxwidth = (int) (0.4 * browserWidth);
						position[1] = imgReview.getAbsoluteLeft()
									  - popovermaxwidth;
						popUp1.getElement().getStyle().setWidth(popovermaxwidth-20, Unit.PX);

						AppManager.showCarouselPanel(popUp1, position, true);
						popUp1.showReview();
						hasElapsed = true;
						popUp1.setFocus(false);
					}
				};
				timer.schedule(timerSeconds);
			}
		});

		aCreate.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (hasElapsed) {
					timer.cancel();
					AppContext.fireEvent(new CloseCarouselEvent());
				}
			}
		});
	
		
		aFollowUp.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (hasElapsed) {
					timer.cancel();
					AppContext.fireEvent(new CloseCarouselEvent());
				}
			}
		});
		aReceive.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (hasElapsed) {
					timer.cancel();
					AppContext.fireEvent(new CloseCarouselEvent());
				}
			}
		});
		aReview.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (hasElapsed) {
					timer.cancel();
					AppContext.fireEvent(new CloseCarouselEvent());
				}
			}
		});

		aClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				divTutorial.addClassName("hidden");
				AppContext.setSessionValue(Definitions.SHOWWELCOMEWIDGET, "false");
			}
		});
		
		if(!AppContext.isShowWelcomeWiget()){
			divTutorial.addClassName("hidden");
		}

		// TODO: Remove this afterwards
		// divTutorial.addClassName("hidden");
		
	}


	@Override
	public void createGroup(String label) {
		HTMLPanel divLabel = new HTMLPanel("<hr/>");
		divLabel.addStyleName("day_divider");
		
		HTMLPanel lbl = new HTMLPanel(label);
		lbl.addStyleName("day_divider_label");
		divLabel.add(lbl); 
		
		panelActivity.add(divLabel);
	}

}
