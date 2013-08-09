package com.duggan.workflow.server.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Document;

/**
 * 
 * @author duggan
 *
 */
public class DocumentDaoImpl {

	EntityManager em;
	
	public DocumentDaoImpl(EntityManager em){
		this.em = em;
	}
	
	@SuppressWarnings("unchecked")
	public List<DocumentModel> getAllDocuments(DocStatus status){
		
		return em.createQuery("FROM DocumentModel d where status=:status and createdBy=:createdBy").
				setParameter("status", status).
				setParameter("createdBy", SessionHelper.getCurrentUser().getId()).
				getResultList();
	}
	
	public DocumentModel getById(Long id){
		List lst = em.createQuery("FROM DocumentModel d where id= :id").setParameter("id", id).getResultList();
		
		if(lst.size()>0){
			return (DocumentModel)lst.get(0);
		}
		
		return null;
	}
	
	public DocumentModel saveDocument(DocumentModel document){
		
		if(document.getId()==null){
			document.setCreated(new Date());
			document.setStatus(DocStatus.DRAFTED);
			document.setCreatedBy(SessionHelper.getCurrentUser().getId());
		}else{
			document.setUpdated(new Date());
			document.setUpdatedBy(SessionHelper.getCurrentUser().getId());
		}
		
		em.persist(document);
		
		/*
		 * Do not flush - This reflects data in the database immediately and the BTM transaction 
		 * in my tests so far cannot rollback the flushed data - You can actually query the new values 
		 * directly from the database even as the transaction is ongoing - Could it be the isolation
		 * level being used in the db?
		 * 
		 */
		//em.flush();
		return document;
	}
	
	public void delete(DocumentModel document){
		
		em.remove(document);
		
	}

	public Integer count(DocStatus status) {

		 Long value = (Long)em.createQuery("select count(d) FROM DocumentModel d where status=:status and createdBy=:createdBy").
				setParameter("status", status).
				setParameter("createdBy", SessionHelper.getCurrentUser().getId()).getSingleResult();
		 
		 return value.intValue();
	}

	public List<DocumentModel> search(String subject) {
		List lst = em.createQuery("FROM DocumentModel d where subject like :subject")
				.setParameter("subject", "%"+subject+"%")
				.getResultList();
				
		return lst;
	}

	public DocType getDocumentType(Long documentId) {
		
		DocType type = (DocType)em.createQuery("select d.type FROM DocumentModel d where d.id=:documentId")
		.setParameter("documentId", documentId).getSingleResult();
		
		return type;
	}

	public Long getProcessInstanceIdByDocumentId(Long documentId) {
		Object processInstanceId = em.createQuery("select n.processInstanceId FROM DocumentModel n" +
				" where n.id=:documentId")
				.setParameter("documentId", documentId)
				.getSingleResult();
	
		return processInstanceId==null? null : (Long)processInstanceId;
	}
	
	public DocumentModel getDocumentByProcessInstanceId(Long processInstanceId){
		List lst = em.createQuery("FROM DocumentModel d where processInstanceId= :processInstanceId " +
				"and createdBy=:createdBy")
				.setParameter("processInstanceId", processInstanceId)
				.setParameter("createdBy", SessionHelper.getCurrentUser().getId())
				.getResultList();
		
		if(lst.size()>0){
			return (DocumentModel)lst.get(0);
		}
		
		return null;
	}

}
