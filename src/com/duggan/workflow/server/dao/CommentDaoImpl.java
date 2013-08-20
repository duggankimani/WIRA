package com.duggan.workflow.server.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.model.CommentModel;
import com.duggan.workflow.server.helper.session.SessionHelper;

public class CommentDaoImpl {

	EntityManager em;
	
	public CommentDaoImpl(EntityManager em) {
		this.em = em;
	}

	public CommentModel getComment(Long id) {
		
		List lst = em.createQuery("FROM CommentModel n where id= :id").setParameter("id", id).getResultList();
		
		if(lst.size()>0){
			return (CommentModel)lst.get(0);
		}
		return null;
	}

	public CommentModel saveOrUpdate(CommentModel comment) {
		if(comment.getId()==null){
			comment.setCreated(new Date());
			comment.setCreatedBy(SessionHelper.getCurrentUser().getId());
		}else{
			comment.setUpdated(new Date());
			comment.setUpdatedBy(SessionHelper.getCurrentUser().getId());
		}
		em.persist(comment);
		return comment;
	}

	@SuppressWarnings("unchecked")
	public List<CommentModel> getAllComments(String userId) {

		return em.createQuery("FROM CommentModel n where n.userId=:userId order by created desc")
				.setParameter("userId", userId)
				.getResultList();
	}

	public void delete(Long id) {
		em.remove(getComment(id));
	}

	@SuppressWarnings("unchecked")
	public List<CommentModel> getAllComments(Long documentId) {
		
		return em.createQuery("FROM CommentModel n where n.documentId=:documentId order by created desc")
				.setParameter("documentId", documentId)
				.getResultList();
	}

}
