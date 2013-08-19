package com.duggan.workflow.client.ui.view;

import java.util.Date;
import java.util.List;

import com.duggan.workflow.client.ui.wfstatus.ProcessState;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.NodeDetail;
import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import static com.duggan.workflow.client.ui.util.DateUtils.*;

public class GenericDocumentView extends ViewImpl implements
		GenericDocumentPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, GenericDocumentView> {
	}

	@UiField
	SpanElement spnCreated;
	@UiField
	SpanElement spnDocType;
	@UiField
	SpanElement spnSubject;
	@UiField
	SpanElement spnDocDate;
	@UiField
	SpanElement spnValue;
	@UiField
	SpanElement spnPartner;
	@UiField
	SpanElement spnDescription;

	@UiField Anchor aSimulate;
	
	@UiField Anchor aEdit;
	
	@UiField Anchor aApprove;
	
	@UiField Anchor aReject;

	@UiField Anchor aForward;
	
	@UiField HTMLPanel statusContainer;
	
	@UiField Element eOwner;
	
	@Inject
	public GenericDocumentView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		UIObject.setVisible(aEdit.getElement(), false);
		aEdit.getElement().setAttribute("type","button");
		aSimulate.getElement().setAttribute("type","button");
		UIObject.setVisible(aForward.getElement(), false);
		UIObject.setVisible(aApprove.getElement(), false);
		UIObject.setVisible(aReject.getElement(), false);
		aApprove.getElement().setAttribute("type", "button");
		aReject.getElement().setAttribute("type", "button");
		aForward.getElement().setAttribute("type", "button");
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public void setValues(HTUser createdBy, Date created, DocType type, String subject,
			Date docDate, String value, String partner, String description, Integer priority,DocStatus status) {
		if(createdBy!=null){
			eOwner.setInnerText(createdBy.getName());
		}
		if (created != null)
			spnCreated.setInnerText(CREATEDFORMAT.format(created));

		if (type != null)
			spnDocType.setInnerText(type.getDisplayName());

		if (subject != null)
			spnSubject.setInnerText(subject);

		if (docDate != null)
			spnDocDate.setInnerText(DATEFORMAT.format(docDate));

		if(value!=null){
			spnValue.setInnerText(value);
			//UIObject.setVisible(divValue, true);
		}
		
		if (partner != null){
			//UIObject.setVisible(divPartner, true);
			spnPartner.setInnerText(partner);
		}			

		if (description != null)
			spnDescription.setInnerText(description);
		
		if(status==null || status==DocStatus.DRAFTED){
			showForward(true);
		}else{
			showForward(false);
		}
	}
	
	public HasClickHandlers getForward(){
		return aForward;
	}

	public HasClickHandlers getApproveButton(){
		return aApprove;
	}
	
	public HasClickHandlers getRejectButton(){
		return aReject;
	}
	
	public void setValidTaskActions(List<Actions> actions){
		
		if(actions!=null)
		for(Actions action : actions){			
			switch(action){
			case CLAIM:
				break;
			case COMPLETE:
				//target=aComplete;
				UIObject.setVisible(aApprove.getElement(),true);
				UIObject.setVisible(aReject.getElement(),true);
				break;
			case DELEGATE:
				break;
			case FORWARD:
				break;
			case RESUME:
				break;
			case REVOKE:
				break;
			case START:
				break;
			case STOP:
				break;
			case SUSPEND:
				break;
			}
		}
		
	}
	
	@Override
	public void showForward(boolean show) {
		UIObject.setVisible(aForward.getElement(), show);
	}

	@Override
	public void show(boolean IsShowapprovalLink, boolean IsShowRejectLink) {
		UIObject.setVisible(aApprove.getElement(), IsShowapprovalLink);
		UIObject.setVisible(aReject.getElement(), IsShowapprovalLink);
	}
	

	public HasClickHandlers getSimulationBtn(){
		return aSimulate;
	}
	
	
	public HasClickHandlers getEditButton(){
		return aEdit;
	}

	@Override
	public void showEdit(boolean displayed) {
		UIObject.setVisible(aEdit.getElement(), displayed);
	}

	@Override
	public void setStates(List<NodeDetail> states) {
		if(states!=null)
			for(NodeDetail state:states){
				statusContainer.add(new ProcessState(state));
			}
	}

	
	
}
