package com.duggan.workflow.server.servlets.upload;

import gwtupload.server.exceptions.UploadActionException;

import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.server.db.DB;

public class ImportFileExecutor extends FileExecutor {

	@Override
	String execute(HttpServletRequest request, List<FileItem> sessionFiles)
			throws UploadActionException {

		String err = null;
		
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {					
					String fieldName = item.getFieldName();
					byte[] bites = item.get();
					String str = new String(bites);
					
					Long formId = FormDaoHelper.importForm(str);
					registerFile(request, fieldName, formId);
					
				}catch(Exception e){
					e.printStackTrace();
					err = e.getMessage();
				}
			}
		}
			
		return err;
	}
	
	public void removeItem(HttpServletRequest request, String fieldName){
		Hashtable<String, Long> receivedFiles = getSessionFiles(request, false);
		Long formId = receivedFiles.get(fieldName);
		
		DB.getFormDao().deleteForm(formId);		
	}

}
