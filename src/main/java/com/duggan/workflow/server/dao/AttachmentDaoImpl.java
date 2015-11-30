package com.duggan.workflow.server.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;

public class AttachmentDaoImpl extends BaseDaoImpl{

	public LocalAttachment getAttachmentById(long id){
		Object obj = getEntityManager().createQuery("FROM LocalAttachment d where id= :id").setParameter("id", id).getSingleResult();
		
		LocalAttachment attachment = null;
		
		if(obj!=null){
			attachment = (LocalAttachment)obj;
		}
		
		return attachment;
	}
	
	public List<LocalAttachment> getAttachmentsForDocument(long documentId){
		List lst  = getEntityManager().createQuery("FROM LocalAttachment l where documentId= :documentId").setParameter("documentId", documentId).getResultList();
		
		return lst;
	}

	public void deactivate(long attachmentId) {
		LocalAttachment attachment = getAttachmentById(attachmentId);
		attachment.setArchived(true);
		getEntityManager().persist(attachment);
	}	
	
	public void delete(long attachmentId){
		LocalAttachment attachment = getAttachmentById(attachmentId);
		
		delete(attachment);
	}

	public List<LocalAttachment> getAttachmentsForProcessDef(ProcessDefModel model){
		return getAttachmentsForProcessDef(model,false);
	}
	
	public List<LocalAttachment> getAttachmentsForProcessDef(ProcessDefModel model,  boolean isImage) {
		
		return getAttachmentsForProcessDef( model,null, isImage); 
	}

	public List<LocalAttachment> getAttachmentsForProcessDef(ProcessDefModel model,String name, boolean isImage) {

		String sql= "FROM LocalAttachment t where t.processDef=:processDef";
		
		if(isImage){
			sql= "FROM LocalAttachment t where t.processDefImage=:processDef";
		}
		
		if(name!=null && !isImage){
			sql = sql.concat(" and t.name=:attachmentName");
		}
		
		
		Query query = getEntityManager().createQuery(sql)
			.setParameter("processDef", model);
			
		if(name!=null && !isImage){
			query.setParameter("attachmentName", name);
		}
			
		@SuppressWarnings("unchecked")
		List<LocalAttachment> attachments  = query.getResultList();
		
		return attachments;
		
	}

	public boolean getHasAttachment(Long documentId) {
		if(documentId==null){
			return false;
		}
		Long count = (Long)getEntityManager().createQuery("Select count(l) FROM LocalAttachment l " +
				"where l.document= :document")
		.setParameter("document", DB.getDocumentDao().getById(documentId))
		.getSingleResult();
		
		return count>0;
	}

	public void deleteUserImage(String userId) {
		String sql = "update localattachment set isActive=0 where imageUserId=?";
		
		Query query = getEntityManager().createNativeQuery(sql).setParameter(1, userId);
		query.executeUpdate();
	}
	
	public LocalAttachment getUserImage(String userId){
		Object obj = null;
		
		try{
			obj = getEntityManager().createQuery("FROM LocalAttachment d where imageUserId= :userId and isActive=:isActive")
					.setParameter("userId", userId)
					.setParameter("isActive", 1)
					.getSingleResult();
		}catch(Exception e){			
		}
		
		
		LocalAttachment attachment = null;
		
		if(obj!=null){
			attachment = (LocalAttachment)obj;
		}
		
		return attachment;
	}

	public void deleteSettingImage(String settingName) {
		String sql = "update localattachment set isActive=0 where settingName=?";
		
		Query query = getEntityManager().createNativeQuery(sql).setParameter(1, settingName);
		query.executeUpdate();
	}

	public LocalAttachment getSettingImage(SETTINGNAME settingName) {

		Object obj = null;
		
		try{
			obj = getEntityManager().createQuery("FROM LocalAttachment d where d.settingName=:settingName and d.isActive=:isActive")
					.setParameter("settingName", settingName)
					.setParameter("isActive", 1)
					.getSingleResult();
		}catch(Exception e){	
			e.printStackTrace();
		}
		
		LocalAttachment attachment = null;
		
		if(obj!=null){
			attachment = (LocalAttachment)obj;
		}
		
		return attachment;
	}

	public List<LocalAttachment> getAttachmentsForUser(String userId) {
		
		List<Long> tasksOwnedIds = JBPMHelper.get().getTaskIdsForUser(userId);
		
		if(tasksOwnedIds==null || tasksOwnedIds.isEmpty()){
			return new ArrayList<>();
		}
		List<Long> ids = getResultList(
				getEntityManager().createQuery("Select t.taskData.processInstanceId from Task t "
						+ "where t.id in (:ids)")
				.setParameter("ids", tasksOwnedIds)); 
				
		String sql = "Select l from LocalAttachment l "
				+ "where "
				+ "(l.document.processInstanceId is not null "
				+ "and l.document.processInstanceId in (:ids)) "
				+ "or l.document.createdBy=:userId";
		Query query = getEntityManager().createQuery(sql).setParameter("ids", ids)
				.setParameter("userId", userId);
				
		return getResultList(query);
	}

	public List<LocalAttachment> getAttachmentsForDocument(Long documentId,
			String name) {
		Query query  = getEntityManager().createQuery("FROM LocalAttachment l where documentId= :documentId and l.name=:name")
				.setParameter("documentId", documentId)
				.setParameter("name", name);
		
		return getResultList(query);
	}

	public void delete(Long[] attachmentIds) {
		Query query  = getEntityManager().createQuery("DELETE FROM LocalAttachment where id in (:ids)")
				.setParameter("ids", Arrays.asList(attachmentIds));
		query.executeUpdate();
	}
	
}
