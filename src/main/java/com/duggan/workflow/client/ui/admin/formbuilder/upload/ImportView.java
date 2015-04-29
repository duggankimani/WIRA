package com.duggan.workflow.client.ui.admin.formbuilder.upload;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class ImportView extends Composite {

	private static FormImportViewUiBinder uiBinder = GWT
			.create(FormImportViewUiBinder.class);

	interface FormImportViewUiBinder extends UiBinder<Widget, ImportView> {
	}

	@UiField SpanElement spnMessage;
	@UiField HTMLPanel panelUpload;
	Uploader uploader;
	
	public ImportView(String htmlMessage, UploadContext context) {
		initWidget(uiBinder.createAndBindUi(this));
		spnMessage.setInnerHTML(htmlMessage);
		uploader = new Uploader(context);
		panelUpload.add(uploader);
		
	}
	
	public void setAvoidRepeatFiles(boolean allow){
		uploader.setAvoidRepeatFiles(allow);
	}

	public void cancelImport() {
		uploader.cancel();
	}
	
	public Uploader getUploader(){
		return uploader;
	}
	
	public void setMessage(String htmlMessage){
		htmlMessage = htmlMessage.replace("\n", "<br/>");
		spnMessage.setInnerHTML(htmlMessage);
	}

}
