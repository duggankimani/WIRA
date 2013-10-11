package com.duggan.workflow.client.ui.admin.formbuilder.propertypanel;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.PopupView;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;

public class PropertyPanelPresenter extends
		PresenterWidget<PropertyPanelPresenter.MyView> {

	public interface MyView extends PopupView {
		// TODO Put your view methods here
	}

	@Inject
	public PropertyPanelPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
