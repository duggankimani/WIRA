package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.rule.Collect;

public abstract class Doc implements Serializable,Comparable<Doc>{

	/**
	 * 
	 */
	protected static final long serialVersionUID = 1L;

	private boolean hasAttachment=false;

	public abstract String getSubject();

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
	
	/**
	 * Sorts document/task elements in descending order
	 * hence the negative sign (-)
	 */
	@Override
	public int compareTo(Doc o) {
		
		return - getCreated().compareTo(o.getCreated());
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
		values.put(name, value);
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

}
