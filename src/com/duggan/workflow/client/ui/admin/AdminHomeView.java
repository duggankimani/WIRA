package com.duggan.workflow.client.ui.admin;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class AdminHomeView extends ViewImpl implements
		AdminHomePresenter.MyView {

	private final Widget widget;
	
	@UiField Anchor aNewProcess;
	@UiField HTMLPanel tblRow;
	
	public interface Binder extends UiBinder<Widget, AdminHomeView> {
	}

	@Inject
	public AdminHomeView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}
	
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		if(slot == AdminHomePresenter.TABLE_SLOT){
			tblRow.clear();
			addHeaders();
			if(content!=null){
				tblRow.add(content);
			}
		}else{
		super.setInSlot(slot, content);
		}
	}
	
	private void addHeaders() {
		
	}


	@Override
	public void addToSlot(Object slot, Widget content) {
		if(slot == AdminHomePresenter.TABLE_SLOT){
			if(content!=null){
				tblRow.add(content);
			}
		}else{
		super.setInSlot(slot, content);
		}
	}

	public Anchor getaNewProcess() {
		return aNewProcess;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
