package com.duggan.workflow.client.ui.upload;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.PopupView;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;

public class UploadDocumentPresenter extends
		PresenterWidget<UploadDocumentPresenter.MyView> {

	public interface MyView extends PopupView {
		Uploader getUploader();
	}

	@Inject
	public UploadDocumentPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	public void setContext(UploadContext context) {
		getView().getUploader().setContext(context);
	}
}
