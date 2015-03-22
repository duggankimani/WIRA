package com.duggan.workflow.client.ui.admin.processes.save;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.events.DeleteAttachmentEvent;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Attachment;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class ProcessAttachment extends Composite {

	private static ProcessAttachmentUiBinder uiBinder = GWT
			.create(ProcessAttachmentUiBinder.class);

	interface ProcessAttachmentUiBinder extends
			UiBinder<Widget, ProcessAttachment> {
	}

	private Attachment attachment;
	@UiField Anchor aDelete;
	@UiField InlineLabel lblFileName;
	@UiField InlineLabel lblCreatedBy;
	@UiField InlineLabel lblCreated;
	
	private ProcessAttachment(){
		initWidget(uiBinder.createAndBindUi(this));
		aDelete.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("Confirm Delete",
						new HTMLPanel("Do you want to delete attachment - "+attachment.getName()),
						new OnOptionSelected() {
							
							@Override
							public void onSelect(String name) {
								if(name.equals("Ok")){
									AppContext.fireEvent(new DeleteAttachmentEvent(attachment));
									ProcessAttachment.this.removeFromParent();
								}
							}
						}, "Ok", "Cancel");
			}
		});
	}
	
	public ProcessAttachment(Attachment attachment) {
		this();
		this.attachment = attachment;
		setValues(attachment);
	}

	private void setValues(Attachment attach) {
		lblFileName.setText(attach.getName());
		lblCreatedBy.setText(attach.getCreatedBy().getFullName());
		lblCreated.setText(DateUtils.DATEFORMAT.format(attach.getCreated()));
	}

}
