package com.duggan.workflow.server.dao.helper;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.OutputDocumentDao;
import com.duggan.workflow.server.dao.model.ADOutputDoc;
import com.duggan.workflow.server.dao.model.LocalAttachment;
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
		
		if(output.getAttachment()!=null){
			LocalAttachment attachment= output.getAttachment();
			document.setAttachmentName(attachment.getName());
			document.setAttachmentId(attachment.getId());
		}
		return document;
	}

	private static ADOutputDoc get(OutputDocument doc) {
		OutputDocumentDao dao = DB.getOutputDocDao();
		
		ADOutputDoc adDoc = new ADOutputDoc();
		if(doc.getId()!=null){
			adDoc=dao.getOuputDocument(doc.getId());
		}
		adDoc.setIsActive(doc.isActive()?1:0);
		adDoc.setCode(doc.getCode());
		adDoc.setName(doc.getName());
		adDoc.setDescription(doc.getDescription());
		
		return adDoc;
	}

	public static List<OutputDocument> getDocuments(Long documentId) {
		OutputDocumentDao dao = DB.getOutputDocDao();
		List<OutputDocument> documents = new ArrayList<>();
		if(documentId!=null){
			documents.add(get(dao.getOuputDocument(documentId)));
		}else{
			List<ADOutputDoc> docs  =dao.getOutputDocuments();
			for(ADOutputDoc doc: docs){
				documents.add(get(doc));
			}
		}
		return documents;
	}

	public static String getHTMLTemplate(String templateName) {

		return null;
	}
}
