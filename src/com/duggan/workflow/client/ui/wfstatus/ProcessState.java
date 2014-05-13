package com.duggan.workflow.client.ui.wfstatus;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.NodeDetail;
import com.duggan.workflow.shared.model.UserGroup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.UIObject;
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
	APPROVALNODECURRENT currentApprovalNodeTemplate = GWT.create(APPROVALNODECURRENT.class);
	ENDNODETEMPLATE endTemplate = GWT.create(ENDNODETEMPLATE.class);
	
	@UiField DivElement popOverDiv;
	@UiField FocusPanel parentContainer;
	@UiField HTML htmlContainer;
	@UiField DivElement divActors;
	@UiField Element hTitle;
	
	List<UserGroup> groups = new ArrayList<UserGroup>();
	
	NodeDetail node;
	public ProcessState() {
		initWidget(binder.createAndBindUi(this));
		UIObject.setVisible(popOverDiv, false);
	}

	String actors="";
	public ProcessState(NodeDetail state) {
		this();
		this.node=state;
		
		if(state.getActors()!=null){
			for(HTUser user: state.getActors()){
				if(user==null){
					continue;
				}
				
				String actor = user.getFullName()+" ("+
						(AppContext.isCurrentUser(user.getUserId())? "You": user.getUserId())
						+"), ";
				actors = actors.concat(actor);
			}
			if(!actors.isEmpty()){
				divActors.setInnerText(actors.substring(0, actors.length()-2));
			}
		}
		
		if(state.getGroups()!=null){
			String groups = "";
			for(UserGroup group: state.getGroups()){
				if(group==null){
					continue;
				}
				String groupStr = group.getDisplayName()+", ";	
				groups= groups.concat(groupStr);
			}
			
			if(!groups.isEmpty()){
				hTitle.setInnerText(groups.substring(0, groups.length()-2));
			}
		}
		
		SafeHtml html = null;
		
		if(state.isStartNode()){
			html = startTemplate.display(state.getName());
			
		}
		
		if(state.isCurrentNode()){
			html =  currentApprovalNodeTemplate.display(state.getName());			
		}
		
		if(state.isEndNode()){
			html = endTemplate.display(state.getName());
		}
		
		//default
		if(html==null)
			html = approvalTemplate.display(state.getName());
		
		htmlContainer.setHTML(html);
		
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if(node.isStartNode() || node.isEndNode()){
			return;
		}		
		
		parentContainer.addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				int top = getElement().getAbsoluteTop();
				int left= getElement().getAbsoluteLeft();
			
				int textLength = actors.length();
				if(textLength>30){
					popOverDiv.getStyle().setTop(top-145, Unit.PX);	 //if
					popOverDiv.getStyle().setLeft(left-276, Unit.PX);
				}else{
					popOverDiv.getStyle().setTop(top-127, Unit.PX);	 //if			
					popOverDiv.getStyle().setLeft(left-230, Unit.PX);
				}

				UIObject.setVisible(popOverDiv, true);
				popOverDiv.getStyle().setDisplay(Display.BLOCK);
			}
		});
		
		parentContainer.addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				UIObject.setVisible(popOverDiv, false);
			}
		});
	}

	interface STARTNODETEMPLATE extends SafeHtmlTemplates {
		
		@Template("<span class=\"label label-success ellipsis\">" +
				"<i class=\"icon-ok-circle\"></i>{0}</span>" +
						"<i class=\"icon-arrow-down pull-right\"></i>")		
		public SafeHtml display(String name);
	}

	interface APPROVALNODETEMPLATE extends SafeHtmlTemplates {
		@Template("<span class=\"label label-success ellipsis\">" +
				"<i class=\"icon-ok-circle\" title=\"{0}\"></i>" +
						"{0}</span><i class=\"icon-arrow-down pull-right\"></i>")
		public SafeHtml display(String name);
	}
	
	
	interface APPROVALNODECURRENT extends SafeHtmlTemplates{
		@Template("<span class=\"label label-default ellipsis\">" +
				"<i class=\"icon-remove-circle\" title=\"{0}\"></i>" +
						"{0}</span>")
		public SafeHtml display(String name);
	}
	
	interface ENDNODETEMPLATE extends SafeHtmlTemplates {
		
		@Template("<span class=\"label label-success ellipsis\">" +
				"<i class=\"icon-ok-circle\"></i>{0}</span>")		
		public SafeHtml display(String name);
	}
	
}
