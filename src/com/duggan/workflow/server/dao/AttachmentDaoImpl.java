package com.duggan.workflow.server.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.model.LocalAttachment;

public class AttachmentDaoImpl extends BaseDaoImpl<LocalAttachment>{

	public AttachmentDaoImpl(EntityManager em) {
		super(em);
	}
	
	public LocalAttachment getAttachmentById(long id){
		Object obj = em.createQuery("FROM LocalAttachment d where id= :id").setParameter("id", id).getSingleResult();
		
		LocalAttachment attachment = null;
		
		if(obj!=null){
			attachment = (LocalAttachment)obj;
		}
		
		return attachment;
	}
	
	public List<LocalAttachment> getAttachmentsForDocument(long documentId){
		List lst  = em.createQuery("FROM LocalAttachment l where documentId= :documentId").setParameter("documentId", documentId).getResultList();
		
		return lst;
	}	
	
}
