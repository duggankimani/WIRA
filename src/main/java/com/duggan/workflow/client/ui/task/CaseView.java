package com.duggan.workflow.client.ui.task;

import com.duggan.workflow.client.ui.task.CaseViewPresenter.ICaseView;
import com.google.gwt.dom.client.Style.Unit;
import com.google.inject.Inject;

public class CaseView extends AbstractTaskView implements ICaseView{

	@Inject
	public CaseView(Binder binder) {
		super(binder);
		
//		container.getElementById("")
//		divDocView.removeClassName("span10");
//		divDocView.addClassName("full-page");
//		divDocView.getStyle().setWidth(99.9, Unit.PCT);
	}
	
	protected void displayTable(boolean isDisplayTable) {
		if (isDisplayTable) {
			divDocView.addClassName("hide");
			divTableListing.removeStyleName("hide");
		} else {
			divTableListing.addStyleName("hide");
			divDocView.removeClassName("span10");
			divDocView.removeClassName("hide");
			divDocView.addClassName("full-page");
			divDocView.getStyle().setWidth(99.9, Unit.PCT);
		}
	}
}
