package com.duggan.workflow.client.ui.admin.formbuilder.upload;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class FormImportView extends Composite {

	private static FormImportViewUiBinder uiBinder = GWT
			.create(FormImportViewUiBinder.class);

	interface FormImportViewUiBinder extends UiBinder<Widget, FormImportView> {
	}

	@UiField HTMLPanel panelUpload;
	Uploader uploader;
	
	public FormImportView() {
		initWidget(uiBinder.createAndBindUi(this));
		
		UploadContext context = new UploadContext();
		context.setAction(UPLOADACTION.IMPORTFORM);
		context.setAccept("xml,json");
		
		uploader = new Uploader(context);
		panelUpload.add(uploader);
		
	}
	
	public void setAvoidRepeatFiles(boolean allow){
		uploader.setAvoidRepeatFiles(allow);
	}

	public void cancelImport() {
		uploader.cancel();
	}

}
