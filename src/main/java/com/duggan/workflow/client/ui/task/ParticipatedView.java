package com.duggan.workflow.client.ui.task;

import com.duggan.workflow.client.ui.task.ParticipatedPresenter.IParticipatedView;
import com.google.inject.Inject;

public class ParticipatedView extends AbstractTaskView implements IParticipatedView{

	@Inject
	public ParticipatedView(Binder binder) {
		super(binder);
	}
}

