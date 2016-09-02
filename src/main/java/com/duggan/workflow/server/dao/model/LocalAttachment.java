package com.duggan.workflow.server.dao.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.duggan.workflow.shared.model.AttachmentType;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;


@Entity
@Table(name="localattachment")
@NamedNativeQueries({
		@NamedNativeQuery(name="Attachment.GetAllAttachmentsByUser",
		resultSetMapping="Attachment.GetAllAttachmentsMapping",
		query="select a.refid,"
				+ "a.name,"
				+ "a.created,"
				+ "a.createdby,"
				+ "concat(u.firstname,' ',u.lastname) creator,"
				+ "d.subject,p.name,pinfo.state "
				+ "from localattachment a "
				+ "inner join buser u on (u.userid=a.createdby) "
				+ "inner join localdocument d on (d.id=a.documentid) "
				+ "inner join addoctype t on (t.id=d.doctype) "
				+ "inner join processdefmodel p on (p.id=t.processdefid) "
				+ "left join processinstanceinfo pinfo "
				+ "on (pinfo.instanceid=d.processinstanceid) "
				+ "where "
				+ "a.isActive=1 and (a.createdby=:userId or :isUserAdmin)"),
				
			@NamedNativeQuery(name="Attachment.GetDirectoryTree",
			resultSetMapping="Attachment.GetDirectoryTreeMapping",
			query="With recursive tree as "
					+ "("
					+ "select id,refid,name,parentid, "
					+ "cast(null as varchar) parentRefId,0 ct,cast(id as text) AS path "
					+ "from localattachment p where parentid is null and p.isdirectory=1 "
					+ "union "
					+ "select f.id,f.refid,f.name,f.parentid,tree.refid as parentRefId,"
					+ "coalesce(c.ct,0),tree.path || '-' || cast(f.id as text) AS path "
					+ "from tree "
					+ "join localattachment f "
					+ "on (f.parentid=tree.id and f.isDirectory=1) "
					+ "left join (select cast(count(*) as integer) ct,parentid "
					+ "from localattachment "
					+ "where isdirectory=0 "
					+ "group by parentid) c on c.parentid=f.id"
					+ ") "
					+ "select id,refid,name,parentid,parentRefId,ct from tree order by path"),
					
				@NamedNativeQuery(name="Attachment.GetProcessTree",
				resultSetMapping="Attachment.GetProcessTreeMapping",
				query="select pm.refid,pm.name,pm.processid,c.ct "
						+ "from processdefmodel pm "
						+ "inner join addoctype t on t.processdefid=pm.id "
						+ "left join (select count(*) ct, d.doctype from localdocument d "
						+ "inner join localattachment f "
						+ "on (f.documentid=d.id) group by doctype) c on c.doctype=t.id order by pm.name asc"),
								
				@NamedNativeQuery(name="Attachment.GetAttachmentOwners",
				resultSetMapping="Attachment.GetAttachmentOwnersMapping",
				query="select u.refId,"
						+ "a.createdby, "
						+ "concat(u.lastname,' ',u.lastname) as names, "
						+ "a.ct "
						+ "from  "
						+ "(select count(*) ct, createdby from localattachment group by createdby) as a "
						+ "inner join buser u  on (u.userid=a.createdby) order by names asc"),

})

@SqlResultSetMappings({@SqlResultSetMapping(name="Attachment.GetAllAttachmentsMapping",
columns={@ColumnResult(name="refid",type=String.class),
		@ColumnResult(name="name",type=String.class),
		@ColumnResult(name="created",type=Date.class),
		@ColumnResult(name="createdby",type=String.class),
		@ColumnResult(name="creator",type=String.class),
		@ColumnResult(name="subject",type=String.class),
		@ColumnResult(name="name",type=String.class),
		@ColumnResult(name="state",type=Integer.class)
}),

@SqlResultSetMapping(name="Attachment.GetDirectoryTreeMapping",
columns={@ColumnResult(name="id",type=Long.class),
		@ColumnResult(name="refid",type=String.class),
		@ColumnResult(name="name",type=String.class),
		@ColumnResult(name="parentid",type=Long.class),
		@ColumnResult(name="ct",type=Integer.class),
		@ColumnResult(name="parentRefId",type=String.class)
}),

@SqlResultSetMapping(name="Attachment.GetProcessTreeMapping",
columns={
		@ColumnResult(name="refid",type=String.class),
		@ColumnResult(name="name",type=String.class),
		@ColumnResult(name="processid",type=String.class),
		@ColumnResult(name="ct",type=Integer.class)
}),

@SqlResultSetMapping(name="Attachment.GetAttachmentOwnersMapping",
columns={
		@ColumnResult(name="refid",type=String.class),
		@ColumnResult(name="createdby",type=String.class),
		@ColumnResult(name="names",type=String.class),
		@ColumnResult(name="ct",type=Integer.class)
})

})
@XmlRootElement(name="attachment")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocalAttachment extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlTransient
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@XmlAttribute
	private String name;
	
	@XmlAttribute
	private AttachmentType type = AttachmentType.UPLOADED;
	
	@XmlAttribute
	private long size;
	
	@XmlAttribute
	private String contentType;
	
	@XmlAttribute
	@Enumerated(EnumType.STRING)
	private SETTINGNAME settingName;

	@XmlTransient
	@Basic(fetch=FetchType.LAZY)
	@Lob
	private byte[] attachment;
	
	@XmlAttribute
	private boolean archived;
	
	@XmlAttribute
	private Integer isDirectory=0;
	
	
	//This is meant for output documents - eg Requisitions/HR/REQ-IPA-009-14.pdf
	//This will be used to dynamically generate the document tree in the front end
	@XmlAttribute
	private String path;

	//UserId=Username
	@XmlAttribute
	private String imageUserId;
	
	private String docRefId;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="documentId",referencedColumnName="id")
	private DocumentModel document;
	
	//Form Field Name; against which this file was uploaded
	private String fieldName;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="processDefId", referencedColumnName="id")
	private ProcessDefModel processDef;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="processDefIdImage", referencedColumnName="id")
	private ProcessDefModel processDefImage;
	
	@XmlTransient
	@OneToOne(fetch=FetchType.LAZY)
	private ADOutputDoc outputDoc;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="parentid")
	private LocalAttachment parent;
	
	@XmlTransient
	@OneToMany(fetch=FetchType.LAZY, cascade= {CascadeType.REMOVE}, mappedBy="parent")
	private Set<LocalAttachment>  children = new HashSet<LocalAttachment>();

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

	public AttachmentType getType() {
		return type;
	}

	public void setType(AttachmentType type) {
		this.type = type;
	}

	public Boolean isDirectory() {
		return isDirectory==1;
	}

	public void setDirectory(Boolean isDirectory) {
		this.isDirectory = isDirectory==null? 0: isDirectory?  1:0;
	}

	public LocalAttachment getParent() {
		return parent;
	}

	public void setParent(LocalAttachment parent) {
		this.parent = parent;
	}

	public String getDocRefId() {
		return docRefId;
	}

	public void setDocRefId(String docRefId) {
		this.docRefId = docRefId;
	}

}
