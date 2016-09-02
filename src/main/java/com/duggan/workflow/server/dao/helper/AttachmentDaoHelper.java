package com.duggan.workflow.server.dao.helper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.AttachmentDaoImpl;
import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.ApproverAction;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.model.AttachmentType;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;
import com.wira.commons.shared.models.HTUser;

public class AttachmentDaoHelper {

	public static void saveDocument(long documentId, LocalAttachment attachment) {
		DocumentModel doc = DB.getDocumentDao().getById(documentId);
		attachment.setDocument(doc);

		save(attachment);
	}

	public static void saveDocument(String docRefId, LocalAttachment attachment) {
//		DocumentModel doc = DB.getDocumentDao().findByRefId(docRefId,
//				DocumentModel.class);
//		attachment.setDocument(doc);
		attachment.setDocRefId(docRefId);

		save(attachment);
	}

	public static void save(LocalAttachment attachment) {

		DB.getAttachmentDao().save(attachment);
		// upload to a task only
		if (attachment.getDocument() != null
				&& attachment.getDocument().getProcessInstanceId() != null) {
			DocumentModel doc = attachment.getDocument();
			Notification notification = new Notification();
			notification.setDocumentId(attachment.getDocument().getId());
			notification.setDocumentType(DocumentDaoHelper.getType(doc
					.getType()));
			notification.setNotificationType(NotificationType.FILE_UPLOADED);
			notification.setApproverAction(ApproverAction.UPLOADFILE);
			notification.setOwner(SessionHelper.getCurrentUser());
			notification.setRead(false);
			notification.setSubject(doc.getSubject());
			notification.setFileName(attachment.getName());
			notification.setFileId(attachment.getId());
			// notificatin
			NotificationDaoHelper.saveNotification(notification);
		}
	}

	public static List<Attachment> getAttachments(Long documentId) {
		List<LocalAttachment> models = DB.getAttachmentDao()
				.getAttachmentsForDocument(documentId);

		return get(models, false);

	}

	public static List<Attachment> getAttachmentsByDocRefId(String docRefId) {
		DocumentModel model = DB.getDocumentDao().findByRefId(docRefId,
				DocumentModel.class);
		if (model == null) {
			return new ArrayList<>();
		}
		return getAttachments(model.getId());
	}

	private static List<Attachment> get(List<LocalAttachment> models,
			boolean loadDocumentDetails) {
		List<Attachment> attachments = new ArrayList<>();

		for (LocalAttachment model : models) {
			Attachment attachment = get(model, loadDocumentDetails);
			attachments.add(attachment);
		}
		return attachments;
	}

	public static Attachment get(LocalAttachment model) {
		return get(model, false);
	}

	public static Attachment get(LocalAttachment model,
			boolean loadDocumentDetails) {
		Attachment attachment = new Attachment();
		attachment.setArchived(model.isArchived());
		attachment.setContentType(model.getContentType());
		if (model.getDocument() != null) {
			attachment.setDocumentid(model.getDocument().getId());
			attachment.setDocRefId(model.getDocument().getRefId());
		}
		if (model.getProcessDef() != null)
			attachment.setProcessDefId(model.getProcessDef().getId());

		attachment.setId(model.getId());
		attachment.setName(model.getName());
		attachment.setType(model.getType()==null? AttachmentType.UPLOADED: model.getType());
		attachment.setFieldName(model.getFieldName());
		attachment.setSize(model.getSize());
		attachment.setSizeStr(getSizeAsStr(model.getSize()));
		attachment.setCreated(model.getCreated());
		attachment.setCreatedBy(LoginHelper.get().getUser(model.getCreatedBy(),
				false));

		if (loadDocumentDetails) {
			attachment.setDocumentType(model.getDocument().getType()
					.getDisplay());
			attachment.setSubject(model.getDocument().getSubject());
		}
		return attachment;
	}

	public static String getSizeAsStr(long size) {

		int kb = 1024;

		int mb = kb * kb;

		if (size < kb) {
			return size + "b";
		}

		DecimalFormat formatter = new DecimalFormat("0.0");

		if (size < mb) {

			return formatter.format(((double) size / kb)) + "Kb";
		}

		return formatter.format(((double) size / mb)) + " MB";
	}

	public static boolean deactivate(long attachmentId) {
		AttachmentDaoImpl dao = DB.getAttachmentDao();
		dao.deactivate(attachmentId);
		return true;
	}

	public static boolean delete(long attachmentId) {
		AttachmentDaoImpl dao = DB.getAttachmentDao();
		dao.delete(attachmentId);
		return true;
	}

	public static List<Attachment> getAllAttachments(String userId,
			boolean loadDocumentDetails) {
		AttachmentDaoImpl dao = DB.getAttachmentDao();
		List<LocalAttachment> attachments = dao.getAttachmentsForUser(userId);
		return get(attachments, loadDocumentDetails);
	}

	public static boolean delete(Long[] attachmentIds) {
		AttachmentDaoImpl dao = DB.getAttachmentDao();
		dao.delete(attachmentIds);
		return true;
	}

	public static List<Attachment> getAllAttachments() {
		AttachmentDaoImpl dao = DB.getAttachmentDao();
		List<LocalAttachment> attachments = dao.getAllAttachments();
		return get(attachments, true);
	}

}