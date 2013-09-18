package com.duggan.workflow.client.ui.admin.addprocess;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ProcessAttachmentView extends ViewImpl implements
		ProcessAttachmentPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ProcessAttachmentView> {
	}

	@Inject
	public ProcessAttachmentView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}
}
