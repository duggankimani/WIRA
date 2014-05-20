package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.ui.events.FileLoadEvent;
import com.duggan.workflow.client.ui.events.FileLoadEvent.FileLoadHandler;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class FileUploadField extends FieldWidget implements FileLoadHandler{

	private static UploadFileFieldUiBinder uiBinder = GWT
			.create(UploadFileFieldUiBinder.class);

	interface UploadFileFieldUiBinder extends UiBinder<Widget, FileUploadField> {
	}

	private final Widget widget;
	
	@UiField HTMLPanel uploadContainer;
	
	@UiField Element lblEl;
	@UiField InlineLabel lblReadOnly;
	@UiField HTMLPanel panelControls;
	@UiField SpanElement spnMandatory;
	@UiField HTMLPanel values;
	
	Uploader uploader = null;
	
	public FileUploadField() {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, id));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
		addProperty(new Property(UPLOADERTYPE, "Type", DataType.SELECTBASIC, 
				new KeyValuePair("singleupload", "Single Upload"),
				new KeyValuePair("multiupload", "Multi Upload")));
		addProperty(new Property(ACCEPT, "Accept", DataType.STRINGLONG));
		
		widget = uiBinder.createAndBindUi(this);
		add(widget);
		addRegisteredHandler(FileLoadEvent.TYPE, this);
		
	}

	@Override
	public void setField(Field field) {
		String uploaderType = getPropertyValue(UPLOADERTYPE);
		if(uploaderType==null){
			uploaderType="multiupload";
		}
		
		uploadContainer.clear();
		if(uploaderType.equals("singleupload")){			
			uploader = new Uploader(true);
			uploadContainer.add(uploader);
		}else{
			uploader = new Uploader();
			uploadContainer.add(uploader);
		}
		
		super.setField(field);
		initUploader();
	}
	
	
	private void initUploader() {
		UploadContext context = new UploadContext();
		context.setContext("formFieldName", field.getName());
		context.setContext("documentId", field.getDocId());
		context.setContext("ACTION", UPLOADACTION.UPLOADDOCFILE.name());
		String accept = getPropertyValue(ACCEPT);
		if(accept!=null)
			context.setAccept(accept);
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
	}

	@Override
	public Widget getComponent(boolean small) {
				
		if(!readOnly)
		if(small){
			//txtComponent.setClass("input-medium");
		}
		return panelControls;
	}

	@Override
	public void onFileLoad(FileLoadEvent event) {
		Attachment attachment = event.getAttachment();
		String fieldName = attachment.getFieldName();
		System.err.println("######## OnFileLoad FieldName>> "+
		fieldName+" >> "+attachment.getName()
		+" DocId = "+field.getDocId()+"; ATTDOCID="+attachment.getDocumentid());
		
		if(this.field.getName().equals(fieldName) && 
				this.field.getDocId().equals(attachment.getDocumentid().toString())){
			System.err.println("##################RENDERING ATTACHMENT...........");
			render(attachment);
		}
	}

	private void render(Attachment attachment) {
		//lblReadOnly.setText(attachment.getName());
		values.add(new InlineLabel(attachment.getName()));
	}
}
