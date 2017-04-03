package com.duggan.workflow.client.ui.admin.formbuilder.component;

import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnCancelUploaderHandler;
import gwtupload.client.IUploader.OnFinishUploaderHandler;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.ui.component.AttachmentsPanel;
import com.duggan.workflow.client.ui.component.ImageCarousel;
import com.duggan.workflow.client.ui.events.FileLoadEvent;
import com.duggan.workflow.client.ui.events.FileLoadEvent.FileLoadHandler;
import com.duggan.workflow.client.ui.events.ReloadAttachmentsEvent;
import com.duggan.workflow.client.ui.events.ReloadAttachmentsEvent.ReloadAttachmentsHandler;
import com.duggan.workflow.client.ui.images.ImageResources;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class FileUploadField extends FieldWidget implements FileLoadHandler, ReloadAttachmentsHandler{

	private static UploadFileFieldUiBinder uiBinder = GWT
			.create(UploadFileFieldUiBinder.class);

	interface UploadFileFieldUiBinder extends UiBinder<Widget, FileUploadField> {
	}

	private final Widget widget;
	
	@UiField HTMLPanel uploadContainer;
	@UiField HTMLPanel panelGroup;
	
	@UiField Element lblEl;
	@UiField InlineLabel lblReadOnly; 
	@UiField HTMLPanel panelControls;
	@UiField SpanElement spnMandatory;

	@UiField AttachmentsPanel attachmentsPanel;
	@UiField ImageCarousel imgCarousel;
	@UiField Image imgSingleUpload;
		
	Uploader uploader = null;
	
	public FileUploadField() {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, refId));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
		addProperty(new Property(UPLOADERTYPE, "Type", DataType.SELECTBASIC, 
				new KeyValuePair("fileuploader", "File Upload"),
				new KeyValuePair("imageuploader", "Image Upload")));
		addProperty(new Property(UPLOADER, "Uploader", DataType.SELECTBASIC, 
				new KeyValuePair("singleupload", "Single Upload"),
				new KeyValuePair("multiupload", "Multi Upload")));
		addProperty(new Property(ACCEPT, "Accept", DataType.STRINGLONG));
		addProperty(new Property(PATH, "File Path", DataType.STRING));
		addProperty(new Property(CUSTOMTRIGGER, "Trigger Class",
				DataType.STRING));
		
		widget = uiBinder.createAndBindUi(this);
		add(widget);
		addRegisteredHandler(FileLoadEvent.TYPE, this);
		addRegisteredHandler(ReloadAttachmentsEvent.TYPE, this);
		imgSingleUpload.setResource(ImageResources.IMAGES.img());
		imgCarousel.registerImage(imgSingleUpload);
	}

	@Override
	public void setField(Field field) {
		
		String uploaderCat = field.getPropertyValue(UPLOADER);
		if(uploaderCat==null){
			uploaderCat="multiupload";
		}
		
		uploadContainer.clear();
		if(uploaderCat.equals("singleupload")){			
			uploader = new Uploader(true);
			uploadContainer.add(uploader);
		}else{
			uploader = new Uploader();
			uploadContainer.add(uploader);
		}
		uploader.addOnFinishUploaderHandler(new OnFinishUploaderHandler() {
			
			@Override
			public void onFinish(IUploader uploader) {
				String UploadedFileId = uploader.getServerInfo().getField();
				getField().setUploadedFileId(UploadedFileId);
				
				String triggerName = getPropertyValue(CUSTOMTRIGGER);
				boolean hasTrigger = triggerName != null && !triggerName.isEmpty();
				if(hasTrigger){
					execTrigger();
				}else{
					AppContext.fireEvent(new ReloadAttachmentsEvent());
				}
			}
		});
		
		uploader.addOnCancelUploaderHandler(new OnCancelUploaderHandler() {
			
			@Override
			public void onCancel(IUploader uploader) {
				AppContext.fireEvent(new ReloadAttachmentsEvent());
			}
		});
		
		
		String uploaderType = field.getPropertyValue(UPLOADERTYPE);
		if(uploaderType==null){
			uploaderType="fileuploader";
		}
		
		imgCarousel.clear();
		imgCarousel.addStyleName("hide");
		imgSingleUpload.addStyleName("hide");
		attachmentsPanel.removeStyleName("hide");
		if(uploaderType.equals("imageuploader")){
			if(uploaderCat.equals("singleupload")){
				imgSingleUpload.removeStyleName("hide");	
				attachmentsPanel.addStyleName("hide");
			}else{
				imgCarousel.removeStyleName("hide");
			}
		}
		
		super.setField(field);
		
		initUploader();
	}
	
	
	private void initUploader() {
		UploadContext context = new UploadContext();
		context.setContext("formFieldName", field.getName());
		context.setContext(uploader.FIELD_REFID, field.getRefId()+"");
		context.setContext("documentId", field.getDocId());
		context.setContext("docRefId", field.getDocRefId());
		context.setContext("ACTION", UPLOADACTION.UPLOADDOCFILE.name());
		String accept = getPropertyValue(ACCEPT);
		if(accept!=null){
			context.setAccept(accept);
		}
		uploader.setContext(context);
	}

	@Override
	public FieldWidget cloneWidget() {
		return new FileUploadField();
	}

	@Override
	protected DataType getType() {
		return DataType.FILEUPLOAD;
	}	

	@Override
	protected void setCaption(String caption) {
		lblEl.setInnerHTML(caption);
	}
	
	@Override
	protected void setHelp(String help) {
		uploader.setTitle(help);
	}
	
	@Override
	public void setValue(Object value) {
		super.setValue(value);
		
		if(value!=null){
			if(!(value instanceof String)){
				value = value.toString();
			}
				
			//txtComponent.setValue((String)value);
			lblReadOnly.setText((String)value);
		}else{
			//txtComponent.setValue(null);
			lblReadOnly.setText(null);
		}
	}
	
	@Override
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly || isComponentReadOnly();
		
		UIObject.setVisible(uploader.getElement(),!this.readOnly);
		UIObject.setVisible(lblReadOnly.getElement(), this.readOnly);
		
		UIObject.setVisible(spnMandatory, (!this.readOnly && isMandatory()));
		
		attachmentsPanel.setReadOnly(isReadOnly);
	}

	@Override
	public Widget createComponent(boolean small) {
				
		if(!readOnly)
		if(small){
			//txtComponent.setClass("input-medium");
		}
		return panelControls;
	}

	@Override
	public void onFileLoad(FileLoadEvent event) {
		Attachment attachment = event.getAttachment();
		
		String docRefId = this.field.getDocRefId();
		String fieldName = this.field.getName();
		
		if(docRefId==null || fieldName==null){
			return;
		}
		
		imgSingleUpload.setUrl("");
		if(attachment.getFieldName().equals(fieldName) && 
				docRefId.equals(attachment.getDocRefId())){
			imgCarousel.addAttachment(attachment);
			attachmentsPanel.addAttachment(attachment);
			imgSingleUpload.setUrl(ImageCarousel.getUrl(attachment));
		}
	}

	@Override
	public void onReloadAttachments(ReloadAttachmentsEvent event) {
		if(uploader!=null){
			uploader.clear();
		}
		imgCarousel.clear();
		attachmentsPanel.clear();
	}

	@Override
	public Widget getInputComponent() {
		return uploader;
	}

	@Override
	public Element getViewElement() {
		return null;
	}

	@Override
	public void setComponentValid(boolean isValid) {
		
	}

	@Override
	public void gridFormat(boolean isGridField) {
		super.gridFormat(isGridField);
		lblEl.addClassName("hide");
		lblEl.removeClassName("control-label");
		panelGroup.removeStyleName("control-group");
		panelControls.removeStyleName("controls");
	}
}
