package com.duggan.workflow.client.ui.view;

import java.util.Date;
import java.util.List;

import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocType;
import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
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
	@UiField
	DivElement divValue;
	@UiField
	DivElement divPartner;
	
	@UiField Button btnApprove;
	
	@UiField Button btnReject;

	@UiField Anchor aForward;
	
	@Inject
	public GenericDocumentView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		UIObject.setVisible(divValue, false);
		UIObject.setVisible(divPartner, false);
		UIObject.setVisible(aForward.getElement(), false);
		UIObject.setVisible(btnApprove.getElement(), false);
		UIObject.setVisible(btnReject.getElement(), false);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public void setValues(Date created, DocType type, String subject,
			Date docDate, String value, String partner, String description, Integer priority,DocStatus status) {
		if (created != null)
			spnCreated.setInnerText(CREATEDFORMAT.format(created));

		if (type != null)
			spnDocType.setInnerText(type.getDisplayName());

		if (subject != null)
			spnSubject.setInnerText(subject);

		if (docDate != null)
			spnDocDate.setInnerText("Dated "+DATEFORMAT.format(docDate));

		if(value!=null){
			spnValue.setInnerText(value);
			UIObject.setVisible(divValue, true);
		}
		
		if (partner != null){
			UIObject.setVisible(divPartner, true);
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
		return btnApprove;
	}
	
	public HasClickHandlers getRejectButton(){
		return btnReject;
	}
	
	public void setValidTaskActions(List<Actions> actions){
		
		if(actions!=null)
		for(Actions action : actions){			
			switch(action){
			case CLAIM:
				break;
			case COMPLETE:
				//target=aComplete;
				UIObject.setVisible(btnApprove.getElement(),true);
				UIObject.setVisible(btnReject.getElement(),true);
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
		UIObject.setVisible(btnApprove.getElement(), IsShowapprovalLink);
		UIObject.setVisible(btnReject.getElement(), IsShowapprovalLink);
	}
	
	
}
