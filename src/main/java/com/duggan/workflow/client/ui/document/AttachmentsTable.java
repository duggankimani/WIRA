package com.duggan.workflow.client.ui.document;

import java.util.ArrayList;

import com.duggan.workflow.client.ui.component.TableView;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.shared.model.Attachment;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.commons.client.util.ArrayUtil;

public class AttachmentsTable extends Composite {

	private static AttachmentsTableUiBinder uiBinder = GWT
			.create(AttachmentsTableUiBinder.class);

	interface AttachmentsTableUiBinder extends
			UiBinder<Widget, AttachmentsTable> {
	}

	@UiField TableView attachmentsTable;
	
	public AttachmentsTable() {
		initWidget(uiBinder.createAndBindUi(this));
		attachmentsTable.setHeaders(ArrayUtil.asList("File Name","Type","Created By","Date Created"));
	}
	
	public void setAttachments(ArrayList<Attachment> attachments){
		clear();
		if(attachments!=null){
			for(Attachment attachment: attachments){
				attachmentsTable.addRow(new InlineLabel(attachment.getName()),
						new InlineLabel(attachment.getDocumentType()),
						new InlineLabel(attachment.getCreatedBy().getFullName()),
						new InlineLabel(DateUtils.CREATEDFORMAT.format(attachment.getCreated())));
			}
		}
	}

	private void clear() {
		attachmentsTable.clearRows();
	}

}
