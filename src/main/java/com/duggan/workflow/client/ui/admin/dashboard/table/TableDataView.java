package com.duggan.workflow.client.ui.admin.dashboard.table;

import java.util.Arrays;
import java.util.List;

import com.duggan.workflow.client.ui.component.TableView;
import com.duggan.workflow.shared.model.dashboard.LongTask;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class TableDataView extends ViewImpl implements
		TableDataPresenter.ITableDataView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, TableDataView> {
	}
	
	@UiField TableView tableView;

	boolean initd=false;
	@Inject
	public TableDataView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		
		if(!initd){
			initd=true;
			tableView.setHeaders(Arrays.asList("PROCESS ITEM","DOCUMENT TYPE","TASK COUNT","PROCESS TIME"));
		}
		return widget;
	}

	@Override
	public void setTasks(List<LongTask> longTasks) {
		tableView.clearRows();
		if(longTasks==null)
			return;
		
		for(LongTask task: longTasks){
			InlineLabel avgTime = new InlineLabel(task.getAverageTime()+" days");
			if(task.getAverageTime()<7){
				avgTime.addStyleName("label label-info arrowed-in");
			}else if(task.getAverageTime()<14){
				avgTime.addStyleName("label label-warning arrowed-in");
			}else{
				avgTime.addStyleName("label label-important arrowed-in");
			}
			tableView.addRow(new InlineLabel(task.getTaskName()),  new InlineLabel(task.getDocumentType()), 
					new InlineLabel(task.getNoOfTasks()+" tasks"), 
					avgTime);
		}
	}
}
