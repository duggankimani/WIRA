package com.duggan.workflow.client.ui.admin.settings;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class SettingsView extends ViewImpl implements SettingsPresenter.MyView {

	private final Widget widget;
	

	public interface Binder extends UiBinder<Widget, SettingsView> {
	}

	@Inject
	public SettingsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		//imgUser.setUrl("img/blueman.png");
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
