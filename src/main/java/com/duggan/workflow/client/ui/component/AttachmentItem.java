package com.duggan.workflow.client.ui.component;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.events.DeleteAttachmentEvent;
import com.duggan.workflow.client.ui.events.ReloadAttachmentsEvent;
import com.duggan.workflow.client.ui.images.ImageResources;
import com.duggan.workflow.client.ui.upload.attachment.ShowAttachmentEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Attachment;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class AttachmentItem extends Composite {

	private static AttachmentItemUiBinder uiBinder = GWT
			.create(AttachmentItemUiBinder.class);

	interface AttachmentItemUiBinder extends UiBinder<Widget, AttachmentItem> {
	}
	
	@UiField InlineLabel lblFileName;
	@UiField Anchor aRemove;
	//@UiField Anchor view;
	@UiField Anchor aDownload;
	@UiField(provided=true) Image img;
	private Attachment attachment=null;

	public AttachmentItem(final Attachment attachment) {
		this(attachment,false,true);
	}
	
	public AttachmentItem(final Attachment attachment, boolean isReadOnly, boolean isShowDownloadLink) {
		this.attachment = attachment;
		String name=attachment.getName();
		if(name.endsWith(".pdf")){
			img = new Image(ImageResources.IMAGES.pdf());
		}else if(name.endsWith(".docx") || name.endsWith(".doc")){
			img = new Image(ImageResources.IMAGES.doc());
		}else if(name.endsWith(".odt")){
			img = new Image(ImageResources.IMAGES.odt());
		}else if(name.endsWith(".ods")){
			img = new Image(ImageResources.IMAGES.ods());
		}else if(name.endsWith(".csv")){
			img = new Image(ImageResources.IMAGES.csv());
		}
		else if(name.endsWith(".xls") || name.endsWith(".xlsx")){
			img = new Image(ImageResources.IMAGES.xls());
		}
		else if(name.endsWith(".png") || name.endsWith(".jpg") 
				|| name.endsWith(".jpeg") ||name.endsWith(".gif")){
			img = new Image(ImageResources.IMAGES.img());
		}else if(name.endsWith(".txt") ||name.endsWith(".text")){
			img = new Image(ImageResources.IMAGES.txt());
		}
		else{
			img = new Image(ImageResources.IMAGES.file());
		}
		img.getElement().getStyle().setFloat(Float.LEFT);
		img.getElement().getStyle().setMarginRight(3, Unit.PX);
		img.getElement().getStyle().setMarginTop(2, Unit.PX);
		img.addStyleName("hand");
		
		//Init widget
		initWidget(uiBinder.createAndBindUi(this));
		lblFileName.setText(name);
		lblFileName.setTitle(name);
		
		setReadOnly(isReadOnly);
		
		aRemove.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("Config Delete", "Are you sure you want to delete '"
			+attachment.getName()+"'",
						new OnOptionSelected() {
							
							@Override
							public void onSelect(String name) {
								if(name.equals("Yes")){
									AppContext.fireEvent(new DeleteAttachmentEvent(attachment));
									AttachmentItem.this.removeFromParent();
								}
							}
						}, "Yes", "Cancel");
			}
		});
		
		aDownload.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				download();
			}
		});
		
		img.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				download();
			}
		});
		
		showDownloadLink(isShowDownloadLink);
	}
	
	protected void download() {
		//Download
		UploadContext context = new UploadContext("getreport");
		context.setContext("attachmentId", attachment.getId()+"");
		context.setContext("ACTION", "GETATTACHMENT");
		final String fullUrl = AppContext.getBaseUrl()+"/"+context.toUrl();
		AppContext.fireEvent(
				new ShowAttachmentEvent(fullUrl, attachment.getName()));
	}

	public void showDownloadLink(boolean isShowDownloadLink){
		if(!isShowDownloadLink){
			aDownload.addStyleName("hide");
		}
	}
	
	public void setReadOnly(boolean isReadOnly){
		if(isReadOnly){
			aRemove.addStyleName("hidden");
			//container.addStyleName("readonly");
		}else{
			aRemove.removeStyleName("hidden");
			//container.removeStyleName("readonly");
		}
	}

}
