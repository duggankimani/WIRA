package com.duggan.workflow.client.ui.header;

import java.util.HashMap;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.AfterSaveEvent.AfterSaveHandler;
import com.duggan.workflow.client.ui.events.AfterSaveEvent;
import com.duggan.workflow.client.ui.events.AlertLoadEvent;
import com.duggan.workflow.client.ui.events.NotificationsLoadEvent;
import com.duggan.workflow.client.ui.notifications.NotificationsPresenter;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.requests.GetAlertCount;
import com.duggan.workflow.shared.requests.GetAlertCountResult;
import com.duggan.workflow.shared.requests.GetNotificationsAction;
import com.duggan.workflow.shared.requests.LogoutAction;
import com.duggan.workflow.shared.responses.GetNotificationsActionResult;
import com.duggan.workflow.shared.responses.LogoutActionResult;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Timer;

public class HeaderPresenter extends PresenterWidget<HeaderPresenter.MyView> implements AfterSaveHandler{

	public interface MyView extends View {
		HasClickHandlers getLogout();

		void setValues(String userNames);
		
		HasClickHandlers getNotificationsButton();

		void setPopupVisible();
		
		void setCount(Integer count);
	}

	@Inject DispatchAsync dispatcher;
	
	@Inject PlaceManager placeManager;
	
	@Inject NotificationsPresenter notifications;
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> NOTIFICATIONS_SLOT = new Type<RevealContentHandler<?>>();

	
	@Inject
	public HeaderPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	
	static int alertReloadInterval = 60 * 1000 * 5; //5 mins
    private Timer alertTimer = new Timer() {

        @Override
        public void run() {
            loadAlertCount();
        }
    };
    
	@Override
	protected void onBind() {
		super.onBind();
		this.addRegisteredHandler(AfterSaveEvent.TYPE, this);
		getView().getLogout().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				logout();
			}
		});
		
		getView().getNotificationsButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getView().setPopupVisible();
				setInSlot(NOTIFICATIONS_SLOT, notifications);	
				loadAlerts();
			}
		});
	}

	@Override
	protected void onReset() {		
		super.onReset();
		loadAlertCount();
		getView().setValues(AppContext.getUserNames());
	}
	
	protected void loadAlertCount() {
		alertTimer.cancel();
		
		dispatcher.execute(new GetAlertCount(), new TaskServiceCallback<GetAlertCountResult>() {
			@Override
			public void processResult(GetAlertCountResult result) {
				HashMap<TaskType,Integer> alerts = result.getCounts();
				
				getView().setCount(alerts.get(TaskType.NOTIFICATIONS));
				
				System.out.println("Alerts - "+alerts.get(TaskType.APPROVALREQUESTDONE));
				fireEvent(new AlertLoadEvent(alerts));				
				alertTimer.schedule(alertReloadInterval);
			}
		});
		
	}

	
	protected void loadAlerts() {
		dispatcher.execute(new GetNotificationsAction(AppContext.getUserId()),
				new TaskServiceCallback<GetNotificationsActionResult>() {
			@Override
			public void processResult(
					GetNotificationsActionResult result) {
				fireEvent(new NotificationsLoadEvent(result.getNotifications()));				
			}
		});
	}


	protected void logout() {
		dispatcher.execute(new LogoutAction(), new TaskServiceCallback<LogoutActionResult>() {
			@Override
			public void processResult(LogoutActionResult result) {
				AppContext.destroy();
				placeManager.revealErrorPlace("login");
			}
		});
	}

	@Override
	public void onAfterSave(AfterSaveEvent event) {
		loadAlertCount();
	}
	
	
}
