package com.duggan.workflow.client.ui.admin.datatable;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class DataTableView extends ViewImpl implements DataTablePresenter.IDataTableView {

	private final Widget widget;
	@UiField Anchor aNew;
	@UiField HTMLPanel tblRow;
	@UiField Anchor aImport;
	
	public interface Binder extends UiBinder<Widget, DataTableView> {
	}

	@Inject
	public DataTableView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public HasClickHandlers getNewDatasourceButton() {
		return aNew;
	}
	
	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if(slot == DataTablePresenter.TABLE_SLOT){
			//tblRow.clear();
			if(content!=null){
				//tblRow.add(content);
			}
		}else{
			super.setInSlot(slot, content);
		}
	}
	
	@Override
	public void addToSlot(Object slot, IsWidget content) {
		if(slot == DataTablePresenter.TABLE_SLOT){
			if(content!=null){
				//tblRow.add(content);
			}
		}else{
			super.setInSlot(slot, content);
		}
	}
	
	@Override
	public HasClickHandlers getTestAllDatasources(){
		return aImport;
	}
	
}
