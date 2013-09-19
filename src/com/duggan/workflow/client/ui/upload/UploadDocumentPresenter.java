package com.duggan.workflow.client.ui.upload;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.ui.events.ReloadAttachmentsEvent;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class UploadDocumentPresenter extends
		PresenterWidget<UploadDocumentPresenter.MyView> {

	public interface MyView extends PopupView {
		Uploader getUploader();
		HasClickHandlers getDoneButton();
	}

	UploadContext ctx = null;
	
	@Inject
	public UploadDocumentPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().getDoneButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ReloadAttachmentsEvent());
			}
		});
	}

	public void setContext(UploadContext context) {
		this.ctx = context;
		getView().getUploader().setContext(context);
	}
}
