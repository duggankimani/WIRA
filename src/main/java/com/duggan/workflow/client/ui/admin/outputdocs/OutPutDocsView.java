package com.duggan.workflow.client.ui.admin.outputdocs;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class OutPutDocsView extends ViewImpl implements
		OutPutDocsPresenter.MyView {

	private final Widget widget;
	@UiField Anchor aNewDocument;

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
	
	public HasClickHandlers getDocumentButton() {
		return aNewDocument;
	}
}
