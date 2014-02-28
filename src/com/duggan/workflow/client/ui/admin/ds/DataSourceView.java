package com.duggan.workflow.client.ui.admin.ds;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class DataSourceView extends ViewImpl implements DataSourcePresenter.IDataSourceView {

	private final Widget widget;
	@UiField Anchor aNewProcess;
	@UiField HTMLPanel tblRow;
	@UiField Anchor aStartProcesses;
	
	public interface Binder extends UiBinder<Widget, DataSourceView> {
	}

	@Inject
	public DataSourceView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public HasClickHandlers getNewDatasourceButton() {
		return aNewProcess;
	}
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		if(slot == DataSourcePresenter.TABLE_SLOT){
			tblRow.clear();
			if(content!=null){
				tblRow.add(content);
			}
		}else{
			super.setInSlot(slot, content);
		}
	}
	
	@Override
	public void addToSlot(Object slot, Widget content) {
		if(slot == DataSourcePresenter.TABLE_SLOT){
			if(content!=null){
				tblRow.add(content);
			}
		}else{
			super.setInSlot(slot, content);
		}
	}
	
	@Override
	public HasClickHandlers getTestAllDatasources(){
		return aStartProcesses;
	}
	
}
