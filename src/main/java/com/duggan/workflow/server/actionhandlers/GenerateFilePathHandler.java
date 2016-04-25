package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.client.ui.admin.formbuilder.HasProperties;
import com.duggan.workflow.server.dao.helper.OutputDocumentDaoHelper;
import com.duggan.workflow.server.dao.model.ADField;
import com.duggan.workflow.server.dao.model.ADProperty;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.servlets.upload.DocumentAttachmentExecutor;
import com.duggan.workflow.shared.requests.GenerateFilePathRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GenerateFilePathResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GenerateFilePathHandler
		extends
		AbstractActionHandler<GenerateFilePathRequest, GenerateFilePathResponse> {

	@Inject
	public GenerateFilePathHandler() {
	}

	@Override
	public void execute(GenerateFilePathRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {

		GenerateFilePathResponse aResp = (GenerateFilePathResponse) actionResult;
		String error = null;
		String path = null;
		if (action.getFieldId() != null) {
			ADField field = DB.getFormDao().getById(ADField.class,
					action.getFieldId());
			ADProperty property = field.getProperty(HasProperties.PATH);
			if (property != null && property.getValue() != null
					&& property.getValue().getStringValue() != null
					&& !property.getValue().getStringValue().trim().isEmpty()) {
				path = OutputDocumentDaoHelper.generatePath(property.getValue()
						.getStringValue(), action.getDoc());
			} else {
				log.warn(error = "Attachment Failed: Could not find file path for Upload Field '"
						+ field.getCaption()
						+ "'. Please inform your Administrator");
			}
		} else {
			log.warn(error = "Attachment Failed: Could not find file path for Upload Field ID='"
					+ action.getFieldId()
					+ "'. Please inform your Administrator");
		}

		LocalAttachment parent = OutputDocumentDaoHelper.generateFolders(path+"/placeholder.pdf");

		DocumentAttachmentExecutor executor = new DocumentAttachmentExecutor();
		for (String fileFieldName : action.getFileFieldNames()) {
			Long attachmentId = executor.getAttachmentId(fileFieldName);
			LocalAttachment attachment = DB.getAttachmentDao()
					.getAttachmentById(attachmentId);
			attachment.setParent(parent);
			DB.getAttachmentDao().save(attachment);
		}

		aResp.setDoc(action.getDoc());
	}

	@Override
	public Class<GenerateFilePathRequest> getActionType() {
		return GenerateFilePathRequest.class;
	}
}
