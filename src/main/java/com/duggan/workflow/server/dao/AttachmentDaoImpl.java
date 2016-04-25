package com.duggan.workflow.server.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.ColumnResult;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.drools.runtime.process.ProcessInstance;

import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.model.AttachmentType;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.HTStatus;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.Status;
import com.duggan.workflow.shared.model.TreeType;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;

public class AttachmentDaoImpl extends BaseDaoImpl {

	public AttachmentDaoImpl(EntityManager em) {
		super(em);
	}

	public LocalAttachment getAttachmentById(long id) {
		Object obj = em.createQuery("FROM LocalAttachment d where id= :id")
				.setParameter("id", id).getSingleResult();

		LocalAttachment attachment = null;

		if (obj != null) {
			attachment = (LocalAttachment) obj;
		}

		return attachment;
	}

	public List<LocalAttachment> getAttachmentsForDocument(long documentId) {
		List lst = em
				.createQuery(
						"FROM LocalAttachment l where documentId= :documentId")
				.setParameter("documentId", documentId).getResultList();

		return lst;
	}

	public void deactivate(long attachmentId) {
		LocalAttachment attachment = getAttachmentById(attachmentId);
		attachment.setArchived(true);
		em.persist(attachment);
	}

	public void delete(long attachmentId) {
		LocalAttachment attachment = getAttachmentById(attachmentId);

		delete(attachment);
	}

	public List<LocalAttachment> getAttachmentsForProcessDef(
			ProcessDefModel model) {
		return getAttachmentsForProcessDef(model, false);
	}

	public List<LocalAttachment> getAttachmentsForProcessDef(
			ProcessDefModel model, boolean isImage) {

		return getAttachmentsForProcessDef(model, null, isImage);
	}

	public List<LocalAttachment> getAttachmentsForProcessDef(
			ProcessDefModel model, String name, boolean isImage) {

		String sql = "FROM LocalAttachment t where t.processDef=:processDef";

		if (isImage) {
			sql = "FROM LocalAttachment t where t.processDefImage=:processDef";
		}

		if (name != null && !isImage) {
			sql = sql.concat(" and t.name=:attachmentName");
		}

		Query query = em.createQuery(sql).setParameter("processDef", model);

		if (name != null && !isImage) {
			query.setParameter("attachmentName", name);
		}

		@SuppressWarnings("unchecked")
		List<LocalAttachment> attachments = query.getResultList();

		return attachments;

	}

	public boolean getHasAttachment(Long documentId) {
		if (documentId == null) {
			return false;
		}
		Long count = (Long) em
				.createQuery(
						"Select count(l) FROM LocalAttachment l "
								+ "where l.document= :document")
				.setParameter("document",
						DB.getDocumentDao().getById(documentId))
				.getSingleResult();

		return count > 0;
	}

	public void deleteUserImage(String userId) {
		String sql = "update localattachment set isActive=0 where imageUserId=?";

		Query query = em.createNativeQuery(sql).setParameter(1, userId);
		query.executeUpdate();
	}

	public LocalAttachment getUserImage(String userId) {
		Object obj = null;

		try {
			obj = em.createQuery(
					"FROM LocalAttachment d where imageUserId= :userId and isActive=:isActive")
					.setParameter("userId", userId).setParameter("isActive", 1)
					.getSingleResult();
		} catch (Exception e) {
		}

		LocalAttachment attachment = null;

		if (obj != null) {
			attachment = (LocalAttachment) obj;
		}

		return attachment;
	}

	public void deleteSettingImage(String settingName) {
		String sql = "update localattachment set isActive=0 where settingName=?";

		Query query = em.createNativeQuery(sql).setParameter(1, settingName);
		query.executeUpdate();
	}

	public LocalAttachment getSettingImage(SETTINGNAME settingName) {

		Object obj = null;

		try {
			obj = em.createQuery(
					"FROM LocalAttachment d where d.settingName=:settingName and d.isActive=:isActive")
					.setParameter("settingName", settingName)
					.setParameter("isActive", 1).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		LocalAttachment attachment = null;

		if (obj != null) {
			attachment = (LocalAttachment) obj;
		}

		return attachment;
	}

	public List<LocalAttachment> getAttachmentsForUser(String userId) {

		List<Long> tasksOwnedIds = JBPMHelper.get().getTaskIdsForUser(userId);

		if (tasksOwnedIds == null || tasksOwnedIds.isEmpty()) {
			return new ArrayList<>();
		}
		List<Long> ids = getResultList(em.createQuery(
				"Select t.taskData.processInstanceId from Task t "
						+ "where t.id in (:ids)").setParameter("ids",
				tasksOwnedIds));

		String sql = "Select l from LocalAttachment l " + "where "
				+ "(l.document.processInstanceId is not null "
				+ "and l.document.processInstanceId in (:ids)) "
				+ "or l.document.createdBy=:userId";
		Query query = em.createQuery(sql).setParameter("ids", ids)
				.setParameter("userId", userId);

		return getResultList(query);
	}

	public List<LocalAttachment> getAttachmentsForDocument(Long documentId,
			String name) {
		Query query = em
				.createQuery(
						"FROM LocalAttachment l where documentId= :documentId and l.name=:name")
				.setParameter("documentId", documentId)
				.setParameter("name", name);

		return getResultList(query);
	}

	public void delete(Long[] attachmentIds) {
		Query query = em.createQuery(
				"DELETE FROM LocalAttachment where id in (:ids)").setParameter(
				"ids", Arrays.asList(attachmentIds));
		query.executeUpdate();
	}

	public List<LocalAttachment> getAllAttachments() {
		return getResultList(em
				.createQuery("FROM LocalAttachment l where l.document is not null"));
	}

	public LocalAttachment getDirectory(LocalAttachment parent,
			String directoryName) {

		StringBuffer jpql = new StringBuffer("FROM LocalAttachment f"
				+ " where f.name=:name" + " and f.isDirectory=1 ");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", directoryName);

		if (parent == null) {
			jpql.append(" and f.parent is null");
		} else {
			jpql.append(" and f.parent=:parent");
			params.put("parent", parent);
		}

		Query query = getEntityManager().createQuery(jpql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}

		return getSingleResultOrNull(query);
	}

	@SuppressWarnings("unchecked")
	public List<Attachment> getFileTree() {

		List<Object[]> rows = getEntityManager().createNamedQuery(
				"Attachment.GetDirectoryTree").getResultList();

		List<Attachment> directories = new ArrayList<Attachment>();

		Attachment parent = null;
		for (Object[] row : rows) {
			int i = 0;
			Attachment attachment = new Attachment();
			Object value = null;
			String id = (value = row[i++]) == null ? null : value.toString();
			String refId = (value = row[i++]) == null ? null : value.toString();
			String name = (value = row[i++]) == null ? null : value.toString();
			String parentid = (value = row[i++]) == null ? null : value
					.toString();
			Integer ct = (value = row[i++]) == null ? 0 : (Integer) value;
			String parentRefId = (value = row[i++]) == null ? null : value
					.toString();

			attachment.setName(name);
			attachment.setRefId(refId);
			attachment.setParentRefId(parentRefId);
			attachment.setDirectory(true);
			attachment.setChildCount(ct);
			parent = addToParent(parent, attachment, directories);
		}

		return directories;
	}

	@SuppressWarnings("unchecked")
	public List<Attachment> getFileProcessTree() {
		List<Object[]> rows = getEntityManager().createNamedQuery(
				"Attachment.GetProcessTree").getResultList();

		List<Attachment> directories = new ArrayList<Attachment>();

		Attachment parent = null;
		for (Object[] row : rows) {
			int i = 0;
			Attachment attachment = new Attachment();

			Object value = null;
			String refId = (value = row[i++]) == null ? null : value.toString();
			String name = (value = row[i++]) == null ? null : value.toString();
			String processId = (value = row[i++]) == null ? null : value
					.toString();
			Integer ct = (value = row[i++]) == null ? 0 : ((Number) value)
					.intValue();

			attachment.setRefId(refId);
			attachment.setName(name);
			attachment.setProcessRefId(refId);
			attachment.setDirectory(true);
			attachment.setChildCount(ct);
			parent = addToParent(parent, attachment, directories);
		}

		return directories;

	}

	@SuppressWarnings("unchecked")
	public List<Attachment> getFileUserTree() {

		List<Object[]> rows = getEntityManager().createNamedQuery(
				"Attachment.GetAttachmentOwners").getResultList();

		List<Attachment> directories = new ArrayList<Attachment>();

		Attachment parent = null;
		for (Object[] row : rows) {
			int i = 0;
			Attachment attachment = new Attachment();

			Object value = null;
			String refId = (value = row[i++]) == null ? null : value.toString();
			String createdBy = (value = row[i++]) == null ? null : value
					.toString();
			String name = (value = row[i++]) == null ? null : value.toString();
			Integer ct = (value = row[i++]) == null ? 0 : ((Number) value)
					.intValue();

			attachment.setRefId(refId);
			attachment.setName(name);
			attachment.setCreatedBy(new HTUser(createdBy));
			attachment.setDirectory(true);
			attachment.setChildCount(ct);
			parent = addToParent(parent, attachment, directories);
		}

		return directories;
	}

	/**
	 * 
	 * @param parent
	 * @param attachment
	 * @param directories
	 */
	private Attachment addToParent(Attachment parent, Attachment attachment,
			List<Attachment> root) {
		assert attachment != null;
		if (parent == null || attachment.getParentRefId() == null) {
			root.add(attachment);
			return attachment;
		}
		if (attachment.getParentRefId() != null
				&& parent.getRefId().equals(attachment.getParentRefId())) {
			parent.addChild(attachment);
			return attachment;
		} else {
			return addToParent(parent.getParent(), attachment, root);
		}

	}

	public List<Attachment> getAttachments(TreeType type, String parentRefId,
			String searchTerm) {
		StringBuffer jpql = new StringBuffer(
				"select a.refid,"
						+ "a.name,"
						+ "a.created,"
						+ "a.createdby,"
						+ "a.type,"
						+ "u.firstname,"
						+ "u.lastname,"
						+ "d.subject,"
						+ "p.refId,"
						+ "p.name processName,"
						+ "pinfo.state,"
						+ "d.status "
						+ "from localattachment a "
						+ "inner join buser u on (u.userid=a.createdby) "
						+ "inner join localdocument d on (d.id=a.documentid) "
						+ "inner join addoctype t on (t.id=d.doctype) "
						+ "inner join processdefmodel p on (p.id=t.processdefid) "
						+ "left join localattachment parent on (parent.id=a.parentid) "
						+ "left join processinstanceinfo pinfo "
						+ "on (pinfo.instanceid=d.processinstanceid) "
						+ "where a.isActive=1 ");

		Map<String, String> params = new HashMap<String, String>();
		switch (type) {
		case FILES:
			if (parentRefId != null) {
				jpql.append("and (parent.refId=:parentRefId) ");
				params.put("parentRefId", parentRefId);
			} else {
//				jpql.append("and (parent.refId is not null) ");
			}

			break;
		case PROCESSES:
			if (parentRefId != null) {
				jpql.append("and (p.refId=:processRefId) ");
				params.put("processRefId", parentRefId);
			} else {
				jpql.append("and (p.refId is not null) ");
			}
			break;
		case USERS:
			if (parentRefId != null) {
				jpql.append("and (u.userId=:userRefId) ");
				params.put("userRefId", parentRefId);
			} else {
				jpql.append("and (a.createdBy is not null) ");
			}
			break;
		}

		if (searchTerm != null) {
			jpql.append("and (lower(a.name) like :searchTerm "
					+ "or lower(d.subject) like :searchTerm "
					+ "or lower(p.name) like :searchTerm " + ") ");
			params.put("searchTerm", "%" + searchTerm.toLowerCase() + "%");
		}
		
		jpql.append(" order by a.created desc");

		logger.info(type + " - jpql: " + jpql);
		Query query = getEntityManager().createNativeQuery(jpql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}

		List<Object[]> rows = getResultList(query);
		List<Attachment> files = new ArrayList<Attachment>();

		for (Object[] row : rows) {
			int i = 0;
			Attachment attachment = new Attachment();
			Object value = null;
			String refId = (value = row[i++]) == null ? null : value.toString();
			String name = (value = row[i++]) == null ? null : value.toString();
			Date created = (value = row[i++]) == null ? null : (Date) value;
			String createdBy = (value = row[i++]) == null ? null : value
					.toString();
			int attachmentType = (value = row[i++]) == null ? -1 : (Integer) value;
			String firstName = (value = row[i++]) == null ? null : value
					.toString();
			String lastName = (value = row[i++]) == null ? null : value
					.toString();
			String caseNo = (value = row[i++]) == null ? null : value
					.toString();
			String processRefId = (value = row[i++]) == null ? null : value
					.toString();
			String processName = (value = row[i++]) == null ? null : value
					.toString();
			int state = (value = row[i++]) == null ? -1 : (Integer) value;
			String docStatus = (value = row[i++]) == null ? null : value
					.toString();

			attachment.setRefId(refId);
			attachment.setName(name);
			attachment.setType(attachmentType==-1? AttachmentType.UPLOADED:
				AttachmentType.values()[attachmentType]);
			attachment.setProcessRefId(processRefId);
			attachment.setCreated(created);
			HTUser user = new HTUser(createdBy);
			user.setName(firstName);
			user.setSurname(lastName);
			attachment.setCreatedBy(user);
			attachment.setCaseNo(caseNo);
			attachment.setProcessName(processName);
//			logger.info("Process Status - "+state);
			
//			int STATE_PENDING   = 0;
//		    int STATE_ACTIVE    = 1;
//		    int STATE_COMPLETED = 2;
//		    int STATE_ABORTED   = 3;
//		    int STATE_SUSPENDED = 4;
		    
			attachment.setProcessStatus(state==0? HTStatus.CREATED:
				state==1? HTStatus.INPROGRESS:
					state==2? HTStatus.COMPLETED:
						state==3? HTStatus.EXITED:
							state==4? HTStatus.SUSPENDED: null);
			attachment.setDocStatus(docStatus==null? null: DocStatus.valueOf(docStatus));
			attachment.setDirectory(false);
			files.add(attachment);
		}

		return files;

	}

}
