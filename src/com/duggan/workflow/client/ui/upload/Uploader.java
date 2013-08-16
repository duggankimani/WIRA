package com.duggan.workflow.client.ui.upload;

import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.SingleUploader;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader.OnStartUploaderHandler;
import gwtupload.client.IUploader.UploadedInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class Uploader extends Composite {

	interface Binder extends UiBinder<Widget, Uploader>{
	}
	
	Binder binder = GWT.create(Binder.class);
	
	@UiField HTMLPanel container;
	
	//MultiUploader uploader;
	SingleUploader uploader;
	private String documentId;
	
	public Uploader(){		
		initWidget(binder.createAndBindUi(this));
		uploader = new SingleUploader();
		uploader.setWidth("100%");
		uploader.setServletPath("/upload");
		uploader.setAvoidRepeatFiles(true);
		uploader.addOnFinishUploadHandler(onFinishHandler);
		container.add(uploader);	
		
		uploader.addOnStartUploadHandler(new OnStartUploaderHandler() {
			
			@Override
			public void onStart(IUploader uploader) {
				uploader.cancel();
			}
		});
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
		uploader.setServletPath("upload?docid="+documentId);
	}
	
	IUploader.OnFinishUploaderHandler onFinishHandler = new IUploader.OnFinishUploaderHandler() {

		@Override
		public void onFinish(IUploader uploader) {
			if (uploader.getStatus() == Status.SUCCESS) {

				//new PreloadedImage(uploader.fileUrl(), showImage);

				// The server sends useful information to the client by default
				UploadedInfo info = uploader.getServerInfo();
				System.out.println("File name " + info.name);
				System.out.println("File content-type " + info.ctype);
				System.out.println("File size " + info.size);

				// You can send any customized message and parse it
				System.out.println("Server message " + info.message);
			}
		}
	};

//	OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler() {
//
//		@Override
//		public void onLoad(PreloadedImage image) {
//			image.setWidth("75px");
//			panelImages.add(image);
//		}
//	};
}
