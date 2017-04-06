package com.duggan.workflow.server.servlets.upload;

import gwtupload.server.exceptions.UploadActionException;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.json.JSONObject;

import com.duggan.workflow.server.dao.helper.CatalogDaoHelper;
import com.duggan.workflow.shared.model.catalog.Catalog;

public class DataTableImporterExecutor extends FileExecutor {

	@Override
	String execute(HttpServletRequest request, List<FileItem> sessionFiles)
			throws UploadActionException {

		String message = "";
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {	
					String fieldName = item.getFieldName();
					
					String contentType=item.getContentType();			
					if(!(contentType.equals("application/json"))){
						throw new RuntimeException("Unexpected content type ["+contentType
								+"]. Only content type application/json are supported.");
					}
					
					byte[] bites = item.get();
					
					String fileName = item.getName();
					long size = item.getSize();
					
					Double val = new Double(size)/1024;
					List<String> messages = new ArrayList<String>();
					messages.add("Importing Data Table - File Name = "+fileName+", Size "+val.intValue()+" bytes");
					Catalog catalog = CatalogDaoHelper.importCatalog(new String(bites), messages);
					
					
//					for(String msg : messages){
//						message = message.concat(msg+"<br/>");
//					}
				
					message = catalog.getRefId();
					registerFile(request, fieldName, catalog.getId());
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		return message;
	}

}
