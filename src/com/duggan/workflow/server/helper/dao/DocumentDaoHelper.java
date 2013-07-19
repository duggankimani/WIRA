package com.duggan.workflow.server.helper.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.dao.DocumentDaoImpl;
import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocSummary;
import com.duggan.workflow.shared.model.Document;

public class DocumentDaoHelper {

	public static List<DocSummary> getAllDocuments(DocStatus status) {
		DocumentDaoImpl dao = DB.getDocumentDao();

		List<DocumentModel> models = dao.getAllDocuments(status);

		List<DocSummary> lst = new ArrayList<>();

		for (DocumentModel m : models) {
			lst.add(getDoc(m));
		}

		return lst;
	}

	public static Document save(Document document) {
		DocumentModel model = getDoc(document);

		DocumentDaoImpl dao = DB.getDocumentDao();

		if (document.getId() != null) {
			model = dao.getById(document.getId());
			model.setDescription(document.getDescription());
			model.setDocumentDate(document.getDocumentDate());
			model.setPartner(document.getPartner());
			model.setPriority(document.getPriority());
			model.setSubject(document.getSubject());
			model.setType(document.getType());
			model.setValue(document.getValue());
			model.setStatus(document.getStatus());
		}

		model = dao.saveDocument(model);

		Document doc = getDoc(model);

		return doc;
	}

	private static Document getDoc(DocumentModel model) {

		Document doc = new Document();
		doc.setCreated(model.getCreated());
		doc.setDescription(model.getDescription());
		doc.setDocumentDate(model.getDocumentDate());
		doc.setId(model.getId());
		// doc.setOwner(model.getCreatedBy());
		doc.setSubject(model.getSubject());
		doc.setType(model.getType());
		doc.setDocumentDate(model.getDocumentDate());
		doc.setPartner(model.getPartner());
		doc.setPriority(model.getPriority());
		doc.setValue(model.getValue());
		doc.setStatus(model.getStatus());

		return doc;
	}

	private static DocumentModel getDoc(Document document) {
		DocumentModel model = new DocumentModel(document.getId(),
				document.getSubject(), document.getDescription(),
				document.getType());

		model.setDocumentDate(document.getDocumentDate());
		model.setPartner(document.getPartner());
		model.setPriority(document.getPriority());
		model.setValue(document.getValue());
		model.setCreated(document.getCreated());
		model.setStatus(document.getStatus());

		return model;
	}

	public static Document getDocument(Integer id) {
		DocumentDaoImpl dao = DB.getDocumentDao();

		DocumentModel model = dao.getById(id);

		return getDoc(model);
	}

	public static Document getDocument(Map<String, Object> content) {
		Document doc = new Document();
		Object idStr = content.get("documentId");
		if (idStr == null || idStr.equals("null")) {
			idStr = null;
		}

		Integer id = idStr == null ? null : new Integer(idStr.toString());

		String description = content.get("description") == null ? null
				: (String) content.get("description");
		
		String subject = content.get("subject") == null ? null
				: (String) content.get("subject");
		
		String value = content.get("value") == null ? null : (String) content
				.get("value");

		doc.setDescription(description);
		doc.setSubject(subject);
		doc.setId(id);
		doc.setValue(value);

		return doc;
	}

	public static void saveApproval(Integer docId,
			Boolean isApproved) {
		DocumentDaoImpl dao = DB.getDocumentDao();
		DocumentModel model = dao.getById(docId);
		if(model==null){
			throw new IllegalArgumentException("Cannot Approve/Reject document: Unknown Model");
		}
		
		model.setStatus(isApproved? DocStatus.APPROVED: DocStatus.REJECTED);
		
		dao.saveDocument(model);
	}
}
