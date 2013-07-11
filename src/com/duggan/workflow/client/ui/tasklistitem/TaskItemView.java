package com.duggan.workflow.client.ui.tasklistitem;

import java.util.List;

import com.duggan.workflow.client.ui.resources.ICONS;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocSummary;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import static com.duggan.workflow.client.ui.util.DateUtils.*;

public class TaskItemView extends ViewImpl implements TaskItemPresenter.MyView {

	private final Widget widget;
	
	public interface Binder extends UiBinder<Widget, TaskItemView> {
	}

	@UiField InlineLabel spnRowNo;
	@UiField InlineLabel spnTaskName;
	//@UiField InlineLabel spnDateDue;
	@UiField InlineLabel spnDateCreated;
	@UiField InlineLabel spnSubject;
	@UiField InlineLabel spnDescription;
	//@UiField InlineLabel spnPriority;
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
	
	@UiField HasClickHandlers aView;
	
	@UiField FocusPanel container;
	
	@UiField Image imgFlag;
	private int severity=0; //(0-grey, 1-yellow, 2-red)
	
	@Inject
	public TaskItemView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		imgFlag.setResource(ICONS.INSTANCE.flagwhite());
		imgFlag.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if(severity==0){
					severity=1;
					imgFlag.setResource(ICONS.INSTANCE.flagyellow());
				}else if(severity==1){
					severity=2;
					imgFlag.setResource(ICONS.INSTANCE.flagred());
				}else if(severity==2){
					severity=0;
					imgFlag.setResource(ICONS.INSTANCE.flagwhite());
				}
				
				
			}
		});
		
	}

	@Override
	public Widget asWidget() {
		//return container;
		return widget;
	}

	@Override
	public void setRowNo(int row) {
		spnRowNo.setText(row+"");
	}

	@Override
	public void bind(DocSummary summaryTask) {
		if(summaryTask.getCreated()!=null)
			spnDateCreated.setText(CREATEDFORMAT.format(summaryTask.getCreated()));

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
		String style="visible";
		target.setStyleName(style);
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
	
	@Override
	public FocusPanel getContainer() {

		return container;
	}
	
	public void setSelected(boolean selected){
		if(selected){
			//container.getElement().getStyle().setBackgroundColor("#f3fffe");
			container.getElement().getStyle().setBackgroundColor("#e3e0e0");
		}else{
			container.getElement().getStyle().setBackgroundColor("#ffffff");
		}
	}
}
