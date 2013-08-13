package com.duggan.workflow.server.servlets.upload;

//import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;

import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.server.helper.error.ErrorLogDaoHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.Document;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.shared.UConsts;

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
		String response = "";
		String docId = request.getParameter("docid");
		Long documentId = null;		
		if(docId!=null && docId.matches("[0-9]+")){
			documentId = Long.parseLong(docId);
		}
		
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
				} catch (Exception e) {
					throw new UploadActionException(e);
				}
			}
		}

		// / Remove files from session because we have a copy of them
		removeSessionFileItems(request);

		// / Send your customized message to the client.
		return response;
	}


	/**
	 * 
	 * @param is
	 * @param documentId
	 * @param name
	 * @param contentType
	 * @param size
	 *
	 */
	private void save(InputStream is, Long documentId, String name,
			String contentType, long size) {
		LocalAttachment attachment = new LocalAttachment();
		attachment.setCreated(new Date());
		attachment.setCreatedBy(SessionHelper.getCurrentUser().getId());
		attachment.setArchived(false);
		attachment.setName(name);
		attachment.setSize(size);
		attachment.setContentType(contentType);
		
		try{
			DB.beginTransaction();
			
			DocumentModel document = DB.getDocumentDao().getById(documentId);
			attachment.setDocument(document);			
			attachment.setAttachment(IsToBytes(is));
			
			if(document==null){
				throw new IllegalStateException("Error: DocumentId must be specified for an attachment");
			}
			DB.getAttachmentDao().saveOrUpdate(attachment);
			
			DB.commitTransaction();
		}catch(Exception e){
			DB.rollback();
			e.printStackTrace();			
			//ErrorLogDaoHelper.saveLog(e, "Upload File");
		}finally{
			DB.closeSession();
		}
		
		
	}


	private byte[] IsToBytes(InputStream is) {

		byte[] bites = null;
		try{
			bites = IOUtils.toByteArray(is);
			IOUtils.closeQuietly(is);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return bites;
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
