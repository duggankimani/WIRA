package com.duggan.workflow.server.servlets.upload;

import gwtupload.server.exceptions.UploadActionException;

import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;

import com.duggan.workflow.server.dao.helper.AttachmentDaoHelper;

public abstract class FileExecutor {

	abstract String execute(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException;

	protected Hashtable<String, Long> getSessionFiles(HttpServletRequest request, boolean createNewIfNone){

		//fieldName - DocNo
		Hashtable<String, Long> receivedFiles = new Hashtable<String, Long>();
		HttpSession session = request.getSession(false);
		if(session!=null){
			if(session.getAttribute("RECEIVEDFILES")!=null){
				receivedFiles = (Hashtable<String, Long>)session.getAttribute("RECEIVEDFILES");
			}else{
				session.setAttribute("RECEIVEDFILES", receivedFiles);
			}
		}
		
		return receivedFiles;
	}
	
	void registerFile(HttpServletRequest req, String fieldName, Long id){
		getSessionFiles(req, true).put(fieldName, id);
	}

	public void removeItem(HttpServletRequest request, String fieldName) {
		Hashtable<String, Long> receivedFiles = getSessionFiles(request, false);
		Long attachmentId = receivedFiles.get(fieldName);
		
		if(attachmentId!=null){
			//System.err.println("#################LOG DElete "+attachmentId);
			AttachmentDaoHelper.delete(attachmentId);
		}
	}


}
