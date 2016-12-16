package com.duggan.workflow.client.ui.task;

import com.duggan.workflow.client.ui.task.DraftsPresenter.IDraftsView;
import com.duggan.workflow.shared.model.Column;
import com.google.inject.Inject;

public class DraftsView extends AbstractTaskView implements IDraftsView{

	@Inject
	public DraftsView(Binder binder) {
		super(binder);
		defaultSchema.getColumns().clear(); //Remove cols added by super class
		defaultSchema.addColumn(new Column(DefaultFields.CaseNo.name(),
				"Case No", "Case", "80px"));
		defaultSchema.addColumn(new Column(DefaultFields.Process.name(),
				"Process", "Process","100px"));
		defaultSchema.addColumn(new Column(DefaultFields.Task.name(), "Task",
				"Task","100px"));
		defaultSchema.addColumn(new Column(DefaultFields.Due.name(), "Due",
				"Due", "80px"));
		defaultSchema.addColumn(new Column(DefaultFields.Modified.name(),
				"Modified", "Modified", "80px"));
		defaultSchema.addColumn(new Column(DefaultFields.Status.name(),
				"Status", "Status", "60px"));
		defaultSchema.addColumn(new Column(DefaultFields.Priority.name(),
				"Status", "Status", "60px"));
		defaultSchema.addColumn(new Column(DefaultFields.Notes.name(), "Notes",
				"Notes", "40px"));
		
	}
}
