package com.duggan.workflow.client.ui.error;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ErrorPageView extends ViewImpl implements
		ErrorPagePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ErrorPageView> {
	}

	@Inject
	public ErrorPageView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
