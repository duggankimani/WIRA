package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author duggan
 *
 */
public class HTComment implements Serializable {

	private static final long serialVersionUID = 9157271983632678635L;
	private String text;
	private Date addedAt;
	private HTUser addedBy;
	private Long id;

	public HTComment() {
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setAddedAt(Date addedAt) {
		this.addedAt = addedAt;
	}

	public void setAddedBy(HTUser addedBy) {
		this.addedBy = addedBy;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public Date getAddedAt() {
		return addedAt;
	}

	public HTUser getAddedBy() {
		return addedBy;
	}

	public Long getId() {
		return id;
	}
}
