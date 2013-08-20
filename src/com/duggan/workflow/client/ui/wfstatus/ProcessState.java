package com.duggan.workflow.client.ui.wfstatus;

import com.duggan.workflow.shared.model.NodeDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This represents a note in the approval process It may be a startnode, an
 * approval/rejection of completion of the approval process
 * 
 * @author duggan
 * 
 */
public class ProcessState extends Composite {

	interface Binder extends UiBinder<Widget, ProcessState> {
	}

	Binder binder = GWT.create(Binder.class);
	
	STARTNODETEMPLATE startTemplate = GWT.create(STARTNODETEMPLATE.class);
	APPROVALNODETEMPLATE approvalTemplate = GWT.create(APPROVALNODETEMPLATE.class);
	APPROVALNOTELAST lastApprovalTemplate = GWT.create(APPROVALNOTELAST.class);
	
	@UiField InlineLabel container;
	
	public ProcessState() {
		initWidget(binder.createAndBindUi(this));
	}

	public ProcessState(NodeDetail state) {
		this();
		
		SafeHtml html = null;
		
		if(state.isStartNode()){
			html = startTemplate.display(state.getName());
			
		}
		
		if(state.isEndNode() || state.isCurrentNode()){
			html =  lastApprovalTemplate.display(state.getName());			
		}
		
		//default
		if(html==null)
			html = approvalTemplate.display(state.getName());
		
		container.getElement().setInnerHTML(html.asString());
	}

	public void setInfo() {

	}

	interface STARTNODETEMPLATE extends SafeHtmlTemplates {
		
		@Template("<span class=\"label label-success\">" +
				"<i class=\"icon-ok-circle\"></i>{0}</span>" +
						"<i class=\"icon-arrow-down pull-right\"></i>")		
		public SafeHtml display(String name);
	}

	interface APPROVALNODETEMPLATE extends SafeHtmlTemplates {
		@Template("<span class=\"label label-success\">" +
				"<i class=\"icon-ok-circle\"></i>" +
						"{0}</span><i class=\"icon-arrow-down pull-right\"></i>")
		public SafeHtml display(String name);
	}
	
	
	interface APPROVALNOTELAST extends SafeHtmlTemplates{
		@Template("<span class=\"label label-default\">" +
				"<i class=\"icon-remove-circle\"></i>" +
						"{0}</span>")
		public SafeHtml display(String name);
	}
	
}
