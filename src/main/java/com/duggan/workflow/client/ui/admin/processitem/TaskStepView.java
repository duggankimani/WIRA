package com.duggan.workflow.client.ui.admin.processitem;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.component.TableHeader;
import com.duggan.workflow.client.ui.component.TableView;
import com.duggan.workflow.shared.model.TaskNode;
import com.duggan.workflow.shared.model.TaskStepDTO;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class TaskStepView extends ViewImpl implements
		TaskStepPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, TaskStepView> {
	}
	
	@UiField HTMLPanel divTasks;
	@UiField TableView tblView;
	@UiField DropDownList<TaskNode> tasksDropDown;
	@UiField ActionLink aAddItem;
	ClickHandler deleteHandler;
	
	@Inject
	public TaskStepView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		show(false);
		setTable();
	}
	
	private void setTable() {
		tblView.setAutoNumber(true);
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Step Name", 40.0,"title"));
		th.add(new TableHeader("Mode", 20.0));
		th.add(new TableHeader("Condition", 30.0));
		th.add(new TableHeader("Action(s)", 10.0));
		
		tblView.setTableHeaders(th);
	}
	
	@Override
	public void displaySteps(List<TaskStepDTO> dtos) {
		tblView.clearRows();
		
		for(TaskStepDTO dto: dtos){
			Link link = new Link("Delete",dto);
			link.addClickHandler(deleteHandler);
			tblView.addRow(
					new InlineLabel(dto.getFormName()==null? dto.getOutputDocName(): dto.getFormName()),
					new InlineLabel(dto.getMode()==null? "": dto.getMode().name()),
					new InlineLabel(dto.getCondition()),
					link);
		}
	}
	
	class Link extends ActionLink{
		TaskStepDTO dto;
		public Link(String txt,TaskStepDTO dto) {
			super(txt);
			this.dto = dto;
		}
	}
	
	public DropDownList<TaskNode> getTasksDropDown(){
		return tasksDropDown;
	}
	
	public HasClickHandlers getAddItemActionLink(){
		return aAddItem;
	}
	
	public void setTasks(List<TaskNode> values){
		tasksDropDown.setItems(values);
	}
	
	public TaskNode getSelectedTask(){
		return (TaskNode)tasksDropDown.getValue();
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public void show(boolean show){
		if(show){
			divTasks.removeStyleName("hidden");
			divTasks.addStyleName("tr");
		}else{
			divTasks.addStyleName("hidden");
			divTasks.removeStyleName("tr");
		}
	}

	@Override
	public void setDeleteHandler(ClickHandler clickHandler) {
		this.deleteHandler = clickHandler;
	}


}
