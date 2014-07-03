package com.duggan.workflow.client.ui.task;

import com.duggan.workflow.client.ui.task.SuspendedTaskPresenter.ISuspendedView;
import com.google.inject.Inject;

public class SuspendedTaskView  extends AbstractTaskView implements ISuspendedView{

	@Inject
	public SuspendedTaskView(Binder binder) {
		super(binder);
	}
}