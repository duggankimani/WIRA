package com.duggan.workflow.client.ui.admin.addprocess;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.ProcessDef;
import com.gwtplatform.mvp.client.PopupViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

public class AddProcessView extends PopupViewImpl implements
		AddProcessPresenter.MyView {

	private final Widget widget;
	@UiField IssuesPanel issues;
	
	@UiField Anchor aNext;
	@UiField Anchor aClose;
	@UiField Anchor aBack;
	@UiField Anchor aFinish;
	@UiField HTMLPanel divProcessDetails;
	@UiField HTMLPanel divUploadDetails;
	@UiField TextField txtName;
	@UiField TextField txtProcess;
	@UiField CheckBox chkLPO;
	@UiField CheckBox chkInvoice;
	@UiField CheckBox chkContract;
	@UiField Uploader uploader;
	

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
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public List<DocType> getTypes(){
		
		List<DocType> types = new ArrayList<DocType>();
		if(chkLPO.getValue()){
			types.add(DocType.LPO);
		}
		
		if(chkInvoice.getValue()){
			types.add(DocType.INVOICE);
		}
		
		if(chkContract.getValue()){
			types.add(DocType.CONTRACT);
		}
		
		return types;
	}
	
	public ProcessDef getProcess(){
		ProcessDef def = new ProcessDef();
		def.setDocTypes(getTypes());
		def.setName(txtName.getValue());
		def.setProcessId(txtProcess.getValue());		
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
		context.setAccept("xml");
		uploader.setContext(context);
	}

	@Override
	public void enable(boolean enableFinish, boolean enableCancel) {
		aFinish.setEnabled(enableFinish);
		aClose.setEnabled(enableCancel);	
	}
}
