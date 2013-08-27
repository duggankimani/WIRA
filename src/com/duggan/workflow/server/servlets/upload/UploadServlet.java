package com.duggan.workflow.server.servlets.upload;

//import java.io.File;
import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.dao.AttachmentDaoHelper;

public class UploadServlet extends UploadAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();

	/**
	 * Override executeAction to save the received files in a custom place and
	 * delete this items from session.
	 */
	@Override
	public String executeAction(HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException {
		
		String result=null;
		try{
			//check session
			
			DB.beginTransaction();
			
			result = execute(request, sessionFiles);
			
			DB.commitTransaction();
		}catch(Exception e){
			DB.rollback();
			e.printStackTrace();			
		}finally{
			DB.closeSession();
		}
		
		return result;
	}
	
	private String execute(HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException{
		
		String response = "";
		
		Enumeration<String> keys= request.getParameterNames();
		
		System.err.println("------------------- Parameters ------------- ");
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			System.err.println(key+""+request.getParameter(key));
		}		
		System.err.println("------------------- END Parameters ------------- ");
		
		int cont = 0;
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				cont++;
				try {					
					String fieldName = item.getFieldName();
					String contentType=item.getContentType();					
					InputStream is = item.getInputStream();
					String name = item.getName();
					long size = item.getSize();
					
					//save(is, documentId, name, contentType,size);
					response += "";
					System.err.format("########### FieldName=%s ; contentType=%s ; IS=%s ; itenName=%s; size=%s",
							fieldName, contentType, is.toString(), name, size+"").println();
					
					
					LocalAttachment attachment  = new LocalAttachment();
					attachment.setCreated(new Date());
					attachment.setArchived(false);
					attachment.setContentType(contentType);
					attachment.setId(null);
					attachment.setName(name);
					attachment.setSize(size);
					attachment.setAttachment(item.get());
					
					saveAttachment(attachment, request);
				} catch (Exception e) {
					throw new UploadActionException(e);
				}
			}else{
				//handle form fields here 
			}
		}

		// / Remove files from session because we have a copy of them
		removeSessionFileItems(request);

		// / Send your customized message to the client. 
		return response;
	}


	private void saveAttachment(LocalAttachment attachment,
			HttpServletRequest request) {
		
		String id = request.getParameter("documentId");
		
		if(id!=null){
			AttachmentDaoHelper.saveDocument(Long.parseLong(id.toString()), attachment);
		}
	}

	/**
	 * Get the content of an uploaded file.
	 */
	@Override
	public void getUploadedFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
//		String fieldName = request.getParameter(UConsts.PARAM_SHOW);
//		File f = receivedFiles.get(fieldName);
//		if (f != null) {
//			response.setContentType(receivedContentTypes.get(fieldName));
//			FileInputStream is = new FileInputStream(f);
//			copyFromInputStreamToOutputStream(is, response.getOutputStream());
//		} else {
//			renderXmlResponse(request, response, XML_ERROR_ITEM_NOT_FOUND);
//		}
	}

	/**
	 * Remove a file when the user sends a delete request.
	 */
	@Override
	public void removeItem(HttpServletRequest request, String fieldName)
			throws UploadActionException {
//		File file = receivedFiles.get(fieldName);
//		receivedFiles.remove(fieldName);
//		receivedContentTypes.remove(fieldName);
//		if (file != null) {
//			file.delete();
//		}
	}
}
