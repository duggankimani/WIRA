package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.SerializableObj;

@XmlType
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Doc extends SerializableObj implements Serializable,
		IsSerializable, Comparable<Doc> {

	/**
	 * 
	 */
	protected static final long serialVersionUID = 1L;

	private boolean hasAttachment = false;

//	@XmlJavaTypeAdapter(MapValuesAdapter.class)
//	@XmlElement(name="docvals")
	@XmlTransient
	protected HashMap<String, Value> values = new HashMap<String, Value>();
	
	@XmlTransient
	protected HashMap<String, ArrayList<DocumentLine>> details = new HashMap<String, ArrayList<DocumentLine>>();

	private String uploadedFileId;

	private String processId;
	private String processName;
	private String nodeName;
	private Long nodeId;
	protected String caseNo;

	private HTStatus processStatus = HTStatus.INPROGRESS;

	private HTUser taskActualOwner;
	private String potentialOwners;

	public Doc() {
	}

	public String getDescription() {
		return null;
	}

	public Date getCreated() {
		return null;
	}

	public Date getDocumentDate() {
		return null;
	}

	public Integer getPriority() {
		return null;
	}

	public Object getId() {
		return null;
	}
	
	/**
	 * Sorts document/task elements in descending order hence the negative sign
	 * (-)
	 */
	@Override
	public int compareTo(Doc o) {
		Date thisDate = getSortDate(this);

		Date other = getSortDate(o);
		
		if(thisDate!=null && other==null){
			return -1;
		}else if(thisDate==null && other!=null){
			return 1;
		}else if(thisDate==null && other==null){
			return 0;
		}
		
		return (-thisDate.compareTo(other));
	}

	public Date getSortDate() {
		return getSortDate(this);
	}

	private Date getSortDate(Doc doc) {

		Date dateToUse = doc.getCreated();
		if (doc instanceof HTSummary) {
			HTSummary summ = (HTSummary) doc;
			if (summ.getStatus() == HTStatus.COMPLETED) {
				dateToUse = summ.getCompletedOn();
			}
			if(dateToUse==null){
				dateToUse = summ.getCreated();
			}
		} else {
			Document document = (Document) doc;
			if (!document.getStatus().equals(DocStatus.DRAFTED)) {
				dateToUse = document.getDateSubmitted();
			}
			if(dateToUse==null){
				dateToUse = doc.getCreated();
			}
		}
		
		

		return dateToUse;
	}

	public HashMap<String, Value> getValues() {
		return values;
	}

	public void setValues(HashMap<String, Value> values) {
		this.values = values;
	}

	public void setValue(String name, Value value) {
		if (value != null) {
			value.setKey(name);
		}

		if (name.equals("subject")) {
			// backward compatibility - Changing subject to-> CaseNo
			setValue("caseNo", value.clone(false));
		}

		if (values.get(name) != null && value != null) {
			// Duggan 15/09/2015- Added this to support Field Triggers that may
			// update a field
			// value with the previous value's id - This update causes
			// duplication of a field value in
			// the db

			//Duggan 12/08/2016 - The above problem is no longer an issue. Document storage has now been upgraded to postgres jsonb format
			//A new issue has arisen due to json storage, which uses duck typing to determine a number class to use i.e Integer, double, long etc
			//This means the frontend might have a double field (DoubleValue) mapped (incorrectly) to an integer value (IntValue) on the server side.
			//Overwriting the value in the document is an easier option to storing types being written
//			Value v = values.get(name);
//			v.setValue(value.getValue());
			values.put(name, value);
		} else {
			values.put(name, value);
		}

	}

	public void _s(String name, Date value) {
		setValue(name, new DateValue(value));
	}

	public void _s(String name, String value) {
		setValue(name, new StringValue(value));
	}

	public void _s(String name, Double value) {
		setValue(name, new DoubleValue(value));
	}

	public void _s(String name, Integer value) {
		setValue(name, new IntValue(value));
	}

	public void _s(String name, Long value) {
		setValue(name, new LongValue(value));
	}

	public void _s(String name, Boolean value) {
		setValue(name, new BooleanValue(value));
	}

	public void copyValue(String name, Value value) {
		Value previous = values.get(name);
		if (previous == null) {
			previous = value.clone(false);
		}

		previous.setKey(name);
		previous.setValue(value.getValue());
		values.put(name, previous);
	}

	public HTUser getOwner() {
		return null;
	}

	public Long getProcessInstanceId() {
		return null;
	}

	public boolean hasAttachment() {
		return hasAttachment;
	}

	public void setHasAttachment(boolean hasAttachment) {
		this.hasAttachment = hasAttachment;
	}

	public HashMap<String, ArrayList<DocumentLine>> getDetails() {
		return details;
	}

	public void setDetails(HashMap<String, ArrayList<DocumentLine>> details) {
		this.details = details;
	}

	public void addDetail(DocumentLine line) {
		String name = line.getName();

		ArrayList<DocumentLine> lines = details.get(name);
		if (lines == null) {
			lines = new ArrayList<DocumentLine>();
			details.put(name, lines);
		}
		lines.add(line);

		/*
		 * Duggan 06/10/2015 ExecuteWorkflow action submits a ArrayList of Value
		 * objects i.e HashMap<String,Value> , which works ok for all fields
		 * except grid fields updated through triggers
		 * 
		 * Grid rows updated through a trigger call to addDetail('gridName',
		 * DocumentLine) are not added to a GridValue object: they are written
		 * directly to a HashMap<String, ArrayList<DocLine>>, hence they are
		 * left out when ExecuteWorkflow is called.
		 * 
		 * To remedy this issue, We need to loop through the document lines
		 * generating a GridValue entry for each document line with no
		 * corresponding gridValue. ALTERNATIVELY, override addDetail and
		 * generate a GridValue entry there - This may be a better fit since
		 * 'after-step' triggers on the last node will not interact with the
		 * interface before calling ExecuteWorkflow.
		 * 
		 * @See Doc.addDetail()
		 */
		GridValue value = null;
		if (values.get(name) == null) {
			value = new GridValue();
			value.setKey(name);
		} else if (values.get(name) instanceof GridValue) {
			value = (GridValue) values.get(name);
		} else {
			value = new GridValue();
			value.setKey(name);
		}

		if (value.getValue().contains(line)) {
			value.getValue().remove(line);
		}
		value.getValue().add(line);
		values.put(name, value);
	}

	public void setDetails(String key, Collection<DocumentLine> values) {
		// Clear previous - Meant to avoid duplicates
		details.remove(key);
		for (DocumentLine line : values) {
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
		this.processStatus = status;
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

	public Long getDocumentId() {
		return null;
	}

	public Object get(String key) {
		Value val = values.get(key);
		if (val == null) {
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

	public HashMap<String, Object> toObjectMap() {
		HashMap<String, Object> conv = new HashMap<String, Object>();
		for (String key : getValues().keySet()) {
			conv.put(key, get(key));
		}
		return conv;
	}

	public String getUploadedFileId() {
		return uploadedFileId;
	}

	public void setUploadedFileId(String uploadedFileId) {
		this.uploadedFileId = uploadedFileId;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		Doc other = (Doc)obj;
		if(getRefId()!=null && !getRefId().equals(other.getRefId())){
			return false;
		}
		
		if(processId!=null && !processId.equals(other.processId)){
			return false;
		}
		
		if(caseNo!=null && !caseNo.equals(caseNo)){
			return false;
		}
		
		if(processStatus!=null && !processStatus.equals(other.processStatus)){
			return false;
		}
		
		if(taskActualOwner!=null && !taskActualOwner.equals(other.taskActualOwner)){
			return false;
		}
		
		if(potentialOwners!=null && !potentialOwners.equals(other.potentialOwners)){
			return false;
		}
		
		if(taskActualOwner!=null && !taskActualOwner.equals(other.taskActualOwner)){
			return false;
		}
		
		if(values.size()!= other.values.size()){
			return false;
		}
		
		if(details.size()!=other.details.size()){
			return false;
		}
		
//		for(Value val: values.values()){
//			Value otherValue = other.values.get(val.getKey());
//			if(otherValue==null){
//				return false;
//			}
//			
//			Object v = val.getValue();
//			Object v1 = otherValue.getValue();
//			if(v!=null){
//				if(v1!=null){
//					if(!v.equals(v1)){
//						return false;
//					}
//				}else{
//					return false;
//				}
//				
//			}
//		}
		

		return true;
	}

}
