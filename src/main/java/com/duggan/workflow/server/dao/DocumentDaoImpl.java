package com.duggan.workflow.server.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.kie.api.task.model.Status;
import org.kie.api.task.model.TaskSummary;

import com.duggan.workflow.server.dao.model.ADDocType;
import com.duggan.workflow.server.dao.model.ADValue;
import com.duggan.workflow.server.dao.model.DetailModel;
import com.duggan.workflow.server.dao.model.DocumentLineJson;
import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.dao.model.DocumentModelJson;
import com.duggan.workflow.server.dao.model.TaskDelegation;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.HTStatus;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.SearchFilter;
import com.duggan.workflow.shared.model.Value;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.UserGroup;

/**
 * 
 * @author duggan
 *
 */
public class DocumentDaoImpl extends BaseDaoImpl {

	public DocumentDaoImpl(EntityManager em) {
		super(em);
	}

	@SuppressWarnings("unchecked")
	public List<DocumentModel> getAllDocuments(int offset, int length,
			DocStatus... status) {

		return em
				.createQuery(
						"FROM DocumentModel d where status in (:status) and createdBy=:createdBy and isActive=:isActive order by created desc")
				.setParameter("status", Arrays.asList(status))
				.setParameter("createdBy",
						SessionHelper.getCurrentUser().getUserId())
				.setParameter("isActive", 1).setFirstResult(offset)
				.setMaxResults(length).getResultList();
	}

	public DocumentModel getById(Long id) {
		List lst = em.createQuery("FROM DocumentModel d where id= :id")
				.setParameter("id", id).getResultList();

		if (lst.size() > 0) {
			return (DocumentModel) lst.get(0);
		}

		return null;
	}

	public DocumentModel saveDocument(DocumentModel document) {

		if (document.getId() == null) {
			document.setCreated(new Date());
			if (document.getStatus() == null) {
				document.setStatus(DocStatus.DRAFTED);
			}
			document.setCreatedBy(SessionHelper.getCurrentUser().getUserId());
		} else {
			document.setUpdated(new Date());
			document.setUpdatedBy(SessionHelper.getCurrentUser().getUserId());
		}

		em.persist(document);

		/*
		 * Do not flush - This reflects data in the database immediately and the
		 * BTM transaction in my tests so far cannot rollback the flushed data -
		 * You can actually query the new values directly from the database even
		 * as the transaction is ongoing - Could it be the isolation level being
		 * used in the db?
		 */
		// em.flush();
		return document;
	}

	public void delete(DocumentModel document) {

		em.remove(document);
	}

	public Integer count(String processId, String userId, DocStatus status) {
		return count(processId, userId, status, true);
	}

	public Integer count(String processId, String userId, DocStatus status,
			boolean isEqualTo) {

		Query query = em
				.createQuery(
						"select count(d) FROM DocumentModelJson d "
								+ "where "
								+ (isEqualTo ? "status=:status "
										: "status!=:status ")
								+ (userId == null ? ""
										: "and createdBy=:createdBy and isActive=:isActive ")
								+ (processId == null ? ""
										: "and processId=:processId"))
				.setParameter("status", status).setParameter("isActive", 1);

		if (userId != null) {
			query.setParameter("createdBy", userId);
		}

		if (processId != null) {
			query.setParameter("processId", processId);
		}

		Long value = (Long) query.getSingleResult();

		return value.intValue();
	}

	public List<DocumentModel> search(String subject) {
		List lst = em
				.createQuery(
						"FROM DocumentModel d where subject like :subject  and isActive=:isActive")
				.setParameter("subject", "%" + subject + "%")
				.setParameter("isActive", 1).getResultList();

		return lst;
	}

	public Long getProcessInstanceIdByDocumentId(Long documentId) {
		Object processInstanceId = em
				.createQuery(
						"select n.processInstanceId FROM DocumentModel n"
								+ " where n.id=:documentId")
				.setParameter("documentId", documentId).getSingleResult();

		return processInstanceId == null ? null : (Long) processInstanceId;
	}

	public Long getProcessInstanceIdByDocRefId(String docRefId) {
		Object processInstanceId = em
				.createQuery(
						"select n.processInstanceId FROM DocumentModelJson n"
								+ " where n.refId=:refId")
				.setParameter("refId", docRefId).getSingleResult();

		return processInstanceId == null ? null : (Long) processInstanceId;
	}

	public DocumentModel getDocumentByProcessInstanceId(Long processInstanceId) {
		return getDocumentByProcessInstanceId(processInstanceId, true);
	}

	/**
	 * Returns only documents created by current user
	 * 
	 * <p>
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public DocumentModel getDocumentByProcessInstanceId(Long processInstanceId,
			boolean checkUser) {
		Query query = em
				.createQuery(
						"FROM DocumentModel d where processInstanceId= :processInstanceId "
								+ (checkUser ? "and createdBy=:createdBy " : "")
								+ " and isActive=:isActive")
				.setParameter("processInstanceId", processInstanceId)
				.setParameter("isActive", 1);

		if (checkUser) {
			query = query.setParameter("createdBy", SessionHelper
					.getCurrentUser().getUserId());
		}

		List lst = query.getResultList();

		if (lst.size() > 0) {
			return (DocumentModel) lst.get(0);
		}

		return null;
	}
	
	public Document getDocumentJsonByProcessInstanceId(Long processInstanceId,
			boolean checkUser) {
		Query query = em
				.createQuery(
						"FROM DocumentModelJson d where processInstanceId= :processInstanceId "
								+ (checkUser ? "and createdBy=:createdBy " : "")
								+ " and isActive=:isActive")
				.setParameter("processInstanceId", processInstanceId)
				.setParameter("isActive", 1);

		if (checkUser) {
			query = query.setParameter("createdBy", SessionHelper
					.getCurrentUser().getUserId());
		}

		List lst = query.getResultList();

		if (lst.size() > 0) {
			return ((DocumentModelJson) lst.get(0)).getDocument();
		}

		return null;
	}


	public List<DocumentModel> search(String processId,String userId, SearchFilter filter) {
		return new ArrayList<DocumentModel>();
	}
	
	public List<DocumentModelJson> searchJson(String processId,String userId, SearchFilter filter) {

		String subject = filter.getSubject();
		Date startDate = filter.getStartDate();
		Date endDate = filter.getEndDate();
		Integer priority = filter.getPriority();
		String phrase = filter.getPhrase();
		DocumentType docTypeIn = filter.getDocType();

		ADDocType docType = null;
		if (docTypeIn != null) {
//			docType = getDocumentTypeById(docTypeIn.getId());
			docType = findByRefId(docTypeIn.getRefId(), ADDocType.class);
		}

		Boolean hasAttachment = filter.hasAttachment();

		Map<String, Object> params = new HashMap<>();

		StringBuffer query = new StringBuffer("select "
				+ "new com.duggan.workflow.server.dao.model.DocumentModelJson(document, data) "
				+ "FROM DocumentModelJson d where ");

		boolean isFirst = true;
		if (subject != null) {
			isFirst = false;
			query.append("(Lower(caseNo) like :caseNo");
			params.put("caseNo", "%" + subject.toLowerCase() + "%");

			if (phrase == null) {
				query.append(" or Lower(description) like :phrase");
				params.put("phrase", "%" + subject.toLowerCase() + "%");
			}
			query.append(")");
		}

		if (!isFirst) {
			query.append(" AND ");
			isFirst = true;// so that we dont add another AND
		}

		if (startDate != null && endDate != null) {
			isFirst = false;
			query.append("created>:startDate " + "and " + "created<:endDate");

			params.put("startDate", startDate);
			params.put("endDate", endDate);
		} else if (startDate != null) {
			isFirst = false;
			// mysql functions - They wont work on postgres
			query.append("STR_TO_DATE(DATE_FORMAT(created, '%d/%m/%y'), '%d/%m/%y')=:startDate");
			params.put("startDate", startDate);

		} else if (endDate != null) {
			isFirst = false;
			// mysql functions - They wont work on postgres
			query.append("STR_TO_DATE(DATE_FORMAT(created, '%d/%m/%y'), '%d/%m/%y')=:endDate");
			params.put("endDate", endDate);

		}

		if (!isFirst) {
			query.append(" AND ");
			isFirst = true;// so that we dont add another AND
		}

		if (priority != null) {
			isFirst = false;
			query.append("priority=:priority");
			params.put("priority", priority);
		}

		if (!isFirst) {
			query.append(" AND ");
			isFirst = true;// so that we dont add another AND
		}

		if (phrase != null) {
			isFirst = false;
			query.append("Lower(description) like :phrase");
			params.put("phrase", "%" + phrase.toLowerCase() + "%");
		}

		if (!isFirst) {
			query.append(" AND ");
			isFirst = true;// so that we dont add another AND
		}

		if (docType != null) {
			isFirst = false;
			query.append("type=:docType");
			params.put("docType", docType);
		}

		if (!isFirst) {
			query.append(" AND ");
			isFirst = true;// so that we dont add another AND
		}

		{
			isFirst = false;
			query.append("createdBy=:createdBy");
			// params.put("createdBy",
			// SessionHelper.getCurrentUser().getUserId());
			params.put("createdBy", userId);
		}

		if (!isFirst) {
			query.append(" AND ");
			isFirst = true;// so that we dont add another AND
		}

		{
			isFirst = false;
			query.append("isActive=:isActive");
			// params.put("createdBy",
			// SessionHelper.getCurrentUser().getUserId());
			params.put("isActive", 1);
		}

		if (isFirst) {
			// we ended up with an and at the end
			query = new StringBuffer(query.subSequence(0, query.length() - 5)
					.toString());
		}

		Query hquery = em.createQuery(query.toString());

		Set<String> keys = params.keySet();

		for (String key : keys) {
			hquery.setParameter(key, params.get(key));
		}

		List list = hquery.getResultList();

		return list;
	}

	public List<TaskSummary> searchTasks(String processId,String userId, SearchFilter filter) {

		String subject = filter.getSubject();
		Date startDate = filter.getStartDate();
		Date endDate = filter.getEndDate();
		Integer priority = filter.getPriority();
		String phrase = filter.getPhrase();
		DocumentType docTypeIn = filter.getDocType();

		ADDocType docType = null;
		if (docTypeIn != null) {
//			docType = getDocumentTypeById(docTypeIn.getId());
			docType = findByRefId(docTypeIn.getRefId(), ADDocType.class);
		}

		Boolean hasAttachment = filter.hasAttachment();

		List<UserGroup> groups = LoginHelper.getHelper().getGroupsForUser(
				userId);
		String groupsIds = "";
		for (UserGroup group : groups) {
			groupsIds = groupsIds.concat(group.getName() + ",");
		}

		if (groupsIds.isEmpty()) {
			return new ArrayList<>();
		}
		groupsIds = groupsIds.substring(0, groupsIds.length() - 1);

		List<Object> params = new ArrayList<>();

		StringBuffer query = new StringBuffer(
				"select t.id "
						+
						// "d.id documentId "+
						"from localdocument d "
						+ "inner join Task t on (t.processInstanceId=d.processInstanceId) "
						+ "left join OrganizationalEntity owner on (owner.id= t.actualOwner_id and owner.DTYPE='User') "
						+ "left join PeopleAssignments_PotOwners potowners on (potowners.task_id=t.id)  "
						+ "where "
						+ "t.archived = 0 and "
						+ "(owner.id = ? "
						+ "or "
						+ "( potowners.entity_id = ? or potowners.entity_id in (?) )) "
						+ "and (:processId='' or d.processId=:processId)"

						+ "and " + "t.expirationTime is null ");

		params.add(userId);
		params.add(userId);
		params.add(groupsIds);
		params.add(processId==null? "": processId);

		boolean isFirst = false;

		if (!isFirst) {
			query.append(" AND ");
			isFirst = true;// so that we dont add another AND
		}

		if (subject != null) {
			isFirst = false;
			/**
			 * TODO:Necessary Table index has to be added; otherwise full table
			 * scan will be used
			 */
			query.append("(LOWER(d.subject) like ?");
			params.add("%" + subject.toLowerCase() + "%");

			if (phrase == null) {
				query.append(" or Lower(d.description) like ?");
				params.add("%" + subject.toLowerCase() + "%");
			}
			query.append(")");
		}

		if (!isFirst) {
			query.append(" AND ");
			isFirst = true;// so that we dont add another AND
		}

		if (startDate != null && endDate != null) {
			isFirst = false;
			query.append("t.createdOn>? " + "and " + "t.createdOn<?");

			params.add(startDate);
			params.add(endDate);
		} else if (startDate != null) {
			isFirst = false;
			/**
			 * TODO:This needs to be changed - It will force full table scan --
			 * further these functions only work on Mysql/ Fail on postgres
			 */
			// query.append(" ?<=created and (? + interval '1 day')>created ");
			// query.append("STR_TO_DATE(DATE_FORMAT(created, '%d/%m/%y'), '%d/%m/%y')=?");

			// params.add(startDate);
			// params.add(startDate);

		} else if (endDate != null) {
			isFirst = false;
			/**
			 * TODO:This needs to be changed - It will force full table scan
			 */
			// same day
			query.append(" ?>=created and (? + interval '1 day')>created ");
			// query.append("STR_TO_DATE(DATE_FORMAT(created, '%d/%m/%y'), '%d/%m/%y')=?");

			// params.add(endDate);
			// params.add(endDate);
		}

		if (!isFirst) {
			query.append(" AND ");
			isFirst = true;// so that we dont add another AND
		}

		if (priority != null) {
			isFirst = false;
			query.append("d.priority=?");
			params.add(priority);
		}

		if (!isFirst) {
			query.append(" AND ");
			isFirst = true;// so that we dont add another AND
		}

		if (phrase != null) {
			isFirst = false;
			query.append("Lower(d.description) like ?");
			params.add("%" + phrase.toLowerCase() + "%");
		}

		if (!isFirst) {
			query.append(" AND ");
			isFirst = true;// so that we dont add another AND
		}

		if (docType != null) {
			isFirst = false;
			query.append("d.docType=?");
			params.add(docType);
		}

		if (!isFirst) {
			query.append(" AND ");
			isFirst = true;// so that we dont add another AND
		}

		if (isFirst) {
			// we ended up with an "and" at the end
			query = new StringBuffer(query.subSequence(0, query.length() - 5)
					.toString());
		}

		Query hquery = em.createNativeQuery(query.toString());

		for (int i = 0; i < params.size(); i++) {
			hquery.setParameter(i + 1, params.get(i));
		}

		List<BigInteger> b_ids = hquery.getResultList();

		List<Long> ids = new ArrayList<>();

		for (BigInteger b : b_ids) {
			ids.add(b.longValue());
		}

		if (ids.isEmpty()) {
			return new ArrayList<>();
		}

		return searchTasks(ids);

		// String queryIds = "select t.id taskId " +
		// "d.id as documentId " +
		// "from localdocument d " +
		// "inner join Task t "+
		// "on t.processInstanceId=d.processInstanceId " +
		// "left join OrganizationalEntity owner " +
		// "on (owner.id= t.actualOwner_id and owner.DTYPE='User') "+
		// "left join PeopleAssignments_PotOwners potowners " +
		// "on (potowners.taskid=t.id) "+
		// "where " +
		// "t.archived = 0 and "+
		// "(owner.id = ? "+
		// " or  "+
		// "( potowners.entity_id = ? or potowners.entity_id in (?) ) "+
		// ") "+
		//
		// "and "+
		//
		// "( "+
		// "name.language = :language "+
		// "or t.names.size = 0 "+
		// ") and "+
		//
		// "( "+
		// " subject.language = :language "+
		// " or t.subjects.size = 0 "+
		// " ) and "+
		//
		// "( "+
		// "description.language = :language "+
		// "or t.descriptions.size = 0 "+
		// ")and  "+
		// "t.taskData.expirationTime is null ";

	}

	public List<TaskSummary> searchTasks(List<Long> ids) {
		
//		new TaskSummaryImpl(id, processInstanceId, name, subject, description, status,
//				priority, skipable, actualOwner, createdBy, createdOn, activationTime, 
//				expirationTime, processId, processSessionId, subTaskStrategy, parentId);
		
		String query = "select " + "distinct "
				+ "new org.jbpm.services.task.query.TaskSummaryImpl( " 
				+ "t.id, "
				+ "t.taskData.processInstanceId, " 
				+ "name.text, "
				+ "subject.text, " 
				+ "description.text, "
				+ "t.taskData.status, " 
				+ "t.priority, "
				+ "t.taskData.skipable, " 
				+ "actualOwner, " 
				+ "createdBy, "
				+ "t.taskData.createdOn, " 
				+ "t.taskData.activationTime, "
				+ "t.taskData.expirationTime, " 
				+ "t.taskData.processId, "
				+ "t.taskData.processSessionId,"
				+ "t.subTaskStrategy,"
				+ "t.taskData.parentId) " 
				+ "from " 
				+ "org.kie.api.task.model.Task t "
				+ "left join t.taskData.createdBy as createdBy "
				+ "left join t.taskData.actualOwner as actualOwner "
				+ "left join t.subjects as subject "
				+ "left join t.descriptions as description "
				+ "left join t.names as name, "

				+ "org.kie.api.task.model.OrganizationalEntity potentialOwners " + "where " +
				// "t.processInstanceId=d.processInstanceId "+
				"t.id in (:taskIds) ";

		Query hquery = getEntityManager().createQuery(query).setParameter(
				"taskIds", ids);

		List list = hquery.getResultList();

		return list;
	}

	/**
	 * Returns DocType with the provided ID
	 * 
	 * @param id
	 * @return
	 */
	public ADDocType getDocumentTypeById(Long id) {

		ADDocType docType = (ADDocType) em
				.createQuery("from ADDocType t where t.id=:id")
				.setParameter("id", id).getSingleResult();

		return docType;
	}

	public ADDocType getDocumentTypeByName(String docTypeName) {

		ADDocType docType = null;

		try {
			docType = (ADDocType) em
					.createQuery("from ADDocType t where t.name=:name")
					.setParameter("name", docTypeName).getSingleResult();
		} catch (RuntimeException e) {
		}

		return docType;
	}

	public ADDocType getDocumentTypeByDocumentId(Long documentId) {

		logger.info("###### Get Document Type for documentId="+documentId);
		assert documentId!=null;
		// ADDocType type =
		// (ADDocType)em.createQuery("select d.type FROM DocumentModel d where d.id=:documentId")
		// .setParameter("documentId", documentId).getSingleResult();

		String sql = "select d.* from ADDocType d "
				+ "where refId=(select doctyperefid from documentjson where id=?)";

		Query query = em.createNativeQuery(sql, ADDocType.class).setParameter(
				1, documentId);

		ADDocType type = getSingleResultOrNull(query);

		return type;
	}

	public String getDocumentTypeDisplayNameByDocumentId(String docRefId) {
		String sql = "select display from ADDocType d "
				+ "where refid=(select docTypeRefId from documentjson where refId=?)";

		Query query = em.createNativeQuery(sql).setParameter(1, docRefId);
		String type = null;

		try {
			type = (String) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return type;
	}

	public List<DocumentType> getDocumentTypes() {

		return getDocumentTypes(SessionHelper.getCurrentUser().getUserId());
	}
	
	public List<DocumentType> getDocumentTypes(String userId){
		String hql="select doctype.id,doctype.refid,doctype.name,doctype.display, "+
		"doctype.backgroundColor,doctype.iconStyle,  "+
		"p.refid processRefId,  "+
		"c.name categoryname, "+
		"c.refid categoryrefid,  "+
		"p.processId,  "+
		"counts.inboxcounts "+ 
		"from addoctype doctype "+  
		"inner join  "+
		"(select distinct(pdm.id), pdm.refid,pdm.processid,pdm.isactive from processdefmodel pdm "+ 
		"inner join "+
		"(select processaccess.uid,processaccess.gid, processaccess.processid from ( "+
		"select null uid,g.id gid,pga.processid from process_groupaccess pga  "+
		"inner join bgroup g on (g.id=pga.groupid)  "+
		"join usergroup ug on (g.id=ug.groupid)  "+
		"inner join buser u on(u.id=ug.userid)  "+
		"where u.userid=:userId "+
		"union "+
		"select u.id uid, null gid, pua.processid from process_useraccess pua "+ 
		"inner join buser u on (u.id=pua.userid)  "+
		"where u.userid=:userId) as processaccess) as paccess "+
		"on (paccess.processid=pdm.id) "+
		"where pdm.isActive=1) as p "+
		"on (p.id=doctype.processdefid) "+
		"left join adprocesscategory c on (c.id=doctype.categoryid) "+ 
		"left join   "+
		"(select count(*) inboxcounts,doctype.refid "+ 
		"from task t   "+
		"inner join documentjson d1 on (d1.processinstanceid=t.processinstanceid) "+  
		"inner join addoctype doctype on (d1.doctyperefid=doctype.refid)  "+
		"inner join peopleassignments_potowners potowners on   "+
		"(potowners.task_id=t.id and   "+
		"(potowners.entity_id=:userId or potowners.entity_id in "+  
		"(select g.name from bgroup g   "+
		"inner join usergroup ug on ug.groupid=g.id "+  
		"inner join buser u on u.id=ug.userid where u.userid=:userId))) "+  
		"where t.status in ('Created','Ready','Reserved','InProgress')   "+
		"group by doctype.refid) as counts on doctype.refid=counts.refId"; 
		
		List<Object[]> results = getResultList(em.createNativeQuery(hql).setParameter("userId", userId));
		
		List<DocumentType> types = new ArrayList<DocumentType>();
		
		for(Object[] row: results){
			Object value = null;
			int i=0;
			long id = ((value=row[i++])==null? 0l: ((Number)value).longValue());
			String refid = ((value=row[i++])==null? null: ((String)value));
			String name = ((value=row[i++])==null? null: ((String)value));
			String display = ((value=row[i++])==null? null: ((String)value));
			String backgroundColor = ((value=row[i++])==null? null: ((String)value));
			String iconStyle = ((value=row[i++])==null? null: ((String)value));
			String processRefId = ((value=row[i++])==null? null: ((String)value));
			String categoryname = ((value=row[i++])==null? null: ((String)value));
			String categoryrefid = ((value=row[i++])==null? null: ((String)value));
			String processId = ((value=row[i++])==null? null: ((String)value));
			int inboxCount = ((value=row[i++])==null? 0: ((Number)value).intValue());
			
			DocumentType type = new DocumentType(id, name, display, null);
			type.setRefId(refid);
			type.setIconStyle(iconStyle);
			type.setBackgroundColor(backgroundColor);
			type.setProcessRefId(processRefId);
			type.setProcessId(processId);
			type.setCategory(categoryname);
			type.setId(id);
			type.setInboxCount(inboxCount);
			
			types.add(type);
			
		}
		
		return types;

	}

//	public List<ADDocType> getDocumentTypes(String userId) {
//
//		User user = DB.getUserGroupDao().getUser(userId);
//		List<Long> groupIds = new ArrayList<>();
//		for (Group group : user.getGroups()) {
//			groupIds.add(group.getId());
//		}
//
//		List<ADDocType> docTypes = getResultList(em
//				.createQuery(
//						"SELECT distinct "
//								+ "new com.duggan.workflow.server.dao.model.ADDocType("
//								+ "t.refId," 
//								+ "t.name," 
//								+ "t.display,"
//								+ "t.backgroundColor,"
//								+ "t.iconStyle,"
//								+ "t.processDef.refId,"
//								+ "t.category) " 
//								+ "FROM "
//								+ "ADDocType t "
//								+ "inner join t.processDef def "
//								+ "left join t.processDef.users u "
//								+ "left join t.processDef.groups g "
//								+ "where (u.userId=:userId "
//								+ "or (g.id in (:groupIds) "
//								+ "and g in elements (t.processDef.groups))) "
//								+ "and t.isActive=1 "
//								+ "and def.isActive=1 "
//								+ "order by t.display")
//				.setParameter("userId", userId)
//				.setParameter("groupIds", groupIds));
//
//		return docTypes;
//	}

	/**
	 * Return form ID for a document type - The document type to form id is
	 * matched using the form name and process name
	 * 
	 * @param documentTypeId
	 * @return
	 */
	public Long getFormId(Long documentTypeId) {
		String query = "select f.id from ADForm f "
				+ "inner join ProcessDefModel p on (f.name=p.processid) "
				+ "inner join ADDocType t on t.processDefId=p.id "
				+ "where t.id=:id";
		Long value = null;

		try {
			BigInteger bint = (BigInteger) em.createNativeQuery(query)
					.setParameter("id", documentTypeId).getSingleResult();
			value = bint.longValue();
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return value;
	}

	public DetailModel getDetailById(Long detailId) {
		return (DetailModel) em.createQuery("FROM DetailModel d where id=:id")
				.setParameter("id", detailId).getSingleResult();
	}

	public TaskDelegation getTaskDelegationByTaskId(Long taskId) {
		try {
			return (TaskDelegation) em
					.createQuery("FROM TaskDelegation d where d.taskId=:taskId")
					.setParameter("taskId", taskId).getSingleResult();
		} catch (Exception e) {
		}

		return null;
	}

	/**
	 * Case/{No}/{YY} - Case/0001/14 <br/>
	 * Case/{No}/{MM}/{YY} - Case/0001/01/14 <br/>
	 * Case/{No}/{YYYY} - Case/0001/2014 <br/>
	 * <p>
	 * TODO: check the impact of locking this record to performance This record
	 * will be locked to the current transaction every time a number is
	 * requested: Using a sequence might offer better performance?
	 * 
	 * <p>
	 * 
	 * @param type
	 * @return String generated subject
	 */
	public String generateDocumentSubject(ADDocType type) {
		String format = "Case-{No}";

		Integer no = getNextCaseNo();

		if (type.getSubjectFormat() != null) {
			no = type.getLastNum();
			if (no == null || no == 0) {
				no = 0;
			}
			++no;
			type.setLastNum(no);
			format = type.getSubjectFormat();
		}

		String num = (no < 10) ? "000" + no : (no < 100) ? "00" + no
				: (no < 1000) ? "0" + no : no + "";

		SimpleDateFormat formatter = new SimpleDateFormat("YY");
		String yy = formatter.format(new Date());

		formatter = new SimpleDateFormat("yyyy");
		String yyyy = formatter.format(new Date());

		formatter = new SimpleDateFormat("MM");
		String mm = formatter.format(new Date());

		format = format.replaceAll("\\{No\\}", num).replaceAll("\\{YY\\}", yy)
				.replaceAll("\\{YYYY\\}", yyyy).replaceAll("\\{MM\\}", mm);

		return format;
	}

	private int getNextCaseNo() {

		String sql = "select nextval('caseno_sequence')";
		Number value = getSingleResultOrNull(em.createNativeQuery(sql));

		return value.intValue();
	}

	public boolean deleteDocument(Long documentId) {
		DocumentModel document = getById(documentId);

		document.setIsActive(0);
		save(document);

		return true;
	}

	public boolean deleteDocument(String docRefId) {
		DocumentModel document = findByRefId(docRefId, DocumentModel.class);
		document.setIsActive(0);
		save(document);

		return true;
	}

	public String getDocumentSubject(Long documentId) {

		String sql = "select caseno from documentjson where id=?";
		String value = getSingleResultOrNull(em.createNativeQuery(sql)
				.setParameter(1, documentId));

		return value;
	}

	public boolean exists(String subject) {

		String sql = "select count(id) from DocumentModelJson d where d.caseNo=:subject";
		Query query = em.createQuery(sql).setParameter("subject", subject);

		Long result = (Long) query.getSingleResult();

		return result > 0;
	}

	public DocumentModel getDocumentByIdAndUser(Long id, String userId) {
		String jpql = "from DocumentModel m where m.createdBy=:userId and m.id=:id";

		Query query = em.createQuery(jpql).setParameter("id", id)
				.setParameter("userId", userId);

		return getSingleResultOrNull(query);
	}

	public DocumentModel getDocumentByIdAndUser(String refId, String userId) {
		String jpql = "from DocumentModel m where m.createdBy=:userId and m.refId=:refId";

		Query query = em.createQuery(jpql).setParameter("refId", refId)
				.setParameter("userId", userId);

		return getSingleResultOrNull(query);
	}

	public void updateStatus(long processInstanceId, DocStatus completed) {
		DocumentModel model = getDocumentByProcessInstanceId(processInstanceId);
		model.setStatus(completed);
		save(model);
	}

	public int getUnassigned(String processId) {
		if(processId==null){
			processId="";
		}
		Query query = em
				.createNamedQuery("TasksUnassignedCount")
				.setParameter("language", "en-UK")
				.setParameter("processId", processId)

				.setParameter("status",
						Arrays.asList(Status.Created, Status.Ready));

		Number count = getSingleResultOrNull(query);
		return count.intValue();
	}

	public List<TaskSummary> getUnassignedTasks(String processId,int offset,int length) {
		Query query = em
				.createNativeQuery("select id from task t "
						+ "left join peopleassignments_potowners o on (o.task_id=t.id)  "
						+ "where status in ('Ready', 'Created') "
						+ "and o.task_id is null "
						+ "and (:processId='' or t.processId=:processId) "
						+ "and actualowner_id is null")
						.setParameter("processId", processId);
		List<BigInteger> idz = getResultList(query,offset,length);

		List<Long> ids = new ArrayList<>();
		for (BigInteger id : idz) {
			ids.add(id.longValue());
		}

		if (ids.isEmpty()) {
			return new ArrayList<>();
		}

		return DB.getDocumentDao().searchTasks(ids);
	}

	public Collection<ADValue> getValues(DocumentModel model) {

		return getResultList(getEntityManager().createQuery(
				"FROM ADValue a where a.document=:document").setParameter(
				"document", model));
	}

	public Collection<DetailModel> getDetails(DocumentModel model) {
		return getResultList(getEntityManager().createQuery(
				"FROM DetailModel a where a.document=:document").setParameter(
				"document", model));
	}

	public DocumentLineJson findDocumentLinesJson(String docRefId, String name) {

		return getSingleResultOrNull(getEntityManager()
				.createQuery(
						"From DocumentLineJson j "
								+ "where j.docRefId=:docRefId and j.name=:name order by id")
				.setParameter("docRefId", docRefId).setParameter("name", name));
	}

	public void deleteJsonDoc(String docRefId) {
		String docSql = "delete from documentjson where refId=:docRefId";
		
		getEntityManager().createNativeQuery(docSql).setParameter("docRefId",
				docRefId);
	}
	
	public void deleteJsonDocLines(String docRefId) {
		String docLineSql = "delete from documentlinejson where docRefId=:docRefId";
		getEntityManager().createNativeQuery(docLineSql).setParameter(
				"docRefId", docRefId).executeUpdate();
	}
	
	public void deleteJsonDocLine(List<String> lineRefIds) {
		if(lineRefIds.isEmpty()){
			return;
		}
		
		String sql = "delete from DocumentLineJson where refId in (:refIds)";
		logger.info("DeleteJsonDocLine "+sql.replace(":refIds", lineRefIds.toString()));
		
		getEntityManager()
			.createQuery(sql)
			.setParameter("refIds", lineRefIds)
			.executeUpdate();
		
	}
	
	public void deleteJsonDocLine(String docRefId, String gridName) {
		String docLineSql = "delete from documentlinejson where docRefId=:docRefId and name=:name";
		getEntityManager().createNativeQuery(docLineSql)
		.setParameter("docRefId", docRefId)
		.setParameter("name", gridName)
		.executeUpdate();
	}
	
	public List<String> getDocumentLinesRefs(String docRefId, String key) {
		
		String sql = "select refId from documentlinejson where docRefId=:docRefId and name=:name";
		
		List<String> refIds = getResultList(getEntityManager().createNativeQuery(sql)
				.setParameter("docRefId", docRefId)
				.setParameter("name", key));
		
		return refIds;
	}


	public void deleteJsonDocLine(String lineRefId) {
		String docLineSql = "delete from documentlinejson where refId=:refId";

		getEntityManager().createNativeQuery(docLineSql).setParameter("refId",
				docLineSql);
	}

	public Document getDocJson(String docRefId) {
		return getDocJson(docRefId, true);
	}
	
	/**
	 * 
	 * @param docRefId
	 * @param loadDetails - Load fieldValues & document lines as well.
	 * @return
	 */
	public Document getDocJson(String docRefId, boolean loadDetails) {

		DocumentModelJson json = findByRefId(docRefId, DocumentModelJson.class);
		
		return getDocJson(json,loadDetails);
	}

	private Document getDocJson(DocumentModelJson json, boolean loadDetails) {
		Document document = json.getDocument();
		
		if(loadDetails){
			document.setValues(json.getData() == null ? new HashMap<String, Value>()
					: json.getData().getValuesMap());
			document.setId(json.getId());
			addDocLinesJson(document);
		}
		return document;
	}

	private void addDocLinesJson(Document document) {

		List<DocumentLineJson> lines = getResultList(getEntityManager()
				.createQuery(
						"from DocumentLineJson l where l.docRefId=:docRefId order by id")
				.setParameter("docRefId", document.getRefId()));

		for (DocumentLineJson json : lines) {
			DocumentLine line = json.getDocumentLine();
			line.setDocumentId(document.getId());
			line.setDocRefId(document.getRefId());
			line.setValues(json.getData() == null ? new HashMap<String, Value>()
					: json.getData().getValuesMap());
			document.addDetail(line);
		}
	}

	public List<Doc> getAllDocumentsJson(String processId,int offset, int length,
			boolean loadValues, boolean loadLines, DocStatus... status) {

		@SuppressWarnings("unchecked")
		List<DocumentModelJson> documents = em
				.createQuery(
						"FROM DocumentModelJson d where status in (:status) and "
								+ "createdBy=:createdBy "
								+ "and isActive=:isActive "
								+ "and (:processId='' or d.processId=:processId)"
								+ "order by created desc")
				.setParameter("status", Arrays.asList(status))
				.setParameter("createdBy",
						SessionHelper.getCurrentUser().getUserId())
				.setParameter("processId", processId==null? "": processId)
				.setParameter("isActive", 1).setFirstResult(offset)
				.setMaxResults(length).getResultList();

		ArrayList<Doc> docs = new ArrayList<Doc>();
		for (DocumentModelJson jsonDoc : documents) {
			Document doc = jsonDoc.getDocument();
			doc.setId(jsonDoc.getId());
			if(loadValues){
				doc.setValues(jsonDoc.getData().getValuesMap());
			}
			
			if (loadLines) {
				addDocLinesJson(doc);
			}
			docs.add(doc);

		}

		return docs;
	}

	public Document getDocJsonByUserRef(String docRefId, String creatorUserId, boolean loadDetails) {
		DocumentModelJson json = getSingleResultOrNull(em.createQuery(
						"FROM DocumentModelJson d where "
						+ "refId=:refId ")
						.setParameter("refId", docRefId));
//						+ "and "
//						+ "createdBy=:createdBy")
//				.setParameter("createdBy",creatorUserId)
				
		return getDocJson(json, loadDetails);
	}

	public List<Doc> getRecentTasks(String processId, String userId, int offset,int limit){
		String hql="select * from( "+
		"select d.id, d.refId docRefId,d.createdby, d.caseno,'Fill in request form' taskName, "+
		"d.processinstanceid,d.created createdon,null completedon,d.status status,false isTask, "+
		"doctype.refid doctyperef,doctype.display "+
		"from documentjson d  "+
		"inner join addoctype doctype on (d.doctyperefid=doctype.refid) "+
		"where "+
		"d.createdby=:userId  "+
		"and (:processId='' or d.processId=:processId)"+
		"union all "+
		"select t.id, d1.refId docRefId,t.actualowner_id,d1.caseno,i.text taskName, "+
		"t.processinstanceid,t.createdon,t.completedon,t.status, true isTask, "+
		"doctype.refid doctyperef,doctype.display   "+
		"from task t "+
		"inner join i18ntext i on i.task_names_id=t.id "+ 
		"inner join documentjson d1 on (d1.processinstanceid=t.processinstanceid) "+ 
		"inner join addoctype doctype on (d1.doctyperefid=doctype.refid) "+
		"inner join peopleassignments_potowners potowners on  "+
		"(potowners.task_id=t.id and  "+
		"(potowners.entity_id=:userId or potowners.entity_id in "+ 
		"(select g.name from bgroup g  "+
		"inner join usergroup ug on ug.groupid=g.id "+ 
		"inner join buser u on u.id=ug.userid where u.userid=:userId))) "+
		"where "+
		"(:processId='' or d1.processId=:processId) "+
		") as docstasks order by  "+
		"(case when completedon is null then "+
		"createdon  "+
		"else  "+
		"completedon "+ 
		"end) desc"; 
		
		List<Doc> tasks = new ArrayList<Doc>();
		
		List<Object[]> results = getResultList(
				em.createNativeQuery(hql)
				.setParameter("userId", userId)
				.setParameter("processId", processId==null? "": processId),offset,limit);
		
		for(Object[] row: results){
			Object value = null;
			int i=0;
			Long id = ((value=row[i++])==null? null: ((Number)value).longValue());
			String docRefId = ((value=row[i++])==null? null: ((String)value));
			String createdby = ((value=row[i++])==null? null: ((String)value));
			String caseno = ((value=row[i++])==null? null: ((String)value));
			String taskName = ((value=row[i++])==null? null: ((String)value));
			Long processInstanceId = ((value=row[i++])==null? null: ((Number)value).longValue());
			Date createdon = ((value=row[i++])==null? null: ((Date)value));
			Date completedon = ((value=row[i++])==null? null: ((Date)value));
			String status = ((value=row[i++])==null? null: ((String)value).toUpperCase());
			Boolean isTask = ((value=row[i++])==null? null: ((Boolean)value));
			String doctyperef = ((value=row[i++])==null? null: ((String)value));
			String doctypeDisplay = ((value=row[i++])==null? null: ((String)value));
			
			if(isTask){
				HTSummary summary = new HTSummary();
				summary.setRefId(docRefId);
				summary.setId(id);
				summary.setCompletedOn(completedon);
				summary.setCreated(createdon);
				summary.setCaseNo(caseno);
//				summary.setCurrentTaskId(currentTaskId);
//				summary.setCurrentTaskName(currentTaskName);
//				summary.setDelegate(delegate);
				summary.setDocumentDate(createdon);
//				summary.setEndDateDue(endDateDue);
				summary.setOwner(new HTUser(createdby));
//				summary.setPotentialOwners(potentialOwners);
				summary.setProcessInstanceId(processInstanceId);
				summary.setProcessName(doctypeDisplay);
				summary.setStatus(HTStatus.valueOf(status.toUpperCase()));
				summary.setTaskName(taskName);
				try{
					String displayName = JBPMHelper.get().getDisplayName(id);
					if(displayName!=null){
						summary.setName(displayName);
					}
				}catch(Exception e){
					summary.setName(taskName);
				}
				
				tasks.add(summary);
			}else{
				Document doc = new Document();
				doc.setCaseNo(caseno);
				doc.setCreated(createdon);
//				doc.setCurrentTaskId(currentTaskId);
//				doc.setCurrentTaskName(currentTaskName);
//				doc.setDateDue(dateDue);
//				doc.setDateSubmitted(dateSubmitted);
//				doc.setDescription(description);
//				doc.setHasAttachment(hasAttachment);
				doc.setId(id);
//				doc.setNodeName(nodeName);
				doc.setOwner(new HTUser(createdby));
				doc.setProcessId(processId);
				DocumentType type = new DocumentType();
				type.setRefId(doctyperef);
				type.setProcessId(processId);
				type.setDisplayName(doctypeDisplay);
				doc.setType(type);
				doc.setTaskActualOwner(new HTUser(createdby));
				doc.setStatus(DocStatus.valueOf(status));
				doc.setRefId(docRefId);
				doc.setCurrentTaskName(taskName);
				doc.setNodeName(taskName);
//				doc.setProcessStatus(HTStatus.valueOf(arg0));
				doc.setProcessName(doctypeDisplay);
				doc.setProcessInstanceId(processInstanceId);
				tasks.add(doc);
			}
				
		}
		
		
		return tasks;
	}

	public List<TaskSummary> getTasksOwnedPerProcess(String processId,
			String userId, List<Status> status, String language, int offset, int length) {
		return getResultList(em.createNamedQuery("TasksOwnedWithParticularStatusAndProcessId")
				.setParameter("userId", userId)
				.setParameter("language", language)
				.setParameter("status", status)
				.setParameter("processId", processId),offset,length);
	}

	public List<TaskSummary> getTasksAssignedAsPotentialOwnerByStatusAndProcessId(
			String processId, String userId, List<Status> status,
			String language, int offset, int length) {
		
		List<String> groups = DB.getUserGroupDao().getGroupsForUser(userId);
		if(groups.isEmpty()){
			//DUGGAN - 25/10/2016 - ADDED TO FIX HIBERNATE 'unexpected end of subtree errors' 
			//CAUSED BY EMPTY IN() STATEMENTS IN THE HQL QUERIES BELOW
			groups.add("UNDEFINED");
		}
		
		return getResultList(em.createNamedQuery("TasksAssignedAsPotentialOwnerByStatusWithGroupsAndProcessId")
				.setParameter("userId", userId)
				.setParameter("groupIds", groups)
				.setParameter("language", language)
				.setParameter("status", status)
				.setParameter("processId", processId)
				,offset,length);
	}
}
