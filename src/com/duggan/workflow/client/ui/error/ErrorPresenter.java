package com.duggan.workflow.client.ui.error;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.PopupView;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;

public class ErrorPresenter extends PresenterWidget<ErrorPresenter.MyView> {

	public interface MyView extends PopupView {

		void setMessage(String message);
		
	}

	@Inject
	public ErrorPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	public void setMessage(String message) {

		getView().setMessage(message);
	}
}
