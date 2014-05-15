package com.duggan.workflow.server.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class BusinessKey {

	private Long workItemId;
	private Long processInstanceId;
	private Long sessionId;
	private Long documentId;
	
	public BusinessKey(){}
	
	public BusinessKey( Long documentId,Long sessionId, Long processInstanceId, Long workItemId){
		this.documentId = documentId;
		this.sessionId = sessionId;
		this.processInstanceId = processInstanceId;
		this.workItemId = workItemId;
	}

	public Long getWorkItemId() {
		return workItemId;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public Long getDocumentId() {
		return documentId;
	}
	
	@Override
	public String toString() {
		String str = "[WorkItemId="+workItemId+"; "+
				"ProcessInstanceId="+processInstanceId+"; " +
						"SessionId="+sessionId+"; " +
								"DocumentId="+documentId+"]";
				
		return str;
	}
}
