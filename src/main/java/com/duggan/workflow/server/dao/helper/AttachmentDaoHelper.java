package com.duggan.workflow.server.dao.helper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.AttachmentDaoImpl;
import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.ApproverAction;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;

public class AttachmentDaoHelper{

	public static void saveDocument(long documentId, LocalAttachment attachment) {
		DocumentModel doc = DB.getDocumentDao().getById(documentId);
		attachment.setDocument(doc);
		
		save(attachment);
	}
	
	public static void save(LocalAttachment attachment){
		
		DB.getAttachmentDao().save(attachment);
		//upload to a task only
		if(attachment.getDocument()!=null && attachment.getDocument().getProcessInstanceId()!=null){
			DocumentModel doc = attachment.getDocument();
			Notification notification = new Notification();
			notification.setDocumentId(attachment.getDocument().getId());
			notification.setDocumentType(DocumentDaoHelper.getType(doc.getType()));
			notification.setNotificationType(NotificationType.FILE_UPLOADED);
			notification.setApproverAction(ApproverAction.UPLOADFILE);
			notification.setOwner(SessionHelper.getCurrentUser());
			notification.setRead(false);
			notification.setSubject(doc.getSubject());
			notification.setFileName(attachment.getName());
			notification.setFileId(attachment.getId());
			//notificatin
			NotificationDaoHelper.saveNotification(notification);
		}
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
	
	public static Attachment get(LocalAttachment model) {
		Attachment attachment = new Attachment();
		attachment.setArchived(model.isArchived());
		attachment.setContentType(model.getContentType());
		if(model.getDocument()!=null){
			attachment.setDocumentid(model.getDocument().getId());			
		}
		if(model.getProcessDef()!=null)
			attachment.setProcessDefId(model.getProcessDef().getId());
		
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