package com.duggan.workflow.server.servlets.upload;

import gwtupload.server.exceptions.UploadActionException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;

public class ImportCSVDataExecutor extends FileExecutor {

	@Override
	String execute(HttpServletRequest request, List<FileItem> sessionFiles)
			throws UploadActionException {
		
		String message = null;
		
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {	
					String fieldName = item.getFieldName();
					String contentType=item.getContentType();			
					if(!contentType.equals("text/csv")){
						return null;
					}
					
					String name = item.getName();
					long size = item.getSize();
					
					message = IOUtils.toString(item.getInputStream());
					registerFile(request, fieldName, System.currentTimeMillis());
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		return message;
	}

}
