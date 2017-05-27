package com.duggan.workflow.shared.model;

import java.util.ArrayList;
import java.util.Date;

import com.wira.commons.shared.models.Listable;
import com.wira.commons.shared.models.SerializableObj;

public class ProcessDef extends SerializableObj implements Listable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME = "name";

	public static final String PROCESSID = "processid";

	public static final String BACKGROUNDCOLOR = "backgroundcolor";

	public static final String ICONSTYLE = "iconstyle";

	public static final String FILENAME = "filename";

	public static final String DESCRIPTION = "description";

	public static final String IMAGENAME = "imagename";

	public static final String CATEGORY = "category";

	public static final String ID = "id";

	private Long id;
	private String name;
	private String processId;
	private String backgroundColor;
	private String iconStyle;
	
	private ArrayList<DocumentType> docTypes = new ArrayList<DocumentType>();
	private Date lastModified;
	private Long fileId;
	private String fileName;
	private String description;
	private Status status;
	private Long imageId;
	private String imageName;
	private ArrayList<Attachment> files = new ArrayList<Attachment>();
	private ProcessCategory category;
	private ArrayList<Listable> usersAndGroups = new ArrayList<Listable>();
	private int participated;
	private int inbox;
	
	private Integer targetDays;//Days

	public ProcessDef() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public ArrayList<DocumentType> getDocTypes() {
		return docTypes;
	}

	public void setDocTypes(ArrayList<DocumentType> docTypes) {
		this.docTypes = docTypes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		if(this.fileName==null && !files.isEmpty()){
		  String names = "";
		  for(Attachment att: files){
			  names = names.concat(att.getName()+",");
			  
		  }
		  
		  fileName = names.substring(0, names.lastIndexOf(','));
		}
		
		return fileName;
	}

	public void setFileName(String fileName) {		
		this.fileName = fileName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public ArrayList<Attachment> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<Attachment> files) {
		this.files = files;
	}

	public void addFile(Attachment attachment) {
		files.add(attachment);
	}

	@Override
	public String getDisplayName() {

		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		return ((ProcessDef)obj).id.equals(id);
	}

	public ProcessCategory getCategory() {
		return category;
	}

	public void setCategory(ProcessCategory category) {
		this.category = category;
	}

	public ArrayList<Listable> getUsersAndGroups() {
		return usersAndGroups;
	}

	public void setUsersAndGroups(ArrayList<Listable> usersAndGroups) {
		this.usersAndGroups = usersAndGroups;
	}
	
	public void addUserOrGroup(Listable userOrGroup){
		usersAndGroups.add(userOrGroup);
	}

	public int getParticipated() {
		return participated;
	}

	public void setParticipated(int participated) {
		this.participated = participated;
	}

	public int getInbox() {
		return inbox;
	}

	public void setInbox(int inbox) {
		this.inbox = inbox;
	}
	
	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getIconStyle() {
		return iconStyle;
	}

	public void setIconStyle(String iconStyle) {
		this.iconStyle = iconStyle;
	}

	public Integer getTargetDays() {
		return targetDays;
	}

	public void setTargetDays(Integer targetDays) {
		this.targetDays = targetDays;
	}

}
