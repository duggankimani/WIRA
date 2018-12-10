package com.duggan.workflow.client.ui.task;

import java.util.ArrayList;

import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.client.ui.task.CaseRegistryPresenter.ICaseRegistryView;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.event.LoadDataEvent;
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
	public void bindProcessInstances(ArrayList<ProcessLog> logs, 
			Integer currentPage, Integer totalCount) {

		tblRegistry.removeAllRows();
		setHeaders(tblRegistry);
		if(logs.isEmpty()){
			spnNoData.removeClassName("hide");
		}else{
			spnNoData.addClassName("hide");
		}
		
		paginator(currentPage, totalCount);
		
		int row=tblRegistry.getRowCount();
		
		for(ProcessLog log: logs){
			int col=0;
			
			String taskOwner = log.getTaskOwner();
			String ownerLabelTitle = "";
			if(taskOwner==null || taskOwner.trim().isEmpty()){
				if(log.getPotOwners()!=null){
					ownerLabelTitle  = "Potential Owner of the task, user has to claim the task!";
					taskOwner = "["+log.getPotOwners()+"]";
				}else{
					taskOwner="--";
				}
			}else {
				ownerLabelTitle = "Actual Task Owner";
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
			
			InlineLabel taskOwnerLabel = new InlineLabel(taskOwner.isEmpty()? "--": taskOwner);
			if(!ownerLabelTitle.isEmpty()) {
				taskOwnerLabel.setTitle(ownerLabelTitle);
			}
			
			tblRegistry.setWidget(row, col++, taskOwnerLabel);
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
		
		String taskIdQ = (log.getTaskId()==null? "1=1&": "tid="+log.getTaskId());
		String processInstanceIdQ = log.getProcessinstanceid()==null? "": "pid="+log.getProcessinstanceid();		
		String href = "#/caseview/"+log.getDocRefId()+"?"+taskIdQ+"&"+processInstanceIdQ;
		anchor.setHref(href);
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
			styleName="label label-warning";
			break;
		case 2:
			//Completed
			name="Done";
			styleName="label label-success";
			break;
		case 3:
			//Aborted
			name="Exited";
			styleName="label label-exited";
			break;
		default:
			name="State"+processState;
			styleName="label label-danger";
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
		HTUser user = listUsers.getValue();
		filter.setUserId(user==null? null: user.getUserId());
		
		return filter;
	}
	
	public void onPageChange(String selectedPage) {
		AppContext.fireEvent(new LoadDataEvent(Integer.parseInt(selectedPage)));
	}
	
	public native Integer paginator(int currentPage, int recordSize)/*-{
	
	var current_page = currentPage;
	var instance = this;
	var records_per_page = 10;
	
	function prevPage()
	{
	    if (current_page > 1) {
	        current_page--;
	        changePage(current_page);
	        instance.@com.duggan.workflow.client.ui.task.CaseRegistryView::onPageChange(Ljava/lang/String;)(current_page);
	    }
	}
	
	function nextPage()
	{
	    if (current_page < numPages()) {
	        current_page++;
	        changePage(current_page);
	        instance.@com.duggan.workflow.client.ui.task.CaseRegistryView::onPageChange(Ljava/lang/String;)(current_page);
	    }
	    
	}
	
	function changePage(page)
	{
	    var btn_next = $doc.getElementById("btn_next");
	    var btn_prev = $doc.getElementById("btn_prev");
	    
	    
	    var listing_table = $doc.getElementById("liPaginator");
	    var page_span = $doc.getElementById("page");
	
	    // Validate page
	    if (page < 1) page = 1;
	    if (page > numPages()) page = numPages();
	
	    listing_table.innerHTML = "<li class='active'>"+
						"<a><span id='spnCount'>"+recordSize+"</span> records</a>"+
						"</li>"+

						"<li>"+
						"	<a>"+
						"		Page"+
						"		<strong id='page'>"+page+"</strong>"+
						"		of"+
						"		<strong id='totalPages'>"+numPages()+"</strong>"+
						"	</a>"+
						"</li>"+
						"<li>"+
						"	<a id='btn_prev'><i class='icon icon-double-angle-left'></i></a>"+
						"</li>";
	
	    for (var i = 1; i <= numPages(); i++) {
	    	var elPage =  $doc.createElement('li');
	    	if(i==page){
	    		elPage.classList.add('active');
	    		elPage.classList.add('currentpage');
	    	}
	    	
	    	if(i<8){
	    		elPage.innerHTML = '<a>'+ i + '</a>';
	    	}else if(i==8){
	    		elPage.innerHTML = '<a>...</a>';
	    	}
	    	if(i<=8){
	        	listing_table.append(elPage);
	        }
	    }
	    listing_table.innerHTML = listing_table.innerHTML+"<li>"+
			"<a id='btn_next'><i class='icon icon-double-angle-right'></i></a>"+
			"</li>";
			
	    page_span.innerHTML = page;
	
	    if (page == 1) {
	        btn_prev.style.visibility = "hidden";
	    } else {
	        btn_prev.style.visibility = "visible";
	    }
	
	    if (page == numPages()) {
	        btn_next.style.visibility = "hidden";
	    } else {
	        btn_next.style.visibility = "visible";
	    }
	    
	    
	    initEvents();
	}
	
	function initEvents(){
		$wnd.jQuery('#btn_next').click(function(){
			nextPage();
		});
		
		$wnd.jQuery('#btn_prev').click(function(){
			prevPage();
		});
	}
	
	function numPages()
	{
	    return Math.ceil(recordSize / records_per_page);
	}
	
	$wnd.jQuery(document).ready(function($) {
		changePage(currentPage);
	});
	
}-*/;


}

