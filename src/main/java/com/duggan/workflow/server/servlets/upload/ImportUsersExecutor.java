package com.duggan.workflow.server.servlets.upload;

import gwtupload.server.exceptions.UploadActionException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.server.helper.auth.UserDaoHelper;

public class ImportUsersExecutor extends FileExecutor {

	Logger log = Logger.getLogger(ImportUsersExecutor.class);
	
	@Override
	String execute(HttpServletRequest request, List<FileItem> sessionFiles)
			throws UploadActionException {
		
		String message = null;
		
		//Clear import log
		ImportLogger.clear();
		
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {
					String fieldName = item.getFieldName();
					String contentType = item.getContentType();
					
					String name = item.getName();
					long size = item.getSize();

					log.debug("Importing new process from "+name+", size: "+(size/1024)+"kb");
					UserDaoHelper.importUsers(name,size,item.getInputStream());
					ImportLogger.log("Importation Succeeded!");
					//response = IOUtils.toString();
					registerFile(request, fieldName, System.currentTimeMillis());
					
				} catch (Exception e) {
					ImportLogger.log("Importation Failed!: "+e.getMessage());
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		
		return message;
	}

}
