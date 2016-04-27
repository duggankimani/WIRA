package com.duggan.workflow.server.servlets.upload;

import gwtupload.server.exceptions.UploadActionException;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import com.duggan.workflow.server.dao.helper.AttachmentDaoHelper;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.AttachmentType;

public class DocumentAttachmentExecutor extends FileExecutor{

	Logger log = Logger.getLogger(DocumentAttachmentExecutor.class);
	
	public String execute(HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException {
		String errorMessage="";
		
		Hashtable<String, Long> receivedFiles = getSessionFiles(request, true);
		
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {					
					String fieldName = item.getFieldName();
					
					//Name of the form field against which this file was uploaded 
					String formFieldName= request.getParameter("formFieldName");
					String fieldIdStr= request.getParameter("fieldId");
					String contentType=item.getContentType();					
					String name = item.getName();
					long size = item.getSize();
					
					LocalAttachment attachment  = new LocalAttachment();
					attachment.setCreated(new Date());
					attachment.setArchived(false);
					attachment.setContentType(contentType);
					attachment.setId(null);
					attachment.setName(name);
					attachment.setSize(size);
					attachment.setAttachment(item.get());
					attachment.setType(AttachmentType.INPUT);
					attachment.setFieldName(formFieldName);
					
					saveAttachment(attachment, request);
					receivedFiles.put(fieldName, attachment.getId());
					
				} catch (Exception e) {
					throw new UploadActionException(e);
				}
			}else{
				//handle form fields here 
			}
		}

		
		return errorMessage;
	}
	
	private void saveAttachment(LocalAttachment attachment,
			HttpServletRequest request) {
		
		//String id = request.getParameter("documentId");
		String docRefId = request.getParameter("docRefId");
		
		if(docRefId!=null){
			AttachmentDaoHelper.saveDocument(docRefId, attachment);
		}
	}

	public Long getAttachmentId(String fileFieldName) {
		Hashtable<String, Long> receivedFiles = getSessionFiles(SessionHelper.getHttpRequest(), false);
		
		return receivedFiles.get(fileFieldName);
	}

}
