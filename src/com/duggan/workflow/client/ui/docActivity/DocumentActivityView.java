package com.duggan.workflow.client.ui.docActivity;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class DocumentActivityView extends ViewImpl implements
		DocumentActivityPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, DocumentActivityView> {
	}

	@Inject
	public DocumentActivityView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
