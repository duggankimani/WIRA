package com.duggan.workflow.server.dao.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.duggan.workflow.shared.model.settings.SETTINGNAME;


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
	
	private long size;
	
	private String contentType;
	
	@Enumerated(EnumType.STRING)
	private SETTINGNAME settingName;

	@Lob
	private byte[] attachment;
	
	private boolean archived;
	
	//This is meant for output documents - eg Requisitions/HR/REQ-IPA-009-14.pdf
	//This will be used to dynamically generate the document tree in the front end
	private String path;

	//UserId=Username
	private String imageUserId;
	
	@ManyToOne
	@JoinColumn(name="documentId",referencedColumnName="id")
	private DocumentModel document;
	
	//Form Field Name; against which this file was uploaded
	private String fieldName;
	
	@ManyToOne
	@JoinColumn(name="processDefId", referencedColumnName="id")
	private ProcessDefModel processDef;
	
	@ManyToOne
	@JoinColumn(name="processDefIdImage", referencedColumnName="id")
	private ProcessDefModel processDefImage;
	
	@OneToOne(fetch=FetchType.LAZY)
	private ADOutputDoc outputDoc;

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

	public DocumentModel getDocument() {
		return document;
	}

	public void setDocument(DocumentModel document) {
		this.document = document;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public ProcessDefModel getProcessDef() {
		return processDef;
	}

	public void setProcessDef(ProcessDefModel processDef) {
		this.processDef = processDef;
	}

	public ProcessDefModel getProcessDefImage() {
		return processDefImage;
	}

	public void setProcessDefImage(ProcessDefModel processDefImage) {
		this.processDefImage = processDefImage;
	}

	public String getImageUserId() {
		return imageUserId;
	}

	public void setImageUserId(String imageUserId) {
		this.imageUserId = imageUserId;
	}

	public SETTINGNAME getSettingName() {
		return settingName;
	}

	public void setSettingName(SETTINGNAME settingName) {
		this.settingName = settingName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public ADOutputDoc getOutputDoc() {
		return outputDoc;
	}

	public void setOutputDoc(ADOutputDoc outputDoc) {
		this.outputDoc = outputDoc;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
