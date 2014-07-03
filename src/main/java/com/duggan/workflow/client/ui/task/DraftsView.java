package com.duggan.workflow.client.ui.task;

import com.duggan.workflow.client.ui.task.DraftsPresenter.IDraftsView;
import com.google.inject.Inject;

public class DraftsView extends AbstractTaskView implements IDraftsView{

	@Inject
	public DraftsView(Binder binder) {
		super(binder);
	}
}
