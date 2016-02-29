package com.duggan.workflow.client.ui.docActivity;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class DocumentActivityPresenter extends
		PresenterWidget<DocumentActivityPresenter.MyView> {

	public interface MyView extends View {
	}

	@Inject
	public DocumentActivityPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
