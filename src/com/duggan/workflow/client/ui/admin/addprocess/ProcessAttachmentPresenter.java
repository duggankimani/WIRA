package com.duggan.workflow.client.ui.admin.addprocess;

import com.duggan.workflow.shared.model.ProcessDef;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class ProcessAttachmentPresenter extends PresenterWidget<ProcessAttachmentPresenter.MyView>{ 

	public interface MyView extends View {
	}

	@Inject DispatchAsync requestHelper; 
	
	ProcessDef process = null;
	
	@Inject
	public ProcessAttachmentPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
