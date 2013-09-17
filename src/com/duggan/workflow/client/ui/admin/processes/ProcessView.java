package com.duggan.workflow.client.ui.admin.processes;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ProcessView extends ViewImpl implements ProcessPresenter.MyView {

	private final Widget widget;
	@UiField Anchor aNewProcess;
	@UiField HTMLPanel tblRow;

	public interface Binder extends UiBinder<Widget, ProcessView> {
	}

	@Inject
	public ProcessView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void addToSlot(Object slot, Widget content) {
		if(slot == ProcessPresenter.TABLE_SLOT){
			if(content!=null){
				tblRow.add(content);
			}
		}else{
		super.setInSlot(slot, content);
		}
	}

	public HasClickHandlers getaNewProcess() {
		return aNewProcess;
	}
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		if(slot == ProcessPresenter.TABLE_SLOT){
			tblRow.clear();
			if(content!=null){
				tblRow.add(content);
			}
		}else{
		super.setInSlot(slot, content);
		}
	}
	
}
