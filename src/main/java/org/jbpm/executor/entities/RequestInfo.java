/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.jbpm.executor.entities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.jbpm.executor.api.CommandCodes;
import org.jbpm.executor.api.CommandContext;

import com.duggan.workflow.server.dao.model.ErrorLog;
import com.duggan.workflow.server.dao.model.PO;
import com.duggan.workflow.shared.model.ErrorInfoDto;
import com.duggan.workflow.shared.model.ExecutionStatus;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.RequestInfoDto;

/**
 *
 * @author salaboy
 */
@Entity(name = "RequestInfo")
public class RequestInfo extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;
	@Enumerated(EnumType.STRING)
	private ExecutionStatus status;
	private String commandName;
	private String commandClass;
	private String message;
	// Business messageKey for callback
	private String messageKey;
	// Number of times that this request must be retryied
	private int retries = 0;
	// Number of times that this request has been executed
	private int executions = 0;

	@Lob
	private byte[] requestData;
	@Lob
	private byte[] responseData;
	@OneToMany(mappedBy = "requestInfo")
	// cascade= CascadeType.ALL,
	private List<ErrorLog> errorInfo = new ArrayList<ErrorLog>();

	public RequestInfo() {
	}

	public List<ErrorLog> getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(List<ErrorLog> errorInfo) {
		this.errorInfo = errorInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public String getKey() {
		return messageKey;
	}

	public void setKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ExecutionStatus getStatus() {
		return status;
	}

	public void setStatus(ExecutionStatus status) {
		this.status = status;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public byte[] getRequestData() {
		return requestData;
	}

	public void setRequestData(byte[] requestData) {
		this.requestData = requestData;
	}

	public byte[] getResponseData() {
		return responseData;
	}

	public void setResponseData(byte[] responseData) {
		this.responseData = responseData;
	}

	@Override
	public String toString() {
		return "RequestInfo{" + "id=" + id + ", time=" + time + ", status="
				+ status + ", commandName=" + commandName + ", message="
				+ message + ", messageKey=" + messageKey + ", requestData="
				+ requestData + ", responseData=" + responseData + ", error="
				+ errorInfo + '}';
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final RequestInfo other = (RequestInfo) obj;
		if (this.id != other.id
				&& (this.id == null || !this.id.equals(other.id))) {
			return false;
		}
		if (this.time != other.time
				&& (this.time == null || !this.time.equals(other.time))) {
			return false;
		}
		if (this.status != other.status) {
			return false;
		}
		if ((this.commandName == null) ? (other.commandName != null)
				: !this.commandName.equals(other.commandName)) {
			return false;
		}
		if ((this.message == null) ? (other.message != null) : !this.message
				.equals(other.message)) {
			return false;
		}
		if ((this.messageKey == null) ? (other.messageKey != null)
				: !this.messageKey.equals(other.messageKey)) {
			return false;
		}
		if (!Arrays.equals(this.requestData, other.requestData)) {
			return false;
		}
		if (!Arrays.equals(this.responseData, other.responseData)) {
			return false;
		}
		if (this.errorInfo != other.errorInfo
				&& (this.errorInfo == null || !this.errorInfo
						.equals(other.errorInfo))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
		hash = 79 * hash + (this.time != null ? this.time.hashCode() : 0);
		hash = 79 * hash + (this.status != null ? this.status.hashCode() : 0);
		hash = 79 * hash
				+ (this.commandName != null ? this.commandName.hashCode() : 0);
		hash = 79 * hash + (this.message != null ? this.message.hashCode() : 0);
		hash = 79 * hash
				+ (this.messageKey != null ? this.messageKey.hashCode() : 0);
		hash = 79 * hash + Arrays.hashCode(this.requestData);
		hash = 79 * hash + Arrays.hashCode(this.responseData);
		hash = 79 * hash
				+ (this.errorInfo != null ? this.errorInfo.hashCode() : 0);
		return hash;
	}

	public RequestInfoDto toDto() {
		RequestInfoDto dto = new RequestInfoDto();
		dto.setCommandName(commandName);
		dto.setExecutions(executions);
		dto.setId(id);
		dto.setMessage(message);
		dto.setMessageKey(messageKey);
		dto.setRefId(getRefId() == null ? messageKey : getRefId());
		dto.setRetries(retries);
		dto.setStatus(status.name());
		dto.setTime(time);
		dto.setCreated(getCreated());
		dto.setUpdated(getUpdated());

		CommandContext ctx = null;
		byte[] reqData = getRequestData();
		
		if (reqData != null) {
			try {
				ObjectInputStream in = new ObjectInputStream(
						new ByteArrayInputStream(reqData));
				ctx = (CommandContext) in.readObject();

				Map<String, Object> data = ctx.getData();
				String subject = data.get("emailSubject") == null ? "" : data
						.get("emailSubject").toString();
				dto.setSubject(subject);

				String body = data.get("Body") == null ? "" : data.get("Body")
						.toString();
				dto.setBody(body);

				Object recipients = data.get("To") == null ? "" : data
						.get("To");

				if (recipients instanceof List) {
					List<HTUser> userList = (List<HTUser>) recipients;
					dto.setUsers(userList);
				} else if (recipients instanceof String) {
					String commaSepList = recipients.toString();
					dto.setRecipients(commaSepList);

				}
			} catch (IOException e) {
				ctx = null;
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		List<ErrorInfoDto> errorDtos = new ArrayList<>();
		for (ErrorLog info : errorInfo) {
			errorDtos.add(info.toDto());
		}

		dto.setErrorInfo(errorDtos);

		return dto;
	}

	public String getCommandClass() {
		return commandClass;
	}

	public void setCommandClass(String commandClass) {
		this.commandClass = commandClass;
	}

}
