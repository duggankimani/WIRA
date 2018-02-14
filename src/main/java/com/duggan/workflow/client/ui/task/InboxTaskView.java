package com.duggan.workflow.client.ui.task;

import java.util.HashMap;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.component.Checkbox;
import com.duggan.workflow.client.ui.task.InboxPresenter.IInboxView;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class InboxTaskView extends AbstractTaskView implements IInboxView {

	HTMLPanel searchPanel = new HTMLPanel("");
	Checkbox mine = new Checkbox();
	Checkbox queued = new Checkbox();
	Checkbox all = new Checkbox();
	private PlaceManager placeManager;

	@Inject
	public InboxTaskView(Binder binder, final PlaceManager placeManager) {
		super(binder);
		this.placeManager = placeManager;
		searchPanel.addStyleName("header-filter");
		mine.setText("Mine");
		mine.setTitle("Tasks assigned to me");
		searchPanel.add(mine);

		queued.setText("Queued");
		queued.setTitle("Queued items");
		searchPanel.add(queued);

		all.setText("All");
		all.setTitle("All tasks including tasks claimed by others");
		searchPanel.add(all);

		mine.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				clearAllSelections();
				mine.setValue(event.getValue());
				revealInbox(event.getValue() ? "mine" : null);
			}
		});

		queued.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				clearAllSelections();
				queued.setValue(event.getValue());
				revealInbox(event.getValue() ? "queued" : null);
			}

		});

		all.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				clearAllSelections();
				all.setValue(event.getValue());
				revealInbox(event.getValue() ? "all" : null);
			}
		});
	}

	@Override
	public void setTaskType(TaskType currentTaskType) {
		clearAllSelections();
		super.setTaskType(currentTaskType);
	}

	private void clearAllSelections() {
		mine.setValue(false);
		queued.setValue(false);
		all.setValue(false);
	}

	protected void revealInbox(String filter) {
		if (filter == null) {
			placeManager.revealPlace(new PlaceRequest.Builder().nameToken(
					NameTokens.inbox).build());
		} else {
			placeManager.revealPlace(new PlaceRequest.Builder()
					.nameToken(NameTokens.inboxwithparams)
					.with("filter", filter).build());
		}

	}

	private void createFilterRow(FlexTable table) {
		int i = table.getRowCount();
		int j = 0;

		table.setWidget(i, j++, searchPanel);
		// table.setWidget(0, j++, new HTMLPanel("<strong>Case No</strong>"));
		// table.setWidget(0, j++, new HTMLPanel("<strong>Process</strong>"));
		// table.setWidget(0, j++, new HTMLPanel("<strong>Task</strong>"));
		// table.setWidget(0, j++, new
		// HTMLPanel("<strong>Current User</strong>"));
		// table.setWidget(0, j++, new
		// HTMLPanel("<strong>Last Modify</strong>"));
		// table.setWidget(0, j++, new HTMLPanel("<strong>Due Date</strong>"));
		// table.setWidget(0, j++, new HTMLPanel("<strong>Status</strong>"));
		table.getFlexCellFormatter().setColSpan(i, 0, 11);
	}

	@Override
	protected void createHeader(FlexTable table) {
		//createFilterRow(table);
		super.createHeader(table);
	}

	@Override
	public void bindAlerts(HashMap<TaskType, Integer> alerts) {
		super.bindAlerts(alerts);

//		for (TaskType type : alerts.keySet()) {
//			Integer count = alerts.get(type);
//			String title = type.getTitle()+ "("+(count==null? 0: count)+")";
//			switch (type) {
//			case MINE:
//				mine.setText(title);
//				break;
//			case QUEUED:
//				queued.setText(title);
//				break;
//			case ALL:
//				all.setText(title);
//				break;
//			}
//		}
	}
}
