package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.events.FileLoadEvent;
import com.duggan.workflow.shared.events.FileLoadEvent.FileLoadHandler;
import com.duggan.workflow.shared.events.ReloadAttachmentsEvent;
import com.duggan.workflow.shared.events.ReloadAttachmentsEvent.ReloadAttachmentsHandler;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ImageField extends FieldWidget implements FileLoadHandler, ReloadAttachmentsHandler{

	private final Widget widget;

	private static ImageViewerUiBinder uiBinder = GWT
			.create(ImageViewerUiBinder.class);

	interface ImageViewerUiBinder extends UiBinder<Widget, ImageField> {
	}

	@UiField(provided=true)
	Uploader uploader;
	@UiField
	FocusPanel panelPicture;
	@UiField
	FocusPanel uploadPanel;
	@UiField
	Image img;

	public ImageField() {
		super();
		addProperty(new Property(WIDTH, "Width", DataType.STRING));
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, refId));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
		addProperty(new Property(ACCEPT, "Accept", DataType.STRINGLONG));
		addProperty(new Property(PATH, "File Path", DataType.STRING));

		uploader = new Uploader(true);
		widget = uiBinder.createAndBindUi(this);
		add(widget);
		addRegisteredHandler(FileLoadEvent.TYPE, this);
		addRegisteredHandler(ReloadAttachmentsEvent.TYPE, this);

		panelPicture.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if (!isReadOnly()) {
					uploader.removeStyleName("hide");
				}
			}
		});

		img.addErrorHandler(new ErrorHandler() {

			@Override
			public void onError(ErrorEvent event) {
				img.setUrl("img/blueman.png");
			}
		});

		uploadPanel.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				uploader.addStyleName("hide");
			}
		});
	}

	@Override
	public void setField(Field field) {
		uploader.clear();
		super.setField(field);
		initUploader();
		if(getPropertyValue(WIDTH)!=null){
			img.setWidth(getPropertyValue(WIDTH));
		}
	}

	private void initUploader() {
		UploadContext context = new UploadContext();
		context.setContext("formFieldName", field.getName());
		context.setContext(uploader.FIELD_REFID, field.getRefId() + "");
		context.setContext("documentId", field.getDocId());
		context.setContext("docRefId", field.getDocRefId());
		context.setContext("ACTION", UPLOADACTION.UPLOADDOCFILE.name());
		String accept = getPropertyValue(ACCEPT);
		if (accept != null) {
			context.setAccept(accept);
		}
		uploader.setContext(context);
	}

	@Override
	public FieldWidget cloneWidget() {
		return new ImageField();
	}

	@Override
	protected DataType getType() {
		return DataType.FILEUPLOAD;
	}

	@Override
	protected void setCaption(String caption) {
		// lblEl.setInnerHTML(caption);
	}

	@Override
	protected void setHelp(String help) {
		uploader.setTitle(help);
	}

	@Override
	public void setValue(Object value) {
		super.setValue(value);

		if (value != null) {
			if (!(value instanceof String)) {
				value = value.toString();
			}

			// txtComponent.setValue((String)value);
			// lblReadOnly.setText((String)value);
		} else {
			// txtComponent.setValue(null);
			// lblReadOnly.setText(null);
		}
	}

	@Override
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly || isComponentReadOnly();

		UIObject.setVisible(uploader.getElement(), !this.readOnly);
		// UIObject.setVisible(lblReadOnly.getElement(), this.readOnly);

		// UIObject.setVisible(spnMandatory, (!this.readOnly && isMandatory()));

		// attachmentsPanel.setReadOnly(isReadOnly);
	}

	@Override
	public Widget createComponent(boolean small) {

		if (!readOnly)
			if (small) {
				// txtComponent.setClass("input-medium");
			}
		return img;
	}

	@Override
	public void onFileLoad(FileLoadEvent event) {
		Attachment attachment = event.getAttachment();
		img.setUrl("");

		String docRefId = this.field.getDocRefId();
		String fieldName = this.field.getName();

		if (docRefId == null || fieldName == null) {
			return;
		}

		if (attachment.getFieldName().equals(fieldName)
				&& docRefId.equals(attachment.getDocRefId())) {

			UploadContext context = new UploadContext("getreport");
			context.setContext("attachmentId", attachment.getId()+"");
			context.setContext("ACTION", "GETATTACHMENT");
			final String fullUrl = AppContext.getBaseUrl()+"/"+context.toUrl();
			img.setUrl(fullUrl);
		}
	}

	@Override
	public void onReloadAttachments(ReloadAttachmentsEvent event) {
		uploader.clear();
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

}
