package com.duggan.workflow.client.ui.task;

import com.duggan.workflow.client.ui.task.UnAssignedPresenter.IUnAssignedView;
import com.google.inject.Inject;

public class UnAssignedView extends AbstractTaskView implements 
IUnAssignedView{

	@Inject
	public UnAssignedView(Binder binder) {
		super(binder);
	}
}

