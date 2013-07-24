package com.duggan.workflow.client.ui.error;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class NotfoundView extends ViewImpl implements NotfoundPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, NotfoundView> {
	}

	@Inject
	public NotfoundView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
