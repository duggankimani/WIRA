package com.duggan.workflow.client.ui.upload.href;

import com.duggan.workflow.client.ui.AppManager;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupViewImpl;

public class IFrameDataView extends PopupViewImpl implements
		IFrameDataPresenter.IFrameView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, IFrameDataView> {
	}

	@UiField Button btnDone;
	@UiField Button btnCancel;
	@UiField DialogBox uploaderDialog;
		
	@Inject
	public IFrameDataView(final EventBus eventBus, final Binder binder) {
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
				hide();
			}
		});
		
		int[] positions=AppManager.calculatePosition(2, 62);
		uploaderDialog.setPopupPosition(positions[1],positions[0]);
		
	}
	
	public HasClickHandlers getDoneButton(){
		return btnDone;
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}
}
