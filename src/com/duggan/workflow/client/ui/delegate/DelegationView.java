package com.duggan.workflow.client.ui.delegate;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class DelegationView extends ViewImpl implements
		DelegationPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, DelegationView> {
	}

	@Inject
	public DelegationView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
