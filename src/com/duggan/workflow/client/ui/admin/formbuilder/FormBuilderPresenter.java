package com.duggan.workflow.client.ui.admin.formbuilder;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;

public class FormBuilderPresenter extends
		PresenterWidget<FormBuilderPresenter.MyView> {

	public interface MyView extends View {
	}

	@Inject
	public FormBuilderPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
