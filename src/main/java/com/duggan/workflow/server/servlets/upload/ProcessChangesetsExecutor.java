package com.duggan.workflow.server.servlets.upload;

import gwtupload.server.exceptions.UploadActionException;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
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
	
	private String saveAttachment(LocalAttachment attachment,
			HttpServletRequest request) {
		
		String id = request.getParameter("processDefId");
		
		String message = null;
		if(id!=null && id.matches("[0-9]+")){
			ProcessDefModel model = DB.getProcessDao().getProcessDef(new Long(id.trim()));
			String name = attachment.getName();
			String xtension = name.substring(name.lastIndexOf('.')+1, name.length()).toLowerCase();
			boolean isImage = xtension.equals("png") || xtension.equals("jpg") || xtension.equals("jpeg") ||
					xtension.equals("gif") || xtension.equals("svg");
					
			if(isImage){
				attachment.setProcessDefImage(model);
			}else{
				attachment.setProcessDef(model);
			}
						
			//Disable existing attachments
			List<LocalAttachment> attachments = 
					DB.getAttachmentDao().getAttachmentsForProcessDef(model,attachment.getName(), isImage);	
			//Manual Delete
			if(attachments!=null){
				for(LocalAttachment att: attachments){
					//delete previous
					DB.getAttachmentDao().delete(att);
				}
			}
			
			DB.getAttachmentDao().save(attachment);
			
			if(!isImage && xtension.endsWith("xml")){
				List<LocalAttachment> list= DB.getAttachmentDao().getAttachmentsForProcessDef(model,attachment.getName(), isImage);
				if(list.size()>1){
					message = "Uploading a guvnor changeset ["+name+"] " +
							"with other resource files for a single process is not supported";
				}
			}
		}else{
			throw new IllegalArgumentException("Cannot save attachment for [ProcessDefId= "+id+"]");
		}
		
		return message;
	}

}
