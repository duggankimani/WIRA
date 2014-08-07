package com.duggan.workflow.server.dao.helper;

import com.duggan.workflow.server.dao.OutputDocumentDao;
import com.duggan.workflow.server.dao.model.ADOutputDoc;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.OutputDocument;

public class OutputDocumentDaoHelper {

	public static OutputDocument saveOutputDoc(OutputDocument doc){
		ADOutputDoc output = get(doc);
		OutputDocumentDao dao = DB.getOutputDocDao();
		dao.save(output);
		
		return get(output);
	}

	private static OutputDocument get(ADOutputDoc output) {
		
		OutputDocument document = new OutputDocument();
		document.setCode(output.getCode());
		document.setDescription(output.getDescription());
		document.setId(output.getId());
		document.setName(output.getName());
		
		return document;
	}

	private static ADOutputDoc get(OutputDocument doc) {
		OutputDocumentDao dao = DB.getOutputDocDao();
		
		ADOutputDoc adDoc = new ADOutputDoc();
		if(doc.getId()!=null){
			adDoc=dao.getOuputDocument(doc.getId());
		}
		adDoc.setCode(doc.getCode());
		adDoc.setName(doc.getName());
		adDoc.setDescription(doc.getDescription());
		
		return adDoc;
	}
}
