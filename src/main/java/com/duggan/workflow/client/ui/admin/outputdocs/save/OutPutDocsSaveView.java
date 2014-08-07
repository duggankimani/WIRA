package com.duggan.workflow.client.ui.admin.outputdocs.save;

import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.shared.model.OutputDocument;
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

	Uploader uploader;
	
	@Inject
	public OutPutDocsSaveView(final EventBus eventBus,final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
	}
	
	public OutputDocument getOutputDoc(){
		OutputDocument document = new OutputDocument();
		document.setCode(code);
		document.setDescription(description);
		document.setName(name);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
