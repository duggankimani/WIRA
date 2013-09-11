package com.duggan.workflow.client.ui.admin.process;

import com.gwtplatform.mvp.client.PopupViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

public class ProcessView extends PopupViewImpl implements
		ProcessPresenter.MyView {

	private final Widget widget;
	@UiField Anchor aNext;
	@UiField Anchor aClose;
	@UiField Anchor aBack;
	@UiField Anchor aFinish;
	@UiField HTMLPanel divProcessDetails;
	@UiField HTMLPanel divUploadDetails;

	public interface Binder extends UiBinder<Widget, ProcessView> {
	}

	@Inject
	public ProcessView(final EventBus eventBus, final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
		
		aNext.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				divProcessDetails.addStyleName("hide");
				divUploadDetails.removeStyleName("hide");
			}
		});
		
		aClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		
		aBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				divProcessDetails.removeStyleName("hide");
				divUploadDetails.addStyleName("hide");
			}
		});
		
		aFinish.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
