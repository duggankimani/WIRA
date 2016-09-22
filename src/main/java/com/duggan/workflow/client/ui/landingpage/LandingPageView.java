package com.duggan.workflow.client.ui.landingpage;

import java.util.ArrayList;

import javax.inject.Inject;

import com.duggan.workflow.client.ui.component.CardRow;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.IsProcessDisplay;
import com.duggan.workflow.shared.model.ProcessCategory;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class LandingPageView extends ViewImpl implements LandingPagePresenter.ILandingPageView {
    interface Binder extends UiBinder<Widget, LandingPageView> {
    }

    @Inject
    LandingPageView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @UiField HTMLPanel tabsPanel;
    @UiField HTMLPanel tabsContent;

	@Override
	public void setProcesses(ArrayList<ProcessCategory> categories) {
		clearTabs();
		for(ProcessCategory cat: categories){
			addTab(cat);
			addTabContent(cat);
		}
	}

	private void clearTabs() {
		tabsContent.getElement().removeAllChildren();
		tabsPanel.getElementById("processtabs").removeAllChildren();
	}

	private void addTab(ProcessCategory cat) {
		Element ul = tabsPanel.getElementById("processtabs");
		Element li = DOM.createElement("li");
		if(!ul.hasChildNodes()){
			li.addClassName("active");
		}
		
		Element a = DOM.createAnchor();
		a.setAttribute("href", "#"+cat.getRefId());
		a.setAttribute("data-toggle", "tab");
		a.setInnerText(cat.getDisplayName());
		li.appendChild(a);
		
		ul.appendChild(li);
//		<li class="active">
//			<a href="#users" data-toggle="tab">FINANCE</a>
//		</li>
	}
	

	@SuppressWarnings("unchecked")
	private void addTabContent(ProcessCategory cat) {
		
		Element tabContentEl = tabsContent.getElement();
		Element tabPane = DOM.createDiv();
		
		if(!tabContentEl.hasChildNodes()){
			tabPane.addClassName("active in");
		}
		tabPane.addClassName("tab-pane fade");
		tabPane.setId(cat.getRefId());
		tabContentEl.appendChild(tabPane);
	
		addProcesses(tabPane, cat);
	}

	private void addProcesses(Element tabPane, ProcessCategory cat) {
		ArrayList<IsProcessDisplay> children =  (ArrayList<IsProcessDisplay>)cat.getChildren();
		
		int rowSize = 4;
		int childCount = children.size();
		int rows = childCount/rowSize;
		int remainder = childCount%rowSize;
		
		
		for(int i=0; i< rows; i++){
			int beginIdx = i*rowSize; 
			int endIdx = beginIdx+rowSize;
			
			ArrayList<DocumentType> sublist = new ArrayList<DocumentType>();
			for(int r=beginIdx; r<endIdx; r++){
				sublist.add((DocumentType)children.get(r));
			}
			
			CardRow row = new CardRow(sublist);
			tabPane.appendChild(row.getElement());
		}
		
		if(remainder!=0){
			//Pick the last items in the main list
			
			ArrayList<DocumentType> sublist = new ArrayList<DocumentType>();
			for(int i=(childCount-remainder); i<childCount; i++){
				sublist.add((DocumentType)children.get(i));
			}
					
			CardRow row = new CardRow(sublist);
			tabPane.appendChild(row.getElement());
		}
	}

	@Override
	public void setNoProcess(boolean noProcessesAssignedToUser) {
		
	}
   
}