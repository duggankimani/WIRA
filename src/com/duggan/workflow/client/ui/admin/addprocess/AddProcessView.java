package com.duggan.workflow.client.ui.admin.addprocess;

import java.util.List;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.admin.component.ListField;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.ProcessDef;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupViewImpl;

public class AddProcessView extends PopupViewImpl implements
		AddProcessPresenter.MyView {

	private final Widget widget;
	@UiField IssuesPanel issues;
	@UiField PopupPanel divPopUp;
	@UiField Anchor aNext;
	@UiField Anchor aClose;
	@UiField Anchor aBack;
	@UiField Anchor aFinish;
	@UiField HTMLPanel divProcessDetails;
	@UiField HTMLPanel divUploadDetails;
	@UiField TextField txtName;
	@UiField TextField txtProcess;
	@UiField Uploader uploader;
	@UiField TextArea txtDescription;
	@UiField ListField<DocumentType> lstDocTypes;

	public interface Binder extends UiBinder<Widget, AddProcessView> {
	}

	@Inject
	public AddProcessView(final EventBus eventBus, final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
		
		aNext.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(isValid()){
					divProcessDetails.addStyleName("hide");
					divUploadDetails.removeStyleName("hide");
				}
			}
		});
		
		aClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				uploader.cancel();
			}
		});
		
		aBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				divProcessDetails.removeStyleName("hide");
				divUploadDetails.addStyleName("hide");
			}
		});
		
		aFinish.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		
		int[] position = AppManager.calculatePosition(5, 50);
		divPopUp.setPopupPosition(position[1], position[0]);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setValues(Long processDefId,
			String name,String processId,String description, List<DocumentType> docTypes) {
		txtName.setValue(name);
		txtProcess.setValue(processId);
		setProcessId(processDefId);
		if(description!=null){
			txtDescription.setValue(description);
		}
		
		if(docTypes!=null){
			lstDocTypes.select(docTypes);
		}
	}

	public ProcessDef getProcess(){
		ProcessDef def = new ProcessDef();
		def.setDocTypes(lstDocTypes.getSelectedItems());
		def.setName(txtName.getValue());
		def.setProcessId(txtProcess.getValue());		
		def.setDescription(txtDescription.getValue());
		
		return def;
	}
	
	public boolean isValid(){
		issues.clear();
		boolean isValid = true;
		
		if(isNullOrEmpty(txtName.getValue())){
			issues.addError("Please specify process name");
			isValid=false;
		}
		
		if(isNullOrEmpty(txtProcess.getValue())){
			issues.addError("Please specify Process Id");
			isValid=false;
		}
		
		return isValid;
	}
	
	public HasClickHandlers getNext(){
		return aNext;
	}
	
	public HasClickHandlers getFinish(){
		return aFinish;
	}
	
	public HasClickHandlers getCancel(){
		return aClose;
	}
	
	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	/**
	 * Process Def  Id
	 */
	@Override
	public void setProcessId(Long id) {
		UploadContext context = new UploadContext();
		context.setAction(UPLOADACTION.UPLOADCHANGESET);
		context.setContext("processDefId", id+"");
		context.setAccept("xml,png");
		uploader.setContext(context);
	}

	@Override
	public void enable(boolean enableFinish, boolean enableCancel) {
		aFinish.setEnabled(enableFinish);
		aClose.setEnabled(enableCancel);	
	}

	@Override
	public void setDocumentTypes(List<DocumentType> documentTypes) {
		lstDocTypes.addItems(documentTypes);
	}

}
