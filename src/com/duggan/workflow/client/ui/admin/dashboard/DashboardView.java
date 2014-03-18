package com.duggan.workflow.client.ui.admin.dashboard;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class DashboardView extends ViewImpl implements
		DashboardPresenter.MyView {

	public interface Binder extends UiBinder<Widget, DashboardView> {
	}

	private Widget widget;

	@Inject
	public DashboardView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

}
