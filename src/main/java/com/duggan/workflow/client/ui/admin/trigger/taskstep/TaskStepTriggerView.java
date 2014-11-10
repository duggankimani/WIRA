package com.duggan.workflow.client.ui.admin.trigger.taskstep;

import java.util.HashMap;
import java.util.List;

import com.duggan.workflow.shared.model.TaskStepDTO;
import com.duggan.workflow.shared.model.TaskStepTrigger;
import com.duggan.workflow.shared.model.TriggerType;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class TaskStepTriggerView extends ViewImpl implements
		TaskStepTriggerPresenter.ITaskStepTriggerView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, TaskStepTriggerView> {
	}

	@UiField Tree tree;
	
	@Inject
	public TaskStepTriggerView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	private Widget createTaskStepWidget(String taskStep) {
		return new InlineLabel(taskStep);
	}

	@Override
	public void addStep(TaskStepDTO step, HashMap<TriggerType, Integer> counts) {
		int total = counts.get(TriggerType.BEFORESTEP)+counts.get(TriggerType.AFTERSTEP);
		
		String stepName = step.getFormName()==null?step.getOutputDocName():
			step.getFormName();
		stepName = stepName.concat(" ("+total+")");
		
		TreeItem taskStep = new TreeItem(createTaskStepWidget(stepName));
		createTriggerTypeItem(taskStep, TriggerType.BEFORESTEP, step, counts.get(TriggerType.BEFORESTEP));
		createTriggerTypeItem(taskStep, TriggerType.AFTERSTEP, step, counts.get(TriggerType.AFTERSTEP));
		tree.addItem(taskStep);
				
	}
	
	void createTriggerTypeItem(TreeItem root, TriggerType type, TaskStepDTO step, int count){
		TaskStepDTO clone = step.clone();
		clone.setTriggerType(type);
		
		TreeItem item = new TreeItem();
		item.setWidget(new TriggerTypePanel(item, clone , count));
		
		item.setUserObject(clone);
		if(count>0){
			item.addItem(new TreeItem());
		}	
		root.addItem(item);
	}
	
	public void setOpenHandler(OpenHandler<TreeItem> openHandler){
		tree.addOpenHandler(openHandler);
	}

	@Override
	public void clearTree() {
		tree.clear();
	}

	/**
	 * Add Triggers to a Before/ After Trigger
	 */
	@Override
	public void setTriggers(TreeItem item, List<TaskStepTrigger> triggers) {
		item.removeItems();
		((TriggerTypePanel)item.getWidget()).setCount(triggers.size());
		for(TaskStepTrigger trigger: triggers){
			TreeItem child = new TreeItem(new TriggerPanel(item, trigger));
			item.addItem(child);
			
		}
	}
	
}
