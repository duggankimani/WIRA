package com.duggan.workflow.server.servlets.upload;

import gwtupload.server.exceptions.UploadActionException;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.fileupload.FileItem;

import com.duggan.workflow.server.dao.helper.CatalogDaoHelper;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogType;

public class AdvancedCSVImporterExecutor extends FileExecutor {

	@Override
	String execute(HttpServletRequest request, List<FileItem> sessionFiles)
			throws UploadActionException {
		
		String message = null;
		String format = request.getParameter("f");
		if(format==null){
			format = "EXCEL";
		}
		
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {	
					String fieldName = item.getFieldName();
					
					String contentType=item.getContentType();			
					if(!(contentType.equals("text/csv") || contentType.equals("text/plain"))){
						throw new RuntimeException("Unexpected content type ["+contentType
								+"]. Only content types text/csv or text/plain are supported.");
					}
					
					byte[] bites = item.get();
					
					String fileName = item.getName();
					long size = item.getSize();
					
					Catalog catalog = CatalogDaoHelper.save(fileName,format, bites);
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
