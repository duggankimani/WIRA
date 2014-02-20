package com.duggan.workflow.client.ui.upload;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
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
	@UiField PopupPanel uploaderDialog;
	
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
		
		int[] positions=AppManager.calculatePosition(2, 70);
		uploaderDialog.setPopupPosition(positions[1],positions[0]);
		
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

	@Override
	public void showCompletedButton(boolean show) {
		btnDone.setVisible(show);
	}
}
