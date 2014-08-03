package com.duggan.workflow.client.ui.admin.outputdocs;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class OutPutDocsView extends ViewImpl implements
		OutPutDocsPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, OutPutDocsView> {
	}

	@Inject
	public OutPutDocsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
