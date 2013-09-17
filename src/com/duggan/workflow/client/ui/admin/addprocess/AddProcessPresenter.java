package com.duggan.workflow.client.ui.admin.addprocess;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.PopupView;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;

public class AddProcessPresenter extends PresenterWidget<AddProcessPresenter.MyView> {

	public interface MyView extends PopupView {
	}

	@Inject
	public AddProcessPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
