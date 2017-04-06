package com.duggan.workflow.client.ui.applicationscentral;

import java.util.ArrayList;

import javax.inject.Inject;

import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.form.Form;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.commons.client.security.CurrentUser;
import com.wira.commons.shared.models.PermissionName;

class ApplicationsCentralView extends ViewImpl implements ApplicationsCentralPresenter.MyView {
    interface Binder extends UiBinder<Widget, ApplicationsCentralView> {
    }
    
    @UiField HTMLPanel docContainer;
    
    @UiField Element spnProcessName;
    @UiField Element spnCaseNumber;
    @UiField Element aZoom;
    
    int perc = 100;
    
    @UiField
	Anchor aDoc;
    @UiField
	Anchor aAudit;
	@UiField
	Anchor aEdit; 
	@UiField
	Anchor aSave;
//	@UiField
//	Anchor aDelete;
	@UiField
	Anchor aComplete;
	@UiField
	Anchor aStop;
	@UiField
	Anchor aForward;
	@UiField
	Anchor aProcess;
	@UiField
	Anchor aViewAttachments;
	
	@UiField
	Anchor aConfigure;
	
	boolean overrideDefaultComplete = false;
	private boolean isUnassignedList = false;
	ArrayList<Actions> validActions = null;
	
	private boolean isLoadAsAdmin;

	private CurrentUser currentUser;
    
    @Inject
    ApplicationsCentralView(Binder uiBinder, CurrentUser currentUser) {
        this.currentUser = currentUser;
		initWidget(uiBinder.createAndBindUi(this));
        bindSlot(ApplicationsCentralPresenter.DOCUMENT_SLOT, docContainer);
        initZoom(aZoom,docContainer.getElement());
    }

	private native void initZoom(Element target, Element contentArea) /*-{
		var that = this;
		$wnd.jQuery($doc).ready(function(){
			$wnd.jQuery(target).find('a').click(function(evt){
				evt.preventDefault();
				
				var current = that.@com.duggan.workflow.client.ui.applicationscentral.ApplicationsCentralView::perc;
				var newValue;
				
				var i = $wnd.jQuery(this).find('i').get(0); 
				if($wnd.jQuery(i).hasClass('icon-zoom-in')){
					newValue=current+10;
				}else{
					newValue = current-10;
				}
				
				that.@com.duggan.workflow.client.ui.applicationscentral.ApplicationsCentralView::perc=newValue
				var scale = newValue/100;
				contentArea.style.zoom=scale;
				
				$wnd.jQuery(target).prop('title',''+newValue+'%');
			});
		});
	}-*/;

	@Override
	public void setContext(Form form,Doc doc) {
		String name = doc.getProcessName();
		String caseNumber = doc.getCaseNo();
		spnProcessName.setInnerText(name);
		spnCaseNumber.setInnerText(caseNumber);
		boolean isEditable = (doc instanceof Document && (((Document)doc).getStatus()==DocStatus.DRAFTED));
		show(aEdit, isEditable);
		show(aForward, isEditable);
		show(aSave, isEditable);
		
		//Configure
		if(form.getProcessRefId()!=null){
			aConfigure.setHref("#/formbuilder?p="+form.getProcessRefId()+"&formRefId="+form.getRefId());
			aConfigure.setTarget("_blank");	
		}

	}
	
	private void disableAll() {
		show(aComplete, false);
		show(aStop, false);
		show(aForward, false);
		show(aConfigure, currentUser.hasPermissions(PermissionName.PROCESSES_CAN_EDIT_PROCESSES.name()));
	}
	
	public void setValidTaskActions(ArrayList<Actions> actions) {
		
		this.validActions = actions;
		if(isLoadAsAdmin){
			disableAll();
			return;
		}
		if (actions != null)
			for (Actions action : actions) {
				Anchor target = null;
				switch (action) {
				case CLAIM:
//					target = aClaim;
					break;
				case COMPLETE:
					// target=aComplete;
					if (!overrideDefaultComplete) {
						// show(aApprove);
						// show(aReject);
					}
					break;
				case DELEGATE:
//					target = aDelegate;
					break;
				case FORWARD:
					target = aForward;
					break;
				case RESUME:
//					target = aResume;
					break;
				case REVOKE:
//					target = aRevoke;
					break;
				case START:
//					target = aStart;
					break;
				case STOP:
					target = aStop;
					break;
				case SUSPEND:
//					target = aSuspend;
					break;
				}

				if (target != null) {
					show(target);
				}
			}

	}

	public void show(Anchor target) {
		show(target, true);
	}

	public void show(Anchor target, boolean isShow) {
		if (isShow) {
			target.removeStyleName("hidden");
		}
		UIObject.setVisible(target.getElement(), isShow);

	}

	public HasClickHandlers getSaveButton() {
		return aSave;
	}

	public HasClickHandlers getCompleteLink() {
		return aComplete;
	}


	public HasClickHandlers getStopLink() {
		return aStop;
	}

	public HasClickHandlers getForwardForApproval() {
		return aForward;
	}

	@Override
	public HasClickHandlers getShowProcessMap() {
		return aProcess;
	}

	@Override
	public HasClickHandlers getEditLink() {
		return aEdit;
	}

	@Override
	public HasClickHandlers getShowAttachmentsLink() {
		return aViewAttachments;
	}

	@Override
	public HasClickHandlers getSaveDocLink() {
		return aSave;
	}

	@Override
	public HasClickHandlers getViewAuditLog() {
		return aAudit;
	}

	@Override
	public HasClickHandlers getViewDocButton() {
		return aDoc;
	}

}