package com.duggan.workflow.server.dao.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name="localcomment",indexes={@Index(name="idx_ref_id",columnList="refId")})
public class CommentModel extends PO{

	private static final long serialVersionUID = 6882858941033696924L;
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=5000,nullable=false)
	private String comment;
	
	private String userId;
	
	@Column(nullable=false)
	private Long documentId;
	
	private String docRefId;
	
	private Long parentId;
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getDocumentId() {
		return documentId;
	}
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getDocRefId() {
		return docRefId;
	}
	public void setDocRefId(String docRefId) {
		this.docRefId = docRefId;
	}
	
}
