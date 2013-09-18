package com.duggan.workflow.server.servlets.upload;

import gwtupload.server.exceptions.UploadActionException;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.db.DB;

public class ProcessChangesetsExecutor extends FileExecutor{

	@Override
	public String execute(HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException {
		String errorMessage="";
		
		Hashtable<String, Long> receivedFiles = getSessionFiles(request, true);
		
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {					
					String fieldName = item.getFieldName();
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
		
		String id = request.getParameter("processDefId");
		
		if(id!=null && id.matches("[0-9]+")){
			attachment.setProcessDef(DB.getProcessDao().getProcessDef(new Long(id.trim())));
			DB.getAttachmentDao().saveOrUpdate(attachment);
		}
	}

}
