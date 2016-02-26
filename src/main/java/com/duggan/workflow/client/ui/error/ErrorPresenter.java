package com.duggan.workflow.client.ui.error;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class ErrorPresenter extends PresenterWidget<ErrorPresenter.MyView> {

	public interface MyView extends PopupView {

		void setMessage(String message, Long id);
		
	}

	@Inject
	public ErrorPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	public void setMessage(String message, Long id) {
		getView().setMessage(message, id);
	}
}
