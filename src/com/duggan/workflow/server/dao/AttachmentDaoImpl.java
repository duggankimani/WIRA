package com.duggan.workflow.server.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.dao.model.ProcessDefModel;

public class AttachmentDaoImpl extends BaseDaoImpl{

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

	public void deactivate(long attachmentId) {
		LocalAttachment attachment = getAttachmentById(attachmentId);
		attachment.setArchived(true);
		em.persist(attachment);
	}	
	
	public void delete(long attachmentId){
		LocalAttachment attachment = getAttachmentById(attachmentId);
		delete(attachment);
	}

	@SuppressWarnings("unchecked")
	public List<LocalAttachment> getAttachmentsForProcessDef(ProcessDefModel model) {
		List<LocalAttachment> attachments = 
				em.createQuery("FROM LocalAttachment t where t.processDef=:processDef")
		.setParameter("processDef", model)
		.getResultList();
		
		return attachments;
	}
	
}
