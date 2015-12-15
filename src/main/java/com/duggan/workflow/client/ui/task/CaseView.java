package com.duggan.workflow.client.ui.task;

import com.duggan.workflow.client.ui.task.CaseViewPresenter.ICaseView;
import com.google.gwt.dom.client.Style.Unit;
import com.google.inject.Inject;

public class CaseView extends AbstractTaskView implements ICaseView{

	@Inject
	public CaseView(Binder binder) {
		super(binder);
		
		divDocView.removeClassName("span10");
		divDocView.addClassName("full-page");
		divDocView.getStyle().setWidth(99.9, Unit.PCT);
		divTasks.replaceClassName("span2","hide");
	}
}