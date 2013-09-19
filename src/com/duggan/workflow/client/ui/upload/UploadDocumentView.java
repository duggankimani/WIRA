package com.duggan.workflow.client.ui.upload;

import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupViewImpl;

public class UploadDocumentView extends PopupViewImpl implements
		UploadDocumentPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, UploadDocumentView> {
	}

	@UiField Uploader uploader;
	@UiField Button btnDone;
	@UiField Button btnCancel;
	@UiField DialogBox uploaderDialog;
	
	@Inject
	public UploadDocumentView(final EventBus eventBus, final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
		
		btnDone.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		
		btnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				uploader.cancel();
				hide();
			}
		});

		uploaderDialog.setPopupPosition(1019,35);
		
	}

	public HasClickHandlers getDoneButton(){
		return btnDone;
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public Uploader getUploader() {
		return uploader;
	}
}
