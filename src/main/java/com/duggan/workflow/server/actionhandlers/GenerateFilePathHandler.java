package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.client.ui.admin.formbuilder.HasProperties;
import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.server.dao.helper.OutputDocumentDaoHelper;
import com.duggan.workflow.server.dao.model.ADField;
import com.duggan.workflow.server.dao.model.ADProperty;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.servlets.upload.DocumentAttachmentExecutor;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.requests.GenerateFilePathRequest;
import com.duggan.workflow.shared.responses.GenerateFilePathResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

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
		if (action.getFieldRefId() != null) {
			Field field = FormDaoHelper.getFieldJson(action.getFieldRefId());
			String pathTemplate = field.getProperty(HasProperties.PATH);
			if (pathTemplate!=null) {
				path = OutputDocumentDaoHelper.generatePath(pathTemplate, action.getDoc());
			} else {
				log.warn(error = "Attachment Failed: Could not find file path for Upload Field '"
						+ field.getCaption()
						+ "'. Please inform your Administrator");
			}
		} else {
			log.warn(error = "Attachment Failed: Could not find file path for Upload Field ID='"
					+ action.getFieldRefId()
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
