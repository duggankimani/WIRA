package com.duggan.workflow.client.ui.task;

import java.util.ArrayList;

import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.client.ui.task.CaseRegistryPresenter.ICaseRegistryView;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.shared.model.CaseFilter;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.ProcessLog;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.commons.shared.models.HTUser;

public class CaseRegistryView extends ViewImpl implements ICaseRegistryView{

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CaseRegistryView> {
	}
	
	@UiField FlexTable tblRegistry;
	@UiField DropDownList<DocumentType> listProcesses;
	@UiField DropDownList<HTUser> listUsers;
	@UiField TextField txtSearch;
	@UiField TextField txtCaseNo;
	@UiField DivElement divTable;
	@UiField Anchor aSearch;
	@UiField SpanElement spnNoData;
	
	@Inject
	public CaseRegistryView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		listProcesses.setNullText("--Select Process--");
		listUsers.setNullText("--Select Users--");
		
	}
	
	private void setHeaders(FlexTable table) {
		
		int j = 0;
		table.setWidget(0, j++, new HTMLPanel("<strong>#</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "20px");

		table.setWidget(0, j++, new HTMLPanel("<strong>Case</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "50px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Summary</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "40px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Start</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		table.setWidget(0, j++, new HTMLPanel("<strong>End</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Process</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Initiated By</strong>"));
		table.setWidget(0, j++, new HTMLPanel("<strong>Task</strong>"));
		table.setWidget(0, j++, new HTMLPanel("<strong>Current User</strong>"));
		table.setWidget(0, j++, new HTMLPanel("<strong>Priority</strong>"));
		table.setWidget(0, j++, new HTMLPanel("<strong>Status</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "60px");
		
		for (int i = 0; i < table.getCellCount(0); i++) {
			table.getFlexCellFormatter().setStyleName(0, i, "th");
		}
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public void onReveal(){
		int height = Window.getClientHeight();
		int tableHeight = height - 95;
		//divTable.setPropertyString("max-height", tableHeight+"px");
		divTable.getStyle().setHeight(tableHeight, Unit.PX);
	}

	@Override
	public void bindProcesses(ArrayList<DocumentType> documentTypes) {
		listProcesses.setItems(documentTypes);
	}

	@Override
	public void bindProcessInstances(ArrayList<ProcessLog> logs) {
		tblRegistry.removeAllRows();
		setHeaders(tblRegistry);
		if(logs.isEmpty()){
			spnNoData.removeClassName("hide");
		}else{
			spnNoData.addClassName("hide");
		}
		
		int row=tblRegistry.getRowCount();
		for(ProcessLog log: logs){
			int col=0;
			
			String taskOwner = log.getTaskOwner();
			if(taskOwner==null || taskOwner.trim().isEmpty()){
				if(log.getPotOwners()!=null){
					taskOwner = "["+log.getPotOwners()+"]";
				}else{
					taskOwner="--";
				}
			}
			
			InlineLabel priority = new InlineLabel("NORMAL");
			//priority.addStyleName("label label-info");
			tblRegistry.setWidget(row, col++, new HTMLPanel("<strong>"+(row)+"</strong>"));
			tblRegistry.setWidget(row, col++, new InlineLabel(parse(log.getCaseNo())));
			tblRegistry.setWidget(row, col++, getSummaryLink(log));
			tblRegistry.setWidget(row, col++, new InlineLabel(DateUtils.DATEFORMAT.format(log.getStartDate())));
			tblRegistry.setWidget(row, col++, new InlineLabel(log.getEndDate()==null? "--" :
				DateUtils.DATEFORMAT.format(log.getEndDate())));
			tblRegistry.setWidget(row, col++, new InlineLabel(log.getProcessName()));
			tblRegistry.setWidget(row, col++, new InlineLabel(log.getInitiator()));
			tblRegistry.setWidget(row, col++, new InlineLabel(log.getTaskName()==null? "--": log.getTaskName()));
			tblRegistry.setWidget(row, col++, new InlineLabel(taskOwner.isEmpty()? "--": taskOwner));
			tblRegistry.setWidget(row, col++, priority);
			tblRegistry.setWidget(row, col++, getProcessState(log.getProcessState()));
			
			++row;
		}
	}
	
	private String parse(String caseNo) {
		return caseNo.replace("Case-","#");
	}

	Anchor getSummaryLink(ProcessLog log){
		ActionLink anchor = new ActionLink();
		anchor.getElement().setInnerHTML("<i class='icon-th-large'/>");
		anchor.setTitle("Summary");
		//anchor.setHref("#caseview;did="+log.getDocId());
		anchor.setHref("#/caseview/"+log.getDocRefId());
		return anchor;
	}

	private Widget getProcessState(int processState) {
		
		String styleName="";
		String name="";
		switch (processState) {
		case 0:
			//Active
			name="ToDo";
			styleName="label label-info";
			break;
		case 1:
			//Aborted
			name="Aborted";
			styleName="label label-danger";
			break;
		case 2:
			//Completed
			name="Done";
			styleName="label label-success";
			break;
		default:
			break;
		}
		
		InlineLabel label = new InlineLabel(name);
		label.addStyleName(styleName);
		return label;
	}

	@Override
	public void bindUsers(ArrayList<HTUser> users) {
		listUsers.setItems(users);
	}

	public Anchor getSearch() {
		return aSearch;
	}
	
	public TextField getTxtCaseNo(){
		return txtCaseNo;
	}
	
	public CaseFilter getCaseFilter(){
		CaseFilter filter = new CaseFilter();
		filter.setCaseNo(txtCaseNo.getValue().trim().isEmpty()? null: txtCaseNo.getValue().trim());
		
		DocumentType type= listProcesses.getValue();
		
		filter.setProcessId(type==null? null: type.getProcessId());
		//Window.alert("ProcessId = "+type+" : "+filter.getProcessId());
		
		HTUser user = listUsers.getValue();
		filter.setUserId(user==null? null: user.getUserId());
		
		return filter;
	}

}

