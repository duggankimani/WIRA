package com.duggan.workflow.server.helper.dao;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.Attachment;

public class AttachmentDaoHelper{

	public static void saveDocument(long documentId, LocalAttachment attachment) {
		DocumentModel doc = DB.getDocumentDao().getById(documentId);
		attachment.setDocument(doc);
		
		DB.getAttachmentDao().saveOrUpdate(attachment);
	}
	
	public static List<Attachment> getAttachments(Long documentId){
		List<LocalAttachment> models = DB.getAttachmentDao().getAttachmentsForDocument(documentId);
		
		List<Attachment> attachments = new ArrayList<>();
		
		for(LocalAttachment model: models){
			Attachment attachment = get(model);
			attachments.add(attachment);
		}
		return attachments;
	}

	private static Attachment get(LocalAttachment model) {
		Attachment attachment = new Attachment();
		attachment.setArchived(model.isArchived());
		attachment.setContentType(model.getContentType());
		attachment.setDocumentid(model.getDocument().getId());
		attachment.setId(model.getId());
		attachment.setName(model.getName());
		attachment.setSize(model.getSize());
		
		return attachment;
	}

}