package com.duggan.workflow.client.ui.header;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class HeaderView extends ViewImpl implements HeaderPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, HeaderView> {
	}
	

	@UiField Anchor aLogout;
	
	@Inject
	public HeaderView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public HasClickHandlers getLogout(){
		return aLogout;
	}
}
