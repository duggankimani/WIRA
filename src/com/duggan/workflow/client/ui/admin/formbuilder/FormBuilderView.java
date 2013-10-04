package com.duggan.workflow.client.ui.admin.formbuilder;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class FormBuilderView extends ViewImpl implements
		FormBuilderPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, FormBuilderView> {
	}

	@Inject
	public FormBuilderView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
