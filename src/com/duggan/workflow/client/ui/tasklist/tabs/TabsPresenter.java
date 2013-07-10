package com.duggan.workflow.client.ui.tasklist.tabs;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;

public class TabsPresenter extends PresenterWidget<TabsPresenter.MyView> {

	public interface MyView extends View {
	}

	@Inject
	public TabsPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
