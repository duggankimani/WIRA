package com.duggan.workflow.server.rest.model;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Response {

	private BusinessKey businessKey;	
	@XmlJavaTypeAdapter(value=MapAdapter.class)
	private Map<String,Object> context;	
	
	@XmlElement
	private String errorCode;
	
	@XmlElement
	private String errorMessage;
	
	public Response() {
	}
	
	public BusinessKey getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(BusinessKey businessKey) {
		this.businessKey = businessKey;
	}
	
	@Override
	public String toString() {
		String str = "Response = [Business Key: "+businessKey+"; "+
				"Context: "+context;
		
		if(errorCode!=null){
			str = str.concat(" - ErrorCode "+errorCode+"; Message = "+errorMessage);
		}
		
		str = str.concat("]");
		return str;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}

	public void setError(String errorCode, String message) {
		this.errorCode= errorCode;
		this.errorMessage = message;
	}
}
