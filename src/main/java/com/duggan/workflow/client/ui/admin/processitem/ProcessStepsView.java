package com.duggan.workflow.client.ui.admin.processitem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jbpm.task.EmailNotification;

import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.component.TableHeader;
import com.duggan.workflow.client.ui.component.TableView;
import com.duggan.workflow.client.ui.events.SaveTaskStepEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.MODE;
import com.duggan.workflow.shared.model.NotificationCategory;
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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ProcessStepsView extends ViewImpl implements ProcessStepsPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ProcessStepsView> {
	}

	@UiField
	HTMLPanel divTasks;
	@UiField
	FlexTable tblView;
	@UiField
	DropDownList<TaskNode> tasksDropDown;
	// @UiField Anchor aAddItem;
	ClickHandler deleteHandler;

	@UiField
	Anchor aNewStep;
	@UiField
	Anchor aNewTrigger;
	
	@UiField DropDownList<NotificationCategory> listNotificationType;
	
	@UiField
	Anchor aStepstab;
	@UiField
	Anchor aTriggerstab;
	@UiField
	Anchor aNotificationstab;

	@UiField
	Element divStepContent;
	@UiField
	Element divTriggerContent;
	@UiField
	Element divNotificationsContent;

	@UiField HTMLPanel panelTriggerContent;
	@UiField HTMLPanel panelNotificationsContent;

	@UiField
	Element liTrigger;
	@UiField
	Element liStep;
	@UiField
	Element liNotifications;

	@Inject
	public ProcessStepsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		aStepstab.getElement().setAttribute("data-toggle", "tab");
		aTriggerstab.getElement().setAttribute("data-toggle", "tab");
		aNotificationstab.getElement().setAttribute("data-toggle", "tab");

		divStepContent.setId("steps");
		divTriggerContent.setId("triggers");
		divNotificationsContent.setId("notifications");
		
		listNotificationType.setNullText(NotificationCategory.EMAILNOTIFICATION.getDisplayName());
		listNotificationType.setItems(Arrays.asList(NotificationCategory.ACTIVITYFEED));
		
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

		aNotificationstab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setType(3);
			}
		});

		setType(1);
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == ProcessStepsPresenter.TRIGGER_SLOT) {
			panelTriggerContent.clear();
			if (content != null) {
				panelTriggerContent.add(content);
			}
		}else if(slot == ProcessStepsPresenter.NOTIFICATIONS_SLOT){ 
			panelNotificationsContent.clear();
			if(content !=null ){
				panelNotificationsContent.add(content);
			}
		}else {
			super.setInSlot(slot, content);
		}
	}

	private void setTableHeaders(FlexTable table) {
		int j = 0;
		table.setWidget(0, j++, new HTMLPanel("<strong>#</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "20px");

		table.setWidget(0, j++, new HTMLPanel("<strong>Step Name</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "200px");
		
		table.setWidget(0, j++, new HTMLPanel("<strong>Mode</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		
		table.setWidget(0, j++, new HTMLPanel("<strong>Condition</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "300px");
		
		table.setWidget(0, j++, new HTMLPanel("<strong>Actions</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");

		for (int i = 0; i < table.getCellCount(0); i++) {
			table.getFlexCellFormatter().setStyleName(0, i, "th");
		}
	}

	@Override
	public void displaySteps(List<TaskStepDTO> dtos) {
		tblView.removeAllRows();
		setTableHeaders(tblView);
		
		int i=1;
		for (TaskStepDTO dto : dtos) {
			int j=0;
			String buttonStyle = "italics";// "btn";

			HTMLPanel actions = new HTMLPanel("");
			Link up = new Link("Up", dto, -1);
			up.addStyleName(buttonStyle + " "
					+ (dto.getSequenceNo() == 1 ? "hide" : ""));
			up.addClickHandler(new SequenceChangeHandler());
			actions.add(up);

			Link down = new Link("Down", dto, +1);
			down.addStyleName(buttonStyle + " "
					+ (dto.getSequenceNo() == dtos.size() ? "hide" : ""));
			down.addClickHandler(new SequenceChangeHandler());
			actions.add(down);

			Link link = new Link("Delete", dto, 0);
			link.addStyleName(buttonStyle);
			link.addClickHandler(deleteHandler);
			actions.add(link);

			DropDownList<MODE> mode = null;

			if (dto.getMode() != null) {
				mode = new DropDownList<MODE>();
				mode.setItems(Arrays.asList(MODE.EDIT, MODE.VIEW));
				mode.setValue(dto.getMode());
				mode.addStyleName("input-small");
				mode.addValueChangeHandler(new ValueChangeHanderEx(dto));
			}
			
			
			tblView.setWidget(i, j++, new InlineLabel(i+""));
			tblView.setWidget(i, j++, new InlineLabel(dto.getFormName() == null ? dto
					.getOutputDocName() : dto.getFormName()));
			tblView.setWidget(i, j++, mode == null ? new InlineLabel("") : mode);
			tblView.setWidget(i, j++, new InlineLabel(
					dto.getCondition()));
			tblView.setWidget(i, j++, actions);
			
			++i;
		}
	}

	class ValueChangeHanderEx implements ValueChangeHandler<MODE> {

		TaskStepDTO dto;

		ValueChangeHanderEx(TaskStepDTO dto) {
			this.dto = dto;
		}

		@Override
		public void onValueChange(ValueChangeEvent<MODE> event) {
			if (event.getValue() != null) {
				dto.setMode(event.getValue());
				AppContext.fireEvent(new SaveTaskStepEvent(dto));
			}

		}
	}

	class SequenceChangeHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			Link link = ((Link) event.getSource());
			TaskStepDTO dto = link.dto;
			int delta = link.positionChange;
			if (delta != 0) {
				dto.setSequenceNo(dto.getSequenceNo() + delta);
				AppContext.fireEvent(new SaveTaskStepEvent(dto));
			}

		}

	}

	class Link extends ActionLink {
		TaskStepDTO dto;
		int positionChange;

		public Link(String txt, TaskStepDTO dto, int positionChange) {
			super(txt);
			this.dto = dto;
			this.positionChange = positionChange;
		}
	}

	public DropDownList<TaskNode> getTasksDropDown() {
		return tasksDropDown;
	}

	public HasClickHandlers getAddItemActionLink() {
		return aNewStep;
	}

	public void setTasks(List<TaskNode> values) {
		tasksDropDown.setItems(values);
	}

	public TaskNode getSelectedTask() {
		return (TaskNode) tasksDropDown.getValue();
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setDeleteHandler(ClickHandler clickHandler) {
		this.deleteHandler = clickHandler;
	}

	@Override
	public void setType(int tab) {
		
		divStepContent.removeClassName("in");
		divStepContent.removeClassName("active");
		divTriggerContent.removeClassName("in");
		divTriggerContent.removeClassName("active");
		divNotificationsContent.removeClassName("in");
		divNotificationsContent.removeClassName("active");
		
		aNewStep.addStyleName("hide");
		aNewTrigger.addStyleName("hide");
		listNotificationType.addStyleName("hide");

		liStep.removeClassName("active");
		liTrigger.removeClassName("active");
		liNotifications.removeClassName("active");

		if (tab == 1) {
			aNewStep.removeStyleName("hide");
			liStep.addClassName("active");

			divStepContent.addClassName("in");
			divStepContent.addClassName("active");
		} else if(tab == 2){
			aNewTrigger.removeStyleName("hide");
			liTrigger.setClassName("active");

			divTriggerContent.addClassName("in");
			divTriggerContent.addClassName("active");
		}else if(tab == 3){
			listNotificationType.removeStyleName("hide");
			liNotifications.setClassName("active");

			divNotificationsContent.addClassName("in");
			divNotificationsContent.addClassName("active");
		}
	}

	public HasClickHandlers getAddTriggerLink() {
		return aNewTrigger;
	}

	@Override
	public HasClickHandlers getTriggersTabLink() {
		return aTriggerstab;
	}

	@Override
	public HasClickHandlers getStepsTabLink() {
		return aStepstab;
	}

	@Override
	public HasClickHandlers getNotificationsTabLink() {
		return aNotificationstab;
	}
	
	public DropDownList<NotificationCategory> getNotificationCategoryDropdown(){
		return listNotificationType;
	}
}
