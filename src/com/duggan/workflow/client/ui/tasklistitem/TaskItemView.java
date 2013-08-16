package com.duggan.workflow.client.ui.tasklistitem;

import java.util.List;

import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocSummary;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.Priority;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;


public class TaskItemView extends ViewImpl implements TaskItemPresenter.MyView {

	private final Widget widget;
	
	public interface Binder extends UiBinder<Widget, TaskItemView> {
	}

	@UiField InlineLabel spnSubject;
	@UiField InlineLabel spnDescription;
	@UiField InlineLabel spnPriority;
	@UiField Anchor aClaim;
	@UiField Anchor aStart;
	@UiField Anchor aSuspend;
	@UiField Anchor aResume;
	@UiField Anchor aComplete;
	@UiField Anchor aDelegate;
	@UiField Anchor aRevoke;
	@UiField Anchor aStop;
	@UiField Anchor aForward;
	
	@UiField Anchor aForwardForApproval;
	
	@UiField Anchor aApprove;
	@UiField Anchor aReject;
	
	@UiField Anchor aView;
	
	@UiField FocusPanel container;
		
	@Inject
	public TaskItemView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		spnPriority.removeStyleName("gwt-InlineLabel");
		spnSubject.removeStyleName("gwt-InlineLabel");
		spnDescription.removeStyleName("gwt-InlineLabel");
		show(aClaim, false);
		show(aStart, false);
		show(aSuspend, false);
		show(aResume, false);
		show(aComplete, false);
		show(aDelegate, false);
		show(aReject, false);
		show(aRevoke, false);
		show(aStop, false);
		show(aForward, false);
		show(aForwardForApproval, false);
		show(aApprove, false);
		show(aView, false);
		
	}
	
	@Override
	public Widget asWidget() {
		//return container;
		return widget;
	}

	@Override
	public void bind(DocSummary summaryTask) {
		//spnTaskName.setText(summaryTask.getTaskName());
		//spnTaskName.setText("Contract Approval");
		//spnDateDue.setText(format(summaryTask.getDateDue()));
		
		spnSubject.setText(summaryTask.getSubject());
		
		if(summaryTask.getDescription()!=null)
			spnDescription.setText(summaryTask.getDescription());
		//spnPriority.setText(summaryTask.getPriority()==null? "": summaryTask.getPriority().toString());
		
		if(summaryTask instanceof HTSummary){
			setTaskAction(((HTSummary)summaryTask).getStatus().getValidActions());
		}else{
			setDocumentActions(((Document)summaryTask).getStatus());
		}
		
		summaryTask.getId();
		
		Priority priority = Priority.get(summaryTask.getPriority());
		
		switch (priority) {
		case CRITICAL:
			spnPriority.addStyleName("label-important");
			spnPriority.setText("Urgent");
			break;

		case HIGH:
			spnPriority.addStyleName("label-warning"); //
			spnPriority.setText("Important");
			break;

		default:
			spnPriority.addStyleName("label-info");
			spnPriority.setText("info");
			break;
		}
	}
	
	private void setDocumentActions(DocStatus status) {
		if(status==DocStatus.DRAFTED){
			aForwardForApproval.removeStyleName("hidden");
			aForwardForApproval.setStyleName("visible", true);
		}
	}

	/**
	 * Task Life Cycle Actions
	 * 
	 * @param actions
	 */
	public void setTaskAction(List<Actions> actions){
		
		String style="visible";
		
		if(actions!=null)
		for(Actions action : actions){
			Anchor target=null;
			
			switch(action){
			case CLAIM:
				target=aClaim;
				break;
			case COMPLETE:
				//target=aComplete;
				show(aApprove);
				show(aReject);
				break;
			case DELEGATE:
				target=aDelegate;
				break;
			case FORWARD:
				target=aForward;
				break;
			case RESUME:
				target=aResume;
				break;
			case REVOKE:
				target=aRevoke;
				break;
			case START:
				target=aStart;
				break;
			case STOP:
				target=aStop;
				break;
			case SUSPEND:
				target=aSuspend;
				break;
			}
			
			if(target!=null){
				show(target);
			}
		}
		
	}
	
	public void show(Anchor target){
		show(target,true);
	}
	public void show(Anchor target, boolean isvisible){
		if(isvisible){
			target.removeStyleName("hidden");
		}
		UIObject.setVisible(target.getElement(), isvisible);
		
	}
	
	public HasClickHandlers getClaimLink(){
		return aClaim;
	}
	
	public HasClickHandlers getStartLink(){
		return aStart;
	}
	
	public HasClickHandlers getSuspendLink(){
		return aSuspend;
	}
	
	public HasClickHandlers getResumeLink(){
		return aResume;
	}
	
	public HasClickHandlers getCompleteLink(){
		return aComplete;
	}
	
	public HasClickHandlers getDelegateLink(){
		return aDelegate;
	}
	
	public HasClickHandlers getRevokeLink(){
		return aRevoke;
	}
	
	public HasClickHandlers getStopLink(){
		return aStop;
	}
	
	public HasClickHandlers getForwardLink(){
		return aForward;
	}
	
	public HasClickHandlers getViewLink(){
		return aView;
	}
	
	public HasClickHandlers getSubmitForApprovalLink(){
		return aForwardForApproval;
	}

	public HasClickHandlers getApproveLink(){
		return aApprove;
	}
	
	public HasClickHandlers getRejectLink(){
		return aReject;
	}
	
	public HasClickHandlers getFocusContainer(){
		return container;
	}
	
	public void setSelected(boolean selected){
		if(selected){
			container.getElement().getStyle().setBackgroundColor("#e3e0e0");
		}else{
			container.getElement().getStyle().setBackgroundColor("#ffffff");
		}
	}
}
