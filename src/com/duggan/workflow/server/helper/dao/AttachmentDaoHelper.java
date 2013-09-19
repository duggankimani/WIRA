package com.duggan.workflow.server.helper.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.AttachmentDaoImpl;
import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.Attachment;

public class AttachmentDaoHelper{

	public static void saveDocument(long documentId, LocalAttachment attachment) {
		DocumentModel doc = DB.getDocumentDao().getById(documentId);
		attachment.setDocument(doc);
		
		DB.getAttachmentDao().save(attachment);
	}
	
	public static void save(LocalAttachment attachment){
		DB.getAttachmentDao().save(attachment);
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
		attachment.setSizeStr(getSizeAsStr(model.getSize()));
		attachment.setCreated(model.getCreated());
		attachment.setCreatedBy(model.getCreatedBy());
		
		return attachment;
	}
	
	public static String getSizeAsStr(long size){
		
		int kb = 1024;
		
		int mb = kb*kb;
		
		if(size<kb){
			return size+"b";
		}
		
		DecimalFormat formatter = new DecimalFormat("0.0");
		
		if(size<mb){
			
			return formatter.format(((double)size/kb))+"Kb";
		}
		
		return formatter.format(((double)size/mb))+" MB";
	}
	
	public static boolean deactivate(long attachmentId){
		AttachmentDaoImpl dao = DB.getAttachmentDao();
		dao.deactivate(attachmentId);
		return true;
	}

	public static boolean delete(long attachmentId){
		AttachmentDaoImpl dao = DB.getAttachmentDao();
		dao.delete(attachmentId);
		return true;
	}

}