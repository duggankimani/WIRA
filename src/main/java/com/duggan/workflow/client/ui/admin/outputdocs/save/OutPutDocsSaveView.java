package com.duggan.workflow.client.ui.admin.outputdocs.save;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewImpl;

public class OutPutDocsSaveView extends PopupViewImpl implements
		OutPutDocsSavePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, OutPutDocsSaveView> {
	}

	@Inject
	public OutPutDocsSaveView(final EventBus eventBus,final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
