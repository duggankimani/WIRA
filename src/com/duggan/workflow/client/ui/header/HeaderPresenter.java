package com.duggan.workflow.client.ui.header;

import java.util.HashMap;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.AlertLoadEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.requests.GetAlertCount;
import com.duggan.workflow.shared.requests.GetAlertCountResult;
import com.duggan.workflow.shared.requests.LogoutAction;
import com.duggan.workflow.shared.responses.LogoutActionResult;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;

public class HeaderPresenter extends PresenterWidget<HeaderPresenter.MyView> {

	public interface MyView extends View {
		HasClickHandlers getLogout();

		void setValues(String userNames);
	}

	@Inject DispatchAsync dispatcher;
	
	@Inject PlaceManager placeManager;
	
	@Inject
	public HeaderPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	
	static int alertReloadInterval = 60 * 1000 * 5; //5 mins
    private Timer alertTimer = new Timer() {

        @Override
        public void run() {
            loadAlerts();
        }
    };
    
	@Override
	protected void onBind() {
		super.onBind();
		getView().getLogout().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				logout();
			}
		});
		
	}
	
	@Override
	protected void onReset() {		
		super.onReset();
		loadAlerts();
		getView().setValues(AppContext.getUserNames());
	}
	
	protected void loadAlerts() {
		alertTimer.cancel();
		
		dispatcher.execute(new GetAlertCount(), new TaskServiceCallback<GetAlertCountResult>() {
			@Override
			public void processResult(GetAlertCountResult result) {
				HashMap<TaskType,Integer> alerts = result.getCounts();
				fireEvent(new AlertLoadEvent(alerts));
				
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
	
	
}
