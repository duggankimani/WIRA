package xtension.workitems;

import java.util.HashMap;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class UpdateActivityStatus implements WorkItemHandler {

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		manager.completeWorkItem(workItem.getId(), new HashMap<String, Object>());
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		
	}
}
