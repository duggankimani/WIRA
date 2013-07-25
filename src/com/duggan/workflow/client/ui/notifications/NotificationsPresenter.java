package com.duggan.workflow.client.ui.notifications;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;

public class NotificationsPresenter extends
		PresenterWidget<NotificationsPresenter.MyView> {

	public interface MyView extends View {
		// TODO Put your view methods here
	}

	@Inject
	public NotificationsPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
