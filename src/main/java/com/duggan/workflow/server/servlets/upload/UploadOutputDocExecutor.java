package com.duggan.workflow.server.servlets.upload;

import gwtupload.server.exceptions.UploadActionException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.duggan.workflow.server.dao.AttachmentDaoImpl;
import com.duggan.workflow.server.dao.model.ADOutputDoc;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.db.DB;

public class UploadOutputDocExecutor extends FileExecutor {

	@Override
	String execute(HttpServletRequest request, List<FileItem> sessionFiles)
			throws UploadActionException {
		String message = null;
		String docName=request.getParameter("name");
		String code=request.getParameter("code");
		String description = request.getParameter("description");
		Long id = request.getParameter("id")==null? null : new Long(request.getParameter("id"));
		
		ADOutputDoc document= new ADOutputDoc();
		if(id!=null){
			document = DB.getOutputDocDao().getOuputDocument(id);
		}
		
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {	
					String fieldName = item.getFieldName();
					String contentType=item.getContentType();					
					String name = item.getName();
					long size = item.getSize();
					if(docName==null || docName.isEmpty()){
						docName=name;
					}
					
					if(description==null || description.isEmpty()){
						description = name;
					}
					
					//Output Doc first
					document.setName(docName);
					document.setCode(code);
					document.setDescription(description);
					DB.getOutputDocDao().save(document);
					
					LocalAttachment attachment  = new LocalAttachment();
					if(document.getAttachment()!=null){
						attachment = document.getAttachment();
					}
					attachment.setOutputDoc(document);
					
					attachment.setContentType(contentType);					
					attachment.setName(name);
					attachment.setSize(size);
					attachment.setDirectory(false);
					attachment.setAttachment(item.get());					
					AttachmentDaoImpl impl = DB.getAttachmentDao();
					impl.save(attachment);
					
					registerFile(request, fieldName, attachment.getId());
					
					message=document.getId()+","+docName+","+description+","+code+","+attachment.getId()+","+attachment.getName();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		return message;
	}

}
