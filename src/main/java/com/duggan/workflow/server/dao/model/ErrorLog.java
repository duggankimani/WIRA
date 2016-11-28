package com.duggan.workflow.server.dao.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.jbpm.executor.entities.RequestInfo;

import com.duggan.workflow.shared.model.ErrorInfoDto;


@Entity
@Table(name="errorlog",indexes={@Index(name="idx_ref_id",columnList="refId")})
public class ErrorLog extends PO{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=2000)
	private String msg;
	
	@Column(length=5000)
	private String stackTrace;
	
	@Temporal(TemporalType.TIMESTAMP)
    private Date time;
	
	private String agent;
	
	private String remoteAddress;
	
	@ManyToOne
    @JoinColumn(name="REQUEST_ID", nullable=true)
    private RequestInfo requestInfo;
    
	
	public ErrorLog() {
	}
	
	public ErrorLog(String msg, String stackTrace){
		this.msg = msg;
		this.stackTrace = stackTrace;
	}
	
	@Override
	public Long getId() {

		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getstackTrace() {
		return stackTrace;
	}

//	public void setstackTrace(String stackTrace) {
//		this.stackTrace = stackTrace;
//	}

	public String getMsg() {
		return msg;
	}

//	public void setMsg(String msg) {
//		this.msg = msg;
//	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ErrorLog other = (ErrorLog) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.time != other.time && (this.time == null || !this.time.equals(other.time))) {
            return false;
        }
        if ((this.msg == null) ? (other.msg != null) : !this.msg.equals(other.msg)) {
            return false;
        }
        if ((this.stackTrace == null) ? (other.stackTrace != null) : !this.stackTrace.equals(other.stackTrace)) {
            return false;
        }
        if (this.requestInfo != other.requestInfo && (this.requestInfo == null || !this.requestInfo.equals(other.requestInfo))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 37 * hash + (this.time != null ? this.time.hashCode() : 0);
        hash = 37 * hash + (this.msg != null ? this.msg.hashCode() : 0);
        hash = 37 * hash + (this.stackTrace != null ? this.stackTrace.hashCode() : 0);
        hash = 37 * hash + (this.requestInfo != null ? this.requestInfo.hashCode() : 0);
        return hash;
    }

    public ErrorInfoDto toDto() {
		ErrorInfoDto dto = new ErrorInfoDto();
		dto.setId(id);		
		return dto;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
