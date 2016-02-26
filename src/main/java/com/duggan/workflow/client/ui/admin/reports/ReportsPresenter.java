package com.duggan.workflow.client.ui.admin.reports;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class ReportsPresenter extends PresenterWidget<ReportsPresenter.MyView> {

	public interface MyView extends View {
	}

	@Inject
	public ReportsPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
