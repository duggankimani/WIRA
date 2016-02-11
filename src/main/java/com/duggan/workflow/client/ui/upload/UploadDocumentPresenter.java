package com.duggan.workflow.client.ui.upload;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.shared.events.CloseAttatchmentEvent;
import com.duggan.workflow.shared.events.ReloadAttachmentsEvent;
import com.duggan.workflow.shared.events.UploadEndedEvent;
import com.duggan.workflow.shared.events.UploadStartedEvent;
import com.duggan.workflow.shared.events.CloseAttatchmentEvent.CloseAttatchmentHandler;
import com.duggan.workflow.shared.events.UploadEndedEvent.UploadEndedHandler;
import com.duggan.workflow.shared.events.UploadStartedEvent.UploadStartedHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class UploadDocumentPresenter extends
		PresenterWidget<UploadDocumentPresenter.MyView> implements CloseAttatchmentHandler,
		UploadStartedHandler, UploadEndedHandler{

	public interface MyView extends PopupView {
		Uploader getUploader();
		HasClickHandlers getDoneButton();
		void showCompletedButton(boolean show);
	}

	UploadContext ctx = null;
	
	@Inject
	public UploadDocumentPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(CloseAttatchmentEvent.TYPE, this);
		addRegisteredHandler(UploadStartedEvent.TYPE, this);
		addRegisteredHandler(UploadEndedEvent.TYPE, this);
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

	@Override
	public void onCloseAttatchment(CloseAttatchmentEvent event) {
		getView().hide();
	}

	@Override
	public void onUploadEnded(UploadEndedEvent event) {
		getView().showCompletedButton(true);
	}

	@Override
	public void onUploadStarted(UploadStartedEvent event) {
		getView().showCompletedButton(false);
	}
}
