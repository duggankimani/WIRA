package com.duggan.workflow.client.ui.upload;

import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;

import com.duggan.workflow.client.model.UploadContext;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author duggan
 *
 */
public class Uploader extends Composite {

	interface Binder extends UiBinder<Widget, Uploader> {
	}

	Binder binder = GWT.create(Binder.class);

	@UiField
	FlowPanel panelImages;
	@UiField
	HTMLPanel uploaderPanel;

	MultiUploader uploader;
	
	UploadContext context;

	public Uploader() {
		initWidget(binder.createAndBindUi(this));
		uploader = new MultiUploader();		
		uploader.setAutoSubmit(true);
		uploaderPanel.add(uploader);
		uploader.addOnFinishUploadHandler(onFinishHandler);

	}
	
	public void setContext(UploadContext context){
		this.context = context;
		
		StringBuffer servletPath = new StringBuffer();
		String url = context.getUrl() == null ? "upload" : context.getUrl();
		servletPath.append(url + "?" + context.getContextValuesAsURLParams());
		uploader.setServletPath(servletPath.toString());
	}

	IUploader.OnFinishUploaderHandler onFinishHandler = new IUploader.OnFinishUploaderHandler() {

		@Override
		public void onFinish(IUploader uploader) {
			if (uploader.getStatus() == Status.SUCCESS) {

				new PreloadedImage(uploader.fileUrl(), showImage);

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

	OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler() {

		@Override
		public void onLoad(PreloadedImage image) {
			//image.setWidth("75px");
			panelImages.add(image);
		}
	};

}
