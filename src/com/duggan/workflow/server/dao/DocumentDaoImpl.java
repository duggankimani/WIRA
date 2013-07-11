package com.duggan.workflow.server.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.DocStatus;

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
	
	public DocumentModel getById(Integer id){
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
		
		return document;
	}
	
	public void delete(DocumentModel document){
		
		em.remove(document);
		
	}
}
