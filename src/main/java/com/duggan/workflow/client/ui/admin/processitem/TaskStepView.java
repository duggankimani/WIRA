package com.duggan.workflow.client.ui.admin.processitem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.component.TableHeader;
import com.duggan.workflow.client.ui.component.TableView;
import com.duggan.workflow.client.ui.events.SaveTaskStepEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.MODE;
import com.duggan.workflow.shared.model.TaskNode;
import com.duggan.workflow.shared.model.TaskStepDTO;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
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
	//@UiField Anchor aAddItem;
	ClickHandler deleteHandler;
	
	@UiField Anchor aNewStep;
	@UiField Anchor aNewTrigger;
	@UiField Anchor aStepstab;
	@UiField Anchor aTriggerstab;
	
	@UiField Element divStepContent;
	@UiField Element divTriggerContent;
	@UiField HTMLPanel panelTriggerContent;
	
	@UiField Element liTrigger;
	@UiField Element liStep;

	
	@Inject
	public TaskStepView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		show(false);
		setTable();
		aStepstab.getElement().setAttribute("data-toggle", "tab");
		aTriggerstab.getElement().setAttribute("data-toggle", "tab");
		
		divStepContent.setId("user");
		divTriggerContent.setId("groups");
		
		aStepstab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setType(1);
			}
		});
		
		aTriggerstab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setType(2);
			}
		});
		
		setType(1);
	}
	
	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if(slot==TaskStepPresenter.TRIGGER_SLOT){
			panelTriggerContent.clear();
			if(content!=null){
				panelTriggerContent.add(content);
			}
		}else{
			super.setInSlot(slot, content);
		}
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
			String buttonStyle = "italics";//"btn";
			
			HTMLPanel actions = new HTMLPanel("");
			Link up = new Link("Up",dto,-1);
			up.addStyleName(buttonStyle+" "+ (dto.getSequenceNo()==1?"hide":""));
			up.addClickHandler(new SequenceChangeHandler());
			actions.add(up);
			
			Link down = new Link("Down",dto,+1);
			down.addStyleName(buttonStyle+" "+(dto.getSequenceNo()==dtos.size()?"hide":""));
			down.addClickHandler(new SequenceChangeHandler());
			actions.add(down);
			
			Link link = new Link("Delete",dto,0);
			link.addStyleName(buttonStyle);
			link.addClickHandler(deleteHandler);
			actions.add(link);
			
			DropDownList<MODE> mode = null;
			
			if(dto.getMode()!=null){
				mode = new DropDownList<MODE>();
				mode.setItems(Arrays.asList(MODE.EDIT, MODE.VIEW));
				mode.setValue(dto.getMode());
				mode.addStyleName("input-small");
				mode.addValueChangeHandler(new ValueChangeHanderEx(dto));
			}
			
			tblView.addRow(
					new InlineLabel(dto.getFormName()==null? dto.getOutputDocName(): dto.getFormName()),
					mode==null? new InlineLabel(""): mode,
					new InlineLabel(dto.getCondition()),
					actions);
		}
	}
	
	class ValueChangeHanderEx implements ValueChangeHandler<MODE>{
		
		TaskStepDTO dto;
		
		ValueChangeHanderEx(TaskStepDTO dto){
			this.dto = dto;
		}
		
		@Override
		public void onValueChange(ValueChangeEvent<MODE> event) {
			dto.setMode(event.getValue());
			AppContext.fireEvent(new SaveTaskStepEvent(dto));
		}
	}
	
	class SequenceChangeHandler implements ClickHandler{
		
		@Override
		public void onClick(ClickEvent event) {
			Link link = ((Link)event.getSource());
			TaskStepDTO dto = link.dto;
			int delta = link.positionChange;
			if(delta!=0){
				dto.setSequenceNo(dto.getSequenceNo()+delta);
				AppContext.fireEvent(new SaveTaskStepEvent(dto));
			}
				
		}	
		
	}
		
	class Link extends ActionLink{
		TaskStepDTO dto;
		int positionChange;
		
		public Link(String txt,TaskStepDTO dto, int positionChange) {
			super(txt);
			this.dto = dto;
			this.positionChange = positionChange;
		}
	}
	
	public DropDownList<TaskNode> getTasksDropDown(){
		return tasksDropDown;
	}
	
	public HasClickHandlers getAddItemActionLink(){
		return aNewStep;
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

	@Override
	public void setType(int tab) {
		if(tab==1){
			aNewStep.removeStyleName("hide");
			 aNewTrigger.addStyleName("hide");
			 liTrigger.removeClassName("active");
			 liStep.addClassName("active");
			 
			 divStepContent.addClassName("in"); 
			 divStepContent.addClassName("active");
			 
			 divTriggerContent.removeClassName("in");
			 divTriggerContent.removeClassName("active");
		}else{

			aNewStep.addStyleName("hide");
			aNewTrigger.removeStyleName("hide");
			liTrigger.setClassName("active");
			liStep.removeClassName("active");
			
			divStepContent.removeClassName("in");
			divStepContent.removeClassName("active");
			
			divTriggerContent.addClassName("in");
			divTriggerContent.addClassName("active");
		}
	}
	
	public HasClickHandlers getAddTriggerLink(){
		return aNewTrigger;
	}

	@Override
	public HasClickHandlers getTriggersTabLink(){
		return aTriggerstab;
	}
	
	@Override
	public HasClickHandlers getStepsTabLink(){
		return aStepstab;
	}
}
