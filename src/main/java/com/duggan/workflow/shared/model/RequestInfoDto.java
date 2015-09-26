package com.duggan.workflow.shared.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestInfoDto extends SerializableObj implements HasKey{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
    private Date time;
    private Date created;
    private Date updated;
    private String status;    
    private String commandName;
    private String message;
    //Business messageKey for callback
    private String messageKey;
    //Number of times that this request must be retryied
    private int retries = 0;
    //Number of times that this request has been executed
    private int executions = 0;
    private List<ErrorInfoDto> errorInfo = new ArrayList<ErrorInfoDto>();
    
    public RequestInfoDto() {
	}
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCommandName() {
		return commandName;
	}
	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessageKey() {
		return messageKey;
	}
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
	public int getRetries() {
		return retries;
	}
	public void setRetries(int retries) {
		this.retries = retries;
	}
	public int getExecutions() {
		return executions;
	}
	public void setExecutions(int executions) {
		this.executions = executions;
	}
	public List<ErrorInfoDto> getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(List<ErrorInfoDto> errorInfo) {
		this.errorInfo = errorInfo;
	}

	@Override
	public String getKey() {
		return getRefId();
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
}
