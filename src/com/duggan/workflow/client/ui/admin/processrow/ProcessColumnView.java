package com.duggan.workflow.client.ui.admin.processrow;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ProcessColumnView extends ViewImpl implements
		ProcessColumnPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ProcessColumnView> {
	}

	@Inject
	public ProcessColumnView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
