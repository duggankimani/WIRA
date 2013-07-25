package com.duggan.workflow.client.ui.notifications;

import com.gwtplatform.mvp.client.PopupViewImpl;
import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

public class NotificationsView extends ViewImpl implements
		NotificationsPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, NotificationsView> {
	}

	@Inject
	public NotificationsView( final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
