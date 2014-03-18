package com.duggan.workflow.client.ui.admin.dashboard;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;

public class DashboardPresenter extends
		PresenterWidget<DashboardPresenter.MyView> {

	public interface MyView extends View {
		public void createPieChart();
	}

	@Inject
	public DashboardPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		getView().createPieChart();
	}
}
