package com.duggan.workflow.server.dao.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


@Entity
@Table(name="localattachment")
public class LocalAttachment extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Lob
	private byte[] attachment;
	
	private boolean archived;

	public LocalAttachment(){
		super();
	}
	
	public LocalAttachment(Long id,String name, byte[] attachment){
		this();
		this.name=name;
		this.attachment=attachment;
		this.id=id;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public boolean isArchived() {
		return archived;
	}

}
