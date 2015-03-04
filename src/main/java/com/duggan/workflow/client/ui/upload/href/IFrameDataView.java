package com.duggan.workflow.client.ui.upload.href;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.shared.model.settings.REPORTVIEWIMPL;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewImpl;

public class IFrameDataView extends PopupViewImpl implements
		IFrameDataPresenter.IFrameView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, IFrameDataView> {
	}

//	@UiField Button btnDone;
//	@UiField Button btnCancel;
	@UiField PopupPanel uploaderDialog;
	@UiField HTMLPanel panelBody;
	@UiField IFrameElement iframe;
	@UiField Anchor aClose;
	@UiField SpanElement spnHeader;
		
	@Inject
	public IFrameDataView(final EventBus eventBus, final Binder binder) {
		super(eventBus);
		
		widget = binder.createAndBindUi(this);
		
		aClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		
		int client = Window.getClientHeight();
		//uploaderDialog.
		panelBody.setHeight((0.9*client - 30)+"px");
		
//		int[] positions=AppManager.calculatePosition(2, 62);
//		uploaderDialog.setPopupPosition(positions[1],positions[0]);
		
	}
	
	
	public HasClickHandlers getDoneButton(){
		return aClose;
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setInfo(String uri, String title) {
		
		iframe.setSrc(uri);
		spnHeader.setInnerText(title);
		//uploaderDialog.setText(title);
	}
}
