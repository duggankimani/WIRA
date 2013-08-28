package com.duggan.workflow.client.ui.header;

import java.util.HashMap;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.AfterSaveEvent.AfterSaveHandler;
import com.duggan.workflow.client.ui.events.AfterSaveEvent;
import com.duggan.workflow.client.ui.events.AlertLoadEvent;
import com.duggan.workflow.client.ui.events.BeforeNotificationsLoadEvent;
import com.duggan.workflow.client.ui.events.NotificationsLoadEvent;
import com.duggan.workflow.client.ui.notifications.NotificationsPresenter;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.requests.GetAlertCount;
import com.duggan.workflow.shared.requests.GetAlertCountResult;
import com.duggan.workflow.shared.requests.GetNotificationsAction;
import com.duggan.workflow.shared.requests.LogoutAction;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.GetNotificationsActionResult;
import com.duggan.workflow.shared.responses.LogoutActionResult;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class HeaderPresenter extends PresenterWidget<HeaderPresenter.MyView> implements AfterSaveHandler{

	public interface MyView extends View {
		HasClickHandlers getLogout();
		void setValues(String userNames, String userGroups);
		Anchor getNotificationsButton();
		void setPopupVisible();
		void removePopup();
		void setCount(Integer count);
		HasBlurHandlers getpopupContainer();
		void setLoading(boolean b);
	}

	@Inject DispatchAsync dispatcher;
	
	@Inject PlaceManager placeManager;
	
	@Inject NotificationsPresenter notifications;
	boolean onFocus =true;
	
	
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
				onFocus =true;
			}
		});
		
		getView().getNotificationsButton().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				//getView().removePopup();
			}
		});	
		
	}

	@Override
	protected void onReset() {		
		super.onReset();
		loadAlertCount();
		getView().setValues(AppContext.getUserNames(), AppContext.getUserGroups());
	}
	
	protected void loadAlertCount() {
		alertTimer.cancel();
		getView().setLoading(true);
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetAlertCount());
		action.addRequest(new GetNotificationsAction(AppContext.getUserId()));
		
		dispatcher.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult results) {
				
				
				GetAlertCountResult result = (GetAlertCountResult)results.get(0);				
				HashMap<TaskType,Integer> alerts = result.getCounts();				
				getView().setCount(alerts.get(TaskType.NOTIFICATIONS));				
				fireEvent(new AlertLoadEvent(alerts));				
				
				GetNotificationsActionResult notificationsResult = (GetNotificationsActionResult)results.get(1);
				assert notificationsResult!=null;
				fireEvent(new NotificationsLoadEvent(notificationsResult.getNotifications()));
				getView().setLoading(false);
				
				alertTimer.schedule(alertReloadInterval);
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
