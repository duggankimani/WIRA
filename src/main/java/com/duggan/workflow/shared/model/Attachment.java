package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.ArrayList;

import com.sencha.gxt.data.shared.TreeStore;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.SerializableObj;

public class Attachment extends SerializableObj implements Serializable,TreeStore.TreeNode<Attachment> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String ID = "id";

	public static final String TYPE = "type";

	public static final String SIZE = "size";

	public static final String CONTENTTYPE = "contenttype";

	public static final String PATH = "path";

	public static final String NAME = "name";
	
	private Long id;
	private String name;
	private AttachmentType type = AttachmentType.UPLOADED;
	private boolean archived;
	private Long documentid;
	private String docRefId;
	private String fieldName;
	private Long processDefId;
	private Long size;
	private String sizeStr;
	private String contentType;
	private HTUser createdBy;
	private Date created;
	private ArrayList<Attachment> children;
	private String documentType;
	private String subject;
	private String path;
	private String caseNo;
	private String processName;
	private HTStatus processStatus;
	private boolean isDirectory;
	private String processRefId;
	private Attachment parent;
	private String parentRefId;
	private DocStatus docStatus;
	private int childCount;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isArchived() {
		return archived;
	}
	public void setArchived(boolean archived) {
		this.archived = archived;
	}
	public Long getDocumentid() {
		return documentid;
	}
	public void setDocumentid(Long documentid) {
		this.documentid = documentid;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getSizeAsStr() {
		return sizeStr;
	}
	public void setSizeStr(String size) {
		this.sizeStr = size;
	}
	public HTUser getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(HTUser createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getSizeStr() {
		return sizeStr;
	}
	public Long getProcessDefId() {
		return processDefId;
	}
	public void setProcessDefId(Long processDefId) {
		this.processDefId = processDefId;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public void setParent(Attachment parent) {
		this.parent = parent;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}
	public void addChild(Attachment child){
		if(children==null){
			children = new ArrayList<Attachment>();
		}
		childCount+=child.getChildCount();
		isDirectory=true;
		children.add(child);
		child.setParent(this);
	}
	
	public void setChildren(ArrayList<Attachment> children){
		this.children = children;
	}
	
	public boolean hasChildren(){
		
		return children!=null && children.size()>0; 
	}
	
	public ArrayList<Attachment> getChildren(){
		sort();
		if(children==null || children.size()==0)
			return null;
		
		return children;
	}

	@Override
	public Attachment getData() {
		
		return this;
	}
	
	public void sort(){
		if(children!=null){
			Collections.sort(children, new Comparator<Attachment>() {
				@Override
				public int compare(Attachment o1, Attachment o2) {
					Integer score1 = o1.getScore();
					Integer score2 = o2.getScore();
					
					int comparison = score1.compareTo(score2);
					
					if(comparison==0)
						comparison = o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
					
					return comparison;
				}
			});
		}
	}
	
	private int getScore(){
		if(isDirectory){
			return 1;
		}
		
		return 2;
	}
	
	public Attachment getParent() {
		return parent;
	}
	
	public boolean isDirectory() {
		return isDirectory;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDocRefId() {
		return docRefId;
	}
	public void setDocRefId(String docRefId) {
		this.docRefId = docRefId;
	}
	public AttachmentType getType() {
		return type;
	}
	public void setType(AttachmentType type) {
		this.type = type;
	}
	public String getParentRefId() {
		return parentRefId;
	}
	public void setParentRefId(String parentRefId) {
		this.parentRefId = parentRefId;
	}
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public HTStatus getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(HTStatus processStatus) {
		this.processStatus = processStatus;
	}
	public String getProcessRefId() {
		return processRefId;
	}
	public void setProcessRefId(String processRefId) {
		this.processRefId = processRefId;
	}
	public DocStatus getDocStatus() {
		return docStatus;
	}
	public void setDocStatus(DocStatus docStatus) {
		this.docStatus = docStatus;
	}
	public int getChildCount() {
		return childCount;
	}
	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}
	
}
