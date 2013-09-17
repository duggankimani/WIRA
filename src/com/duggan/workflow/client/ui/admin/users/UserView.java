package com.duggan.workflow.client.ui.admin.users;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class UserView extends ViewImpl implements UserPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, UserView> {
	}

	@Inject
	public UserView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
