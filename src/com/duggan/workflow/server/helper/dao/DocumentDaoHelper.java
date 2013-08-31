package com.duggan.workflow.server.helper.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.dao.DocumentDaoImpl;
import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocSummary;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.SearchFilter;

/**
 * This class is Dao Helper for persisting all document related entities.
 * 
 * @author duggan
 *
 */
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

	/**
	 * This method saves updates a document
	 * 
	 * @param document
	 * 
	 * @return Document
	 */
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
			model.setProcessInstanceId(document.getProcessInstanceId());
		}

		model = dao.saveDocument(model);

		Document doc = getDoc(model);

		return doc;
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	private static Document getDoc(DocumentModel model) {
		if(model==null){
			return null;
		}
		Document doc = new Document();
		doc.setCreated(model.getCreated());
		doc.setDescription(model.getDescription());
		doc.setDocumentDate(model.getDocumentDate());
		doc.setId(model.getId());
		doc.setOwner(LoginHelper.get().getUser(model.getCreatedBy()));
		doc.setSubject(model.getSubject());
		doc.setType(model.getType());
		doc.setDocumentDate(model.getDocumentDate());
		doc.setPartner(model.getPartner());
		doc.setPriority(model.getPriority());
		doc.setValue(model.getValue());
		doc.setStatus(model.getStatus());
		doc.setProcessInstanceId(model.getProcessInstanceId());

		return doc;
	}

	/**
	 * 
	 * @param document
	 * @return
	 */
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
		model.setProcessInstanceId(document.getProcessInstanceId());
		
		return model;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static Document getDocument(Long id) {
		DocumentDaoImpl dao = DB.getDocumentDao();

		DocumentModel model = dao.getById(id);

		return getDoc(model);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static Document getDocumentByProcessInstance(Long processInstanceId) {
		DocumentDaoImpl dao = DB.getDocumentDao();

		DocumentModel model = dao.getDocumentByProcessInstanceId(processInstanceId);

		return getDoc(model);
	}

	/**
	 * 
	 * @param content
	 * @return
	 */
	public static Document getDocument(Map<String, Object> content) {
		Document doc = new Document();
		Object idStr = content.get("documentId");
		if (idStr == null || idStr.equals("null")) {
			idStr = null;
		}

		Long id = idStr == null ? null : new Long(idStr.toString());

		String description = content.get("description") == null ? null
				: (String) content.get("description");
		
		String subject = content.get("subject") == null ? null
				: (String) content.get("subject");
		
		String value = content.get("value") == null ? null : (String) content
				.get("value");
		
		Integer priority = content.get("priority")==null? null:
					(Integer)content.get("priority");

		doc.setDescription(description);
		doc.setSubject(subject);
		doc.setId(id);
		doc.setValue(value);
		doc.setPriority(priority);
		return doc;
	}

	/**
	 * 
	 * @param docId
	 * @param isApproved
	 */
	public static void saveApproval(Long docId,
			Boolean isApproved) {
		DocumentDaoImpl dao = DB.getDocumentDao();
		DocumentModel model = dao.getById(docId);
		if(model==null){
			throw new IllegalArgumentException("Cannot Approve/Reject document: Unknown Model");
		}
		
		model.setStatus(isApproved? DocStatus.APPROVED: DocStatus.REJECTED);
		
		dao.saveDocument(model);
	}

	public static void getCounts(HashMap<TaskType, Integer> counts) {
		DocumentDaoImpl dao = DB.getDocumentDao();
		
		counts.put(TaskType.DRAFT, dao.count(DocStatus.DRAFTED));
		counts.put(TaskType.INPROGRESS, dao.count(DocStatus.INPROGRESS));
		counts.put(TaskType.APPROVED, dao.count(DocStatus.APPROVED));
		counts.put(TaskType.REJECTED, dao.count(DocStatus.REJECTED));
		//counts.put(TaskType.FLAGGED, dao.count(DocStatus.));
	}

	public static List<Document> search(String subject) {
		DocumentDaoImpl dao = DB.getDocumentDao();
		List<DocumentModel> models = dao.search(subject);
		
		List<Document> docs = new ArrayList<>();
		for(DocumentModel doc: models){
			docs.add(getDoc(doc));
		}
		
		return docs;
	}

	public static Long getProcessInstanceIdByDocumentId(Long documentId) {
		DocumentDaoImpl dao = DB.getDocumentDao();
		
		return dao.getProcessInstanceIdByDocumentId(documentId);
	}

	public static List<Document> search(SearchFilter filter) {
		DocumentDaoImpl dao = DB.getDocumentDao();
		List<DocumentModel> models = dao.search(filter);
		List<Document> docs = new ArrayList<>();
		for(DocumentModel doc: models){
			docs.add(getDoc(doc));
		}
		
		return docs;
	}
	

}
