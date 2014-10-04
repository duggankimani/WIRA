package com.duggan.workflow.client.ui.admin.outputdocs.save;

import com.duggan.workflow.shared.model.OutputDocument;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class OutPutDocsSavePresenter extends
		PresenterWidget<OutPutDocsSavePresenter.MyView> {

	public interface MyView extends PopupView{
	}

	@Inject
	public OutPutDocsSavePresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	public OutputDocument getOutputDocument() {

		return null;
	}
}
