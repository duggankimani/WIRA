package com.duggan.workflow.client.ui.header;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.util.AppContext;
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

public class HeaderPresenter extends PresenterWidget<HeaderPresenter.MyView> {

	public interface MyView extends View {
		HasClickHandlers getLogout();
	}

	@Inject
	public HeaderPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Inject DispatchAsync dispatcher;
	
	@Inject PlaceManager placeManager;
	
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
