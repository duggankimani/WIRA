package com.duggan.workflow.client.ui.task;

import com.duggan.workflow.client.ui.task.InboxPresenter.IInboxView;
import com.google.inject.Inject;

public class InboxTaskView extends AbstractTaskView implements IInboxView{

	@Inject
	public InboxTaskView(Binder binder) {
		super(binder);
	}
}
