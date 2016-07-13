package com.duggan.workflow.server.dao.helper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.jbpm.executor.commands.SendMailCommand;
import org.jbpm.executor.impl.ExecutorImpl;

import com.duggan.workflow.server.dao.CommentDaoImpl;
import com.duggan.workflow.server.dao.DocumentDaoImpl;
import com.duggan.workflow.server.dao.model.CommentModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.CustomEmailHandler;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.model.Document;
import com.wira.commons.shared.models.HTUser;

public class CommentDaoHelper {

	public static Comment saveComment(Comment comment){
		
		CommentDaoImpl dao = DB.getCommentDao();
		
		CommentModel model = new CommentModel();
		
		if(comment.getId()!=null){
			model = dao.getComment(comment.getId());			
		}
		
		copyData(model, comment);
		
		model = dao.saveOrUpdate(model);
		
		comment.setId(model.getId());
		
		sendEmail(comment);
		
		return comment;
	}
	
	/**
	 * TODO: This mechanism will fail for some email variables that are 
	 * associated with the current task as opposed to the original document.
	 * <p>
	 * This is because the additional metadata captured in the HTask is not
	 * available in the original document.
	 * <p> 
	 * @param comment
	 */
	private static void sendEmail(Comment comment) {
		Document doc = null;
		if(comment.getDocRefId()!=null){
			doc = DocumentDaoHelper.getDocument(comment.getDocRefId());
		}else if(comment.getDocumentId()!=null){
			doc = DocumentDaoHelper.getDocument(comment.getDocumentId());
		}
		
		List<HTUser> recipients= DB.getCommentDao().getEmailRecipients(doc.getId());
		
		Map<String, Object> params = new HashMap<>();
		params.put(SendMailCommand.SUBJECT, SessionHelper.getCurrentUser().getFullName()+" comment on task "
				+ doc.getCaseNo());
		
		try{
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("comment-email.html");
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			IOUtils.copy(is, bout);
			String htmlTemplate = new String(bout.toByteArray());
			
			params.put("commenter", SessionHelper.getCurrentUser().getFullName());
			params.put("caseNumber", doc.getCaseNo());
			params.put("commentDate", new Date());
			params.put("initiator", SessionHelper.getCurrentUser());
			params.put("Body", comment.getComment());
			new CustomEmailHandler().sendMail(htmlTemplate, doc, recipients, params);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		 
		
	}

	public static List<Comment> getAllComments(String userId){
		CommentDaoImpl dao = DB.getCommentDao();
		List<CommentModel> models = dao.getAllComments(userId);
		
		return copyData(models);
	}
	
	private static List<Comment> copyData(List<CommentModel> models) {
		List<Comment> Comments = new ArrayList<>();
		
		for(CommentModel m:models){
			Comment note = new Comment();
			copyData(note, m);
			Comments.add(note);
		}
		
		return Comments;
	}


	private static void copyData(CommentModel commentTo,
			Comment modelFrom) {
		assert commentTo!=null;
				
		if(commentTo.getId()==null){
			commentTo.setCreated(new Date());
			
			if(SessionHelper.getCurrentUser()!=null)
				commentTo.setCreatedBy(SessionHelper.getCurrentUser().getUserId());
		}else{
			commentTo.setUpdated(new Date());
			if(SessionHelper.getCurrentUser()!=null)
				commentTo.setUpdatedBy(SessionHelper.getCurrentUser().getUserId());
		}
		
		commentTo.setDocumentId(modelFrom.getDocumentId());
		commentTo.setDocRefId(modelFrom.getDocRefId());
		commentTo.setComment(modelFrom.getComment());
		commentTo.setId(modelFrom.getId());
		commentTo.setUserId(modelFrom.getUserId());
		commentTo.setParentId(modelFrom.getParentId());
		
	}
	
	private static void copyData(Comment commentTo,
			CommentModel modelFrom) {
				
		commentTo.setDocumentId(modelFrom.getDocumentId());
		
		String owner = modelFrom.getCreatedBy();
		HTUser createdBy = LoginHelper.get().getUser(owner);
		commentTo.setCreated(modelFrom.getCreated());
		commentTo.setCreatedBy(createdBy);
		commentTo.setDocumentId(modelFrom.getDocumentId());
		commentTo.setDocRefId(modelFrom.getDocRefId());
		commentTo.setId(modelFrom.getId());
		commentTo.setUpdated(modelFrom.getUpdated());
		commentTo.setUpdatedBy(modelFrom.getUpdatedBy());
		commentTo.setUserId(commentTo.getUserId());
		commentTo.setComment(modelFrom.getComment());
		commentTo.setParentId(modelFrom.getParentId());
		
		DocumentDaoImpl dao = DB.getDocumentDao();
		String name = dao.getDocumentTypeByDocumentId(modelFrom.getDocumentId()).getDisplay();
		commentTo.setDocType(name);
		
		String subject = dao.getDocumentSubject(modelFrom.getDocumentId());
		commentTo.setSubject(subject);
	}

	public static void delete(Long id) {
		CommentDaoImpl dao = DB.getCommentDao();
		dao.delete(id);
	}

	public static List<Comment> getAllCommentsByDocumentId(Long documentId) {
		CommentDaoImpl dao = DB.getCommentDao();
		List<CommentModel> comments = dao.getAllComments(documentId);
		
		List<Comment> rtn = copyData(comments);
		
		return rtn;
	}
	
	public static List<Comment> getAllCommentsByDocRefId(String docRefId) {
		CommentDaoImpl dao = DB.getCommentDao();
		List<CommentModel> comments = dao.getAllCommentsByDocRefId(docRefId);
		
		List<Comment> rtn = copyData(comments);
		
		return rtn;
	}
}
