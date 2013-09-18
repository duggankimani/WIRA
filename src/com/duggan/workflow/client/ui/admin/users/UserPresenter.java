package com.duggan.workflow.client.ui.admin.users;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class UserPresenter extends PresenterWidget<UserPresenter.MyView> {

	public interface MyView extends View {
	}

	@Inject
	public UserPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}
	

	@Override
	protected void onBind() {
		super.onBind();
	}
}
