package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Doc extends SerializableObj implements Serializable,Comparable<Doc>{

	/**
	 * 
	 */
	protected static final long serialVersionUID = 1L;

	private boolean hasAttachment=false;

	public abstract String getDescription();

	public abstract Date getCreated();

	public abstract Date getDocumentDate();
	
	public abstract Integer getPriority();

	public abstract Object getId();
	
	protected Map<String, Value> values = new HashMap<String, Value>();
	
	protected Map<String, List<DocumentLine>> details = new HashMap<String, List<DocumentLine>>();
	
	private String processId;
	private String processName;
	private String nodeName;
	private Long nodeId;
	protected String caseNo;
	
	private HTStatus processStatus=HTStatus.INPROGRESS;
	
	private HTUser taskActualOwner;
	private String potentialOwners;
	
	
	/**
	 * Sorts document/task elements in descending order
	 * hence the negative sign (-)
	 */
	@Override
	public int compareTo(Doc o) {
		Date thisDate = getSortDate(this);
				
		Date other = getSortDate(o);
		return (- thisDate.compareTo(other));
	}

	public Date getSortDate(){
		return getSortDate(this);
	}
	
	private Date getSortDate(Doc doc) {
		
		Date dateToUse = doc.getCreated();
		if(doc instanceof HTSummary){
			HTSummary summ = (HTSummary)doc;
			if(summ.getStatus()==HTStatus.COMPLETED){
				dateToUse  = summ.getCompletedOn();
			}
		}else{
			Document document = (Document)doc;
			if(!document.getStatus().equals(DocStatus.DRAFTED)){
				dateToUse  = document.getDateSubmitted();
			}
		}
		
		return dateToUse;
	}

	public Map<String, Value> getValues() {
		return values;
	}

	public void setValues(Map<String, Value> values) {		
		this.values = values;
	}
	
	public void setValue(String name, Value value){
		if(value!=null){
			value.setKey(name);
		}
		
		if(name.equals("subject")){
			//backward compatibility - Changing subject to-> CaseNo
			setValue("caseNo", value.clone(false));
		}
		
		if(values.get(name)!=null && value!=null){
			//Duggan 15/09/2015- Added this to support Field Triggers that may update a field
			//value with the previous value's id - This update causes duplication of a field value in 
			//the db
			
			Value v= values.get(name);
			v.setValue(value.getValue());
			values.put(name, v);
		}else{
			values.put(name, value);
		}
		
	}
	
	public void copyValue(String name, Value value){
		Value previous = values.get(name);
		if(previous==null){
			previous = value.clone(false);
		}
		
		previous.setKey(name);
		previous.setValue(value.getValue());
		values.put(name, previous);
	}
	
	public abstract HTUser getOwner();
	
	public abstract Long getProcessInstanceId();
	
	public boolean hasAttachment() {
		return hasAttachment;
	}

	public void setHasAttachment(boolean hasAttachment) {
		this.hasAttachment = hasAttachment;
	}
	
	public Map<String, List<DocumentLine>> getDetails() {
		return details;
	}

	public void setDetails(Map<String, List<DocumentLine>> details) {
		this.details = details;
	}
	
	public void addDetail(DocumentLine line){
		String name = line.getName();
		
		List<DocumentLine> lines = details.get(name);
		if(lines==null){
			lines = new ArrayList<DocumentLine>();
			details.put(name, lines);
		}
		
		lines.add(line);
	}
	
	public void setDetails(String key, Collection<DocumentLine> values){
		//Clear previous - Meant to avoid duplicates
		details.remove(key);
		for(DocumentLine line: values){
			line.setName(key);
			addDetail(line);
		}
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}
	
	public HTUser getTaskActualOwner() {
		return taskActualOwner;
	}

	public void setTaskActualOwner(HTUser taskActualOwner) {
		this.taskActualOwner = taskActualOwner;
	}

	public void setProcessStatus(HTStatus status) {
		this.processStatus=status;
	}

	public HTStatus getProcessStatus() {
		return processStatus;
	}

	public String getPotentialOwners() {
		return potentialOwners;
	}

	public void setPotentialOwners(String potentialOwners) {
		this.potentialOwners = potentialOwners;
	}
	
	public abstract Long getDocumentId();
	
	public Object get(String key){
		Value val = values.get(key);
		if(val==null){
			return null;
		}
		
		return val.getValue();
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
		setValue("caseNo", new StringValue(null, "caseNo", caseNo));
	}
}	
