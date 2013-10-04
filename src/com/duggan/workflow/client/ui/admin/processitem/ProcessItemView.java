package com.duggan.workflow.client.ui.admin.processitem;

import java.util.Date;
import java.util.List;

import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.ProcessDefStatus;
import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ProcessItemView extends ViewImpl implements
		ProcessItemPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ProcessItemView> {
	}
	
	@UiField SpanElement spnName;
	@UiField SpanElement spnDocTypes;
	@UiField SpanElement spnFileName;
	@UiField SpanElement spnProcessId;
	@UiField SpanElement spnStatus;
	@UiField SpanElement spnLastModified;
	
	@UiField Anchor aActivate;
	@UiField Anchor aDeactivate;
	@UiField Anchor aRefresh;
	@UiField Anchor aEdit;
	@UiField Anchor aDelete;

	@Inject
	public ProcessItemView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public HasClickHandlers getActivateButton(){
		return aActivate;
	}
	
	public HasClickHandlers getDeactivateButton(){
		return aDeactivate;
	}
	
	public HasClickHandlers getRefreshButton(){
		return aRefresh;
	}
	
	public HasClickHandlers getEditButton(){
		return aEdit;
	}
	
	public HasClickHandlers getDeleteButton(){
		return aDelete;
	}

	@Override
	public void setValues(String name, String processId,String description,
			List<DocumentType> docTypes, Date lastModified, Long fileId,
			String fileName, ProcessDefStatus status) {
		
		spnName.setInnerText(name);
		spnName.setTitle(description);
		spnProcessId.setInnerText(processId);
		
		if(docTypes!=null && !docTypes.isEmpty()){
			StringBuffer docs = new StringBuffer();
			for(DocumentType type: docTypes){
				docs.append(type.getName()+",");
			}			
			
			spnDocTypes.setInnerText(docs.substring(0, docs.length()-1));
		}
		
		if(fileName!=null)
			spnFileName.setInnerText(fileName);
		
		if(lastModified!=null)
			spnLastModified.setInnerText(DateUtils.CREATEDFORMAT.format(lastModified));
		
		spnStatus.setInnerText(status.name());
		
		switch (status) {
		case INACTIVE:
			aActivate.removeStyleName("hide");
			aDeactivate.addStyleName("hide");
			spnStatus.setClassName("label label-warning arrowed-in");
			break;
			
		case RUNNING:
			aActivate.addStyleName("hide");
			aDeactivate.removeStyleName("hide");
			spnStatus.setClassName("label label-success arrowed-in");
			break;
		}
		
	}
}
